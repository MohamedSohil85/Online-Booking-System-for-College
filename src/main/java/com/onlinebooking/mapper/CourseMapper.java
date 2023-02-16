package com.onlinebooking.mapper;

import com.onlinebooking.dtos.coursedto.CourseDto;
import com.onlinebooking.entities.Course;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper implements EntityMapper<CourseDto,Course> {

    ModelMapper mapper=new ModelMapper();
    @Override
    public Course toEntity(CourseDto dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto,Course.class);
    }

    @Override
    public CourseDto toDto(Course entity) {

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,CourseDto.class);
    }

    @Override
    public List<Course> toEntity(List<CourseDto> dtoList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Course>courses=new ArrayList<>();
        dtoList.forEach(courseDto -> {
            courses.add(mapper.map(courseDto,Course.class));
        });

        return courses;
    }

    @Override
    public List<CourseDto> toDto(List<Course> entityList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<CourseDto>dtoList=new ArrayList<>();
        entityList.forEach(course -> {
            dtoList.add(mapper.map(course,CourseDto.class));
        });

        return dtoList;
    }
}
