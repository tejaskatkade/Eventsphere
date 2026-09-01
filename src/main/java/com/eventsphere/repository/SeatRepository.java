package com.eventsphere.repository;

import com.eventsphere.entity.Hall;
import com.eventsphere.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByHall(Hall hall);
}
