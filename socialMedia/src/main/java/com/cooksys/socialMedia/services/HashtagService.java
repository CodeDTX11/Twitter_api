package com.cooksys.socialMedia.services;

import java.util.List;


import com.cooksys.socialMedia.dtos.HashtagDto;
import com.cooksys.socialMedia.dtos.TweetResponseDto;

public interface HashtagService {
	List<HashtagDto> getAllHashtags();
	List<TweetResponseDto> getTweetsOfHashtag(String label);
}
