package com.cooksys.socialMedia.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {

    private String username;

    private CredentialsDto credentials;

    private ProfileDto profile;
}
