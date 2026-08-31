package com.eventsphere.dto.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleReqDto {

    @NotEmpty
    private String name;

    private String description;
}
