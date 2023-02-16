package com.onlinebooking.dtos.coursedto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onlinebooking.enumerations.CourseStatus;
import com.onlinebooking.enumerations.Semster;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.onlinebooking.enumerations.TypeOfCourse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseDto {

    private String courseName;
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(as = LocalDate.class)
    private LocalDate courseBegin;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(as = LocalDate.class)
    private LocalDate courseEnd;
    @Enumerated(EnumType.STRING)
    private TypeOfCourse typeOfCourse;
    @Enumerated(EnumType.STRING)
    private Semster semster;
    private String courseLanguage;
    private String room;
    private int capacity;


}
