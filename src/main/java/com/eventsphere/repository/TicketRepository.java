package com.eventsphere.repository;

import com.eventsphere.entity.BookingTicket;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<BookingTicket, Long> {

    Boolean existsByEventScheduleAndSeat(EventSchedule eventSchedule, Seat seat);

    Boolean existsBySeat(Seat seat);

    @Query("SELECT bt.seat.id FROM BookingTicket bt WHERE bt.eventSchedule.id = :scheduleId")
    List<Long> findBookedSeatIdsByScheduleId(@Param("scheduleId") Long scheduleId);
}
