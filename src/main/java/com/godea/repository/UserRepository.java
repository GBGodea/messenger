package com.godea.repository;

import com.godea.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    // @Param - позволяет передать в @Query какое-либо значение
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:query% or u.email LIKE %:query%")
    List<User> searchUser(@Param("query") String query);
}
