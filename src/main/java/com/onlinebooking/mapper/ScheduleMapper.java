package com.onlinebooking.mapper;

import com.onlinebooking.dtos.coursedto.ScheduleOfCourse;
import com.onlinebooking.entities.Schedule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class ScheduleMapper implements EntityMapper<ScheduleOfCourse, Schedule>{

    ModelMapper mapper=new ModelMapper();

    @Override
    public Schedule toEntity(ScheduleOfCourse dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(dto,Schedule.class);
    }

    @Override
    public ScheduleOfCourse toDto(Schedule entity) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,ScheduleOfCourse.class);
    }

    @Override
    public List<Schedule> toEntity(List<ScheduleOfCourse> dtoList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Schedule>schedules=new ArrayList<>();
        dtoList.forEach(scheduleOfCourse -> {
            schedules.add(mapper.map(scheduleOfCourse,Schedule.class));
        });
        return schedules;
    }

    @Override
    public List<ScheduleOfCourse> toDto(List<Schedule> entityList) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ScheduleOfCourse>scheduleOfCourses=new ArrayList<>();
        entityList.forEach(schedule -> {
            scheduleOfCourses.add(mapper.map(schedule,ScheduleOfCourse.class));
        });
        return scheduleOfCourses;
    }
}
