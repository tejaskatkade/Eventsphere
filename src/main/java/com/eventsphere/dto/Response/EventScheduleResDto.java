package com.eventsphere.dto.Response;

import com.eventsphere.entity.Hall;
import com.eventsphere.entity.ScheduleStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventScheduleResDto {

    private Long hallId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ScheduleStatus status;

    private BigDecimal ticketPrice;

}
