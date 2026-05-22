package com.lifeline.lifeline.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeline.lifeline.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(
            String email
    );
}