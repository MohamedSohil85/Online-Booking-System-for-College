package com.onlinebooking.dtos.ExaminationDto;

import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.entities.Student;
import com.onlinebooking.enumerations.ExaminationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetExamination {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate examination_date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_begin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_end;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate registration_deadline;
    private String room;
    @Enumerated(EnumType.STRING)
    private ExaminationType examinationType;
    private String examinationName;
    private String firstName;
    private String lastName;
    private String matriculationNumber;
}
