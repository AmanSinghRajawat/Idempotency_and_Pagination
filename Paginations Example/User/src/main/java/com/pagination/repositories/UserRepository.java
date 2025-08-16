package com.pagination.repositories;

import com.pagination.entities.User;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.userId = :userID")
    Optional<User> findById(@PathParam("userID") String userID);

}
