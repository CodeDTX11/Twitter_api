package com.cooksys.socialMedia.services;

import com.cooksys.socialMedia.dtos.*;

import java.util.List;

public interface TweetService {
	   List<TweetResponseDto> getAllTweets();
	   TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) ;
	   TweetResponseDto getTweet(long id);
	   TweetResponseDto deleteTweet(long id, CredentialsDto tweetRequestDto);
	   void likeTweet(long id, CredentialsDto credentialsRequestDto) ;
	   TweetResponseDto replyTweet( long id, TweetRequestDto tweetRequestDto) ;
	   TweetResponseDto repostTweet(long id, CredentialsDto tweetRequestDto);
	   List<HashtagDto> getTagsOfTweet(long id);
	   List<UserResponseDto> getUsersThatLikedTweet(long id) ;
	   ContextDto getContext(long id);
	   List<TweetResponseDto> getReplies(long id);
	   List<TweetResponseDto> getReposts(long id);
	   List<UserResponseDto> getMentions(long id);
}
