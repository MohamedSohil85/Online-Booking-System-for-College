package com.onlinebooking.services;

import com.onlinebooking.dtos.StaffDto.StaffDto;
import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.entities.Address;
import com.onlinebooking.entities.Staff;

import com.onlinebooking.exceptions.ResourceNotFoundException;
import com.onlinebooking.mapper.StaffDtoMapper;
import com.onlinebooking.mapper.StaffMapper;
import com.onlinebooking.persistence.AddressRepository;
import com.onlinebooking.persistence.StaffRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StaffService {
    @Inject
    AddressRepository addressRepository;
    @Inject
    StaffRepository staffRepository;
    StaffMapper staffMapper=new StaffMapper();
    StaffDtoMapper staffDtoMapper=new StaffDtoMapper();
    @Transactional
    public Response addNewStaff(AccountDto accountDto) {

        List<Staff> staffs = staffRepository.listAll();
        List<AccountDto> accounts = staffMapper.toDto(staffs);

        for (AccountDto staff : accounts) {
            if ((staff.getUserName().equalsIgnoreCase(accountDto.getUserName())) ||
                    (staff.getEmail().equalsIgnoreCase(accountDto.getEmail()))) {
                return Response.status(Response.Status.FOUND).build();
            }
        }
        String pwd = accountDto.getPassword();
        String encodepwd = BcryptUtil.bcryptHash(pwd);
        Staff staff = staffMapper.toEntity(accountDto);
        staff.setPassword(encodepwd);
        staff.setDateOfRegistration(new Date());
        String token_ = UUID.randomUUID().toString();
        staff.setToken(token_);

        Address address = new Address();
        address.setCity(accountDto.getCity());
        address.setCountry(accountDto.getCountry());
        address.setState(accountDto.getState());
        address.setStreet(accountDto.getStreet());
        address.setZipcode(accountDto.getZipcode());
        addressRepository.persist(address);
        address.setUsers(staff);
        staff.setAddress(address);
        staffRepository.persist(staff);

        return Response.status(Response.Status.CREATED).build();
    }

    public List<StaffDto>fetchAllStaffs()throws ResourceNotFoundException{
        List<Staff>staffList=staffRepository.listAll(Sort.by("lastName"));
        if (staffList.isEmpty()){
             throw new ResourceNotFoundException("List of Staff is empty");
        }
        return staffDtoMapper.toDto(staffList);
    }


}
