package com.onlinebooking.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onlinebooking.dtos.Register.RegistrationDto;
import com.onlinebooking.dtos.coursedto.CourseDto;
import com.onlinebooking.dtos.coursedto.CourseView;
import com.onlinebooking.entities.Department;
import com.onlinebooking.entities.Registration;
import com.onlinebooking.enumerations.CourseStatus;
import com.onlinebooking.enumerations.Semster;
import com.onlinebooking.entities.Course;
import com.onlinebooking.entities.Student;
import com.onlinebooking.enumerations.TypeOfCourse;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.CourseMapper;
import com.onlinebooking.mapper.CourseViewMapper;
import com.onlinebooking.mapper.RegistrationMapper;
import com.onlinebooking.mapper.StudentInfoMapper;
import com.onlinebooking.persistence.*;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CourseService {

    @Inject
    CourseRepository courseRepository;
    @Inject
    DepartmentRepository departmentRepository;
    CourseMapper courseMapper=new CourseMapper();
    CourseViewMapper courseViewMapper=new CourseViewMapper();
    @Inject
    StudentRepository studentRepository;
    @Inject
    RegistrationRepository registrationRepository;
    @Inject
    StaffRepository staffRepository;
    StudentInfoMapper studentInfoMapper=new StudentInfoMapper();
    RegistrationMapper registrationMapper=new RegistrationMapper();
    @Transactional
    public CourseDto addNewCourse(CourseDto courseDto,String departmentName)throws ResourceNotFoundException {
        return departmentRepository.findDepartmentByName(departmentName).map(department -> {
            List<Course>courses=courseRepository.listAll();
            List<CourseDto>coursesList=courseMapper.toDto(courses);
            for (CourseDto courseDto1:coursesList){
                if (courseDto1.getTypeOfCourse().equals(courseDto.getTypeOfCourse()) && courseDto1.getCourseName().equals(courseDto.getCourseName())){
                    return courseDto1;
                }
            }
            Course course=courseMapper.toEntity(courseDto);
            department.getCourses().add(course);
            course.setDepartment(department);
            department.persist();
            return courseDto;
        }).orElseThrow(()->new ResourceNotFoundException("no Resource with Name: "+departmentName));
    }

    // get all courses of Winter
   public List<CourseDto> getAllCoursesInWinter()throws ResourceNotFoundException{
       List<Course>courses=courseRepository
               .findAll()
               .stream()
               .filter(course -> course.getSemster().equals(Semster.Winter))
               .collect(Collectors.toList());

       if (courses.isEmpty()){
            throw new ResourceNotFoundException("list is Empty");
        }

        return courseMapper.toDto(courses);
   }
    // get all courses of Summer
    public List<CourseDto>getAllSummerCourses()throws ResourceNotFoundException{
        List<Course>courses=courseRepository
                .findAll()
                .stream()
                .filter(course -> course.getSemster().equals(Semster.Summer))
                .collect(Collectors.toList());

        if (courses.isEmpty()){
            throw new ResourceNotFoundException("list is Empty");
        }

        return courseMapper.toDto(courses);
    }
    // get list of summer and winter course
    public List<CourseView>getAllCourses()throws ResourceNotFoundException{
        List<Course>courses= courseRepository.listAll();

        if (courses.isEmpty()){
            throw new ResourceNotFoundException("list is Empty");
        }

        return courseViewMapper.toDto(courses);
    }
    // get status of course
    public CourseView getCourseStatus(String name)throws ResourceNotFoundException{
        Optional<Course> course=courseRepository.findCourseByName(name);

        if (!course.isPresent()){
            throw new ResourceNotFoundException("Course not found !");
        }
        Course course_=course.get();

        return courseViewMapper.toDto(course_);
    }
    // sign up to the course
   @Transactional
    public Response signup(String matriculationNumber, String courseName,TypeOfCourse typeOfCourse){
        return studentRepository.findStudentByMatriculationNumber(matriculationNumber).map(student -> {
            Optional<Course> course=courseRepository.findCourseByNameAndCourseType(courseName, typeOfCourse);
            Course course_=course.get();
            RegistrationDto registrationDto=new RegistrationDto(matriculationNumber,courseName,LocalDate.now());
            Registration registration=registrationMapper.toEntity(registrationDto);

           List<Registration>registrations=registrationRepository.listAll();
           for (Registration registration_:registrations){
               if (registration_.getCourse().equals(course_) && registration_.getStudent().equals(student)){
                   return Response.status(Response.Status.FOUND).build();
               }
           }
            int capacity_=course_.getCapacity();

                if (capacity_ == 0) {
                    course_.setCourseStatus(CourseStatus.overfilled);
                    return Response.status(200,"No place").build();

                }

                capacity_--;
                course_.setCapacity(capacity_);
                registration.setCourse(course_);
                registration.setStudent(student);
                student.getRegistrations().add(registration);
                registration.persist();
                return Response.status(Response.Status.OK).build();



        }).orElse(Response.noContent().build());
    }
    @Transactional
   public Response register(String token,String courseName){
        return staffRepository.findByToken(token).map(staff -> {
            Optional<Course> course=courseRepository.findCourseByName(courseName);
            Course course_=course.get();
            course_.setStaff(staff);
            staff.getCourses().add(course_);
            course_.persist();
            return Response.ok(course_).build();
        }).orElse(Response.noContent().build());
}

@Transactional
  public CourseDto updateCourseByName(String name,CourseDto courseDto)throws ResourceNotFoundException{
            Course course_=courseRepository.findCourseByName(name).map(course ->{
            course.setCourseStatus(courseDto.getCourseStatus());
            course.setCourseLanguage(courseDto.getCourseLanguage());
            course.setCapacity(courseDto.getCapacity());
            course.setCourseBegin(courseDto.getCourseBegin());
            course.setCourseEnd(courseDto.getCourseEnd());
            course.setTypeOfCourse(courseDto.getTypeOfCourse());
            course.setSemster(courseDto.getSemster());
            course.setCourseName(courseDto.getCourseName());
            courseRepository.persist(course);
            return course;

        } ).orElseThrow(()->new ResourceNotFoundException(("could not find the course")));
        return courseMapper.toDto(course_);
  }
  // delete student from course
    @Transactional
    public void deleteStudent(Long registrationId){
        registrationRepository.findByIdOptional(registrationId).map(registration -> {
            Student student = registration.getStudent();
            ;
            Course course_ = registration.getCourse();
            int capacity_ = course_.getCapacity();

            if (student.isPersistent()) {
                registrationRepository.delete(registration);
                capacity_++;
                course_.setCapacity(capacity_);
                course_.setCourseStatus(CourseStatus.not_filled);

                return Response.status(Response.Status.FOUND).build();
            }


            return Response.status(Response.Status.OK).build();


        });
        Response.noContent().build();
    }

    // find courses by Name of Department

    public List<CourseView>fetchCoursesByDepartmentName(String name)throws ResourceNotFoundException{
        List<Course>courseList=courseRepository.listAll().stream().filter(course -> course.getSemster().equals(Semster.Summer)).collect(Collectors.toList());

        List<Course>courses=departmentRepository.findDepartmentByName(name).map(Department::getCourses).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        return courseViewMapper.toDto(courses);
    }
    public List<CourseView>fetchCoursesByDepartmentNameAndTerm(String departmentName,Semster semstername)throws ResourceNotFoundException{
        List<Course>courseList=courseRepository.listAll().stream().filter(course -> course.getSemster().equals(semstername)).sorted(Comparator.comparing(Course::getCourseName)).collect(Collectors.toList());
        List<Course>courses=departmentRepository.findDepartmentByName(departmentName).map(Department::getCourses).map(courses1 -> courseList).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        return courseViewMapper.toDto(courses);
    }
    @Transactional
    public Response deleteCourseById(Long id){
        Course course=courseRepository.findByIdOptional(id).orElse(null);
        courseRepository.delete(course);
        return Response.status(Response.Status.OK).build();
    }
    public List<RegistrationDto>fetchAllCoursesByStudentNameAndBirthday(String lastName, String firstName, LocalDate date)throws ResourceNotFoundException{
        Optional<Student> student=studentRepository.findStudentByNameAndBirthDay(lastName, firstName, date);
       List<Registration>registrations=studentRepository.findStudentByNameAndBirthDay(lastName, firstName, date).map(Student::getRegistrations).orElseThrow(()->new ResourceNotFoundException(""));
       // List<Registration>registrations=registrationRepository.listAll();
        return registrationMapper.toDto(registrations);


    }
    public ByteArrayInputStream convertToPDF(String courseName,TypeOfCourse typeOfCourse) throws DocumentException {

        List<Registration>registrations=courseRepository.findCourseByNameAndCourseType(courseName, typeOfCourse).map(Course::getRegistrations).orElse(null);


        Document document=new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document,out);
        document.open();
        Font font= FontFactory.getFont(FontFactory.COURIER,13, BaseColor.BLACK);
        Paragraph paragraph=new Paragraph("Registered Students of "+courseName+" Course",font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        PdfPTable pdfPTable=new PdfPTable(3);
        Stream.of("Matriculation Number","Lastname","Firstname").forEach(headerTitle->{
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(3);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(headerTitle, headFont));
            pdfPTable.addCell(header);
        });
        for (Registration registration:registrations){
             //=========== Matriculation Number ===========
            PdfPCell matriculationNumber=new PdfPCell(new Phrase(registration.getStudent().getMatriculationNumber()));
            matriculationNumber.setVerticalAlignment(Element.ALIGN_CENTER);
            matriculationNumber.setHorizontalAlignment(Element.ALIGN_LEFT);
            matriculationNumber.setPadding(4);
            pdfPTable.addCell(matriculationNumber);
            //=========== LastName ======================00
            PdfPCell lastNameCell=new PdfPCell(new Phrase(registration.getStudent().getLastName()));
            lastNameCell.setPadding(4);
            lastNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            lastNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(lastNameCell);
            //============ FirstName Cell =================
            PdfPCell firstNameCell=new PdfPCell(new Phrase(registration.getStudent().getFirstName()));
            firstNameCell.setPadding(4);
            firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(firstNameCell);


            //================

        }
        document.add(pdfPTable);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream load(String courseName,TypeOfCourse typeOfCourse) throws DocumentException {

        ByteArrayInputStream byteArrayInputStream=convertToPDF(courseName,typeOfCourse);
        return byteArrayInputStream;
    }
}
    /*public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        group.getUsers().forEach(u -> u.getGroups().remove(group));
        userRepository.saveAll(group.getUsers());
        groupRepository.delete(group);
    }*/