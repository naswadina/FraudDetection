package com.pa.FraudDetection.repository;

import com.pa.FraudDetection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    User findOneByUsername(String username);
}
