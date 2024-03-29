package com.cooksys.socialMedia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialMedia.services.ValidateService;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class validateController {
	private final ValidateService validateService;

	  @GetMapping("/tag/exists/{label}")
	  public boolean doesHashtagExist(@PathVariable String label) {
		  return validateService.doesHashtagExist(label);
	  }
		
	  @GetMapping("/username/exists/@{username}")
	  public boolean doesUsernameExist(@PathVariable String username) {
		  return validateService.doesUsernameExist(username);
	  }
	  @GetMapping("/username/available/@{username}")
	  public boolean isUsernameAvailable(@PathVariable String username) {
		  return validateService.isUsernameAvailable(username);
	  }
	  
}
