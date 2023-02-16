package com.onlinebooking.resources;

import com.itextpdf.text.DocumentException;
import com.onlinebooking.dtos.ExaminationDto.ExaminationDto;
import com.onlinebooking.dtos.ExaminationDto.GetExamination;
import com.onlinebooking.entities.Examination;
import com.onlinebooking.entities.ExaminationRegistration;
import com.onlinebooking.entities.Student;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.services.ExaminationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class ExaminationResource {
    @Inject
    ExaminationService examinationService;

    @POST
    @Path("/examination/{id}")
    public Response saveExamination(@PathParam("id")Long id, ExaminationDto dto){
        return examinationService.addExamination(id, dto);
    }
    @POST
    @Path("/examination/register")
    public Response registerToExamination(@QueryParam("matriculationNumber")String matriculationNumber,@QueryParam("examination")String name){
        return examinationService.registerExamination(matriculationNumber, name);
    }
    @GET
    @Path("/examination/{examName}")
    public ExaminationDto findExaminationByName(@PathParam("examName")String name) throws ResourceNotFoundException {
        return examinationService.getExaminationByName(name);
    }


    @PUT
    @Path("/examination/{id}")
    public Response editExamination(@PathParam("id")Long id,ExaminationDto examinationDto){
        return examinationService.changeExaminationInfo(id, examinationDto);
    }
    @GET
    @Path("/export-students/{examName}")
    public Response exportPDF(@PathParam("examName")String name) throws DocumentException {
        return Response.ok(examinationService.load(name),MediaType.APPLICATION_OCTET_STREAM).header("content-disposition",
                "attachment; filename = students-examination.pdf").build();
    }
    @DELETE
    @Path("/delete/registration-examination/{id}")
    public void deleteExamination(@PathParam("id")Long id){
        examinationService.deleteRegistrationOfExamById(id);
    }
    @GET
    @Path("/examinations/{name}")
    public List<GetExamination>getRegistrationByExamName(@PathParam("name")String name){
        return examinationService.getStudentsByExamName(name);
    }
    @GET
    @Path("/examinations-student/{matNumber}")
    public List<GetExamination>getExaminationListByMatNumber(@PathParam("matNumber")String matNumber){
        return examinationService.getExamListByMatNumber(matNumber);
    }
}
