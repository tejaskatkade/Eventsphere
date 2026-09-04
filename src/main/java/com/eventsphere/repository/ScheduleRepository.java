package com.eventsphere.repository;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<EventSchedule, Long> {
    List<EventSchedule> findAllByEvent(Event event);

    List<EventSchedule> findAllByHall(Hall hall);
}
