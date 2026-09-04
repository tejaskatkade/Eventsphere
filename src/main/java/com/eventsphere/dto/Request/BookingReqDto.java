package com.eventsphere.dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookingReqDto {

    private Long memberId;

    private Long eventScheduleId;

    private Set<Long> seatIds = new HashSet<>();
}
