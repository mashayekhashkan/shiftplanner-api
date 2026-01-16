package com.shiftplanner.shiftplanner_api.service;

import com.shiftplanner.shiftplanner_api.domain.User;
import com.shiftplanner.shiftplanner_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUserByEmail(String email) {
        return repository.findUserByEmailIgnoreCse(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public User getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not fond: " + id));
    }

    public List<User> getByName(String name) {
        return repository.findUserByNameIgnoreCase(name);
    }

    public List<User> getUserByRolle(String rolle) {
        return repository.findUserByRolleIgnoreCase(rolle);
    }
}
