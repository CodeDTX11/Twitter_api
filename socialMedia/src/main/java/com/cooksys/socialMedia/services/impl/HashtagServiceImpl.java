package com.cooksys.socialMedia.services.impl;

import com.cooksys.socialMedia.dtos.HashtagDto;
import com.cooksys.socialMedia.dtos.TweetResponseDto;
import com.cooksys.socialMedia.entities.Hashtag;
import com.cooksys.socialMedia.entities.Tweet;
import com.cooksys.socialMedia.exceptions.NotFoundException;
import com.cooksys.socialMedia.mappers.*;
import com.cooksys.socialMedia.repositories.HashtagRepository;
import com.cooksys.socialMedia.repositories.TweetRepository;
import com.cooksys.socialMedia.repositories.UserRepository;
import com.cooksys.socialMedia.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	  private final HashtagRepository hashtagRepository;
	  private final TweetRepository tweetRepository;
	  private final UserRepository userRepository;
	  
	  private final CredentialsMapper credentialsMapper;
	  private final HashtagMapper hashtagMapper;
	  private final ProfileMapper profileMapper;
	  private final TweetMapper tweetMapper;
	  private final UserMapper userMapper;

	  @Override
	  public List<HashtagDto> getAllHashtags(){
		  return hashtagMapper.entitiesToHashtagDtos(hashtagRepository.findAll());
	  }
	  @Override
	  public List<TweetResponseDto> getTweetsOfHashtag(String label){
		  Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);

		  if(optionalHashtag.isEmpty()){
			  throw new NotFoundException("No hashtag with label: " + label);
		  }

		  //sort in reverse chronological order

		  List<Tweet> tweetList = optionalHashtag.get().getTweetsWithThisHashtag();

//		  tl.sort((o1,o2) -> o1.getPosted().compareTo(o2.getPosted()));
		  tweetList.sort(Comparator.comparing(Tweet::getPosted).reversed());

//		  return tweetMapper.entitiesToTweetResponseDtos(optionalHashtag.get().getTweetsWithThisHashtag());
		  return tweetMapper.entitiesToTweetResponseDtos(tweetList);
//		  return null;
	  }
	  
}
