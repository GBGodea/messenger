package com.godea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.*;

@Configuration
@EnableWebSecurity
public class MessengerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Ограничивает все URL адреса, если у пользователя нет роли USER - то есть, перед тем как пользоваться сервисом пользователю нужно зарегистрироваться и получить роль USER
        // При попытке открыть сайт по URL сразу перебрасывает на страницу login
        /*
        SessionManagement - настраивает веб сессию клиента, чтобы при определённых случаях могла быть только одна сессия клиента
        в случае входа в аккаунт, т.е., если пользователь под именем Godea уже вошёл, и попытается войти в другом месте снова,
        то сессия на первом устройстве сбросится, а на втором произойдёт вход в аккаунт
        cors - настраивает политику cors
         */
        httpSecurity.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
                        .requestMatchers("/api/**")
                        .hasRole("USER"))
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS).sessionConcurrency(sessionConcurrency -> sessionConcurrency
                        .maximumSessions(1)
                        .expiredUrl("/login?expired")))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("localhost:3000"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setMaxAge(1111L);
                    return config;
                }))
                .formLogin(formLogin -> formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/authentication/login")
//                        .successForwardUrl("localhost:8080")
                        .failureForwardUrl("/authentication/login?failure"));
        return httpSecurity.build();
    }

    // Пока что не знаю, что лучше использовать мой метод написанный, или метод с видео
    // Объявляю как Bean, чтобы потом использовать данную функцию перед сохранением пароля в БД
//    @Bean
//    public PasswordEncoder passwordEncoder(PasswordEncoder passwordEncoder, String password) {
//        Map<String, PasswordEncoder> encoders = new HashMap<>();
//        encoders.put(password, new BCryptPasswordEncoder());
//        passwordEncoder = new DelegatingPasswordEncoder(password, encoders);
//        passwordEncoder.encode(chars);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        // Ограничивает все URL адреса, если у пользователя нет роли USER - то есть, перед тем как пользоваться сервисом пользователю нужно зарегистрироваться и получить роль USER
//        // При попытке открыть сайт по URL сразу перебрасывает на страницу login
//        /*
//        SessionManagement - настраивает веб сессию клиента, чтобы при определённых случаях могла быть только одна сессия клиента
//        в случае входа в аккаунт, т.е., если пользователь под именем Godea уже вошёл, и попытается войти в другом месте снова,
//        то сессия на первом устройстве сбросится, а на втором произойдёт вход в аккаунт
//        cors - настраивает политику cors
//         */
//        httpSecurity.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
//                        .requestMatchers("/api/**")
//                        .hasRole("USER"))
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS).sessionConcurrency(sessionConcurrency -> sessionConcurrency
//                        .maximumSessions(1)
//                        .expiredUrl("/login?expired")))
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowedOrigins(List.of("localhost:3000"));
//                    config.setAllowedMethods(Collections.singletonList("*"));
//                    config.setAllowCredentials(true);
//                    config.setAllowedHeaders(Collections.singletonList("*"));
//                    config.setExposedHeaders(List.of("Authorization"));
//                    config.setMaxAge(1111L);
//                    return config;
//                }))
//                .formLogin(formLogin -> formLogin
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .loginPage("/authentication/login")
////                        .successForwardUrl("localhost:8080")
//                        .failureForwardUrl("/authentication/login?failure"));
//        return httpSecurity.build();
//    }