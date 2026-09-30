package com.eventsphere.repository;

import com.eventsphere.entity.BookingTicket;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<BookingTicket, Long> {

    Boolean existsByEventScheduleAndSeat(EventSchedule eventSchedule, Seat seat);

    Boolean existsBySeat(Seat seat);

}
