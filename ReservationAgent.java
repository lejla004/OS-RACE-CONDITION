/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
import java.util.Random;

public class ReservationAgent implements Runnable {

    private final int threadId;
    private final SeatMap seatMap;
    private final ReservationApp gui;
    // NEW: The specific seat this agent is assigned to target
    private final int targetSeatId; 

    private static final int DELAY_MS = 150;

    // CONSTRUCTOR CHANGE: Accepts targetSeatId
    public ReservationAgent(int threadId, SeatMap seatMap, ReservationApp gui, int targetSeatId) {
        this.threadId = threadId;
        this.seatMap = seatMap;
        this.gui = gui;
        this.targetSeatId = targetSeatId;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        try {
            // 1. TIME OF CHECK (Read)
            boolean available = seatMap.isAvailable(targetSeatId);
            gui.log("Thread " + threadId + " checked seat " + targetSeatId + ": " + (available ? "Available" : "TAKEN"));

            // 2. SIMULATED DELAY (The TOCTOU window where the race occurs)
            Thread.sleep(DELAY_MS); 
            
            // 3. TIME OF USE (The critical commit attempt)
            boolean success = seatMap.tryReserve(targetSeatId, threadId);
            
            long duration = System.currentTimeMillis() - startTime;

            if (!success) {
                int actualHolder = seatMap.getCurrentHolder(targetSeatId);

                // Race Lost or already taken
                gui.log("Thread " + threadId + " FAILED to reserve seat " + targetSeatId + 
                        " (Lost race to T" + actualHolder + ") - Time: " + duration + "ms");
                
                // Visualization: Briefly flash the seat to show the failed attempt
                gui.flashSeatButton(targetSeatId);
                
                // Revert color back to the winner's color or white if the seat wasn't actually taken
                gui.updateSeatButton(targetSeatId, actualHolder); 
            } else {
                // Race Won
                gui.log("Thread " + threadId + " RESERVED seat " + targetSeatId + 
                        " (SUCCESS) - Time: " + duration + "ms");
                gui.updateSeatButton(targetSeatId, threadId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}