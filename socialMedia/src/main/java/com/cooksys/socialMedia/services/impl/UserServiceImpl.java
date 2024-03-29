package com.cooksys.socialMedia.services.impl;

import com.cooksys.socialMedia.dtos.*;
import com.cooksys.socialMedia.entities.Credentials;
import com.cooksys.socialMedia.entities.Profile;
import com.cooksys.socialMedia.entities.Tweet;
import com.cooksys.socialMedia.entities.User;
import com.cooksys.socialMedia.exceptions.BadRequestException;
import com.cooksys.socialMedia.exceptions.NotFoundException;
import com.cooksys.socialMedia.mappers.*;
import com.cooksys.socialMedia.repositories.HashtagRepository;
import com.cooksys.socialMedia.repositories.TweetRepository;
import com.cooksys.socialMedia.repositories.UserRepository;
import com.cooksys.socialMedia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	  private final HashtagRepository hashtagRepository;
	  private final TweetRepository tweetRepository;
	  private final UserRepository userRepository;
	  
	  private final CredentialsMapper credentialsMapper;
	  private final HashtagMapper hashtagMapper;
	  private final ProfileMapper profileMapper;
	  private final TweetMapper tweetMapper;
	  private final UserMapper userMapper;
	  @Override
	  public List<UserResponseDto> getUsers(){
		  List<User> userList = userRepository.findAll();
		  List<UserResponseDto> responseList = new ArrayList<>();
		  for (User user : userList) {
			  if (!user.isDeleted()) {
				  responseList.add(userMapper.entityToUserResponseDto(user));
			  }
		  }
		  return responseList;
	  }
	  
	  @Override
	  public UserResponseDto createUser(UserRequestDto userRequestDto) {
		  Profile profile = profileMapper.DtoToEntity(userRequestDto.getProfile());
		  Credentials credentials = credentialsMapper.DtoToEntity(userRequestDto.getCredentials());

		  if (profile == null || credentials == null) throw new NotFoundException("Missing fields");
		  if (profile.getEmail() == null || credentials.getUsername() == null || credentials.getPassword() == null)
			  throw new NotFoundException("Missing fields");

		  //Search for user already in repository
		  User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		  if (searchUser != null) {
			  if (searchUser.isDeleted() && searchUser.getCredentials().equals(credentials)) { //If deleted, reactivate account
				  searchUser.setDeleted(false);
				  userRepository.saveAndFlush(searchUser);
				  return userMapper.entityToUserResponseDto(searchUser);
			  } else throw new BadRequestException("Username already in use.");
		  }
		  User user = new User();
		  user.setProfile(profile);
		  user.setCredentials(credentials);
		  userRepository.saveAndFlush(user);
		  return userMapper.entityToUserResponseDto(user);
	  }
	  
	  @Override
	  public UserResponseDto getUser(String username) {
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User is not found");
		  if (user.isDeleted()) throw new NotFoundException("User is not found");
		  return userMapper.entityToUserResponseDto(user);
	  }
	  
	  @Override
	  public  UserResponseDto updateUser(UserRequestDto userRequestDto) {
		  Profile profile = profileMapper.DtoToEntity(userRequestDto.getProfile());
		  Credentials credentials = credentialsMapper.DtoToEntity(userRequestDto.getCredentials());

		  if (profile == null || credentials == null) throw new NotFoundException("Missing fields");

		  //Search for user already in repository
		  User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		  if (searchUser == null) {
			  throw new BadRequestException("User not found.");
		  } else if (searchUser.isDeleted()) {
			  throw new BadRequestException("User is deleted.");
		  }
		  if (!searchUser.getCredentials().equals(credentials)) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  if (profile.getFirstName() != null) searchUser.getProfile().setFirstName(profile.getFirstName());
		  if (profile.getLastName() != null) searchUser.getProfile().setLastName(profile.getLastName());
		  if (profile.getEmail() != null) searchUser.getProfile().setEmail(profile.getEmail());
		  if (profile.getPhone() != null) searchUser.getProfile().setPhone(profile.getPhone());
		  userRepository.saveAndFlush(searchUser);
		  return userMapper.entityToUserResponseDto(searchUser);
	  }
	  
	  @Override
	  public UserResponseDto deleteUser(String username, CredentialsDto credentialsRequestDto) {
		  Credentials credentials = credentialsMapper.DtoToEntity(credentialsRequestDto);
		  //Search for user already in repository
		  User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		  if (searchUser == null) {
			  throw new BadRequestException("User not found.");
		  }
		  if (!searchUser.getCredentials().equals(credentials)) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  searchUser.setDeleted(true);
		  userRepository.saveAndFlush(searchUser);
		  return userMapper.entityToUserResponseDto(searchUser);
	  }
	  
	  @Override
	  public void follow(String username, CredentialsDto credentialsRequestDto) {
		  Credentials credentials = credentialsMapper.DtoToEntity(credentialsRequestDto);
		  User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		  User userToFollow = userRepository.findByCredentialsUsername(username);

		  if (userToFollow == null) throw new NotFoundException("User not found");
		  if (userToFollow.isDeleted()) throw new NotFoundException("User not found");

		  if (searchUser == null) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  if (!searchUser.getCredentials().equals(credentials)) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  if (searchUser.getFollowing().contains(userToFollow) || userToFollow.getFollowers().contains(searchUser)) {
			  throw new BadRequestException("Users already followed");
		  }
		  searchUser.getFollowing().add(userToFollow);
		  userToFollow.getFollowers().add(searchUser);
		  userRepository.saveAndFlush(searchUser);
		  userRepository.saveAndFlush(userToFollow);
	  }
	  
	  @Override
	  public void unfollow(String username,CredentialsDto credentialsRequestDto) {
		  Credentials credentials = credentialsMapper.DtoToEntity(credentialsRequestDto);
		  User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		  User userToUnfollow = userRepository.findByCredentialsUsername(username);

		  if (userToUnfollow == null) throw new NotFoundException("User not found");
		  if (userToUnfollow.isDeleted()) throw new NotFoundException("User not found");

		  if (searchUser == null) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  if (!searchUser.getCredentials().equals(credentials)) {
			  throw new BadRequestException("Incorrect credentials");
		  }
		  if (!searchUser.getFollowing().contains(userToUnfollow) || !userToUnfollow.getFollowers().contains(searchUser)) {
			  throw new BadRequestException("No following relationship between users.");
		  }
		  searchUser.getFollowing().remove(userToUnfollow);
		  userToUnfollow.getFollowers().remove(searchUser);
		  userRepository.saveAndFlush(searchUser);
		  userRepository.saveAndFlush(userToUnfollow);
	  }
	  
	  @Override
	  public List<TweetResponseDto> getFeed(String username){
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User not found");
		  if (user.isDeleted()) throw new NotFoundException("User not found");
		  List<User> following = user.getFollowing();
		  List<Tweet> feed = tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(user);
		  for (User u: following) {
			  feed.addAll(tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(u));
		  }
		  feed.sort(Comparator.comparing(Tweet::getPosted));
//		  Collections.reverse(feed);
		  return tweetMapper.entitiesToTweetResponseDtos(feed);
	  }
	  
	  @Override
	  public  List<TweetResponseDto> getTweets(String username){
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User not found");
		  if (user.isDeleted()) throw new NotFoundException("User not found");
		  List<Tweet> tweets = tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(user);
		  return tweetMapper.entitiesToTweetResponseDtos(tweets);
	  }

	  @Override
	  public  List<TweetResponseDto> getMentions(String username){
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User not found");
		  if (user.isDeleted()) throw new NotFoundException("User not found");
		  /////UNTESTED
		  //List<Tweet> tweets  = tweetRepository.findAllByContentContainingOrderByPostedDesc("@" + username);
		  List<Tweet> tweets = user.getUserMentions();

		  return tweetMapper.entitiesToTweetResponseDtos(tweets);
	  }
	  
	  @Override
	  public List<UserResponseDto> getFollowers(String username){
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User not found");
		  if (user.isDeleted()) throw new NotFoundException("User not found");
		  List<UserResponseDto> responseList = new ArrayList<>();
		  for (User u: user.getFollowers()) {
			  if (!u.isDeleted()) responseList.add(userMapper.entityToUserResponseDto(u));
		  }
		  return responseList;
	  }
	  
	  @Override
	  public List<UserResponseDto> getFollowing(String username){
		  User user = userRepository.findByCredentialsUsername(username);
		  if (user == null) throw new NotFoundException("User not found");
		  if (user.isDeleted()) throw new NotFoundException("User not found");
		  List<UserResponseDto> responseList = new ArrayList<>();
		  for (User u: user.getFollowing()) {
			  if (!u.isDeleted()) responseList.add(userMapper.entityToUserResponseDto(u));
		  }
		  return responseList;
	  }
}
