package com.onlinebooking.dtos.StaffDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onlinebooking.enumerations.Gender;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StaffDto {

    @Size(max = 256)
    private String firstName;
    @Size(max = 256)
    private String lastName;
    @JsonDeserialize(as = LocalDate.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
    private LocalDate dateOfBirth;
    private String street;
    private String city;
    private String state;
    private String country;
    private int zipcode;
    @Size(max = 256)
    private String phoneNumber;
    private Gender gender;
    @Email
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfRegistration;
    private String role;
}
