package com.cooksys.socialMedia.mappers;

import com.cooksys.socialMedia.dtos.CredentialsDto;
import com.cooksys.socialMedia.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    CredentialsDto entityToCredentialsDto(Credentials credentials);

    Credentials DtoToEntity(CredentialsDto credentialsDto);
}
