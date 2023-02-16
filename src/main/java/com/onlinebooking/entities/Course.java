package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinebooking.enumerations.CourseStatus;
import com.onlinebooking.enumerations.Semster;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.onlinebooking.enumerations.CourseStatus;
import com.onlinebooking.enumerations.TypeOfCourse;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course extends PanacheEntity {

    private String courseName;
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate courseBegin;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate courseEnd;
    @Enumerated(EnumType.STRING)
    private Semster semster;
    private String courseLanguage;
    private int capacity;
    @Enumerated(EnumType.STRING)
    private TypeOfCourse typeOfCourse;
    @JsonIgnore
    @OneToMany(mappedBy = "course",fetch=FetchType.EAGER,orphanRemoval = true)
    private List<Registration> registrations;
    @OneToOne(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private Examination examination;
    @OneToMany(mappedBy = "course")
    private List<Schedule>schedules;
    @ManyToOne
    private Staff staff;
    @ManyToOne
    private Department department;
}
