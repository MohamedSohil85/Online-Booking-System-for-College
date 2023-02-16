package com.onlinebooking.persistence;

import com.onlinebooking.entities.Staff;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class StaffRepository implements PanacheRepository<Staff> {

    public Optional<Staff>findByToken(String token){
        return Staff.find("token",token).singleResultOptional();
    }
}
