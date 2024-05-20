package com.example.roomreservationsystem.State;

import com.example.roomreservationsystem.Room.Room;
import com.example.roomreservationsystem.ValidationUtility;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class State implements Serializable {
    private static final long serialVersionUID = 1L;
    Room room;
    State (Room room) {
        this.room = room;
    }

    public abstract void bookRoom(Room room, LocalDate startDate, LocalDate endDate);
    public abstract void freeRoom(Room room, LocalDate startDate, LocalDate endDate);
    public abstract Color getColor();

    public boolean checkDateValidity(LocalDate startDate, LocalDate endDate){
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today) || endDate.isBefore(today)) {
            System.out.println("Invalid dates. Dates cannot be set earlier than today.");
            return false;
        }

        if (endDate.isBefore(startDate) || startDate.isEqual(endDate)) {
            System.out.println("Invalid dates. End date must be later than start date.");
            return false;
        }
        return true;
    }
}
