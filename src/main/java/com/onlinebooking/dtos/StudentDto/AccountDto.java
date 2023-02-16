package com.onlinebooking.dtos.StudentDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.onlinebooking.enumerations.Gender;
import com.onlinebooking.util.LocalDateConverter;
import com.onlinebooking.util.LocalDateParamConverterProvider;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDto{
    @NotBlank
    @Size(max = 256)
    private String firstName;
    @NotBlank
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
    private Gender gender;
    private String role;
    private int zipcode;
    @NotBlank
    @Size(max = 256)
    private String phoneNumber;
    @Email
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfRegistration;
    private String userName;
    private String password;
}
