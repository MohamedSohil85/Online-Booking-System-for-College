package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.security.jpa.Roles;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Users {


     private String matriculationNumber;
     @OneToMany(mappedBy = "student",orphanRemoval = true)
     @JsonIgnore
     private List<Registration> registrations;
     @JsonIgnore
     @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
     private List<Note>notes;

}
