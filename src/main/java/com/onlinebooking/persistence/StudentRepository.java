package com.onlinebooking.persistence;



import com.onlinebooking.entities.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

    public Optional<Student>findStudentByToken(String token){
        return Student.find("token",token).singleResultOptional();
    }
    public Optional<Student>findStudentByEmail(String email){
        return Student.find("email",email).singleResultOptional();
    }
  public Optional<Student>findStudentByLastNameAndFirstName(String lastname,String firstname){
        return Student.find("lastName = :lastName and firstName = :firstName", Parameters.with("lastName",lastname).and("firstName",firstname)).singleResultOptional();
  }
  public Optional<Student>findStudentByNameAndBirthDay(String lastName, String firstName, LocalDate dateOfBirth){
        return Student.find("lastName = : lastName and firstName = : firstName and dateOfBirth = : dateOfBirth",Parameters.with("lastName",lastName).and("firstName",firstName).and("dateOfBirth",dateOfBirth)).singleResultOptional();
  }
  public Optional<Student>findStudentByMatriculationNumber(String matriculationNumber){
        return Student.find("matriculationNumber = : matriculationNumber",Parameters.with("matriculationNumber",matriculationNumber)).singleResultOptional();
  }
}
