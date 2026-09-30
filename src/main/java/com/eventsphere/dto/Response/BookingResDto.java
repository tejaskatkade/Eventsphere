package com.eventsphere.dto.Response;

import com.eventsphere.entity.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookingResDto {

    private Long id;

    private String bookingReference;

    private Long memberId;

    private Long eventScheduleId;

    private String eventTitle;

    private String venueName;

    private String hallName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> bookingTicketIds = new HashSet<>();

    private List<String> seatNumbers = new ArrayList<>();

    private List<BookingTicketResDto> tickets = new ArrayList<>();

    private BookingStatus bookingStatus;

    private BigDecimal totalAmount;

    private LocalDateTime bookingTime;
}
