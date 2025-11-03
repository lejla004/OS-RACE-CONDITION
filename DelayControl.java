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
 * Provides a backend representation of a user-adjustable delay.
 * GUI will bind a slider/spinner to this value.
 */
public class DelayControl {

    private long delayMs;

    public DelayControl(long initialDelayMs) {
        this.delayMs = Math.max(0, initialDelayMs);
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = Math.max(0, delayMs);
    }
}

