package com.cooksys.socialMedia.services;

public interface ValidateService {
	boolean doesHashtagExist(String label);
	boolean doesUsernameExist( String username);
	boolean isUsernameAvailable(String username);
}
