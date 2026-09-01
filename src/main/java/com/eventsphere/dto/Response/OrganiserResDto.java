package com.eventsphere.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganiserResDto {

    private Long Id;

    private Long memberId;

    private String organisationName;

    private String description;
}
