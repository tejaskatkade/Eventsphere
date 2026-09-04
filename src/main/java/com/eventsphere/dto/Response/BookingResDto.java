package com.eventsphere.dto.Response;

import com.eventsphere.entity.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookingResDto {
    private String bookingReference;

    private Long memberId;

    private Long eventScheduleId;

    private Set<Long> bookingTicketIds = new HashSet<>();

    private BookingStatus bookingStatus;

    private BigDecimal totalAmount;

    private LocalDateTime bookingTime;

}
