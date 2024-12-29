package com.godea.services;

import com.godea.entities.User;
import com.godea.exceptions.UserException;
import com.godea.request.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

// Общая реализация для поиска пользователей, админов и т.д.
public interface IUserService {
    User findUserById(UUID id) throws UserException;
    User findUserProfile(String jwt) throws UserException;
    User updateUser(UUID id, UpdateUserRequest userRequest) throws UserException;
    List<User> searchUser(String query) throws UserException;
}
