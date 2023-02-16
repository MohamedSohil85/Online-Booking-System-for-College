package com.onlinebooking.mapper;


import com.onlinebooking.dtos.StudentDto.AccountDto;

import com.onlinebooking.entities.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class StudentMapper implements EntityMapper<AccountDto, Student> {

    @Override
    public Student toEntity(AccountDto dto) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(dto,Student.class);
    }

    @Override
    public AccountDto toDto(Student entity) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,AccountDto.class);
    }

    @Override
    public List<Student> toEntity(List<AccountDto> dtoList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Student>students=new ArrayList<>();
        dtoList.forEach(accountDto -> {
            students.add(mapper.map(accountDto,Student.class));
        });

        return students;
    }

    @Override
    public List<AccountDto> toDto(List<Student> entityList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<AccountDto>accounts=new ArrayList<>();
        entityList.forEach(student -> {
            accounts.add(mapper.map(student,AccountDto.class));
        });

        return accounts;
    }
}
