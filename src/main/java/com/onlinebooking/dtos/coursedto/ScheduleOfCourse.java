package com.onlinebooking.dtos.coursedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onlinebooking.enumerations.TypeOfCourse;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleOfCourse {

    private String courseName;
    @Enumerated(EnumType.STRING)
    private TypeOfCourse typeOfCourse;
    private String room;
    @JsonFormat(pattern = "HH:mm a")
    @JsonDeserialize(as = LocalTime.class)
    private LocalTime course_beginTime;
    @JsonFormat(pattern = "HH:mm a")
    @JsonDeserialize(as = LocalTime.class)
    private LocalTime course_endTime;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
