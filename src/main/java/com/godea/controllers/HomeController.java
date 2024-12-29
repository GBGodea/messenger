package com.godea.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Rest Controller - чтобы сразу была сериализация и десереализация JSON
@RestController
//@RequestMapping("/api/home")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> HomeController() {
        return new ResponseEntity<String>("It's messenger app", HttpStatus.OK);
    }
}
