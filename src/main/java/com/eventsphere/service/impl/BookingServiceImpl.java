package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.BookingReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.BookingResDto;
import com.eventsphere.entity.*;
import com.eventsphere.exception.ApiException;
import com.eventsphere.exception.ConflictException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.*;
import com.eventsphere.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final MemberRepository memberRepository;

    private final ScheduleRepository scheduleRepository;

    private final SeatRepository seatRepository;

    private final TicketRepository ticketRepository;

    private final ModelMapper modelMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, MemberRepository memberRepository, ScheduleRepository scheduleRepository, SeatRepository seatRepository, TicketRepository ticketRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.memberRepository = memberRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
    }

    /*
     * Books an event for a member based on the provided booking request data.
     * Uses JPA Pessimistic Write Locking on EventSchedule to ensure thread-safe seat allocation.
     */
    @Override
    public ApiResponse bookEvent(BookingReqDto bookingReqDto) {
        log.info("Initiating booking for Member ID: {}, Schedule ID: {}, Seats requested: {}",
                bookingReqDto.getMemberId(), bookingReqDto.getEventScheduleId(), bookingReqDto.getSeatIds());

        if (bookingReqDto.getSeatIds() == null || bookingReqDto.getSeatIds().isEmpty()) {
            throw new ApiException("At least one seat must be selected for booking.");
        }

        Member member = findMemberById(bookingReqDto.getMemberId());

        // 1. Acquire PESSIMISTIC_WRITE lock on EventSchedule to serialize bookings for this schedule
        EventSchedule schedule = findScheduleByIdWithLock(bookingReqDto.getEventScheduleId());
        log.debug("Acquired pessimistic lock on Schedule ID: {}", schedule.getId());

        if (schedule.getStatus() != ScheduleStatus.AVAILABLE) {
            log.warn("Booking rejected: Schedule ID: {} is not AVAILABLE (current: {})", schedule.getId(), schedule.getStatus());
            throw new ApiException("Schedule is not available for booking. Status: " + schedule.getStatus());
        }
        if (schedule.getStartTime() != null && schedule.getStartTime().isBefore(LocalDateTime.now())) {
            log.warn("Booking rejected: Schedule ID: {} is in the past ({})", schedule.getId(), schedule.getStartTime());
            throw new ApiException("Cannot book a past event schedule.");
        }

        // 2. Fetch and validate selected seats
        List<Seat> seats = bookingReqDto
                .getSeatIds()
                .stream()
                .map(this::findSeatById)
                .toList();

        Long hallId = schedule.getHall().getId();
        for (Seat seat : seats) {
            if (!seat.getHall().getId().equals(hallId)) {
                log.warn("Seat ID: {} does not belong to Hall ID: {}", seat.getId(), hallId);
                throw new ApiException("Seat " + seat.getId() + " does not belong to the hall for this schedule.");
            }
            if (Boolean.FALSE.equals(seat.getIsActive())) {
                log.warn("Seat ID: {} is currently inactive", seat.getId());
                throw new ApiException("Seat " + seat.getId() + " is currently inactive.");
            }
            if (ticketRepository.existsByEventScheduleAndSeat(schedule, seat)) {
                log.warn("Seat conflict: Seat ID: {} ({}{}) is already booked for Schedule ID: {}",
                        seat.getId(), seat.getRowName(), seat.getSeatNumber(), schedule.getId());
                throw new ConflictException("Seat " + seat.getRowName() + seat.getSeatNumber() + " is already booked for this schedule.");
            }
        }

        // 3. Build Booking and Tickets
        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setMember(member);
        booking.setSchedules(schedule);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setBookingTime(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Seat seat : seats) {
            BookingTicket ticket = generateBookingTicket(booking, seat, schedule);
            booking.getBookingTickets().add(ticket);
            totalAmount = totalAmount.add(ticket.getTicketPrice());
        }
        booking.setTotalAmount(totalAmount);

        // Cascades automatically to booking tickets
        bookingRepository.save(booking);
        log.info("Booking created successfully. Reference: {}, Total Amount: ₹{}, Tickets: {}",
                booking.getBookingReference(), booking.getTotalAmount(), booking.getBookingTickets().size());

        return new ApiResponse("Booking created successfully. Reference: " + booking.getBookingReference());
    }

    @Override
    public BookingResDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));

        BookingResDto bookingResDto = modelMapper.map(booking, BookingResDto.class);
        bookingResDto.getBookingTicketIds().addAll(
                booking.getBookingTickets()
                        .stream()
                        .map(BookingTicket::getId)
                        .toList()
        );
        bookingResDto.setMemberId(booking.getMember().getId());
        bookingResDto.setEventScheduleId(booking.getSchedules().getId());
        return bookingResDto;
    }

    @Override
    public List<BookingResDto> getBookingsByMember(Long memberId) {
        Member member = findMemberById(memberId);
        return bookingRepository.findAllByMemberWithDetails(member)
                .stream()
                .map(booking -> {
                    BookingResDto bookingResDto = modelMapper.map(booking, BookingResDto.class);
                    bookingResDto.getBookingTicketIds().addAll(
                            booking.getBookingTickets()
                                    .stream()
                                    .map(BookingTicket::getId)
                                    .toList()
                    );
                    bookingResDto.setMemberId(booking.getMember().getId());
                    bookingResDto.setEventScheduleId(booking.getSchedules().getId());
                    return bookingResDto;
                })
                .toList();
    }

    /*
     * Generates a unique booking reference for a new booking.
     * This method uses UUID to ensure uniqueness.
     *
     * @return A unique booking reference string.
     */
    private String generateBookingReference() {
        return "BR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

    private EventSchedule findScheduleByIdWithLock(Long scheduleId) {
        return scheduleRepository.findByIdWithPessimisticLock(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", scheduleId));
    }

    private Seat findSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId));
    }

    /*
     * Generates a BookingTicket for the given booking, seat, and schedule.
     * This method creates a new BookingTicket entity, sets its properties,
     * and saves it to the database.
     *
     * @param booking  The booking associated with the ticket.
     * @param seat     The seat associated with the ticket.
     * @param schedule The event schedule associated with the ticket.
     * @return The generated BookingTicket entity.
     */
    private BookingTicket generateBookingTicket(Booking booking, Seat seat, EventSchedule schedule) {
        BookingTicket ticket = new BookingTicket();
        ticket.setBooking(booking);
        ticket.setSeat(seat);
        ticket.setEventSchedule(schedule);
        // TODO: Set ticket price based on seat type or other criteria
        ticket.setTicketPrice(schedule.getTicketPrice());
        return ticket;
    }
}
