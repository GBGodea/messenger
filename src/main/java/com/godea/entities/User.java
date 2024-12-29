package com.godea.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullName;
    private String email;
    private String profilePicture;
    private String password;

//    @OneToMany(mappedBy = "User", cascade = CascadeType.ALL)
//    private List<Notification> notifications = new ArrayList<Notification>();
}
