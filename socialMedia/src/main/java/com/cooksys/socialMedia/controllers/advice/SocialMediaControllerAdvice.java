package com.cooksys.socialMedia.controllers.advice;

import com.cooksys.socialMedia.dtos.ErrorDto;
import com.cooksys.socialMedia.exceptions.BadRequestException;
import com.cooksys.socialMedia.exceptions.NotAuthorizedException;
import com.cooksys.socialMedia.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"com.cooksys.socialMedia.controllers"})
@ResponseBody
public class SocialMediaControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(BadRequestException badRequestException) {
        return new ErrorDto(badRequestException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDto handleNotAuthorizedException (NotAuthorizedException notAuthorizedException) {
        return new ErrorDto(notAuthorizedException.getMessage());
    }

}
