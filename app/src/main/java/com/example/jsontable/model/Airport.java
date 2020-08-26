package com.example.jsontable.model;

public class Airport {
    private String city;
    private String airport;
    private String code;
    private String country;

    public Airport() {
    }

    public Airport(String city, String airport, String code, String country) {
        this.city = city;
        this.airport = airport;
        this.code = code;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getAirport() {
        return airport;
    }

    public String getCode() {
        return code;
    }

    public String getCountry() {
        return country;
    }
}
