package com.sohaib.userservice.repo;

import com.sohaib.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserNameIgnoreCase(String userName);
}
