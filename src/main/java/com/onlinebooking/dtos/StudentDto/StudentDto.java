package com.onlinebooking.dtos.StudentDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.onlinebooking.entities.Course;
import com.onlinebooking.enumerations.Gender;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDto {
    private String matriculationNumber;
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
    private List<Course>courses;
}
