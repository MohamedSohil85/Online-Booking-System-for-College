package com.onlinebooking.services;

import com.onlinebooking.dtos.DepartmentDto.DepartmentDto;
import com.onlinebooking.entities.Department;
import com.onlinebooking.mapper.DepartmentMapper;
import com.onlinebooking.persistence.DepartmentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ApplicationScoped
public class DepartmentService {

    DepartmentMapper departmentMapper=new DepartmentMapper();
    @Inject
    DepartmentRepository departmentRepository;

    @Transactional
   public Response addNewDepartment(DepartmentDto departmentDto){
       List<Department>departments=departmentRepository.listAll();
       List<DepartmentDto>departments_=departmentMapper.toDto(departments);
       for (DepartmentDto department:departments_){
           if (departmentDto.getDepartmentName().equalsIgnoreCase(department.getDepartmentName())){
               return Response.status(Response.Status.FOUND).build();
           }
       }
       Department department_=departmentMapper.toEntity(departmentDto);
       departmentRepository.persist(department_);
       return  Response.status(Response.Status.CREATED).build();
   }
   @Transactional
   public Response deleteDepartmentById(Long id){
       List<Department>departments=departmentRepository.listAll();
       if (departments.isEmpty()){
           return Response.status(204, "List is Empty").build();
       }
       Optional<Department> department=departmentRepository.findByIdOptional(id);
       Department dep=department.orElseGet(null);
       departmentRepository.delete(dep);
       return Response.status(200,"Resource deleted !").build();

   }
}
