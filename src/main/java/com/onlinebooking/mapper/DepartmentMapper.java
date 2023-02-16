package com.onlinebooking.mapper;


import com.onlinebooking.dtos.DepartmentDto.DepartmentDto;

import com.onlinebooking.entities.Department;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class DepartmentMapper implements EntityMapper<DepartmentDto, Department> {


    @Override
    public Department toEntity(DepartmentDto dto) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto,Department.class);
    }

    @Override
    public DepartmentDto toDto(Department entity) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,DepartmentDto.class);
    }

    @Override
    public List<Department> toEntity(List<DepartmentDto> dtoList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Department>departments=new ArrayList<>();
        dtoList.forEach(departmentDto -> {
            departments.add(mapper.map(departmentDto,Department.class));
        });
        return departments;
    }

    @Override
    public List<DepartmentDto> toDto(List<Department> entityList) {
        ModelMapper mapper=new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<DepartmentDto>dtoList=new ArrayList<>();
        entityList.forEach(department -> {
            dtoList.add(mapper.map(department,DepartmentDto.class));
        });
        return dtoList;
    }
}
