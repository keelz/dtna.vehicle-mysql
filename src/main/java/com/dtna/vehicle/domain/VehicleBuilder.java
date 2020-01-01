package com.dtna.vehicle.domain;

public class VehicleBuilder {

    private static VehicleBuilder instance = new VehicleBuilder();
    private String id = null;
    private String vin = "";

    private VehicleBuilder() {}

    public static VehicleBuilder create() {
        return instance;
    }

    public VehicleBuilder withId(String id) {
        this.id = id;
        return instance;
    }

    public VehicleBuilder withVin(String vin) {
        this.vin = vin;
        return instance;
    }

    public Vehicle build() {
        Vehicle result = new Vehicle(vin);
        if (null != id) {
            result.setId(id);
        }
        return result;
    }
}
