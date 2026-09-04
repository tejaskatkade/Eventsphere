package com.eventsphere.repository;

import com.eventsphere.entity.BookingTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<BookingTicket, Long> {
}
