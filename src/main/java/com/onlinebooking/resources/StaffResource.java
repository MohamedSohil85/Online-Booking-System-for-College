package com.onlinebooking.resources;

import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.services.StaffService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StaffResource {

    @Inject
    StaffService staffService;

    @Path("/staff")
    @POST
    @Transactional
    public Response signup(AccountDto accountDto){
        return staffService.addNewStaff(accountDto);
    }


}
