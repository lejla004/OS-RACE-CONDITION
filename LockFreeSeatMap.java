/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeSeatMap implements SeatMap {

    // Note: The AtomicReference must hold the Integer, not the primitive int
    private final AtomicReference<Integer>[] seats;

    @SuppressWarnings("unchecked")
    public LockFreeSeatMap(int totalSeats) {
        seats = new AtomicReference[totalSeats];
        for (int i = 0; i < totalSeats; i++) {
            // 0 = Available
            seats[i] = new AtomicReference<>(0);
        }
    }

    @Override
    public boolean isAvailable(int seatId) {
        // Safe read of the atomic reference
        return seats[seatId].get() == 0;
    }

    @Override
    public boolean tryReserve(int seatId, int threadId) {
        // The single, atomic CAS operation
        return seats[seatId].compareAndSet(0, threadId);
    }
    
    public int getCurrentHolder(int seatId) {
        // Safe read of the current holder ID
        return seats[seatId].get();
    }

    @Override
    public int getTotalSeats() {
        return seats.length;
    }
}