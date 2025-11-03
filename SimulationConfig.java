/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.List;

/**
 * Stores runtime configuration for a simulation.
 * All fields are mutable for easy updating via GUI bindings.
 */
public class SimulationConfig {

    // Which seat map mode: "Locked" or "LockFree"
    private String mode = "Locked";

    // Simulation delay (ms)
    private long delayMs = 150;

    // Assignments for this run
    private List<Assignment> assignments;

    // Pause/step mode
    private boolean stepMode = false;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        if (mode != null) this.mode = mode;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = Math.max(0, delayMs);
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
    }
}
