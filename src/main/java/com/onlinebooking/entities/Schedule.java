package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Schedule extends PanacheEntity {

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(as = LocalTime.class)
    private LocalTime course_beginTime;
    @JsonDeserialize(as = LocalTime.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime course_endTime;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private String room;
    @ManyToOne
    private Course course;
}
