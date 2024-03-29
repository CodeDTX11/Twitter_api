package com.cooksys.socialMedia.mappers;

import com.cooksys.socialMedia.dtos.UserRequestDto;
import com.cooksys.socialMedia.dtos.UserResponseDto;
import com.cooksys.socialMedia.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

    User userRequestDtoToEntity(UserRequestDto userRequestDto);
    @Mapping(target = "username", source = "user.credentials.username")
    UserResponseDto entityToUserResponseDto(User user);

    @Mapping(target = "username", source = "user.credentials.username")
    List<UserResponseDto> entitiesToUserResponseDtos(List<User> users);

}
