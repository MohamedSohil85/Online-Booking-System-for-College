package com.onlinebooking.persistence;

import com.onlinebooking.entities.Schedule;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScheduleRepository implements PanacheRepository<Schedule> {
}
