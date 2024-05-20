package com.example.roomreservationsystem.Memento;

import java.io.*;

public class Caretaker implements Serializable {
    private static final long serialVersionUID = 1L;
    public void saveReservations(String filePath, Memento memento) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(memento);
        }
    }

    public Memento loadReservations(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Memento) in.readObject();
        }
    }
}