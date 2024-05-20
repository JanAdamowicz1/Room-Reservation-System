package com.example.roomreservationsystem.Room;

import com.example.roomreservationsystem.State.Free;
import com.example.roomreservationsystem.State.State;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private State state;
    private int number;
    private int capacity;
    private List<LocalDate> occupiedDates;
    private transient Rectangle rectangle;

    public Room() {
        this.state = new Free(this);
        this.occupiedDates = new ArrayList<>();
    }
    public void changeState(State newState) {
        if (!this.state.equals(newState)) {
            this.state = newState;
        }
    }
    public State getState() {
        return this.state;
    }
    public abstract String getType();
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int getCapacity() {
        return this.capacity;
    }
    public List<LocalDate> getOccupiedDates(){
        return this.occupiedDates;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
    public void addOccupiedDates(LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            occupiedDates.add(date);
        }
        for(LocalDate date : occupiedDates){
            System.out.println(date);
        }
    }
    public void removeOccupiedDates(LocalDate startDate, LocalDate endDate) {
        occupiedDates.removeIf(date -> (date.equals(startDate) || date.isAfter(startDate)) && (date.equals(endDate) || date.isBefore(endDate)));

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            System.out.println("Removed " + date + " for room number " + this.getNumber());
        }
    }

    public boolean checkDatesAvailability(LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!this.isDateAvailable(date)) {
                System.out.println("Invalid dates. One or more dates are already booked. " + date);
                return false;
            }
        }
        return true;
    }

    public boolean checkDatesOccupation(LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (this.isDateAvailable(date)) {
                System.out.println("Invalid dates. One or more dates are already free.");
                return false;
            }
        }
        return true;
    }
    public boolean isDateAvailable(LocalDate date) {
        return !occupiedDates.contains(date);
    }
    public void bookRoom(LocalDate startDate, LocalDate endDate) {
        state.bookRoom(this, startDate, endDate);
    }
    public void freeRoom(LocalDate startDate, LocalDate endDate){
        state.freeRoom(this, startDate, endDate);
    }
}
