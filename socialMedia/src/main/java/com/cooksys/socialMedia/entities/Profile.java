package com.cooksys.socialMedia.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;
}
