package com.dtna.vehicle.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Vehicle {

    @NotNull
    private String id;
    @NotNull
    @NotBlank
    private String vin;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean completed;

    public Vehicle() {
        LocalDateTime now = LocalDateTime.now();
        id = UUID.randomUUID().toString();
        created = now;
        modified = now;
    }

    public Vehicle(String vid) {
        this();
        this.vin = vin;
    }
}
