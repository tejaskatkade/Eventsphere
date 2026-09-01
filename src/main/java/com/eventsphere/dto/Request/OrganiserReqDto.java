package com.eventsphere.dto.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganiserReqDto {

    @NotEmpty
    private String organisationName;

    private String description;

    private String GSTNumber;

    private String website;

    private String address;

    private String city;

    private String state;

    private Integer postalCode;
}
