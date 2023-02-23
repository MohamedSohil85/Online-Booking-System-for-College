package com.onlinebooking.resources;

import com.itextpdf.text.DocumentException;
import com.onlinebooking.dtos.NoteDto.NoteApply;
import com.onlinebooking.entities.Student;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.services.NoteService;
import io.quarkus.panache.common.Parameters;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class NoteResource {

    @Inject
    NoteService noteService;

    @POST
    @Path("/note")
    public Response applyNote(NoteApply noteApply,@QueryParam("examName")String examName,@QueryParam("matriculationNumber")String matriculationNumber, @QueryParam("firstName")String firstName,@QueryParam("lastName") String lastName) throws ResourceNotFoundException {
        return noteService.applyNote(noteApply,examName,matriculationNumber,lastName,firstName);
    }
   //  show note of student


    @GET
    @Path("/notes")
    public List<NoteApply>getNotesByStudentName(@QueryParam("lastName")String lastName,@QueryParam("firstName")String firstName) throws ResourceNotFoundException {
        return noteService.getNotesByName(lastName, firstName);
    }
    @GET
    @Path("/findNoteOfStudent/student")
    public List<NoteApply> findNoteByStudentName(@QueryParam("examName")String examName) throws ResourceNotFoundException {
      return   noteService.findNoteByName(examName);

    }
    @GET
    @Path("/export-note-students/{name}")
    public Response exportPDF(@PathParam("name")String name) throws DocumentException, ResourceNotFoundException {
        return Response.ok(noteService.load(name),MediaType.APPLICATION_OCTET_STREAM).header("content-disposition",
                "attachment; filename = students.pdf").build();
    }
   @GET
    @Path("/note-student/{matNumber}")
    public List<NoteApply>fetchAllGradeOfStudentByMatNumber(@PathParam("matNumber")String matNumber) throws ResourceNotFoundException {
        return noteService.getListOfGrade(matNumber);
   }
   @GET
    @Path("/export-Grade-Student/{matNumber}")
    public Response convertToPDF(@PathParam("matNumber")String matNumber) throws DocumentException, ResourceNotFoundException {
        return Response.ok(noteService.loadAllGradeOfStudent(matNumber),MediaType.APPLICATION_OCTET_STREAM).header("content-disposition","attachment; filename = Grade-student.pdf").build();
   }
}
