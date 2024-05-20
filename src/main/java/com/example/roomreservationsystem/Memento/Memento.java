
package com.example.roomreservationsystem.Memento;

import com.example.roomreservationsystem.Reservation;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Memento implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> reservations;

    public Memento(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

}
