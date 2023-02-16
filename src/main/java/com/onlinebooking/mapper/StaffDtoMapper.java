package com.onlinebooking.mapper;

import com.onlinebooking.dtos.StaffDto.StaffDto;
import com.onlinebooking.dtos.StudentDto.AccountDto;
import com.onlinebooking.entities.Staff;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class StaffDtoMapper implements EntityMapper<StaffDto, Staff> {
    @Override
    public Staff toEntity(StaffDto dto) {
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Staff.class);
    }

    @Override
    public StaffDto toDto(Staff entity) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity, StaffDto.class);
    }

    @Override
    public List<Staff> toEntity(List<StaffDto> dtoList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Staff>staffs=new ArrayList<>();
        dtoList.forEach(accountDto -> {
            staffs.add(mapper.map(accountDto,Staff.class));
        });

        return staffs;
    }

    @Override
    public List<StaffDto> toDto(List<Staff> entityList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<StaffDto>accounts=new ArrayList<>();
        entityList.forEach(staff -> {
            accounts.add(mapper.map(staff,StaffDto.class));
        });

        return accounts;
    }
}
