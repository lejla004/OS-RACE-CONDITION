/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
public interface SeatMap {
    /**
     * Check if the seat is currently available.
     * This is a logical read and may be non-atomic depending on implementation.
     *
     * @param seatId seat index (0-based)
     * @return true if available (no current holder), false otherwise
     */
    boolean isAvailable(int seatId);

    /**
     * Attempt to reserve the seat for the given threadId.
     * Implementations should ensure a consistent outcome (either succeed or fail).
     *
     * @param seatId   seat index (0-based)
     * @param threadId positive id of the thread trying to reserve (use -1 for manual or special)
     * @return true if this call successfully reserved the seat, false if the seat was already taken
     */
    boolean tryReserve(int seatId, int threadId);

    /**
     * Return the current holder id for the seat.
     * 0 or a defined constant denotes "available" for your implementations.
     *
     * @param seatId seat index
     * @return threadId currently holding the seat (0 if none)
     */
    int getCurrentHolder(int seatId);

    /**
     * Number of seats managed by this map.
     */
    int getTotalSeats();
}
