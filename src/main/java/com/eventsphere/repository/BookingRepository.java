package com.eventsphere.repository;

import com.eventsphere.entity.Booking;
import com.eventsphere.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.member " +
           "JOIN FETCH b.schedules s " +
           "LEFT JOIN FETCH s.event " +
           "LEFT JOIN FETCH s.hall h " +
           "LEFT JOIN FETCH h.venue " +
           "LEFT JOIN FETCH b.bookingTickets bt " +
           "LEFT JOIN FETCH bt.seat " +
           "WHERE b.member = :member")
    List<Booking> findAllByMemberWithDetails(@Param("member") Member member);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN FETCH b.member " +
           "JOIN FETCH b.schedules s " +
           "LEFT JOIN FETCH s.event " +
           "LEFT JOIN FETCH s.hall h " +
           "LEFT JOIN FETCH h.venue " +
           "LEFT JOIN FETCH b.bookingTickets bt " +
           "LEFT JOIN FETCH bt.seat " +
           "WHERE b.id = :bookingId")
    Optional<Booking> findByIdWithDetails(@Param("bookingId") Long bookingId);

    List<Booking> findAllByMember(Member member);
}
