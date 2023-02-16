package com.onlinebooking.persistence;



import com.onlinebooking.entities.Note;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteRepository implements PanacheRepository<Note> {
}
