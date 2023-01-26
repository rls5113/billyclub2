package com.billyclub.points.repository;

import com.billyclub.points.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String userName);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
