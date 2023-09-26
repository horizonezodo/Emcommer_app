package com.example.backend.service;

import com.example.backend.exception.UserException;
import com.example.backend.model.User;
import org.springframework.stereotype.Service;


public interface UserService {
    public User findUserById(Long id) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;
}
