package com.onlinebooking.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.onlinebooking.enumerations.Gender;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@UserDefinition
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Users extends PanacheEntity {
    @NotBlank
    @Size(max = 256)
    private String firstName;
    @NotBlank
    @Size(max = 256)
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
    private LocalDate dateOfBirth;
    private String token;
    @NotBlank
    @Size(max = 256)
    private String phoneNumber;
    @Email
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfRegistration;
    @Username
    private String userName;
    @Password
    private String password;
    @OneToOne(mappedBy = "users",cascade = CascadeType.ALL)
    private Address address;
    @Roles
    private String role;
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ExaminationRegistration> examinationRegistrations;
}
