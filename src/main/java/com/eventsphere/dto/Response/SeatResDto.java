package com.eventsphere.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatResDto {
    
    private String rowName;

    private Integer seatNumber;

    private String seatType;

    private Boolean isActive;
}
