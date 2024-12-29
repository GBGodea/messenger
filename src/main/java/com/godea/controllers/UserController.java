package com.godea.controllers;

import com.godea.entities.User;
import com.godea.exceptions.UserException;
import com.godea.request.UpdateUserRequest;
import com.godea.response.ApiResponse;
import com.godea.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// @RestController - Bean, который является контроллером и имеет авто сереализацию и десереализацю
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ApiResponse apiResponse;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    // Передаю в GetMapping - query как переменную, которая будет использовать в адресе поиска(URL)
    @GetMapping("/{query}")
    public ResponseEntity<List<User>> getUserListHandler(@PathVariable String query) throws UserException {
        List<User> userList = userService.searchUser(query);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<User> getUserByIdHandler(@RequestBody UUID id) throws UserException {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestHeader("Authorization") String token, @RequestBody UpdateUserRequest request) throws UserException {
        User updateRequest = userService.findUserProfile(token);
        userService.updateUser(updateRequest.getId(), request);
        apiResponse.setMessage("User updated");
        apiResponse.setStatus(true);
        // Возможно лучше не Autowire с ApiResponse не делать, а создать тут внутренний класс
//        ApiResponse apiResponse = new ApiResponse("User updated", true);
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
