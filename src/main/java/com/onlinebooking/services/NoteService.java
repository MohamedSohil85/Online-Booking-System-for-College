package com.onlinebooking.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onlinebooking.dtos.NoteDto.NoteApply;
import com.onlinebooking.entities.*;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.NoteMapper;
import com.onlinebooking.persistence.ExamRegistration;
import com.onlinebooking.persistence.ExaminationRepository;
import com.onlinebooking.persistence.NoteRepository;
import com.onlinebooking.persistence.StudentRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class NoteService {

    @Inject
    NoteRepository noteRepository;
    @Inject
    StudentRepository studentRepository;
    @Inject
    ExaminationRepository examinationRepository;
    @Inject
    ExamRegistration examRegistration;
    @Inject
    Mailer mailer;
    NoteMapper noteMapper=new NoteMapper();

    @Transactional
    public Response applyNote(NoteApply note, String examinationName,String matriculationNumber, String lastName,String firstName) throws ResourceNotFoundException {
        Optional<Student>studentOptional=studentRepository.findStudentByMatriculationNumber(matriculationNumber);
        if (!studentOptional.isPresent()){
            throw new ResourceNotFoundException("Student with Matriculation Number :"+matriculationNumber+" not found");
        }
        List<Student>students=studentRepository.listAll();
        if (students.isEmpty()){
            throw  new ResourceNotFoundException("List is Empty");
        }
        Optional<Student>optionalStudent= Student.find("lastName = : lastName and firstName = : firstName",Parameters.with("lastName",lastName).and("firstName",firstName)).singleResultOptional();
        Student student=optionalStudent.orElseThrow(()->new ResourceNotFoundException("no Resource with name :"+firstName+" " + lastName));
        Optional<Examination>examination=examinationRepository.findExaminationByName(examinationName);
        List<Note>notes=noteRepository.listAll();
        for (Note note_:notes) {
            if (note_.getStudent().equals(student) && note_.getExamination().equals(examination.get())) {
                return Response.status(Response.Status.FOUND).build();
            }
        }
        List<ExaminationRegistration>registrationList=examRegistration.listAll();
        for (ExaminationRegistration registration:registrationList){
            if ((registration.getStudent().equals(student) && registration.getExamination().equals(examination.get()))){
                note.setAppliedDate(LocalDate.now());
                note.setFirstName(firstName);
                note.setLastName(lastName);
                Note note_=noteMapper.toEntity(note);
                note_.setExamination(examination.get());
                note_.setStudent(student);
                note_.persist();
                mailer.send(Mail.withText(student.getEmail(),"Grade-Info","Grade of Exam entered"));
                return Response.status(Response.Status.CREATED).build();
            }
        }




return Response.status(Response.Status.OK).build();


            }
    // find list of Note  by Name of Student

    public List<NoteApply> getNotesByName(String lastName,String firstName)throws ResourceNotFoundException{
        Optional<Student>optionalStudent=studentRepository.findStudentByLastNameAndFirstName(lastName, firstName);
        Student student=optionalStudent.orElseThrow(()->new ResourceNotFoundException("not student with this name found"));
      List<Note>notes=studentRepository.findStudentByLastNameAndFirstName(lastName, firstName).map(Student::getNotes).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
      return noteMapper.toDto(notes);

    }
    //find Note of Examination by Student name

    public  List<NoteApply> findNoteByName(String examinationName)throws ResourceNotFoundException{
      List<Note> notes= examinationRepository.findExaminationByName(examinationName).map(Examination::getNotes).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
       return noteMapper.toDto(notes);

    }

    public ByteArrayInputStream convertToPDF(String examinationName) throws DocumentException, ResourceNotFoundException {

        List<Note> notes= examinationRepository.findExaminationByName(examinationName).map(Examination::getNotes).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        Optional<Examination> examination=examinationRepository.findExaminationByName(examinationName);
        Examination examination_=examination.get();


        Document document=new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document,out);
        document.open();
        Font font= FontFactory.getFont(FontFactory.COURIER,13, BaseColor.BLACK);
        Paragraph paragraph=new Paragraph("List of Note of "+examinationName+" "+examination_.getExamination_date()+" "+"in term:"+examination_.getCourse().getSemster()+" "+examination_.getCourse().getTypeOfCourse()+" "+examination_.getCourse().getDepartment().getDepartmentName(),font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        PdfPTable pdfPTable=new PdfPTable(5);
        Stream.of("Matriculation Number,Lastname","Firstname","Note","Notice").forEach(headerTitle->{
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
        for (Note note:notes){
            //=========== matriculationNumber ============
            PdfPCell matriculationNumberCell=new PdfPCell(new Phrase(note.getStudent().getMatriculationNumber()));
            matriculationNumberCell.setVerticalAlignment(Element.ALIGN_CENTER);
            matriculationNumberCell.setPadding(4);
            matriculationNumberCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable.addCell(matriculationNumberCell);
            //=========== LastName ======================00
            PdfPCell lastNameCell=new PdfPCell(new Phrase(note.getStudent().getLastName()));
            lastNameCell.setPadding(4);
            lastNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            lastNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(lastNameCell);
            //============ FirstName Cell =================
            PdfPCell firstNameCell=new PdfPCell(new Phrase(note.getStudent().getFirstName()));
            firstNameCell.setPadding(4);
            firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(firstNameCell);


            //================

            PdfPCell noteCell=new PdfPCell(new Phrase(note.getMark()));
            noteCell.setPadding(4);
            noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(noteCell);

            PdfPCell noticeCell=new PdfPCell(new Phrase(note.getNotice()));
            noticeCell.setPadding(4);
            noticeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            noticeCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(noticeCell);

        }
        document.add(pdfPTable);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    public ByteArrayInputStream load(String examinationName) throws DocumentException, ResourceNotFoundException {

        return convertToPDF(examinationName);
    }

    // retrieve all grade of student(Name,Matriculation Number)

public List<NoteApply>getListOfGrade(String matNumber)throws ResourceNotFoundException{
        List<Note>notes=studentRepository.findStudentByMatriculationNumber(matNumber).map(Student::getNotes).orElseThrow(()->new ResourceNotFoundException("Student with Matriculation Number :"+matNumber+" not found"));
        return noteMapper.toDto(notes);
}

// change Grade

    //
    public ByteArrayInputStream writeGradeOfStudentToPDF(String matNumber) throws ResourceNotFoundException, DocumentException {
        List<Note>notes=studentRepository.findStudentByMatriculationNumber(matNumber).map(Student::getNotes).orElseThrow(()->new ResourceNotFoundException("Student not found"));
        Student student=studentRepository.findStudentByMatriculationNumber(matNumber).get();
        List<Registration>registrations=studentRepository.findStudentByMatriculationNumber(matNumber).map(Student::getRegistrations).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        Document document=new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document,out);
        document.open();
        Font font= FontFactory.getFont(FontFactory.COURIER,13, BaseColor.BLACK);
        Paragraph paragraph=new Paragraph("Transcript of Records with all graduated courses",font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);

        Paragraph paragraph1=new Paragraph("Name :"+student.getLastName()+"\n");
        paragraph1.add("Firstname :"+student.getFirstName()+"\n");
        paragraph1.add("Date of Birth :"+student.getDateOfBirth()+"\n");
        paragraph1.add("Place of Birth :"+student.getAddress().getCity()+", "+student.getAddress().getCountry()+"\n");
        paragraph1.add("Matriculation Number :"+student.getMatriculationNumber()+"\n");
        paragraph1.setAlignment(Element.ALIGN_LEFT);
        paragraph1.setFont(font);
        paragraph1.add(Chunk.NEWLINE);
        paragraph1.add(Chunk.NEWLINE);
        document.add(paragraph1);

        PdfPTable pdfPTable=new PdfPTable(4);
        pdfPTable.setWidthPercentage(100);

        Stream.of("Applied Date","Name of Exam","Grade","Notice").forEach(headerTitle->{
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD,10);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(headerTitle, headFont));
            pdfPTable.addCell(header);

        });
        for (Note note:notes){
            PdfPCell dateCell=new PdfPCell(new Phrase(String.valueOf(note.getAppliedDate())));
            dateCell.setVerticalAlignment(Element.ALIGN_CENTER);
            dateCell.setPadding(4);
            dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable.addCell(dateCell);

            PdfPCell examName=new PdfPCell(new Phrase(note.getExamination().getExaminationName()));
            examName.setVerticalAlignment(Element.ALIGN_CENTER);
            examName.setHorizontalAlignment(Element.ALIGN_LEFT);
            examName.setPadding(4);
            pdfPTable.addCell(examName);

            PdfPCell noteCell=new PdfPCell(new Phrase(note.getMark()));
            noteCell.setPadding(4);
            noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(noteCell);

            PdfPCell noticeCell=new PdfPCell(new Phrase(note.getNotice()));
            noticeCell.setPadding(4);
            noticeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            noticeCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(noticeCell);

        }
        document.add(Chunk.NEWLINE);
        PdfPTable pdfPTable1=new PdfPTable(5);
        pdfPTable1.setWidthPercentage(100);

        Stream.of("Applied Date","Name of Course","Type of Course","Language","Term").forEach(headerTitle-> {
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.
                    getFont(FontFactory.HELVETICA_BOLD, 10);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(headerTitle, headFont));
            pdfPTable1.addCell(header);
        });
        for (Registration registration:registrations){
            PdfPCell dateCell=new PdfPCell(new Phrase(String.valueOf(registration.getDateOfRegistration())));
            dateCell.setVerticalAlignment(Element.ALIGN_CENTER);
            dateCell.setPadding(4);
            dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable1.addCell(dateCell);

            PdfPCell courseNameCell=new PdfPCell(new Phrase(registration.getCourse().getCourseName()));
            courseNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            courseNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            courseNameCell.setPadding(4);
            pdfPTable1.addCell(courseNameCell);

            PdfPCell courseTypeCell=new PdfPCell(new Phrase(String.valueOf(registration.getCourse().getTypeOfCourse())));
            courseTypeCell.setVerticalAlignment(Element.ALIGN_CENTER);
            courseTypeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            courseTypeCell.setPadding(4);
            pdfPTable1.addCell(courseTypeCell);

            PdfPCell lang=new PdfPCell(new Phrase(registration.getCourse().getCourseLanguage()));
            lang.setHorizontalAlignment(Element.ALIGN_LEFT);
            lang.setVerticalAlignment(Element.ALIGN_CENTER);
            lang.setPadding(4);
            pdfPTable1.addCell(lang);

            PdfPCell term=new PdfPCell(new Phrase(String.valueOf(registration.getCourse().getSemster())));
            term.setVerticalAlignment(Element.ALIGN_CENTER);
            term.setHorizontalAlignment(Element.ALIGN_LEFT);
            term.setPadding(4);
            pdfPTable1.addCell(term);

        }

        document.add(pdfPTable);
        document.add(Chunk.NEWLINE);
        document.add(pdfPTable1);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());

    }
    public ByteArrayInputStream loadAllGradeOfStudent(String matNumber) throws DocumentException, ResourceNotFoundException {

        return writeGradeOfStudentToPDF(matNumber);
    }
}
