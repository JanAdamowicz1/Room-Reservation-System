package com.example.roomreservationsystem.Factory;

import com.example.roomreservationsystem.Room.*;

public class StandardRoomFactory implements RoomFactory {
    private static StandardRoomFactory instance = null;
    private StandardRoomFactory() {

    }

    public static StandardRoomFactory getInstance() {
        if (instance == null) {
            instance = new StandardRoomFactory();
        }
        return instance;
    }

    @Override
    public Room createDoubleRoom() {
        Room newRoom = new StandardRoom();
        newRoom.setCapacity(2);
        return newRoom;
    }

    @Override
    public Room createTripleRoom() {
        Room newRoom = new StandardRoom();
        newRoom.setCapacity(3);
        return newRoom;
    }

    @Override
    public Room createFourPersonRoom() {
        Room newRoom = new StandardRoom();
        newRoom.setCapacity(4);
        return newRoom;
    }
}
