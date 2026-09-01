package com.eventsphere.repository;

import com.eventsphere.entity.Hall;
import com.eventsphere.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    List<Hall> findAllByVenue(Venue venue);
}
