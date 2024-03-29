package com.cooksys.socialMedia.controllers;

import com.cooksys.socialMedia.dtos.HashtagDto;
import com.cooksys.socialMedia.dtos.TweetResponseDto;
import com.cooksys.socialMedia.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class hashtagController {
	private final HashtagService hashtagService;

	  @GetMapping
	  public List<HashtagDto> getAllHashtags() {
		  return hashtagService.getAllHashtags();
	  }
	  @GetMapping("/{label}")
	  public List<TweetResponseDto> getTweetsOfHashtag(@PathVariable String label) {
		  return hashtagService.getTweetsOfHashtag(label);
	  }
}
