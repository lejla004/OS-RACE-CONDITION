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
 * ReservationAgent — performs:
 *  1) time-of-check via seatMap.isAvailable(seatId)
 *  2) simulated delay (to create the TOCTOU window)
 *  3) time-of-use via seatMap.tryReserve(seatId, threadId)
 *
 * On success/failure it reports to the GUI and can notify other listeners if needed.
 */


public class ReservationAgent implements Runnable {

    private final int threadId;
    private final SeatMap seatMap;
    private final ReservationApp gui;
    private final int targetSeatId;
    private final DelayControl delayControl;
    private final PauseController pauseController;

    public ReservationAgent(int threadId, SeatMap seatMap, ReservationApp gui,
                            int targetSeatId, DelayControl delayControl, PauseController pauseController) {
        this.threadId = threadId;
        this.seatMap = seatMap;
        this.gui = gui;
        this.targetSeatId = targetSeatId;
        this.delayControl = delayControl;
        this.pauseController = pauseController;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        try {
            // --- TIME OF CHECK ---
            boolean available = seatMap.isAvailable(targetSeatId);
            gui.log("Thread " + threadId + " checked seat " + targetSeatId + ": " + (available ? "Available" : "TAKEN"));

            // Pause / Step
            pauseController.waitIfPaused();

            // --- SIMULATED DELAY (TOCTOU window) ---
            Thread.sleep(delayControl.getDelayMs());

            // Pause / Step again
            pauseController.waitIfPaused();

            // --- TIME OF USE ---
            boolean success = seatMap.tryReserve(targetSeatId, threadId);
            long duration = System.currentTimeMillis() - startTime;

            if (!success) {
                int actualHolder = seatMap.getCurrentHolder(targetSeatId);
                gui.log("Thread " + threadId + " FAILED to reserve seat " + targetSeatId +
                        " (Lost race to T" + actualHolder + ") - Time: " + duration + "ms");

                gui.flashSeatButton(targetSeatId);        // Flash yellow/red
                gui.updateSeatButton(targetSeatId, actualHolder); // Revert to actual owner

            } else {
                gui.log("Thread " + threadId + " RESERVED seat " + targetSeatId +
                        " (SUCCESS) - Time: " + duration + "ms");
                gui.updateSeatButton(targetSeatId, threadId);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
