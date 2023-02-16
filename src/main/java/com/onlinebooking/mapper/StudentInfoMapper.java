package com.onlinebooking.mapper;


import com.onlinebooking.dtos.StudentDto.StudentDto;

import com.onlinebooking.entities.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class StudentInfoMapper implements EntityMapper<StudentDto, Student>{
    ModelMapper mapper=new ModelMapper();
    @Override
    public Student toEntity(StudentDto dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(dto,Student.class);
    }

    @Override
    public StudentDto toDto(Student entity) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return mapper.map(entity,StudentDto.class);
    }

    @Override
    public List<Student> toEntity(List<StudentDto> dtoList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Student>students=new ArrayList<>();
        dtoList.forEach(studentDto -> {
            students.add(mapper.map(studentDto,Student.class));
        });
        return students;
    }

    @Override
    public List<StudentDto> toDto(List<Student> entityList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
       List<StudentDto>dtoList=new ArrayList<>();
       entityList.forEach(student ->{
           dtoList.add(mapper.map(student,StudentDto.class));
       } );
        return dtoList;
    }
}
