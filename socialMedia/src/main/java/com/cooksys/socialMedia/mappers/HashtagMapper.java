package com.cooksys.socialMedia.mappers;

import com.cooksys.socialMedia.dtos.HashtagDto;
import com.cooksys.socialMedia.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagDto entityToHashtagDto(Hashtag hashtag);

    List<HashtagDto> entitiesToHashtagDtos(List<Hashtag> hashtags);

}
