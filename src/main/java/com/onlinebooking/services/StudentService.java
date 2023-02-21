package com.onlinebooking.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.dtos.StudentDto.StudentDto;
import com.onlinebooking.entities.Address;
import com.onlinebooking.entities.Student;

import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.StudentInfoMapper;
import com.onlinebooking.mapper.StudentMapper;
import com.onlinebooking.persistence.AddressRepository;
import com.onlinebooking.persistence.StudentRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;


@ApplicationScoped
public class StudentService {


    @Inject
    AddressRepository addressRepository;
    @Inject
    StudentRepository studentRepository;

    StudentMapper studentMapper=new StudentMapper();
    StudentInfoMapper studentInfoMapper =new StudentInfoMapper();
    @Inject
    Mailer mailer;

    @Transactional
    public Response signUp(AccountDto accountDto){

        List<Student> students=studentRepository.listAll();
        List<AccountDto>accounts=studentMapper.toDto(students);
        for (AccountDto student:accounts){
            if ((student.getUserName().equalsIgnoreCase(accountDto.getUserName()))||
                    (student.getEmail().equalsIgnoreCase(accountDto.getEmail()))){
                    return Response.status(Response.Status.FOUND).build();
                }
        }
        String pwd= accountDto.getPassword();
        String encode_pwd=BcryptUtil.bcryptHash(pwd);
        Student student=studentMapper.toEntity(accountDto);
        student.setPassword(encode_pwd);
        student.setDateOfRegistration(new Date());
        String random = UUID.randomUUID().toString();
        random = random.replaceAll("-", "");
        random=random.substring(0,2);
        random=accountDto.getFirstName().substring(0,2)+accountDto.getLastName().substring(0,2)+random;
        student.setUserName(random);
        String token_=UUID.randomUUID().toString();
        token_=token_.replaceAll("-","");
        student.setToken(token_);
        student.setRole("student");
        //=================================================================

        String mat_numb = String.format("%d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        mat_numb=mat_numb.substring(0,7);
        student.setMatriculationNumber(mat_numb);
        mailer.send(Mail.withText(accountDto.getEmail(),"Your Username/Matriculation Number","your Username :"+student.getUserName()+" "+"your Matriculation Number :"+student.getMatriculationNumber()));

        Address address=new Address();
        address.setCity(accountDto.getCity());
        address.setCountry(accountDto.getCountry());
        address.setState(accountDto.getState());
        address.setStreet(accountDto.getStreet());
        address.setZipcode(accountDto.getZipcode());
        addressRepository.persist(address);
        address.setUsers(student);
        student.setAddress(address);
        studentRepository.persist(student);

        return Response.status(Response.Status.CREATED).build();
        //

    }

// get student by name and Date of Birth
public StudentDto findStudentByNameAndBirthday(String lastName,String firstName,Date dateOfBirth)throws ResourceNotFoundException{

        Optional<Student> student =studentRepository.find("lastName = : lastName and firstName = : firstName and dateOfBirth = : dateOfBirth", Parameters.with("lastName",lastName).and("firstName",firstName).and("dateOfBirth",dateOfBirth)).singleResultOptional();

        Student student_=student.orElseThrow(()->new ResourceNotFoundException("no student is found with that name !"));
        return studentInfoMapper.toDto(student_);
}

//  change / forget the password by E-mail
public String changePassword(String email)throws ResourceNotFoundException{
        Optional<Student>userOptional=studentRepository.findStudentByEmail(email);
        Student accountDto=userOptional.orElseThrow(()->new ResourceNotFoundException("Resource cannot found"));

        mailer.send(Mail.withText(accountDto.getEmail(),"change your password","http://localhost:8080/api/student/change-my-password?token="+accountDto.getToken()));
        return "check your E-mail, Please!";
}

@Transactional
public Response addNewPasswordByEmail(AccountDto studentDto,String token){
    return studentRepository.findStudentByToken(token).map(student -> {
        String pwd=studentDto.getPassword();
        String encoded=BcryptUtil.bcryptHash(pwd);
        student.setPassword(encoded);
        student.persist();
        return Response.status(Response.Status.CREATED).build();
    }).orElse(Response.noContent().build());


}
@Transactional
public StudentDto changeStudentDataById(AccountDto accountDto,Long id)throws ResourceNotFoundException{
     return studentRepository.findByIdOptional(id).map(student -> {
         Address address=new Address();
         address.setCountry(accountDto.getCountry());
         address.setState(accountDto.getState());
         address.setCity(accountDto.getCity());
         address.setStreet(accountDto.getStreet());
         address.setZipcode(accountDto.getZipcode());
         addressRepository.persist(address);
         student.setPhoneNumber(accountDto.getPhoneNumber());
         student.persist();
         return studentInfoMapper.toDto(student);
     }).orElseThrow(()->new ResourceNotFoundException("no Resource with Id :"+id+" found"));
}

// retrieve list of  students

    public List<StudentDto>getAllStudents()throws ResourceNotFoundException{
        List<Student>students=studentRepository.listAll();
        if (students.isEmpty()){
           throw  new ResourceNotFoundException("List is Empty");
        }
        return studentInfoMapper.toDto(students);
    }
    public StudentDto getStudentByName(String lastName,String firstName)throws ResourceNotFoundException{
        List<Student>students=studentRepository.listAll();
        if (students.isEmpty()){
            throw  new ResourceNotFoundException("List is Empty");
        }
        Optional<Student>optionalStudent= Student.find("lastName = : lastName and firstName = : firstName",Parameters.with("lastName",lastName).and("firstName",firstName)).singleResultOptional();
        Student student=optionalStudent.orElseThrow(()->new ResourceNotFoundException("no Resource with name :"+firstName + lastName));
        return studentInfoMapper.toDto(student);

    }
    @Transactional
    public Response deleteStudentById(Long id){
        Student student=  studentRepository.findByIdOptional(id).orElse(null);
        studentRepository.delete(student);
        return Response.status(Response.Status.OK).build();
    }
    public List<StudentDto> findStudentByBirthday( LocalDate dateOfBirth)throws ResourceNotFoundException{
        List<Student>students=studentRepository.listAll();
        if (students.isEmpty()){
            throw new ResourceNotFoundException("List is Empty");
        }

        List<Student>studentList= studentRepository.find("dateOfBirth = : dateOfBirth",Parameters.with("dateOfBirth",dateOfBirth)).list();
        return studentInfoMapper.toDto(studentList);
    }
    public StudentDto findStudentByMatriculationNumberAndName(String lastName,String firstName,String matNumber)throws ResourceNotFoundException{
        Optional<Student>studentOptional=studentRepository.find("lastName = : lastName and firstName = : firstName and matriculationNumber = : matNumber",Parameters.with("lastName",lastName).and("firstName",firstName).and("matNumber",matNumber)).singleResultOptional();
        Student student=studentOptional.orElseThrow(()->new ResourceNotFoundException("Student not found"));
        return studentInfoMapper.toDto(student);
    }
    @Transactional
    public void deleteAddressById(Long id){
        addressRepository.deleteById(id);
    }


   public ByteArrayInputStream convertToPDF(List<Student>students) throws DocumentException {

       Document document=new Document();

       ByteArrayOutputStream out = new ByteArrayOutputStream();

       PdfWriter.getInstance(document,out);
       document.open();
       Font font= FontFactory.getFont(FontFactory.COURIER,10, BaseColor.BLACK);
       Paragraph paragraph=new Paragraph("Registered Students ",font);
       paragraph.setAlignment(Element.ALIGN_CENTER);
       document.add(paragraph);
       document.add(Chunk.NEWLINE);
       PdfPTable pdfPTable=new PdfPTable(7);
       pdfPTable.setWidthPercentage(100);
       pdfPTable.setWidths(new float[]{3,1});
       Stream.of("Role","Date of Registration","Matriculation Number","Lastname","FirstName","Date Of Birth","Phone Number").forEach(headerTitle->{
           PdfPCell header = new PdfPCell();
           Font headFont = FontFactory.
                   getFont(FontFactory.HELVETICA_BOLD,10);
           header.setBackgroundColor(BaseColor.LIGHT_GRAY);
           header.setHorizontalAlignment(Element.ALIGN_CENTER);
           header.setBorderWidth(2);

           header.setPhrase(new Phrase(headerTitle, headFont));
           pdfPTable.addCell(header);
       });
       for (Student student:students){
           Font cellFont = FontFactory.
                   getFont(FontFactory.HELVETICA_BOLD,10);
           PdfPCell roleCell=new PdfPCell(new Phrase(student.getRole(),cellFont));
           roleCell.setPadding(4);

           roleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           roleCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(roleCell);
           //============ Date Of Registration ============
           PdfPCell DateCell=new PdfPCell(new Phrase(String.valueOf(student.getDateOfRegistration()),cellFont));
           DateCell.setPadding(4);
           DateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           DateCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(DateCell);
           //=========== Matriculation Number ==============
           PdfPCell mat_numb=new PdfPCell(new Phrase(student.getMatriculationNumber(),cellFont));
           mat_numb.setHorizontalAlignment(Element.ALIGN_LEFT);
           mat_numb.setVerticalAlignment(Element.ALIGN_CENTER);
           mat_numb.setPadding(4);
           pdfPTable.addCell(mat_numb);
           //=========== LastName ======================00
           PdfPCell lastNameCell=new PdfPCell(new Phrase(student.getLastName(),cellFont));
           lastNameCell.setPadding(4);
           lastNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           lastNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(lastNameCell);
           //============ FirstName Cell =================
           PdfPCell firstNameCell=new PdfPCell(new Phrase(student.getFirstName(),cellFont));
           firstNameCell.setPadding(4);
           firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           firstNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(firstNameCell);

           //============ Date of Birth Cell==============

           PdfPCell dateOfBirthCell=new PdfPCell(new Phrase(String.valueOf(student.getDateOfBirth()),cellFont));
           dateOfBirthCell.setPadding(4);
           dateOfBirthCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           dateOfBirthCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(dateOfBirthCell);

           //========== Phone Number Cell ==================

           PdfPCell phoneNumberCell=new PdfPCell(new Phrase(student.getPhoneNumber(),cellFont));
           phoneNumberCell.setPadding(4);
           phoneNumberCell.setHorizontalAlignment(Element.ALIGN_LEFT);
           phoneNumberCell.setVerticalAlignment(Element.ALIGN_CENTER);
           pdfPTable.addCell(phoneNumberCell);

          //================

       }
       document.add(pdfPTable);
       document.close();
       return new ByteArrayInputStream(out.toByteArray());
   }

    public ByteArrayInputStream load() throws DocumentException {
        List<Student> students = studentRepository.listAll();
        ByteArrayInputStream byteArrayInputStream=convertToPDF(students);
        return byteArrayInputStream;
    }
    /*public ByteArrayInputStream convertStudentDataToPDF(String lastName,String firstName,String matNumber) throws ResourceNotFoundException, DocumentException {
        Optional<Student>studentOptional=studentRepository.find("lastName = : lastName and firstName = : firstName and matriculationNumber = : matNumber",Parameters.with("lastName",lastName).and("firstName",firstName).and("matNumber",matNumber)).singleResultOptional();
        Student student=studentOptional.orElseThrow(()->new ResourceNotFoundException("Student not found"));
        Document document=new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

      PdfWriter writer=  PdfWriter.getInstance(document,out);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        Font font= FontFactory.getFont(FontFactory.COURIER,10, BaseColor.BLACK);
        Paragraph paragraph2=new Paragraph("Lastname :"+student.getLastName(),font);
        Paragraph paragraph3=new Paragraph("Matriculation Number :"+student.getMatriculationNumber(),font);


        Barcode128 barcode=new Barcode128();
        barcode.setCode(student.getToken());
        barcode.setCodeType(Barcode.CODE128);


        Image image=barcode.createImageWithBarcode(cb,null,null);
    }*/
}
