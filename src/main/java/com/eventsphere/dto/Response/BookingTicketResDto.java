package com.eventsphere.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingTicketResDto {

    private Long id;

    private Long seatId;

    private String rowName;

    private Integer seatNumber;

    private String seatType;

    private BigDecimal ticketPrice;
}
