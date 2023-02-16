package com.onlinebooking.persistence;


import com.onlinebooking.entities.Course;
import com.onlinebooking.enumerations.TypeOfCourse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<Course> {

    public Optional<Course>findCourseByName(String courseName) {
    return Course.find("courseName",courseName).singleResultOptional();
    }
    public void deleteById(Long courseId,Long studentId){
        delete("delete from CourseStudent where student_Id=:studentId and courses_Id = : courseId", Parameters.with("students_Id",studentId).and("courses_Id",courseId));
    }
    public Optional<Course>findCourseByNameAndCourseType(String courseName, TypeOfCourse typeOfCourse){
        return Course.find("courseName = : courseName and typeOfCourse = : typeOfCourse",Parameters.with("courseName",courseName).and("typeOfCourse",typeOfCourse)).singleResultOptional();
    }
}
