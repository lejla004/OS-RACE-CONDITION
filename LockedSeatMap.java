/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SeatMap implementation that protects the entire seats array with a single Lock.
 * This eliminates races by serializing access to the seats array.
 *
 * Representation: int[] seats where 0 == available, >0 == threadId holding the seat.
 */
public class LockedSeatMap implements SeatMap {

    private final Lock mapLock = new ReentrantLock();
    private final int[] seats;

    /**
     * Create a LockedSeatMap with the given number of seats.
     *
     * @param totalSeats number of seats (must be > 0)
     */
    public LockedSeatMap(int totalSeats) {
        if (totalSeats <= 0) throw new IllegalArgumentException("totalSeats must be > 0");
        seats = new int[totalSeats];
        // all seats default to 0 (available)
    }

    @Override
    public boolean isAvailable(int seatId) {
        mapLock.lock();
        try {
            validateSeatId(seatId);
            return seats[seatId] == 0;
        } finally {
            mapLock.unlock();
        }
    }

    @Override
    public boolean tryReserve(int seatId, int threadId) {
        mapLock.lock();
        try {
            validateSeatId(seatId);
            if (seats[seatId] == 0) {
                seats[seatId] = threadId;
                return true;
            }
            return false;
        } finally {
            mapLock.unlock();
        }
    }

    @Override
    public int getCurrentHolder(int seatId) {
        mapLock.lock();
        try {
            validateSeatId(seatId);
            return seats[seatId];
        } finally {
            mapLock.unlock();
        }
    }

    @Override
    public int getTotalSeats() {
        return seats.length;
    }

    private void validateSeatId(int seatId) {
        if (seatId < 0 || seatId >= seats.length) {
            throw new IndexOutOfBoundsException("seatId out of range: " + seatId);
        }
    }
}
