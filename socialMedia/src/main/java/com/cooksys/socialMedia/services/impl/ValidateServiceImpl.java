package com.cooksys.socialMedia.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.socialMedia.mappers.CredentialsMapper;
import com.cooksys.socialMedia.mappers.HashtagMapper;
import com.cooksys.socialMedia.mappers.ProfileMapper;
import com.cooksys.socialMedia.mappers.TweetMapper;
import com.cooksys.socialMedia.mappers.UserMapper;
import com.cooksys.socialMedia.repositories.HashtagRepository;
import com.cooksys.socialMedia.repositories.TweetRepository;
import com.cooksys.socialMedia.repositories.UserRepository;
import com.cooksys.socialMedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	  private final HashtagRepository hashtagRepository;
	  private final TweetRepository tweetRepository;
	  private final UserRepository userRepository;
	  
	  private final CredentialsMapper credentialsMapper;
	  private final HashtagMapper hashtagMapper;
	  private final ProfileMapper profileMapper;
	  private final TweetMapper tweetMapper;
	  private final UserMapper userMapper;
	  
	  @Override
	  public boolean doesHashtagExist(String label) {
		  //toLowercase?
          return hashtagRepository.findByLabel(label).isPresent();
	  }
	  
	  @Override
	  public boolean doesUsernameExist( String username) {
//		  return userRepository.findByCredentialsUsername(username).isPresent();
		  return userRepository.findByCredentialsUsername(username) != null;
	  }
	  
	  @Override
	  public boolean isUsernameAvailable(String username) {
//		  return userRepository.findByCredentialsUsername(username).isEmpty();
		  return userRepository.findByCredentialsUsername(username) == null;
	  }
	
}
