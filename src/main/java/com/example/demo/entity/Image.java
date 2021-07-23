package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Lob
	@Column(name = "img")
	private byte[] img;

	@JsonBackReference(value = "imageRoom")
	@ManyToOne(cascade = {CascadeType.ALL})
	private Room room;

	@JsonBackReference(value = "imageHotel")
	@ManyToOne(cascade = {CascadeType.ALL})
	private Hotel hotel;
	
	//-----------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public Image() {
	}

	public Image(long id, byte[] img, Room room, Hotel hotel) {
		this.id = id;
		this.img = img;
		this.room = room;
		this.hotel = hotel;
	}

	public Image(byte[] img, Room room) {
		this.img = img;
		this.room = room;
	}

	public Image(byte[] img, Hotel hotel) {
		this.img = img;
		this.hotel = hotel;
	}
}
