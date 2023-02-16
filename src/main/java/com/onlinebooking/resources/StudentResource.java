package com.onlinebooking.resources;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.itextpdf.text.DocumentException;
import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.entities.Student;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.persistence.StudentRepository;
import com.onlinebooking.services.StudentService;
import io.quarkus.panache.common.Parameters;
import org.jboss.resteasy.plugins.server.servlet.HttpServletResponseWrapper;

import javax.inject.Inject;

import javax.print.attribute.standard.MediaSize;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Path( "/api")
public class StudentResource {

    @Inject
    StudentService studentService;
    @Inject
    StudentRepository studentRepository;
    @POST
    @Path("/register")

    public Response saveNewStudent(AccountDto accountDto){
        return studentService.signUp(accountDto);
    }


    @GET
    @Path("/students")
    public List<StudentDto> getAllStudents() throws ResourceNotFoundException {
        return studentService.getAllStudents();
    }
    @Path("/findStudentByNameAndBirth")
    @GET
    public StudentDto findStudentByNameAndBirthday(@QueryParam("lastName") String lastName, @QueryParam("firstName") String firstName, @QueryParam("dateOfBirth")Date dateOfBirth) throws ResourceNotFoundException {
        return studentService.findStudentByNameAndBirthday(lastName, firstName, dateOfBirth);
    }
    @Path("/findStudent")
    @GET
    public StudentDto findStudentByName(@QueryParam("lastName") String lastName,@QueryParam("firstName")String firstName) throws ResourceNotFoundException {
        return studentService.getStudentByName(lastName, firstName);
    }
    @Path("/findStudentsByBirth")
    @GET
    public List<StudentDto> findStudentsByBirth(@QueryParam("dateOfBirth")LocalDate dateOfBirth) throws ResourceNotFoundException {
       return studentService.findStudentByBirthday(dateOfBirth);
    }
    @Path("/deleteStudent/{id}")
    @DELETE
    public Response deleteStudentById(@PathParam("id")Long id){
        return studentService.deleteStudentById(id);
    }
    @PUT
    @Path("/edit/{id}/student")
    public StudentDto updateStudent(@PathParam("id")Long id,AccountDto accountDto) throws ResourceNotFoundException {
        return studentService.changeStudentDataById(accountDto, id);
    }
    @DELETE
    @Path("deleteAddress/{id}")
    public void deleteAddress(@PathParam("id")Long id){
        studentService.deleteAddressById(id);
    }
    @GET
    @Path("/student/change-password")
    public String changePassword(@QueryParam("email")String email) throws ResourceNotFoundException {
        return studentService.changePassword(email);
    }
    @PUT
    @Path("/student/change-my-password")
    public Response addNewPassword(@QueryParam("token")String token,AccountDto accountDto){
        return studentService.addNewPasswordByEmail(accountDto,token);
    }
    @GET
    @Path("/student")
    public StudentDto findStudentByNameAndMatNumber(@QueryParam("lastName")String lastName,@QueryParam("firstName")String firstName,@QueryParam("matriculationNumber")String matriculationNumber) throws ResourceNotFoundException {
        return studentService.findStudentByMatriculationNumberAndName(lastName, firstName, matriculationNumber);
    }
    @GET
    @Path("/export-students")
    public Response exportPDF() throws DocumentException, IOException {
        return Response.ok(studentService.load(),MediaType.APPLICATION_OCTET_STREAM).header("content-disposition",
                "attachment; filename = students.pdf").build();
    }
}
