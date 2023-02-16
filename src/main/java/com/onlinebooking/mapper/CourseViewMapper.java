package com.onlinebooking.mapper;


import com.onlinebooking.dtos.coursedto.CourseView;

import com.onlinebooking.entities.Course;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class CourseViewMapper implements EntityMapper<CourseView, Course> {

    ModelMapper mapper=new ModelMapper();
    @Override
    public Course toEntity(CourseView dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(dto,Course.class);
    }

    @Override
    public CourseView toDto(Course entity) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return mapper.map(entity,CourseView.class);
    }

    @Override
    public List<Course> toEntity(List<CourseView> dtoList) {
        return null;
    }

    @Override
    public List<CourseView> toDto(List<Course> entityList) {
        List<CourseView>courseViewList=new ArrayList<>();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        entityList.forEach(course -> {
            courseViewList.add(mapper.map(course,CourseView.class));
        });
        return courseViewList;
    }
}
