package com.example.buchladen.Repositories;

import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {

    Optional<User> findByEmailOrCustomUsername(String email, String customUsername);
    User findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.shippingDetailsList WHERE u.email = :identifier OR u.customUsername = :identifier")
    Optional<User> findByLoginIdentifierWithShipping(@Param("identifier") String identifier);


    @Query("SELECT u FROM User u WHERE u.email = :login OR u.customUsername = :login")
    Optional<User> findByLogin(@Param("login") String login);

    User findByCustomUsername(String username);

    boolean existsByEmail(String email);

}
