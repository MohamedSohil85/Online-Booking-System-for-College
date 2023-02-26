package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department extends PanacheEntity {

    private String departmentName;
    @JsonIgnore
    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
    private List<Course>courses;
    private String departmentLocation;
    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Users>usersList;

}
