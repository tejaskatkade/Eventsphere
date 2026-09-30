package com.eventsphere.repository;

import com.eventsphere.entity.Booking;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Member;
import com.eventsphere.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByMember(Member member);
}
