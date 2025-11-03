/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lock-free SeatMap backed by AtomicReference<Integer> per seat.
 * Value semantics: 0 == available, >0 == threadId owner.
 *
 * Uses compareAndSet(0, threadId) to atomically claim a seat.
 */
@SuppressWarnings("unchecked")
public class LockFreeSeatMap implements SeatMap {

    private final AtomicReference<Integer>[] seats;

    public LockFreeSeatMap(int totalSeats) {
        if (totalSeats <= 0) throw new IllegalArgumentException("totalSeats must be > 0");
        seats = new AtomicReference[totalSeats];
        for (int i = 0; i < totalSeats; i++) {
            seats[i] = new AtomicReference<>(0); // 0 = available
        }
    }

    @Override
    public boolean isAvailable(int seatId) {
        validateSeatId(seatId);
        return seats[seatId].get() == 0;
    }

    @Override
    public boolean tryReserve(int seatId, int threadId) {
        validateSeatId(seatId);
        // Atomic CAS: succeed only if current value is 0
        return seats[seatId].compareAndSet(0, threadId);
    }

    @Override
    public int getCurrentHolder(int seatId) {
        validateSeatId(seatId);
        return seats[seatId].get();
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
