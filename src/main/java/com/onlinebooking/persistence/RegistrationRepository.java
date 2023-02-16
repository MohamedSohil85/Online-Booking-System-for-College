package com.onlinebooking.persistence;

import com.onlinebooking.entities.Registration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationRepository implements PanacheRepository<Registration> {

    public void deleteById(Long courseId,Long studentId) {

            delete("FROM Registration WHERE course_id=:courseId AND student_id=: studentId", Parameters.with("student_id",studentId).and("course_id",courseId));

    }
}
