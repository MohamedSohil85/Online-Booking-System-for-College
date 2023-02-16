package com.onlinebooking.dtos.coursedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onlinebooking.entities.Schedule;
import com.onlinebooking.enumerations.ExaminationType;
import com.onlinebooking.enumerations.TypeOfCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedules {

    private String courseName;
    @Enumerated(EnumType.STRING)
    private TypeOfCourse typeOfCourse;
    private String room;
    private String departmentName;
    @JsonFormat(pattern = "HH:mm a")
    private LocalTime course_beginTime;
    @JsonFormat(pattern = "HH:mm a")
    private LocalTime course_endTime;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate examination_date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_begin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_end;
    private ExaminationType examinationType;
}
