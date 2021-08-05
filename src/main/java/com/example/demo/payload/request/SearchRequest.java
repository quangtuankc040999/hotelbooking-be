package com.example.demo.payload.request;

import java.time.LocalDate;

public class SearchRequest {
    private String cityName;
    private LocalDate start;
    private LocalDate end;
    private int capacity;

    public SearchRequest() {
    }

    public SearchRequest(String cityName, LocalDate start, LocalDate end, int capacity) {
        this.cityName = cityName;
        this.start = start;
        this.end = end;
        this.capacity = capacity;
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}

