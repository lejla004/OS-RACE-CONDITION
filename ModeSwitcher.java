/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
/**
 * Creates SeatMap instances based on user-selected mode.
 */
public class ModeSwitcher {

    public static SeatMap createSeatMap(String mode, int totalSeats) {
        switch (mode) {
            case "Locked":
                return new LockedSeatMap(totalSeats);
            case "LockFree":
                return new LockFreeSeatMap(totalSeats);
            case "Synchronized":
                return new SynchronizedSeatMap(totalSeats);
            case "ReadWriteLock":
                return new ReadWriteLockSeatMap(totalSeats);
            default:
                throw new IllegalArgumentException("Unknown seat map mode: " + mode);
        }
    }
}
