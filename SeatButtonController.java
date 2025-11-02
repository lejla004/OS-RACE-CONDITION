/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
public class SeatButtonController {
    public static void handleManualClick(int row, int col, ReservationApp gui) {
        int seatId = row * 10 + col;
        gui.log("Manual click on seat " + seatId);
        if (gui.seatMap != null) {
            boolean success = gui.seatMap.tryReserve(seatId, -1); // -1 = manual
            gui.updateSeatButton(seatId, success ? -1 : 0);
        }
    }
}
