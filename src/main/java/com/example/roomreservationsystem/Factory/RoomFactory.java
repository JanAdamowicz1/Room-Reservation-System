package com.example.roomreservationsystem.Factory;

import com.example.roomreservationsystem.Room.*;

public interface RoomFactory {
    Room createDoubleRoom();
    Room createTripleRoom();
    Room createFourPersonRoom();
}
