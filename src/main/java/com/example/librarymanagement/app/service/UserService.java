package com.example.librarymanagement.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.librarymanagement.app.entity.UserInfo;
import com.example.librarymanagement.app.exception.errorResponse;
import com.example.librarymanagement.app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo createUser(UserInfo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserInfo b = userRepository.save(user);
        return b;
    }

     public void deleteUser(Long userid) {
        UserInfo user = userRepository.findById(userid)
        .orElseThrow(() -> new errorResponse("User not found : " + userid));
        userRepository.delete(user);
    }
}
