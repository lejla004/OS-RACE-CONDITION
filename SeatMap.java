/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
public interface SeatMap {
    boolean isAvailable(int seatId);
    boolean tryReserve(int seatId, int threadId);
    int getTotalSeats();

    public int getCurrentHolder(int targetSeatId);
}