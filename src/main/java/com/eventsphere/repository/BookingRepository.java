package com.eventsphere.repository;

import com.eventsphere.entity.Booking;
import com.eventsphere.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.member " +
           "JOIN FETCH b.schedules " +
           "LEFT JOIN FETCH b.bookingTickets " +
           "WHERE b.member = :member")
    List<Booking> findAllByMemberWithDetails(@Param("member") Member member);

    List<Booking> findAllByMember(Member member);
}
