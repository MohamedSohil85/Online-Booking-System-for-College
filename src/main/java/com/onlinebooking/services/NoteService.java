package com.onlinebooking.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onlinebooking.dtos.NoteDto.NoteApply;
import com.onlinebooking.entities.*;
import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.NoteMapper;
import com.onlinebooking.persistence.ExaminationRepository;
import com.onlinebooking.persistence.NoteRepository;
import com.onlinebooking.persistence.StudentRepository;
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
        for (Note note1:notes){
            if (note1.getStudent().equals(student)&&note1.getExamination().equals(examination)){
                return Response.status(Response.Status.FOUND).build();
            }
        }



          note.setAppliedDate(LocalDate.now());
          note.setFirstName(firstName);
          note.setLastName(lastName);
          Note note_=noteMapper.toEntity(note);
          note_.setExamination(examination.get());
          note_.setStudent(student);
          note_.persist();

                return Response.status(Response.Status.CREATED).build();

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


}
