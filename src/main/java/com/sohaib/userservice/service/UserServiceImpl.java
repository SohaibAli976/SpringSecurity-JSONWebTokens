package com.sohaib.userservice.service;

import com.sohaib.userservice.domain.Roles;
import com.sohaib.userservice.domain.User;
import com.sohaib.userservice.repo.RoleRepo;
import com.sohaib.userservice.repo.UserRepo;
import com.sohaib.userservice.security.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("Saving new User {} to the Database",user.getName());
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Roles saveRole(Roles role) {
        log.info("Saving new Role {} to the Database",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        log.info("Saving new Role {} to the User {}",roleName,userName);
        User user = userRepo.findByUserNameIgnoreCase(userName);
        Roles role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public void removeRoleFromUser(String userName, String roleName) {
        log.info("Removing Role {} from the User {}",roleName,userName);
        User user = userRepo.findByUserNameIgnoreCase(userName);
        Roles role = roleRepo.findByName(roleName);
        user.getRoles().remove(role);
    }

    @Override
    public User getUserByUserName(String userName) {
        log.info("Fetching User {}",userName);
        return userRepo.findByUserNameIgnoreCase(userName);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all User from the Database");
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user=userRepo.findByUserNameIgnoreCase(username);
         if(user==null){
             log.error("User not found in the database");
             throw new UsernameNotFoundException("User not found");
         }else {
             log.info("user found in the database {}",user.getName());
         }
         Collection<GrantedAuthority> grantedAuthorities=new ArrayList<>();
         user.getRoles().forEach(role->{
             grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
         });
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),grantedAuthorities);
    }
}
