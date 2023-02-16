package com.onlinebooking.mapper;

import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.entities.Staff;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class StaffMapper implements EntityMapper<AccountDto, Staff> {
    @Override
    public Staff toEntity(AccountDto dto) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Staff.class);
    }

    @Override
    public AccountDto toDto(Staff entity) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,AccountDto.class);
    }

    @Override
    public List<Staff> toEntity(List<AccountDto> dtoList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Staff>staffs=new ArrayList<>();
        dtoList.forEach(accountDto -> {
            staffs.add(mapper.map(accountDto,Staff.class));
        });

        return staffs;
    }

    @Override
    public List<AccountDto> toDto(List<Staff> entityList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<AccountDto>accounts=new ArrayList<>();
        entityList.forEach(staff -> {
            accounts.add(mapper.map(staff,AccountDto.class));
        });

        return accounts;
    }
}
