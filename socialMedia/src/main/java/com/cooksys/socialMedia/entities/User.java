package com.cooksys.socialMedia.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @Column(nullable = false)
    private Credentials credentials;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp joined;

    private boolean deleted = false;

    @Embedded
    @Column(nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "author")
    List<Tweet> authoredTweets;

    @ManyToMany
    @JoinTable(name = "followers_following")
    List<User> following;

    @ManyToMany(mappedBy = "following")
    List<User> followers;

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    List<Tweet> userLikedTweets;

    @ManyToMany(mappedBy="usersMentionedByThisTweet")
    List<Tweet> userMentions;
    
}