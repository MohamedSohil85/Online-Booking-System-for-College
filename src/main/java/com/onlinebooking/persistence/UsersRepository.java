package com.onlinebooking.persistence;


import com.onlinebooking.entities.Users;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<Users> {
    public Users findUsersByNameAndDateOfBirth(String name, Date dateOfBirth){
        return Users.find("lastName = :lastName and dateOfBirth = :dateOfBirth", Parameters.with("lastName",name)
                .and("dateOfBirth",dateOfBirth)).singleResult();
    }
    public List<Users>findUsers(){
        return Users.listAll(Sort.by("lastName").descending());
    }
    public List<Users>findUsersByKeyword(String keyword){
        return Users.find("SELECT * FROM Users WHERE lastName LIKE = :keyword",Parameters.with("keyword",keyword)).list();
    }
    public Optional<Users>findUsersByEmail(String email){
        return Users.find("email",email).singleResultOptional();
    }
}
