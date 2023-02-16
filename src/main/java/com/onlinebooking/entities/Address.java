package com.onlinebooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinebooking.entities.Users;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address extends PanacheEntity {

    private String street;
    private String city;
    private String state;
    private String country;
    private int zipcode;
    @OneToOne
    @JsonIgnore
    private Users users;
}
