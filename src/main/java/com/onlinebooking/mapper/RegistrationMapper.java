package com.onlinebooking.mapper;

import com.onlinebooking.dtos.Register.RegistrationDto;
import com.onlinebooking.entities.Registration;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper implements EntityMapper<RegistrationDto, Registration>{
    @Override
    public Registration toEntity(RegistrationDto dto) {
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto,Registration.class);
    }

    @Override
    public RegistrationDto toDto(Registration entity) {
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity,RegistrationDto.class);    }

    @Override
    public List<Registration> toEntity(List<RegistrationDto> dtoList) {
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<Registration>registrations=new ArrayList<>();
        dtoList.forEach(registrationDto -> {
            registrations.add(mapper.map(registrationDto,Registration.class));
        });
        return registrations;
    }

    @Override
    public List<RegistrationDto> toDto(List<Registration> entityList) {
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<RegistrationDto>dtos=new ArrayList<>();
        entityList.forEach(registration -> {
            dtos.add(mapper.map(registration,RegistrationDto.class));
        });
        return dtos;
    }
}
