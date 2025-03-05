package com.example.librarymanagement.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.librarymanagement.app.entity.AuthRequest;
import com.example.librarymanagement.app.entity.UserInfo;
import com.example.librarymanagement.app.exception.errorResponse;
import com.example.librarymanagement.app.repository.UserRepository;
import com.example.librarymanagement.app.service.JwtService;
import com.example.librarymanagement.app.service.UserService;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public UserInfo createUser(@RequestBody UserInfo users) {
        return userService.createUser(users);
    }

    @PutMapping("/{userid}")
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    public UserInfo updateUser(@PathVariable Long userid, @RequestBody UserInfo users) {
        UserInfo existingUser = userRepository.findById(userid)
        .orElseThrow(() -> new errorResponse("User not found" + userid));
        existingUser.setName(users.getName() !=null ? users.getName() : existingUser.getName());
        existingUser.setRoles(users.getRoles() != null ? users.getRoles() : existingUser.getRoles());
        //existingUser.setPassword(users.getPassword()!=null ? passwordEncoder.encode(users.getPassword()) : existingUser.getPassword());
        return userService.createUser(existingUser);
    }

    

    @DeleteMapping("/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long userid) {
        userService.deleteUser(userid);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new errorResponse("Invalid user request");
        }

    }


    
}