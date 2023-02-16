package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Registration extends PanacheEntity {
    @ManyToOne
    private Course course;
    @ManyToOne
    private Student student;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRegistration;
}
