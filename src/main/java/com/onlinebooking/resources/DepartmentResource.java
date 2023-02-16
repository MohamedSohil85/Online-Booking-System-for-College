package com.onlinebooking.resources;

import com.onlinebooking.dtos.DepartmentDto.DepartmentDto;
import com.onlinebooking.services.DepartmentService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class DepartmentResource {
    @Inject
    DepartmentService departmentService;

    @Path("/department")
    @POST
    public Response saveNewDepartment(DepartmentDto departmentDto){
        return departmentService.addNewDepartment(departmentDto);
    }
}
