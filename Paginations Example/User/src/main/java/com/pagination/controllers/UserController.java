package com.pagination.controllers;

import com.pagination.entities.User;
import com.pagination.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public String testing() {
        return "User-Controller is OK.";
    }

    // POST is not idempotent by default
    // to make POST idempotent, we send one idempotency-key (generally UUID) in request header
    // Idempotency keys ensure that retrying the same POST request doesn't create duplicate resources.
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }


    // Pagination
    @GetMapping({"", "/getAll"})
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam int page, @RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userService.findAllUsers(pageRequest).getBody();
        return userService.findAllUsers(pageRequest);
    }

    // idempotent method  = Same output after multiple calls with the same input.
    // no need to make this idempotent because get, delete and put are idempotent by default
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String userId) {
        User userById = userService.findById(userId).getBody();

        // used ETag to achieve Idempotency in Rest API
        String etag = "\"" + userById.getVersion() + "\""; // e.g., MD5 hash of content

        return ResponseEntity.ok().eTag(etag).body(userById);
    }

    // no need to make this idempotent because get, delete and put are idempotent by default
    @DeleteMapping("/id/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") String userId) {
        return userService.deleteById(userId);
    }

    // idempotent method  = Same output after multiple calls with the same input.
    @PutMapping("/id/{id}")
    public ResponseEntity<User> updateUserById(
            @PathVariable("id") String userId,
            @RequestBody User user,
            @RequestHeader("If-Match") String ifMatch ) {

        // these all lines for achieving idempotency in Rest API

        User userInDB = userService.findById(userId).getBody();

        // Validate ETag
        String currentEtag = "\"" + userInDB.getVersion() + "\"";

        if(!ifMatch.equals(currentEtag)){
            return ResponseEntity.status(412).build(); // 412 Precondition Failed
        }
        User updatedUser = userService.updateById(userId, user).getBody();

        // Return new ETag
        String newEtag = "\"" + updatedUser.getVersion() + "\"";
        return ResponseEntity.ok().eTag(newEtag).body(updatedUser);
    }


}
