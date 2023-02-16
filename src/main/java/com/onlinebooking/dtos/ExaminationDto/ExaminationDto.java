package com.onlinebooking.dtos.ExaminationDto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onlinebooking.enumerations.ExaminationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExaminationDto {
    @JsonDeserialize(as = LocalDate.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate examination_date;
    @JsonDeserialize(as = LocalTime.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_begin;
    @JsonDeserialize(as = LocalTime.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime examination_end;
    @JsonDeserialize(as = LocalDate.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate registration_deadline;
    private String room;
    @Enumerated(EnumType.STRING)
    private ExaminationType examinationType;
    private String examinationName;
}
