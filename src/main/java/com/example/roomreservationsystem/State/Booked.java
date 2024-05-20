package com.example.roomreservationsystem.State;
import com.example.roomreservationsystem.Room.Room;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class Booked extends State {
    public Booked(Room room) {
        super(room);
    }

    @Override
    public void bookRoom(Room room, LocalDate startDate, LocalDate endDate) {
        room.addOccupiedDates(startDate, endDate);
        System.out.println("You can not book a booked room");
    }

    @Override
    public void freeRoom(Room room, LocalDate startDate, LocalDate endDate) {
        if (checkDateValidity(startDate, endDate) && room.checkDatesOccupation(startDate, endDate)) {
            room.removeOccupiedDates(startDate, endDate);
            room.changeState(new Free(room));
            System.out.println("Room " + room.getNumber() + " - " + room.getType() + " is now free from " + startDate + " to " + endDate);
        }
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }
}
