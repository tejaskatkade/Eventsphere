package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.BookingReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.BookingResDto;
import com.eventsphere.entity.*;
import com.eventsphere.exception.ApiException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.*;
import com.eventsphere.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
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
     * This method creates a new Booking entity, generates a unique booking reference,
     * associates the member and event schedule, calculates the total amount based on selected seats,
     * and saves the booking along with its associated tickets to the database.
     *
     * @param bookingReqDto The booking request data transfer object containing member ID, event schedule ID, and seat IDs.
     * @return An ApiResponse indicating the success of the booking operation along with the generated booking reference.
     */
    @Override
    public ApiResponse bookEvent(BookingReqDto bookingReqDto) {

        Booking booking = modelMapper.map(bookingReqDto, Booking.class);

        booking.setBookingReference(generateBookingReference());
        booking.setMember(findMemberById(bookingReqDto.getMemberId()));
        booking.setSchedules(findScheduleById(bookingReqDto.getEventScheduleId()));
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingTime(java.time.LocalDateTime.now());

        List<BookingTicket> tickets = bookingReqDto
                .getSeatIds()
                .stream()
                .map(this::findSeatById)
                .peek(seat -> {
                    if (ticketRepository.existsByEventScheduleAndSeat(findScheduleById(bookingReqDto.getEventScheduleId()), seat)) {
                        throw new ApiException("Seat " + seat.getId() + " is already booked for the given schedule.");
                    }
                })
                .map(seat -> generateBookingTicket(booking, seat, booking.getSchedules()))
                .toList();

        BigDecimal totalAmount = tickets.stream()
                .map(ticket -> {
                    booking.getBookingTickets().add(ticket);
                    return ticket;
                })
                .map(BookingTicket::getTicketPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        booking.setTotalAmount(totalAmount);
        bookingRepository.save(booking);
        tickets.forEach(ticketRepository::save);
        return new ApiResponse("Booking created successfully. Reference: " + booking.getBookingReference());
    }

    @Override
    public BookingResDto getBookingById(Long bookingId) {
        BookingResDto bookingResDto = modelMapper.map(
                bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking" + bookingId)),
                BookingResDto.class
        );
        bookingResDto.getBookingTicketIds().addAll(
                bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking" + bookingId))
                        .getBookingTickets()
                        .stream()
                        .map(BookingTicket::getId)
                        .toList()
        );
        bookingResDto.setMemberId(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking" + bookingId))
                .getMember().getId());
        bookingResDto.setEventScheduleId(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking" + bookingId))
                .getSchedules().getId());
        return bookingResDto;
    }

    @Override
    public List<BookingResDto> getBookingsByMember(Long memberId) {
        return bookingRepository.findAllByMember(findMemberById(memberId))
                .stream()
                .map(
                        booking -> {
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
                )
                .toList();
    }

    /*
     * Generates a unique booking reference for a new booking.
     * This method uses UUID to ensure uniqueness.
     *
     * @return A unique booking reference string.
     */
    private String generateBookingReference() {
        // Implementation for generating a unique booking reference
        return "BR-" + UUID.randomUUID();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
    }

    private EventSchedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with ID: " + scheduleId));
    }

    private Seat findSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + seatId));
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
