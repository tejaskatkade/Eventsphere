package com.eventsphere.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HallReqDto {

    @NotNull
    private Long venueId;

    private String name;

    @Min(value = 1)
    private Integer capacity;
}
