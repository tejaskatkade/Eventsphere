package com.eventsphere.dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatReqDto {

    private Long hallId;

    private String rowName;

    private Integer seatNumber;

    private String seatType;

    private Boolean isActive;
}
