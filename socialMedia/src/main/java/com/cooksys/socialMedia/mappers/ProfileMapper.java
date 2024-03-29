package com.cooksys.socialMedia.mappers;

import com.cooksys.socialMedia.dtos.ProfileDto;
import com.cooksys.socialMedia.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToProfileDto (Profile Profile);

    Profile DtoToEntity(ProfileDto profileDto);
}