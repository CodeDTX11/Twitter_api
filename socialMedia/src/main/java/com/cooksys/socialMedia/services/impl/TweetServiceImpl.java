package com.cooksys.socialMedia.services.impl;

import com.cooksys.socialMedia.dtos.*;
import com.cooksys.socialMedia.entities.Credentials;

import com.cooksys.socialMedia.entities.Hashtag;
import com.cooksys.socialMedia.entities.Tweet;
import com.cooksys.socialMedia.entities.User;
import com.cooksys.socialMedia.exceptions.BadRequestException;
import com.cooksys.socialMedia.exceptions.NotAuthorizedException;
import com.cooksys.socialMedia.exceptions.NotFoundException;
import com.cooksys.socialMedia.mappers.*;
import com.cooksys.socialMedia.repositories.HashtagRepository;
import com.cooksys.socialMedia.repositories.TweetRepository;
import com.cooksys.socialMedia.repositories.UserRepository;
import com.cooksys.socialMedia.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final HashtagRepository hashtagRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    private final CredentialsMapper credentialsMapper;
    private final HashtagMapper hashtagMapper;
    private final ProfileMapper profileMapper;
    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;


    private Tweet findTweet(Long tweetId) {
        Optional<Tweet> optTweet = tweetRepository.findByIdAndDeletedFalse(tweetId);
        if (optTweet.isEmpty())
            throw new NotFoundException("No tweet with id: " + tweetId);
        return optTweet.get();
    }

    private User verifyUserCredentials(Credentials requestCredentials) {

        Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(requestCredentials.getUsername());

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Username: " + requestCredentials.getUsername() + " not found");
        }

        if (!requestCredentials.getPassword().equals(optionalUser.get().getCredentials().getPassword())) {
            throw new NotAuthorizedException("Incorrect password");
        }

        return optionalUser.get();
    }

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToTweetResponseDtos(tweetRepository.findAllByOrderByPostedDesc());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {

        if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
        || tweetRequestDto.getCredentials().getUsername() == null || tweetRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("All fields must be provided");
        }

        User verifiedUser = verifyUserCredentials(credentialsMapper.DtoToEntity(tweetRequestDto.getCredentials()));
        String content = tweetRequestDto.getContent();

        Tweet createdTweet = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);

