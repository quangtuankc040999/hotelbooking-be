package com.example.demo.payload.request;

import com.example.demo.entity.Localization;

public class HotelRequest {
    public String name;
    public float standard;
    public Localization localization;

    public HotelRequest(String name, float standard, Localization localization) {
        this.name = name;
        this.standard = standard;
        this.localization = localization;
    }

    public float getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }
}
