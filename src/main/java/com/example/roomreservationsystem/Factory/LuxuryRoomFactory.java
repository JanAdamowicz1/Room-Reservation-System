package com.example.roomreservationsystem.Factory;

import com.example.roomreservationsystem.Room.*;

public class LuxuryRoomFactory implements RoomFactory {
    private static LuxuryRoomFactory instance = null;

    private LuxuryRoomFactory() {

    }

    public static LuxuryRoomFactory getInstance() {
        if (instance == null) {
            instance = new LuxuryRoomFactory();
        }
        return instance;
    }

    @Override
    public Room createDoubleRoom() {
        Room newRoom = new LuxuryRoom();
        newRoom.setCapacity(2);
        return newRoom;
    }

    @Override
    public Room createTripleRoom() {
        Room newRoom = new LuxuryRoom();
        newRoom.setCapacity(3);
        return newRoom;
    }

    @Override
    public Room createFourPersonRoom() {
        Room newRoom = new LuxuryRoom();
        newRoom.setCapacity(4);
        return newRoom;
    }
}
