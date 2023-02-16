package com.onlinebooking.dtos.NoteDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteApply {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(as = LocalDate.class)
    private LocalDate appliedDate;
    private String mark;
    private String notice;
    private String examinationName;
    private String matriculationNumber;
    private String lastName;
    private String firstName;
}
