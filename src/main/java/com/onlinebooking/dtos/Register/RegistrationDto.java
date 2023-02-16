package com.onlinebooking.dtos.Register;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationDto {

    private String matriculationNumber;
    private String courseName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRegistration;
}
