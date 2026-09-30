package com.eventsphere.dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookingReqDto {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Event schedule ID is required")
    private Long eventScheduleId;

    @NotEmpty(message = "At least one seat must be selected")
    private Set<Long> seatIds = new HashSet<>();
}
