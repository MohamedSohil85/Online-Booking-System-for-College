package com.onlinebooking.persistence;

import com.onlinebooking.entities.ExaminationRegistration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ExamRegistration implements PanacheRepository<ExaminationRegistration> {
    public Optional<ExaminationRegistration>  findExaminationByName(String examName) {
        return ExaminationRegistration.find("examinationName",examName).singleResultOptional();
    }
    public void deleteById(Long examId,Long studentId) {

        delete("from ExaminationRegistration where examination_id = : examId and student_id = : studentId", Parameters.with("student_id",studentId).and("examination_id",examId));

    }
}
