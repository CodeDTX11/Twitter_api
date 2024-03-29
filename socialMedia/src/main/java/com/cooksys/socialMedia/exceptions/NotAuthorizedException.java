package com.cooksys.socialMedia.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotAuthorizedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2801732214457257859L;
	private String message;
}
