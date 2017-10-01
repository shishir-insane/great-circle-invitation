package com.sk.intercom.test.dto;

public class Customer {
    private final String name;
    private final double distance;

    public Customer(final String name, final double distance) {
        this.name = name;
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }
}
