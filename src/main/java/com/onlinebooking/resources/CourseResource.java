package com.onlinebooking.resources;

import com.itextpdf.text.DocumentException;
import com.onlinebooking.dtos.Register.RegistrationDto;
import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.dtos.coursedto.CourseDto;
import com.onlinebooking.dtos.coursedto.CourseView;
import com.onlinebooking.entities.Course;
import com.onlinebooking.entities.Student;
import com.onlinebooking.enumerations.Semster;
import com.onlinebooking.enumerations.TypeOfCourse;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.services.CourseService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/api")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class CourseResource {

    @Inject
    CourseService courseService;

    @POST
    @Path("/course/{departmentName}")
    public CourseDto addCourse(CourseDto courseDto,@PathParam("departmentName") String departmentName) throws ResourceNotFoundException {
        return courseService.addNewCourse(courseDto, departmentName);
    }
    @GET
    @Path("/coursesInWS")
    public List<CourseDto>getCoursesInWinter() throws ResourceNotFoundException {
        return courseService.getAllCoursesInWinter();
    }
    @GET
    @Path("/coursesInSummer")
    public List<CourseDto>getCoursesInSummer() throws ResourceNotFoundException {
        return courseService.getAllSummerCourses();
    }
    @GET
    @Path("/courses")
    public List<CourseView>getALlCourses() throws ResourceNotFoundException {
        return courseService.getAllCourses();
    }
    @GET
    @Path("/course_status/{name}")
    public CourseView getCourseStatus(@PathParam("name")String name) throws ResourceNotFoundException {
        return courseService.getCourseStatus(name);
    }
    @POST
    @Path("/signup")
    public Response signup(@QueryParam("matriculationNumber")String matriculationNumber, @QueryParam("courseName")String courseName, @QueryParam("TypeOfCourse")TypeOfCourse typeOfCourse) throws ResourceNotFoundException {
        return courseService.signup(matriculationNumber, courseName, typeOfCourse);
    }
    @POST
    @Path("/signUpCourse")
    public Response register(@QueryParam("token")String token,@QueryParam("courseName")String courseName){
        return courseService.register(token,courseName);
    }
    @GET
    @Path("/courses/{name}")
    public List<CourseView>fetchAllCoursesByDepartmentName(@PathParam("name")String name) throws ResourceNotFoundException {
        return courseService.fetchCoursesByDepartmentName(name);
    }
    @GET
    @Path("/courses/{name}/semster")
    public List<CourseView>fetchCoursesOfDepartmentByNameAndSemster(@PathParam("name")String name,@QueryParam("semster") Semster semster) throws ResourceNotFoundException {
        return courseService.fetchCoursesByDepartmentNameAndTerm(name, semster);
    }
    @DELETE
    @Path("/delete/{id}")
    public Response deleteById(@PathParam("id")Long id){
        return courseService.deleteCourseById(id);
    }
    @GET
    @Path("/courses/students")
    public List<RegistrationDto>fetchCoursesOfStudentByNameAndBirth(@QueryParam("lastName")String lastName, @QueryParam("firstName")String firstName, @QueryParam("dateOfBirth")LocalDate date) throws ResourceNotFoundException {
        return courseService.fetchAllCoursesByStudentNameAndBirthday(lastName, firstName, date);
    }

    // update info of courses
    @PUT
    @Path("/update/{courseName}/course")
    public CourseDto updateCourseInfos(@PathParam("courseName")String courseName,CourseDto courseDto) throws ResourceNotFoundException {
        return courseService.updateCourseByName(courseName, courseDto);
    }
    @DELETE
    @Path("/delete/course-registration/{registrationId}")
    public void deleteStudentFromCourse(@PathParam("registrationId")Long registrationId){
        courseService.deleteStudent(registrationId);
    }
    @GET
    @Path("/export-students-course/{name}/courseType/{courseType}")
    public Response exportPDF(@PathParam("name")String name,@PathParam("courseType")TypeOfCourse typeOfCourse) throws DocumentException {
        return Response.ok(courseService.convertToPDF(name,typeOfCourse),MediaType.APPLICATION_OCTET_STREAM).header("content-disposition",
                "attachment; filename = students-course.pdf").build();
    }

}
