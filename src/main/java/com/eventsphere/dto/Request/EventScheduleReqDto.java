package com.eventsphere.dto.Request;

import com.eventsphere.entity.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventScheduleReqDto {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Hall ID is required")
    private Long hallId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private ScheduleStatus status;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be greater than zero")
    private BigDecimal ticketPrice;

}
