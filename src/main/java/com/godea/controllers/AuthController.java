package com.godea.controllers;

import com.godea.config.JwtConstant;
import com.godea.config.TokenProvider;
import com.godea.entities.User;
import com.godea.exceptions.UserException;
import com.godea.repository.UserRepository;
import com.godea.request.LoginRequest;
import com.godea.response.AuthResponse;
import com.godea.services.CustomUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthResponse authResponse;

    @Autowired
    private CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> userHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();

        User isUser = userRepository.findByEmail(email);

        if(isUser == null) {
            throw new UserException("This email is already used");
        }

        isUser.setEmail(email);
        isUser.setPassword(passwordEncoder.encode(password));
        isUser.setFullName(fullName);

        userRepository.save(isUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authResponse.setJwt(tokenProvider.generateToken(authentication));
        authResponse.setAuth(true);

        return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) throws UserException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication auth = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        authResponse.setJwt(tokenProvider.generateToken(auth));
        authResponse.setAuth(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
