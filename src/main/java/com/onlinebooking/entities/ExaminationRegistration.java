package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExaminationRegistration extends PanacheEntity {

    @ManyToOne
    private Examination examination;
    @ManyToOne
    private Student student;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRegistration;
}
