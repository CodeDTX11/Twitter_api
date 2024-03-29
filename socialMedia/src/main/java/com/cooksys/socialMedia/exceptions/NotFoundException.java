package com.cooksys.socialMedia.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1793545539036533121L;
	private String message;
}
