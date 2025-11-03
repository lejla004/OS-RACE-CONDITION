package osproj;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulatorManager {

    private final List<Assignment> assignments; // now List<Assignment>
    private final SeatMap seatMap;
    private final ReservationApp gui;
    private final long delayMs;
    private ExecutorService executor;

    public SimulatorManager(List<Assignment> assignments, SeatMap seatMap, ReservationApp gui, long delayMs) {
        if (assignments == null) throw new IllegalArgumentException("assignments required");
        if (seatMap == null) throw new IllegalArgumentException("seatMap required");
        this.assignments = assignments;
        this.seatMap = seatMap;
        this.gui = gui;
        this.delayMs = Math.max(0, delayMs);
    }

    public void startSimulation() {
        stopSimulation(); // stop any previous run

        if (assignments.isEmpty()) {
            if (gui != null) gui.log("SimulationManager: no assignments to run.");
            return;
        }

        executor = Executors.newFixedThreadPool(assignments.size());
        for (Assignment a : assignments) {
    ReservationAgent agent = new ReservationAgent(
        a.getThreadId(),
        seatMap,
        gui,
        a.getSeatId(),
        gui.getDelayControl(),     // you need to provide a getter
        gui.getPauseController()   // you need to provide a getter
);
executor.submit(agent);

            executor.submit(agent);
        }

        executor.shutdown();

        if (gui != null) {
            new Thread(() -> {
                try {
                    boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
                    if (finished) gui.log("Simulation completed.");
                    else gui.log("Simulation did not finish within timeout.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    gui.log("Simulation watcher interrupted.");
                }
            }, "Simulation-Watcher").start();
        }
    }

    public void stopSimulation() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
            if (gui != null) gui.log("SimulationManager: requested shutdownNow.");
        }
    }
}

