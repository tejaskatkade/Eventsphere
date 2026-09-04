package com.eventsphere.repository;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findAllByCity(String city);
}
