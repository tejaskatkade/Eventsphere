package com.eventsphere.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MemberResDto {

    private Long Id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Boolean isActive;

    private List<RoleResDto> role;
}
