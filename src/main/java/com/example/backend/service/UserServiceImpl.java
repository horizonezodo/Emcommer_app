package com.example.backend.service;

import com.example.backend.config.JwtProvider;
import com.example.backend.exception.UserException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;
    private JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepo, JwtProvider jwtProvider) {
        this.userRepo = userRepo;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("user not found with id " + id);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);

        if(user==null){
            throw new UserException("user not found with email: "+ email);
        }
        return user;
    }
}
