package com.openclassrooms.springsecurityauth.repository;

import com.openclassrooms.springsecurityauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByname(String name);

    User findByEmail(String email);
}
