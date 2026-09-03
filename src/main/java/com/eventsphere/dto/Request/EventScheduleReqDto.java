package com.eventsphere.dto.Request;

import com.eventsphere.entity.ScheduleStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventScheduleReqDto {

    private Long eventId;

    private Long hallId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ScheduleStatus status;

    private BigDecimal ticketPrice;

}
