package com.cooksys.socialMedia.mappers;

import com.cooksys.socialMedia.dtos.TweetRequestDto;
import com.cooksys.socialMedia.dtos.TweetResponseDto;
import com.cooksys.socialMedia.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

    Tweet tweetRequestDtoToEntity(TweetRequestDto tweetRequestDto);

    TweetResponseDto entityToTweetResponseDto(Tweet tweet);

    List<TweetResponseDto> entitiesToTweetResponseDtos(List<Tweet> tweets);

}
