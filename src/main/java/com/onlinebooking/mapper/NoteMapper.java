package com.onlinebooking.mapper;


import com.onlinebooking.dtos.NoteDto.NoteApply;


import com.onlinebooking.entities.Note;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

public class NoteMapper implements EntityMapper<NoteApply, Note> {
    ModelMapper mapper=new ModelMapper();

    @Override
    public Note toEntity(NoteApply dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Note.class);    }

    @Override
    public NoteApply toDto(Note entity) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(entity, NoteApply.class);    }

    @Override
    public List<Note> toEntity(List<NoteApply> dtoList) {
        return null;
    }

    @Override
    public List<NoteApply> toDto(List<Note> entityList) {
        List<NoteApply>notes=new ArrayList<>();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        entityList.forEach(note -> {
            notes.add(mapper.map(note,NoteApply.class));
        });
        return notes;
    }
}
