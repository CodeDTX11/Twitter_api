package com.cooksys.socialMedia.controllers;

import com.cooksys.socialMedia.dtos.CredentialsDto;
import com.cooksys.socialMedia.dtos.TweetResponseDto;
import com.cooksys.socialMedia.dtos.UserRequestDto;
import com.cooksys.socialMedia.dtos.UserResponseDto;
import com.cooksys.socialMedia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class userController {
	private final UserService userService;

	  @GetMapping
	  public List<UserResponseDto> getUsers() {
		  return userService.getUsers();
	  }
	  @PostMapping
	  public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		  return userService.createUser(userRequestDto);
	  }
	  @GetMapping("/@{username}")
	  public UserResponseDto getUser(@PathVariable String username) {
		  return userService.getUser(username);
	  }
	  
	  @PatchMapping("/@{username}")
	  public UserResponseDto updateUser(@RequestBody UserRequestDto userRequestDto) {
		  return userService.updateUser(userRequestDto);
	  }
	  
	  @DeleteMapping("/@{username}")
	  public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsRequestDto) {
		  return userService.deleteUser(username, credentialsRequestDto);
	  }
	  @PostMapping("/@{username}/follow")
	  public void follow(@PathVariable String username, @RequestBody CredentialsDto credentialsRequestDto){
		  userService.follow(username, credentialsRequestDto);
	  }
	  @PostMapping("/@{username}/unfollow")
	  public void unfollow(@PathVariable String username, @RequestBody CredentialsDto credentialsRequestDto){
		  userService.unfollow(username, credentialsRequestDto);
	  }
	  @GetMapping("/@{username}/feed")
	  public List<TweetResponseDto> getFeed(@PathVariable String username) {
		  return userService.getFeed(username);
	  }
	  
	  @GetMapping("/@{username}/tweets")
	  public List<TweetResponseDto> getTweets(@PathVariable String username) {
		  return userService.getTweets(username);
	  }
	  
	  @GetMapping("/@{username}/mentions")
	  public List<TweetResponseDto> getMentions(@PathVariable String username) {
		  return userService.getMentions(username);
		  
	  }
	  @GetMapping("/@{username}/followers")
	  public List<UserResponseDto> getFollowers(@PathVariable String username) {
		  return userService.getFollowers(username);
	  }
	  
	  @GetMapping("/@{username}/following")
	  public List<UserResponseDto> getFollowing(@PathVariable String username) {
		 return userService.getFollowing(username);
	  }

	  
}
