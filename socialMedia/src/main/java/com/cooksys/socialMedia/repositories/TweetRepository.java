package com.cooksys.socialMedia.repositories;

import com.cooksys.socialMedia.entities.Tweet;
import com.cooksys.socialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    // ADD Derived Queries if needed
    List<Tweet> findAllByOrderByPostedDesc();
    Optional<Tweet> findByIdAndDeletedFalse(Long id);

    List<Tweet> findAllByAuthorAndDeletedFalseOrderByPostedDesc(User author);

    List<Tweet> findAllByContentContainingOrderByPostedDesc(String string);
}