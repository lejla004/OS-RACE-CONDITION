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
 * Stores temporary "phase" states for each seat:
 * CHECK → DELAY → ATTEMPT → SUCCESS/FAIL
 * GUI can query to change colors/animations.
 */
public class ThreadPhaseVisualizer {

    public enum Phase {
        IDLE, CHECK, DELAY, ATTEMPT, SUCCESS, FAIL
    }

    private final Map<Integer, Phase> seatPhases = new HashMap<>();

    public void setPhase(int seatId, Phase phase) {
        seatPhases.put(seatId, phase);
    }

    public Phase getPhase(int seatId) {
        return seatPhases.getOrDefault(seatId, Phase.IDLE);
    }

    public void reset() {
        seatPhases.clear();
    }
}
