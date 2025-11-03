/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * SeatMap demonstrating concurrent reads and exclusive writes.
 */
public class ReadWriteLockSeatMap implements SeatMap {

    private final int[] seats;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ReadWriteLockSeatMap(int totalSeats) {
        seats = new int[totalSeats];
    }

    @Override
    public boolean isAvailable(int seatId) {
        rwLock.readLock().lock();
        try {
            return seats[seatId] == 0;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public boolean tryReserve(int seatId, int threadId) {
        rwLock.writeLock().lock();
        try {
            if (seats[seatId] == 0) {
                seats[seatId] = threadId;
                return true;
            }
            return false;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public int getCurrentHolder(int seatId) {
        rwLock.readLock().lock();
        try {
            return seats[seatId];
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public int getTotalSeats() {
        return seats.length;
    }
}
