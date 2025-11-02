/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationManager {

    // A list of (ThreadId, SeatId) pairs passed from the GUI
    private final List<int[]> assignments; 
    private final SeatMap seatMap;
    private final ReservationApp gui;

    private ExecutorService executor;

    // CONSTRUCTOR: Accepts the list of assignments
    public SimulationManager(List<int[]> assignments, SeatMap seatMap, ReservationApp gui) {
        this.assignments = assignments;
        this.seatMap = seatMap;
        this.gui = gui;
    }

    public void startSimulation() {
        // Create a thread pool sized exactly to the number of assignments
        executor = Executors.newFixedThreadPool(assignments.size());

        for (int[] assignment : assignments) {
            int threadId = assignment[0];
            int seatId = assignment[1];
            
            // Create a ReservationAgent for this specific assignment
            ReservationAgent agent = new ReservationAgent(threadId, seatMap, gui, seatId);
            executor.submit(agent);
        }
        
        // Shut down the pool after all tasks are submitted
        executor.shutdown(); 
    }

    public void stopSimulation() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}