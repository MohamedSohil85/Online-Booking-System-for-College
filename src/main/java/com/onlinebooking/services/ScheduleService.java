package com.onlinebooking.services;

import com.onlinebooking.dtos.coursedto.ScheduleOfCourse;
import com.onlinebooking.entities.Course;
import com.onlinebooking.entities.Schedule;
import com.onlinebooking.enumerations.TypeOfCourse;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.ScheduleMapper;
import com.onlinebooking.persistence.CourseRepository;
import com.onlinebooking.persistence.ScheduleRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ScheduleService {

    @Inject
    ScheduleRepository scheduleRepository;
    @Inject
    CourseRepository courseRepository;
    ScheduleMapper scheduleMapper=new ScheduleMapper();

    @Transactional
    public ScheduleOfCourse saveSchedule(ScheduleOfCourse schedule)throws ResourceNotFoundException{
        Optional<Course>optionalCourse=courseRepository.findCourseByNameAndCourseType(schedule.getCourseName(), schedule.getTypeOfCourse());
        Course course=optionalCourse.orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        Schedule schedule_=new Schedule();
        schedule_.setCourse(course);
        schedule_.setCourse_beginTime(schedule.getCourse_beginTime());
        schedule_.setCourse_endTime(schedule.getCourse_endTime());
        schedule_.setDayOfWeek(schedule.getDayOfWeek());
        schedule_.persist();

        return scheduleMapper.toDto(schedule_);

    }
    public List<ScheduleOfCourse> getSchedules(String courseName,TypeOfCourse typeOfCourse)throws ResourceNotFoundException{
        List<Schedule> schedules=courseRepository.findCourseByNameAndCourseType(courseName, typeOfCourse).map(Course::getSchedules).orElseThrow(()->new ResourceNotFoundException("Resource with Name " +courseName+" "+typeOfCourse +" not found"));
        return scheduleMapper.toDto(schedules);
    }


}
