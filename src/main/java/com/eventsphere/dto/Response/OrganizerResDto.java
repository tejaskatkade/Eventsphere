package com.eventsphere.dto.Response;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizerResDto {

    private Long Id;

    private Long memberId;

    private String organizationName;

    private String description;
}
