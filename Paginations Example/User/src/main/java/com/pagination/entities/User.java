package com.pagination.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNo;

    private String userId;

    private String username;

    @Column( nullable = true)
    private String email;
    @Column(length = 20, nullable = true)
    private String password;

    private String city;


    // this version is used just to achieve Idempotency in Rest API using ETag or If-Match
    // Version prevent concurrent update ->If two users fetch version 1, but one updates first, the second update will fail (version is now 2).
    @Version  // Automatically incremented on update
    private Long version;


    // ratings
    @Transient
//    private List<Ratings> ratings = new ArrayList<>();


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", version=" + version +
                '}';
    }
}
