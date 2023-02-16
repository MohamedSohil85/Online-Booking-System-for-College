package com.onlinebooking.resources;

import com.onlinebooking.dtos.coursedto.ScheduleOfCourse;
import com.onlinebooking.entities.Schedule;
import com.onlinebooking.enumerations.TypeOfCourse;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.services.ScheduleService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScheduleResource {

    @Inject
    ScheduleService scheduleService;

    @POST
    @Path("/schedule")
    public ScheduleOfCourse saveSchedule(ScheduleOfCourse schedule) throws ResourceNotFoundException {
        return scheduleService.saveSchedule(schedule);
    }
    @Path("/schedules")
    @GET
    public List<ScheduleOfCourse>getAllScheduleOfCourse(@QueryParam("courseName")String courseName,@QueryParam("TypeOfCourse")TypeOfCourse typeOfCourse) throws ResourceNotFoundException {
        return scheduleService.getSchedules(courseName, typeOfCourse);
    }

}
