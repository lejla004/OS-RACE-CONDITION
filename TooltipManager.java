/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.HashMap;
import java.util.Map;

/**
 * Stores tooltips for seats like "Seat 9 → T2".
 * GUI reads this map and attaches to buttons.
 */
public class TooltipManager {

    private final Map<Integer, String> seatTooltips = new HashMap<>();

    /**
     * Update tooltip for a seat.
     *
     * @param seatId seat index
     * @param threadId thread that owns the seat, or 0 if available
     */
    public void updateTooltip(int seatId, int threadId) {
        if (threadId > 0) {
            seatTooltips.put(seatId, "Seat " + seatId + " → T" + threadId);
        } else {
            seatTooltips.put(seatId, "Seat " + seatId + " → Available");
        }
    }

    public String getTooltip(int seatId) {
        return seatTooltips.getOrDefault(seatId, "Seat " + seatId + " → Unknown");
    }
}
