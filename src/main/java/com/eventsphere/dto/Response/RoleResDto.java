package com.eventsphere.dto.Response;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// TODO : Currently this class is same as @RoleReqDto. Hence, can be removed if it remains same.
public class RoleResDto {

    private Long Id;

    @NotEmpty
    private String name;

    private String description;
}
