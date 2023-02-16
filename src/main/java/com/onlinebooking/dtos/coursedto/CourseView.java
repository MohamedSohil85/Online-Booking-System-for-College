package com.onlinebooking.dtos.coursedto;

import com.onlinebooking.dtos.DepartmentDto.DepartmentDto;
import com.onlinebooking.dtos.ExaminationDto.ExaminationDto;
import com.onlinebooking.dtos.StaffDto.StaffDto;
import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.entities.Department;
import com.onlinebooking.entities.Examination;
import com.onlinebooking.entities.Schedule;
import com.onlinebooking.entities.Staff;
import com.onlinebooking.enumerations.CourseStatus;
import com.onlinebooking.enumerations.ExaminationType;
import com.onlinebooking.enumerations.Semster;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseView {

    private String courseName;
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate courseBegin;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate courseEnd;
    @Enumerated(EnumType.STRING)
    private Semster semster;
    private String courseLanguage;
    private int capacity;
   private List<Schedule>schedules;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate examination_date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_begin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_end;
    @Enumerated(EnumType.STRING)
    private ExaminationType examinationType;
    private String room;
    private DepartmentDto department;

}
