package com.cooksys.socialMedia.repositories;

import com.cooksys.socialMedia.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    // ADD Derived Queries if needed
    Optional<Hashtag> findByLabel(String label);

//    Optional<Hashtag> findByLabelOrderByTweetPostedDesc(String label);
}
