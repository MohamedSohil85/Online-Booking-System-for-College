package com.onlinebooking.persistence;



import com.onlinebooking.entities.Examination;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ExaminationRepository implements PanacheRepository<Examination> {
   public Optional<Examination>findExaminationByName(String name){
        return Examination.find("examinationName",name).singleResultOptional();
    }
}
