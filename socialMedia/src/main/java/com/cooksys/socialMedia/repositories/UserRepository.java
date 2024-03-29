package com.cooksys.socialMedia.repositories;

import com.cooksys.socialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // ADD Derived Queries if needed
    User findByCredentialsUsername(String username);

    Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);
}
