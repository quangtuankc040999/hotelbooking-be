package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    private double rating;

    @Max(5)
    @Min(0)
    private float standard;

    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "hotel")
    @PrimaryKeyJoinColumn
    private Localization address;

    @JsonManagedReference(value = "hotel")
    @ManyToOne
    private User hOwner;

    @JsonManagedReference(value = "room")
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Room> rooms;

    @JsonManagedReference(value = "imageHotel")
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Image> images;

    public Hotel() {
    }

    public Hotel(long id, @NotBlank String name, double rating, @Max(5) @Min(0) float standard, Localization address, User hOwner, List<Room> rooms, List<Image> images) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.standard = standard;
        this.address = address;
        this.hOwner = hOwner;
        this.rooms = rooms;
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public float getStandard() {
        return standard;
    }

    public void setStandard(float standard) {
        this.standard = standard;
    }

    public Localization getAddress() {
        return address;
    }

    public void setAddress(Localization address) {
        this.address = address;
    }

    public User gethOwner() {
        return hOwner;
    }

    public void sethOwner(User hOwner) {
        this.hOwner = hOwner;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
