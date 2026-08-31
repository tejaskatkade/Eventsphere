package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Organizer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false, unique = true)
    private Member member;

    @Column(unique = true, nullable = false)
    private String organizationName;

    private String description;

    private String GSTNumber;

    private String website;

    private String address;

    private String city;

    private String state;

    private Integer postalCode;

    private Boolean isVerified;
}
