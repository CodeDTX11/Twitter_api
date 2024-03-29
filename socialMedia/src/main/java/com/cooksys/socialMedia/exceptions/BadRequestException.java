package com.cooksys.socialMedia.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{
    /**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -3707835410684502803L;
	private String message;
}