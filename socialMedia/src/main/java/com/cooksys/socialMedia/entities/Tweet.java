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
public class Tweet {
    
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    @CreationTimestamp
    private Timestamp posted;

    private boolean deleted = false;
    
    private String content;

    @ManyToOne
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> tweetReplies;

    @ManyToOne
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    List<Tweet> tweetReposts;

    @ManyToMany(mappedBy = "userLikedTweets")
    List<User> usersThatLikedThisTweet;

    @ManyToMany
    @JoinTable(
            name = "user_mentions",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> usersMentionedByThisTweet;

    @ManyToMany
    @JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    List<Hashtag> hashtagsOnThisTweet;

}
