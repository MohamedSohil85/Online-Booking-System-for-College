package com.onlinebooking.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinebooking.enumerations.ExaminationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Examination extends PanacheEntity {

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
    @OneToOne
    @JsonIgnore
    private Course course;
    @OneToMany(mappedBy = "examination",cascade = CascadeType.ALL)
    private List<Note> notes;
    @OneToMany(mappedBy = "examination",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<ExaminationRegistration>examinationRegistrationList;
}
