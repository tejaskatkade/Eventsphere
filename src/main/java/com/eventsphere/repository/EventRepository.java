package com.eventsphere.repository;

import com.eventsphere.entity.Category;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrganiser(Organiser organiser);

    List<Event> findAllByCategory(Category category);
}
