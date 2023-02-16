package com.onlinebooking.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.onlinebooking.dtos.ExaminationDto.ExaminationDto;
import com.onlinebooking.dtos.ExaminationDto.GetExamination;
import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.entities.*;
import com.onlinebooking.enumerations.TypeOfCourse;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.ExamMapper;
import com.onlinebooking.persistence.CourseRepository;
import com.onlinebooking.persistence.ExamRegistration;
import com.onlinebooking.persistence.ExaminationRepository;
import com.onlinebooking.persistence.StudentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ExaminationService {

    @Inject
    ExaminationRepository examinationRepository;
    @Inject
    CourseRepository courseRepository;
    @Inject
    StudentRepository studentRepository;
    ExamMapper examMapper = new ExamMapper();
    @Inject
    ExamRegistration examRegistration;


    @Transactional
    public Response addExamination(Long id, ExaminationDto dto) {
        return courseRepository.findByIdOptional(id).map(course -> {
            Examination examination_ = examMapper.toEntity(dto);

            examination_.setExaminationName(course.getCourseName());
            course.setExamination(examination_);
            examination_.setCourse(course);
            course.persist();
            return Response.status(Response.Status.CREATED).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // register to examination
    @Transactional
    public Response registerExamination(String matriculationNumber, String examinationName) {
        return studentRepository.findStudentByMatriculationNumber(matriculationNumber).map(student -> {
            Optional<Examination> examination = examinationRepository.findExaminationByName(examinationName);
            Examination exam = examination.get();
            List<ExaminationRegistration> registrations = examRegistration.listAll();
            for (ExaminationRegistration registration_ : registrations) {
                if (registration_.getExamination().equals(exam) && registration_.getStudent().equals(student)) {
                    return Response.status(Response.Status.FOUND).build();
                }
            }
            ExaminationRegistration examinationRegistration = new ExaminationRegistration(exam, student, LocalDate.now());
            examinationRegistration.setExamination(exam);
            examinationRegistration.setStudent(student);
            examinationRegistration.persist();
            return Response.status(Response.Status.CREATED).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // get all examination

    //
    public ExaminationDto getExaminationByName(String name) throws ResourceNotFoundException {
        Examination examination = examinationRepository
                .findExaminationByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find exam"));
        return examMapper.toDto(examination);
    }

    public List<String> getAllStudentsByExamName(String examName) throws ResourceNotFoundException {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ExaminationRegistration> examinations = examRegistration.listAll();
        if (examinations.isEmpty()) {
            throw new ResourceNotFoundException("List is empty");
        }
        List<GetExamination> examinationList = new ArrayList<>();
        examinations.forEach(examination -> {
            examinationList.add(mapper.map(examination, GetExamination.class));
        });
        List<String>students=examinationList.stream().filter(getExamination -> getExamination.getExaminationName().equals(examName)).map(getExamination -> {
            return getExamination.getMatriculationNumber()+" "+getExamination.getFirstName()+" "+getExamination.getLastName()+" "+getExamination.getRoom();
        }).collect(Collectors.toList());
        return students;
    }

    //change Examination data

    public Response changeExaminationInfo(Long id, ExaminationDto newExamination) {
        return examinationRepository.findByIdOptional(id).map(examination -> {
            examination.setExaminationType(newExamination.getExaminationType());
            examination.setExamination_end(newExamination.getExamination_end());
            examination.setExamination_begin(newExamination.getExamination_begin());
            examination.setExamination_date(newExamination.getExamination_date());
            examination.setRegistration_deadline(newExamination.getRegistration_deadline());
            examination.persist();
            return Response.status(Response.Status.OK).build();
        }).orElse(Response.status(Response.Status.NO_CONTENT).build());

    }

    public ByteArrayInputStream convertToPDF(String examName) throws DocumentException {


        Document document=new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document,out);
        document.open();
        Font font= FontFactory.getFont(FontFactory.COURIER,13, BaseColor.BLACK);
        Paragraph paragraph=new Paragraph("Registered Students of "+examName+" Exam",font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);

        PdfPTable pdfPTable=new PdfPTable(4);
        pdfPTable.setWidthPercentage(100);

        Stream.of("Matriculation Number","Lastname","Firstname","Sign").forEach(headerTitle->{
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD,10);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(headerTitle, headFont));
            pdfPTable.addCell(header);

        });

            Optional<Examination>examination=examinationRepository.findExaminationByName(examName);
            Examination examination_=examination.get();
        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Date :"+examination_.getExamination_date()+"\n");
        document.add(Chunk.NEWLINE);
        paragraph1.add("Exam Begin :"+examination_.getExamination_begin()+"\n");
        paragraph1.add("Exam End :"+examination_.getExamination_end()+"\n");
        paragraph1.add("Room :"+examination_.getRoom());
        paragraph1.setAlignment(Element.ALIGN_LEFT);
        paragraph1.setFont(font);
        document.add(paragraph1);
        document.add(Chunk.NEWLINE);

            //====================================
            List<ExaminationRegistration>registrationList=examinationRepository.findExaminationByName(examName).map(Examination::getExaminationRegistrationList).orElse(null);
            for (ExaminationRegistration registration:registrationList) {


                PdfPCell mat_numb=new PdfPCell(new Phrase(registration.getStudent().getMatriculationNumber()));
                mat_numb.setHorizontalAlignment(Element.ALIGN_LEFT);
                mat_numb.setVerticalAlignment(Element.ALIGN_CENTER);
                mat_numb.setPadding(4);
                pdfPTable.addCell(mat_numb);

                PdfPCell lastNameCell=new PdfPCell(new Phrase(registration.getStudent().getLastName()));
                lastNameCell.setPadding(4);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(lastNameCell);

                PdfPCell firstNameCell=new PdfPCell(new Phrase(registration.getStudent().getFirstName()));
                firstNameCell.setPadding(4);
                firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                firstNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(firstNameCell);

               PdfPCell signCell =new PdfPCell(new Phrase(" "));
               signCell.setPadding(4);
               signCell.setVerticalAlignment(Element.ALIGN_CENTER);
               signCell.setHorizontalAlignment(Element.ALIGN_LEFT);
               pdfPTable.addCell(signCell);
            }









        document.add(pdfPTable);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    public ByteArrayInputStream load(String examName) throws DocumentException {

        return convertToPDF(examName);
    }
// get Student's Name and Matriculation Number,who write the Exam




   @Transactional
    public Response deleteRegistrationOfExamById(Long id){
    examRegistration.deleteById(id);
    return Response.status(Response.Status.OK).build();
   }
   public List<GetExamination>getStudentsByExamName(String name){
        List<GetExamination>getExaminationList=new ArrayList<>();
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ExaminationRegistration>registrationList=examinationRepository.findExaminationByName(name).map(Examination::getExaminationRegistrationList).orElse(null);
        registrationList.forEach(registration -> {
            getExaminationList.add(mapper.map(registration,GetExamination.class));
        });
        return getExaminationList;
   }
   public List<GetExamination>getExamListByMatNumber(String matNumber){
       List<GetExamination>getExaminationList=new ArrayList<>();
       ModelMapper mapper=new ModelMapper();
       mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
       List<ExaminationRegistration>registrationList=studentRepository.findStudentByMatriculationNumber(matNumber).map(Users::getExaminationRegistrations).orElse(null);
       registrationList.forEach(registration -> {
           getExaminationList.add(mapper.map(registration,GetExamination.class));
       });
       return getExaminationList;
   }
}
