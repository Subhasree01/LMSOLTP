package com.example.librarymanagement.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.librarymanagement.app.entity.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
        Optional<UserInfo> findByName(String username);

}