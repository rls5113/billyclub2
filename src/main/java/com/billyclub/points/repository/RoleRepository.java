package com.billyclub.points.repository;

import com.billyclub.points.model.ERole;
import com.billyclub.points.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
//    Role save(String s);
}
