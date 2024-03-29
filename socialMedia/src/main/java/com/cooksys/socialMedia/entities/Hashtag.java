package com.cooksys.socialMedia.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String label;

    @CreationTimestamp
    private Timestamp firstUsed;

    @UpdateTimestamp
    private Timestamp lastUsed;


    @ManyToMany(mappedBy="hashtagsOnThisTweet")
    List<Tweet> tweetsWithThisHashtag;
}