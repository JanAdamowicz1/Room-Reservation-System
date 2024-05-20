package com.example.roomreservationsystem;

import com.example.roomreservationsystem.Room.Room;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
    private String phoneNumber;

    public Reservation(Room room, LocalDate startDate, LocalDate endDate, String customerName, String phoneNumber) {
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Room " + room.getNumber() + ": " + startDate + " : " + endDate;
    }

    public Room getRoom() {
        return this.room;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}