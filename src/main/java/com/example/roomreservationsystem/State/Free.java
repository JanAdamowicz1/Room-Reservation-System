package com.example.roomreservationsystem.State;

import com.example.roomreservationsystem.Room.Room;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class Free extends State {
    public Free(Room room) {
        super(room);
    }

    @Override
    public void bookRoom(Room room, LocalDate startDate, LocalDate endDate) {
        if (checkDateValidity(startDate, endDate) && room.checkDatesAvailability(startDate, endDate)) {
            room.addOccupiedDates(startDate, endDate);
            room.changeState(new Booked(room));
            System.out.println("Room " + room.getNumber() + " - " + room.getType() + " is now booked from " + startDate + " to " + endDate);
        }
    }

    @Override
    public void freeRoom(Room room, LocalDate startDate, LocalDate endDate) {
        room.removeOccupiedDates(startDate, endDate);
        System.out.println("You can not free a free room");
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}
