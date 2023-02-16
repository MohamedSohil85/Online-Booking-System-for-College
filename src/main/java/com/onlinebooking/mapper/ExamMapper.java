package com.onlinebooking.mapper;


import com.onlinebooking.dtos.ExaminationDto.ExaminationDto;
import com.onlinebooking.dtos.StudentDto.AccountDto;

import com.onlinebooking.entities.Examination;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

public class ExamMapper implements EntityMapper<ExaminationDto, Examination>{
    ModelMapper mapper=new ModelMapper();

    @Override
    public Examination toEntity(ExaminationDto dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Examination.class);
    }

    @Override
    public ExaminationDto toDto(Examination entity) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity, ExaminationDto.class);
    }

    @Override
    public List<Examination> toEntity(List<ExaminationDto> dtoList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Examination>examinations=new ArrayList<>();
        dtoList.forEach(examinationDto -> {
            examinations.add(mapper.map(examinationDto,Examination.class));
        });

        return examinations;
    }

    @Override
    public List<ExaminationDto> toDto(List<Examination> entityList) {
        return null;
    }
}
