package com.eventsphere.repository;

import com.eventsphere.entity.Booking;
import com.eventsphere.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByMember(Member member);
}
