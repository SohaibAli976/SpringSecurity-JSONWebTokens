package com.sohaib.userservice.service;

import com.sohaib.userservice.domain.Roles;
import com.sohaib.userservice.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Roles saveRole(Roles role);
    void addRoleToUser(String userName,String roleName);
    void removeRoleFromUser(String userName,String roleName);
    User getUserByUserName(String userName);
    List<User> getAllUsers();
}
