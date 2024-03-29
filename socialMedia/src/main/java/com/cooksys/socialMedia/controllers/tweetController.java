package com.cooksys.socialMedia.controllers;

import com.cooksys.socialMedia.dtos.*;
import com.cooksys.socialMedia.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class tweetController {
	private final TweetService tweetService;

	  @GetMapping
	  public List<TweetResponseDto> getAllTweets() {
		  return tweetService.getAllTweets();
	  }
	  @PostMapping
	  public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		  return tweetService.createTweet(tweetRequestDto);
	  }
	  @GetMapping("/{id}")
	  public TweetResponseDto getTweet(@PathVariable long id) {
		  return tweetService.getTweet(id);
	  }
	  @DeleteMapping("/{id}")
	  public TweetResponseDto deleteTweet(@PathVariable long id, @RequestBody CredentialsDto credentialsRequestDto) {
		  return tweetService.deleteTweet(id, credentialsRequestDto);
	  }
	  @PostMapping("/{id}/like")
	  public void likeTweet(@PathVariable long id, @RequestBody CredentialsDto credentialsRequestDto) {
		  tweetService.likeTweet(id, credentialsRequestDto);//
	  }
	  @PostMapping("/{id}/reply")
	  public TweetResponseDto replyTweet(@PathVariable long id,  @RequestBody TweetRequestDto tweetRequestDto) {
		  return tweetService.replyTweet(id,tweetRequestDto);
	  }
	  @PostMapping("/{id}/repost")
	  public TweetResponseDto repostTweet(@PathVariable long id, @RequestBody CredentialsDto credentialsRequestDto) {
		  return tweetService.repostTweet(id, credentialsRequestDto);
	  }
	  @GetMapping("/{id}/tags")
	  public List<HashtagDto> getTagsOfTweet(@PathVariable long id) {
		  return tweetService.getTagsOfTweet(id);
	  }
	  @GetMapping("/{id}/likes")
	  public List<UserResponseDto> getUsersThatLikedTweet(@PathVariable long id) {
		  return tweetService.getUsersThatLikedTweet(id);
	  }
	  @GetMapping("/{id}/context")
	  public ContextDto getContext(@PathVariable long id) {//right return type??
		  return tweetService.getContext(id);
	  }
	  @GetMapping("/{id}/replies")
	  public List<TweetResponseDto> getReplies(@PathVariable long id) {
		  return tweetService.getReplies(id);
	  }
	  @GetMapping("/{id}/reposts")
	  public List<TweetResponseDto> getReposts(@PathVariable long id) {
		  return tweetService.getReposts(id);
	  }
	  @GetMapping("/{id}/mentions")
	  public List<UserResponseDto> getMentions(@PathVariable long id) {
		  return tweetService.getMentions(id);
	  }
}