//        tweetRepository.save(createdTweet);
//        System.out.println(createdTweet.getId());

        List<Hashtag> hashtags = new ArrayList<>();
        List<User> usersMentioned = new ArrayList<>();

        String[] tokens = content.split("\\s+");

        for (String token : tokens) {
            if (token.charAt(0) == '@') {
//                String usernameMentioned = st.substring(1);
                User userMentioned = userRepository.findByCredentialsUsername(token.substring(1));
                if (userMentioned != null) {
                    usersMentioned.add(userMentioned);
                }
                //idk what to do if user mentioned isnt an actual user, dont think we should throw exception
            } else if (token.charAt(0) == '#') {

                Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(token.substring(1));

                if (optionalHashtag.isEmpty()) {
                    //hashtag label not in database, need to create one
                    Hashtag newHashtag = new Hashtag();
                    newHashtag.setLabel(token.substring(1));
                    newHashtag.setTweetsWithThisHashtag(Collections.singletonList(createdTweet));
                    hashtagRepository.saveAndFlush(newHashtag);
                    hashtags.add(newHashtag);

                } else {
                    //hashtag already used, update timestamp
//                    System.out.println(optionalHashtag.get().getLastUsed());
                    Hashtag hashtag = optionalHashtag.get();
                    List<Tweet> tweets = hashtag.getTweetsWithThisHashtag();
                    tweets.add(createdTweet);
                    hashtag.setTweetsWithThisHashtag(tweets);
//                    hashtag.getTweetsWithThisHashtag().add(createdTweet);
                    hashtagRepository.saveAndFlush(hashtag);
                    hashtags.add(hashtag);
//                    System.out.println(optionalHashtag.get().getLastUsed());
                }
            }
        }

        createdTweet.setAuthor(verifiedUser);
        verifiedUser.getAuthoredTweets().add(createdTweet);

        createdTweet.setHashtagsOnThisTweet(hashtags);
        createdTweet.setUsersMentionedByThisTweet(usersMentioned);

        tweetRepository.saveAndFlush(createdTweet);

        for(User u : usersMentioned){
            u.getUserMentions().add(createdTweet);
        }
        userRepository.saveAllAndFlush(usersMentioned);

        return tweetMapper.entityToTweetResponseDto(createdTweet);
    }

    @Override
    public TweetResponseDto getTweet(long id) {
        return tweetMapper.entityToTweetResponseDto(findTweet(id));
    }

    @Override
    public TweetResponseDto deleteTweet(long id, CredentialsDto credentialsDto) {//404
        Credentials credentials = credentialsMapper.DtoToEntity(credentialsDto);
        User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
        if (searchUser == null || searchUser.isDeleted()) {
            throw new BadRequestException("User not found.");
        }
        if (!searchUser.getCredentials().equals(credentials)) throw new NotAuthorizedException("Incorrect credentials");

        Tweet tweetToDelete = findTweet(id);

        if(!tweetToDelete.getAuthor().equals(searchUser)){
            throw new NotAuthorizedException("Username "+ searchUser.getCredentials().getUsername() + " is not the author of this tweet" );
        }
// if there is an exception it will be thrown by findTweet(id)
//		 if (tweetToDelete == null) throw new BadRequestException("Tweet not found.");

        tweetToDelete.setDeleted(true);
//		 searchUser.getAuthoredTweets().remove(tweetToDelete); // dont think we need to remove relationship, since its a "soft" delete
        tweetRepository.saveAndFlush(tweetToDelete);
//		 userRepository.saveAndFlush(searchUser);
        return tweetMapper.entityToTweetResponseDto(tweetToDelete);
    }

    @Override
    public void likeTweet(long id, CredentialsDto credentialsRequestDto) {
        Credentials credentials = credentialsMapper.DtoToEntity(credentialsRequestDto);
        User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());
        Tweet tweetToLike = findTweet(id);

        if (searchUser == null || searchUser.isDeleted())
            throw new BadRequestException("User not found");
        if (!searchUser.getCredentials().equals(credentials))
            throw new NotAuthorizedException("Incorrect credentials");
