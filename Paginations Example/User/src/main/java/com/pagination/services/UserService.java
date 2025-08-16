package com.pagination.services;

import com.pagination.entities.User;
import com.pagination.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> addUser(User userExample) {
        userRepository.save(userExample);
        return ResponseEntity.status(HttpStatus.CREATED).body(userExample);
    }

    public ResponseEntity<Page<User>> findAllUsers(Pageable pageable) {
        Page<User> allUsers = userRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    public ResponseEntity<User> findById(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found with userID : " + userID));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    public ResponseEntity<User> deleteById(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found with userID : " + userID));
        userRepository.deleteById(userID);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<User> updateById(String userId, User user) {
        User userInDB = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with userId : " + userId));
        userInDB.setCity(user.getCity() != null ? user.getCity() : userInDB.getCity());
        userInDB.setUsername(user.getUsername() != null ? user.getUsername() : userInDB.getUsername());
        userInDB.setEmail(user.getEmail() != null ? user.getEmail() : userInDB.getEmail());
        userInDB.setPassword(user.getPassword() != null ? user.getPassword() : userInDB.getPassword());
        userRepository.save(userInDB);
        return ResponseEntity.status(HttpStatus.OK).body(userInDB);
    }
}
