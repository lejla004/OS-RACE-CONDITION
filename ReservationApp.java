package osproj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationApp extends Application {

    private static final int ROWS = 5;
    private static final int COLS = 10;
    private Button[][] seatButtons = new Button[ROWS][COLS];

    private SeatMap seatMap;
    private SimulatorManager simManager;

    private SimulationConfig config = new SimulationConfig();
    private DelayControl delayControl = new DelayControl(150);
    private PauseController pauseController = new PauseController();
    private LegendPanel legendPanel = new LegendPanel();
    private TooltipManager tooltipManager = new TooltipManager();
    private StatsCollector statsCollector = new StatsCollector();
    private StatsPanel statsPanel = new StatsPanel(statsCollector);

    private TextArea logArea = new TextArea();
    private TextField assignmentInput = new TextField();
    private int[] seatOwners = new int[ROWS * COLS]; // 0 = empty, -1 = manual, >0 = threadId

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        // --- Top Controls ---
        HBox topControls = new HBox(10);
        Button startRaceBtn = new Button("Start Race");
        Button resetBtn = new Button("Reset");
        Button pauseBtn = new Button("Pause");
        Button stepBtn = new Button("Step");

        ChoiceBox<String> modeChoice = new ChoiceBox<>();
        modeChoice.getItems().addAll("Locked", "LockFree", "Synchronized", "ReadWriteLock");
        modeChoice.setValue("Locked");

        Slider delaySlider = new Slider(50, 1000, 150);
        delaySlider.setShowTickLabels(true);
        delaySlider.setShowTickMarks(true);
        delaySlider.valueProperty().addListener((obs, oldV, newV) -> delayControl.setDelayMs(newV.longValue()));

        assignmentInput.setPromptText("T:Seat (e.g., 1:9,2:9)");
        assignmentInput.setPrefWidth(250);
        assignmentInput.setText("1:9, 2:9, 3:10");

        topControls.getChildren().addAll(
                startRaceBtn, resetBtn, pauseBtn, stepBtn,
                new Label("Mode:"), modeChoice,
                new Label("Delay(ms):"), delaySlider,
                new Label("Assignments:"), assignmentInput
        );
        topControls.setPadding(new Insets(10));
        root.setTop(topControls);

        // --- Seat Grid ---
        GridPane seatGrid = createSeatGrid();
        root.setCenter(seatGrid);

        // --- Bottom Log ---
        logArea.setEditable(false);
        logArea.setPrefHeight(200);
        VBox bottomBox = new VBox(10, logArea);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lock-Free Race Condition Simulator");
        primaryStage.show();

        // --- Event Handlers ---
        startRaceBtn.setOnAction(e -> startRace(modeChoice.getValue(), assignmentInput.getText()));
        resetBtn.setOnAction(e -> resetSimulation());
        pauseBtn.setOnAction(e -> pauseController.pause());
        stepBtn.setOnAction(e -> pauseController.step());
    }

    private GridPane createSeatGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int seatId = i * COLS + j;
                Button btn = new Button("S" + (seatId + 1));
                btn.setPrefSize(60, 40);
                btn.setStyle("-fx-background-color: white; -fx-border-color: black;");
                seatButtons[i][j] = btn;
                int finalSeatId = seatId;
                btn.setOnAction(ev -> handleManualClick(finalSeatId));
                grid.add(btn, j, i);
            }
        }
        return grid;
    }

    private void handleManualClick(int seatId) {
        if (seatMap != null) {
            boolean success = seatMap.tryReserve(seatId, -1); // -1 = manual
            seatOwners[seatId] = success ? -1 : seatMap.getCurrentHolder(seatId);
            updateSeatButton(seatId, seatOwners[seatId]);
            tooltipManager.updateTooltip(seatId, seatOwners[seatId]);
            log("Manual click on seat " + seatId + " → " + (success ? "Reserved" : "Already taken"));
        }
    }

   private void startRace(String mode, String assignmentStr) {
    // Stop any previous simulation
    if (simManager != null) simManager.stopSimulation();

    // Parse assignments
    List<Assignment> assignments = AssignmentParser.parse(assignmentStr, ROWS * COLS);
    if (assignments.isEmpty()) {
        log("No valid assignments found!");
        return;
    }
    config.setAssignments(assignments);

    // Setup SeatMap
    seatMap = ModeSwitcher.createSeatMap(mode, ROWS * COLS);

    // Assign thread colors
    legendPanel.assignColors(assignments);

    // Reset seats and stats
    resetSeats();
    statsCollector.start();

    // Start simulation
    simManager = new SimulatorManager(assignments, seatMap, this, delayControl.getDelayMs());
    simManager.startSimulation();
}


    public void flashSeatButton(int seatId) {
        int row = seatId / COLS;
        int col = seatId % COLS;

        Platform.runLater(() -> seatButtons[row][col].setStyle("-fx-background-color: yellow; -fx-border-color: red;"));

        // Revert after 200ms
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int ownerId = seatOwners[seatId];
            Platform.runLater(() -> updateSeatButton(seatId, ownerId));
        }).start();
    }

    public void updateSeatButton(int seatId, int threadId) {
        seatOwners[seatId] = threadId; // store owner
        int row = seatId / COLS;
        int col = seatId % COLS;

        Platform.runLater(() -> {
            Button btn = seatButtons[row][col];
            Color color;

            if (threadId > 0) {
                color = legendPanel.getColor(threadId); // thread color
            } else if (threadId == -1) {
                color = Color.GRAY; // manual reservation
            } else {
                color = Color.WHITE; // available
            }

            btn.setStyle("-fx-background-color: " + toRgbString(color) + "; -fx-border-color: black;");
        });
    }

    private void resetSimulation() {
        if (simManager != null) simManager.stopSimulation();
        resetSeats();
        statsCollector.end();
        log("Simulation reset.");
    }

    private void resetSeats() {
        for (int i = 0; i < seatOwners.length; i++) seatOwners[i] = 0; // clear owners
        Platform.runLater(() -> {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    seatButtons[i][j].setStyle("-fx-background-color: white; -fx-border-color: black;");
                }
            }
            logArea.clear();
        });
    }

    public void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    private String toRgbString(Color c) {
        return "rgb(" + (int) (c.getRed() * 255) + "," + (int) (c.getGreen() * 255) + "," + (int) (c.getBlue() * 255) + ")";
    }
public DelayControl getDelayControl() {
    return delayControl;
}

public PauseController getPauseController() {
    return pauseController;
}

    public static void main(String[] args) {
        launch(args);
    }
}