//		if (tweetToLike == null)
//			throw new BadRequestException("Tweet not found.");

        if(!tweetToLike.getUsersThatLikedThisTweet().contains(searchUser)) {
            tweetToLike.getUsersThatLikedThisTweet().add(searchUser);
            searchUser.getUserLikedTweets().add(tweetToLike);
            tweetRepository.saveAndFlush(tweetToLike);
            userRepository.saveAndFlush(searchUser);
        }

    }

    @Override
    public TweetResponseDto replyTweet(long id, TweetRequestDto tweetRequestDto) {

        if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials().getUsername() == null
                || tweetRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("All fields must be provided");
        }

        Credentials credentials = credentialsMapper.DtoToEntity(tweetRequestDto.getCredentials());
        User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());

        if (searchUser == null || searchUser.isDeleted())
            throw new BadRequestException("User not found.");

        if (!searchUser.getCredentials().equals(credentials))
            throw new NotAuthorizedException("Incorrect credentials");

        Optional<Tweet> tweetToCheck = tweetRepository.findByIdAndDeletedFalse(id);
        if (tweetToCheck.isEmpty()) {
            throw new BadRequestException("Tweet not found.");
        }

        Tweet tweetToReplyTo = tweetToCheck.get();

        Tweet newReplyTweet = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);

        newReplyTweet.setInReplyTo(tweetToReplyTo);
        newReplyTweet.setAuthor(searchUser);
        tweetToReplyTo.getTweetReplies().add(newReplyTweet);

        tweetRepository.saveAndFlush(newReplyTweet);
        tweetRepository.saveAndFlush(tweetToReplyTo);

        return tweetMapper.entityToTweetResponseDto(newReplyTweet);
    }

    @Override
    public TweetResponseDto repostTweet(long id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsMapper.DtoToEntity(credentialsDto);
        User searchUser = userRepository.findByCredentialsUsername(credentials.getUsername());

        if (searchUser == null || searchUser.isDeleted())
            throw new BadRequestException("User not found.");
        if (!searchUser.getCredentials().equals(credentials))
            throw new NotAuthorizedException("Incorrect credentials");

        Tweet tweetToRepost = findTweet(id);
//		 if (tweetToRepost == null)
//			 throw new BadRequestException("Tweet not found.");

        //set contents of new tweet
//		 Tweet newRepostTweet = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);
        Tweet newRepostTweet = new Tweet();

        newRepostTweet.setAuthor(searchUser);
        newRepostTweet.setContent(null);
        newRepostTweet.setInReplyTo(null);
        newRepostTweet.setRepostOf(tweetToRepost);

        //add new tweet to the list of tweets that have reposted the old tweet, then save both to the repository
        tweetToRepost.getTweetReposts().add(newRepostTweet);
        tweetRepository.saveAndFlush(newRepostTweet);
        tweetRepository.saveAndFlush(tweetToRepost);
        return tweetMapper.entityToTweetResponseDto(newRepostTweet);
    }

    @Override
    public List<HashtagDto> getTagsOfTweet(long id) {
        return hashtagMapper.entitiesToHashtagDtos(findTweet(id).getHashtagsOnThisTweet());
    }

    @Override
    public List<UserResponseDto> getUsersThatLikedTweet(long id) {
        List<User> usersThatLiked = findTweet(id).getUsersThatLikedThisTweet();
        usersThatLiked.removeIf(User::isDeleted);
        return userMapper.entitiesToUserResponseDtos(usersThatLiked);
    }

    @Override
    public ContextDto getContext(long id) {

        Tweet targetTweet = findTweet(id);
        ContextDto contextDto = new ContextDto();
        contextDto.setTarget(tweetMapper.entityToTweetResponseDto(targetTweet));
        List<Tweet> beforeTweets = new ArrayList<>();
        Tweet curr = targetTweet.getInReplyTo();

        while (curr != null) {
            if (!curr.isDeleted()) {
                beforeTweets.add(curr);
            }
            curr = curr.getInReplyTo();
        }
        Collections.reverse(beforeTweets);
        contextDto.setBefore(tweetMapper.entitiesToTweetResponseDtos(beforeTweets));
        //////////////////////////////////////////////
        List<Tweet> afterTweets = new ArrayList<>();

        if (!targetTweet.getTweetReplies().isEmpty()) {

            Stack<Tweet> tweetStack = new Stack<>();
            List<Tweet> replies = targetTweet.getTweetReplies();

            replies.sort(Comparator.comparing(Tweet::getPosted).reversed());
//            for (Tweet twt : replies) {
//                tweetStack.add(twt);
//            }
            tweetStack.addAll(replies);

            while (!tweetStack.isEmpty()) {
                Tweet t = tweetStack.pop();

                if(!t.isDeleted()) {
                    afterTweets.add(t);
                }

                if (!t.getTweetReplies().isEmpty()) {
                    replies = t.getTweetReplies();
                    replies.sort(Comparator.comparing(Tweet::getPosted).reversed());

                    for (Tweet twt : replies) {
                        tweetStack.push(twt);
                    }
                }
            }
        }
        ///////////////////////////////////////////
        contextDto.setAfter(tweetMapper.entitiesToTweetResponseDtos(afterTweets));
        return contextDto;
    }

    @Override
    public List<TweetResponseDto> getReplies(long id) {
        List<Tweet> tweetReplies = findTweet(id).getTweetReplies();
        tweetReplies.removeIf(Tweet::isDeleted);
        return tweetMapper.entitiesToTweetResponseDtos(tweetReplies);
    }

    @Override
    public List<TweetResponseDto> getReposts(long id) {
        List<Tweet> tweetReposts = findTweet(id).getTweetReposts();
        tweetReposts.removeIf(Tweet::isDeleted);
        return tweetMapper.entitiesToTweetResponseDtos(tweetReposts);
    }

    @Override
    public List<UserResponseDto> getMentions(long id) {
        List<User> mentionedUsers = findTweet(id).getUsersMentionedByThisTweet();
        mentionedUsers.removeIf(User::isDeleted);
        return userMapper.entitiesToUserResponseDtos(mentionedUsers);
    }
}
