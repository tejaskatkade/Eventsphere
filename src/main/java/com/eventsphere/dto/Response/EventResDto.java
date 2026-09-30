package com.eventsphere.dto.Response;

import com.eventsphere.entity.EventStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventResDto {

    private Long id;

    private Long organiserId;

    private List<EventScheduleResDto> eventSchedule;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String description;

    private String language;

    private Integer durationMinutes;

    private Integer minimumAge;

    private String bannerUrl;

    private EventStatus status;

}
