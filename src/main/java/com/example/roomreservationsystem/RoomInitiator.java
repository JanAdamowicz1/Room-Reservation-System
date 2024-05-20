package com.example.roomreservationsystem;

import com.example.roomreservationsystem.Factory.*;
import com.example.roomreservationsystem.Room.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomInitiator {
    private static final int STANDARD_DOUBLE_ROOM_NUMBER = 4;
    private static final int STANDARD_TRIPLE_ROOM_NUMBER = 2;
    private static final int STANDARD_FOUR_PERSON_ROOM_NUMBER = 4;
    private static final int LUXURY_DOUBLE_ROOM_NUMBER = 4;
    private static final int LUXURY_TRIPLE_ROOM_NUMBER = 2;
    private static final int LUXURY_FOUR_PERSON_ROOM_NUMBER = 4;
    private List<Room> rooms = new ArrayList<>();
    public void initRooms() {
        RoomFactory standardRoomFactory = StandardRoomFactory.getInstance();
        RoomFactory luxuryRoomFactory = LuxuryRoomFactory.getInstance();

        int roomNumber = 1;

        for (int i = 0; i < STANDARD_DOUBLE_ROOM_NUMBER; i++){
            Room room = standardRoomFactory.createDoubleRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }
        for (int i = 0; i < STANDARD_TRIPLE_ROOM_NUMBER; i++){
            Room room = standardRoomFactory.createTripleRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }
        for (int i = 0; i < STANDARD_FOUR_PERSON_ROOM_NUMBER; i++){
            Room room = standardRoomFactory.createFourPersonRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }

        for (int i = 0; i < LUXURY_DOUBLE_ROOM_NUMBER; i++){
            Room room = luxuryRoomFactory.createDoubleRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }
        for (int i = 0; i < LUXURY_TRIPLE_ROOM_NUMBER; i++){
            Room room = luxuryRoomFactory.createTripleRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }
        for (int i = 0; i < LUXURY_FOUR_PERSON_ROOM_NUMBER; i++){
            Room room = luxuryRoomFactory.createFourPersonRoom();
            room.setNumber(roomNumber);
            roomNumber++;
            rooms.add(room);
        }
    }

    public List<Room> getRooms(){
        return this.rooms;
    }
}
