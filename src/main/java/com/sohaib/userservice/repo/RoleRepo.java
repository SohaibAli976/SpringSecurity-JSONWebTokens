package com.sohaib.userservice.repo;

import com.sohaib.userservice.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepo extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
