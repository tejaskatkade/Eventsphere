package com.eventsphere.dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventReqDto {

    private Long organiserId;

    private Long eventScheduleId;

    private Long categoryId;

    private String title;

    private String description;

    private String language;

    private Integer durationMinutes;

    private Integer minimumAge;

    private String bannerUrl;
}
