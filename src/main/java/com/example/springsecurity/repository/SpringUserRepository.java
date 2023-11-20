package com.example.springsecurity.repository;

import com.example.springsecurity.model.SpringUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringUserRepository extends JpaRepository<SpringUser, Long> {
    SpringUser findByUsername(String username);

    SpringUser findById(String id);
}
