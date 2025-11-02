/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedSeatMap implements SeatMap {

    private final Lock mapLock = new ReentrantLock();
    // 0 = available, >0 = threadId
    private final int[] seats; 

    public LockedSeatMap(int totalSeats) {
        seats = new int[totalSeats];
    }

    @Override
    public boolean isAvailable(int seatId) {
        mapLock.lock();
        try {
            return seats[seatId] == 0;
        } finally {
            mapLock.unlock();
        }
    }

    @Override
    public boolean tryReserve(int seatId, int threadId) {
        mapLock.lock();
        try {
            if (seats[seatId] == 0) {
                // If it was available, we reserve it while holding the lock
                seats[seatId] = threadId;
                return true;
            }
            return false;
        } finally {
            mapLock.unlock();
        }
    }
    
    public int getCurrentHolder(int seatId) {
        // Must lock to ensure a safe read from the non-atomic array
        mapLock.lock();
        try {
            return seats[seatId];
        } finally {
            mapLock.unlock();
        }
    }

    @Override
    public int getTotalSeats() {
        return seats.length;
    }
}