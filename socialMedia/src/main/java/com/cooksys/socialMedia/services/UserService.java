package com.cooksys.socialMedia.services;

import java.util.List;


import com.cooksys.socialMedia.dtos.*;


public interface UserService {
	List<UserResponseDto> getUsers(); 
	UserResponseDto createUser(UserRequestDto userRequestDto);
	UserResponseDto getUser(String username);
	UserResponseDto updateUser(UserRequestDto userRequestDto);
	UserResponseDto deleteUser(String username, CredentialsDto credentialsRequestDto);
	void follow(String username, CredentialsDto credentialsRequestDto);
	void unfollow(String username,CredentialsDto credentialsRequestDto);
	List<TweetResponseDto> getFeed(String username); 
	List<TweetResponseDto> getTweets(String username);
	List<TweetResponseDto> getMentions(String username);
	List<UserResponseDto> getFollowers(String username);
	List<UserResponseDto> getFollowing(String username);
}
