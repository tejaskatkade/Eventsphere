package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Organiser extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false, unique = true)
    private Member member;

    @Column(unique = true, nullable = false)
    private String organisationName;

    private String description;

    private String GSTNumber;

    private String website;

    private String address;

    private String city;

    private String state;

    private Integer postalCode;

    private Boolean isVerified;
}
