package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Note extends PanacheEntity  {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(as = LocalDate.class)
    private LocalDate appliedDate;
    private String mark;
    private String notice;
    @ManyToOne
    private Examination examination;
    @ManyToOne
    private Student student;

}
