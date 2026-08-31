package com.eventsphere.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Venue extends BaseEntity {

    private String name;

    private String city;

    private String address;

    private String description;

    private String state;

    private String country;

    private String postalCode;

    private String latitude;

    private String longitude;
}
