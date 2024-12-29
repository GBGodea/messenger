package com.godea.services;

import com.godea.config.TokenProvider;
import com.godea.entities.User;
import com.godea.exceptions.UserException;
import com.godea.repository.UserRepository;
import com.godea.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public User findUserById(UUID id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserException("User not found"));
    }
    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if(email == null) {
            throw new BadCredentialsException("Received Invalid Token");
        }
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        // Для дебага я оставил + email - хотя как по мне это не очень хорошая практика
        return user.orElseThrow(() -> new UserException("User not found " + email));

        // Возможно обёртка Optional будет работать некорректно, поэтому нужно будет переключиться на этот код
//        if(email != null) {
//            return userRepository.findByEmail(email);
//        }
//        throw new UserException("User not found");
    }
    @Override
    public User updateUser(UUID id, UpdateUserRequest userRequest) throws UserException {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            user.get().setFullName(userRequest.getFullName());
            user.get().setProfilePicture(userRequest.getProfilePicture());
            return userRepository.save(user.get());
        }

        throw new UserException("User Not Found");
    }

    @Override
    public List<User> searchUser(String query) throws UserException {
        Optional<List<User>> userList = Optional.ofNullable(userRepository.searchUser(query));
        return userList.orElseThrow(() -> new UserException("User Not Found"));
    }
}
