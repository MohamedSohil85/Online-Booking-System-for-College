package com.onlinebooking.persistence;



import com.onlinebooking.entities.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {
   public Optional<Department>findDepartmentByName(String name){
        return Department.find("departmentName",name).singleResultOptional();
    }
}
