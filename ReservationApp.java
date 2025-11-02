/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lockfreeuserinput;

/**
 *
 * @author Lela
 */
import java.awt.Choice;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReservationApp extends Application {

    private static final int ROWS = 5;
    private static final int COLS = 10;
    private static final int TOTAL_SEATS = ROWS * COLS;
    private Button[][] seatButtons = new Button[ROWS][COLS];
    private Map<Integer, Color> threadColors = new HashMap<>();
    
    public SeatMap seatMap; 
    private TextArea logArea = new TextArea();
    private SimulationManager simManager;

    // NEW: Text field for user input
    private TextField assignmentInput = new TextField(); 

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        // --- Top Controls ---
        HBox topControls = new HBox(10);
Button startRaceBtn = new Button("Start Race");
Button resetBtn = new Button("Reset");
        ChoiceBox<String> modeChoice = new ChoiceBox<>(); 
modeChoice.getItems().addAll("Locked", "Lock-Free");
modeChoice.setValue("Locked");
        
        // Setup input field
        assignmentInput.setPromptText("T:SeatIndex (e.g., 1:9, 2:9, 3:10)");
        assignmentInput.setPrefWidth(250);
        assignmentInput.setText("1:9, 2:9, 3:10"); // Default race setup for convenience

        topControls.getChildren().addAll(
            startRaceBtn, resetBtn, 
            new Label("Mode:"), modeChoice, 
            new Label("Assignments:"), assignmentInput
        );
        topControls.setPadding(new Insets(10));
        root.setTop(topControls);

        // --- Center Seat Grid ---
        GridPane seatGrid = createSeatGrid();
        root.setCenter(seatGrid);

        // --- Bottom Log ---
        logArea.setEditable(false);
        logArea.setPrefHeight(200);
        VBox bottomBox = new VBox(10, logArea);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lock-Free Race Condition Simulator");
        primaryStage.show();

        // --- Event Handlers ---
        startRaceBtn.setOnAction(e -> startRace(modeChoice.getValue(), assignmentInput.getText()));
        resetBtn.setOnAction(e -> resetSimulation());
    }

    private GridPane createSeatGrid() {
        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(5);
        seatGrid.setVgap(5);
        seatGrid.setPadding(new Insets(10));
        
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int seatIndex = i * COLS + j;
                // Display seat index + 1 for user readability (Seat 1, Seat 2, etc.)
                Button btn = new Button("S" + (seatIndex + 1)); 
                btn.setPrefSize(60, 40);
                btn.setStyle("-fx-background-color: white; -fx-border-color: black;");
                seatGrid.add(btn, j, i);
                seatButtons[i][j] = btn;
            }
        }
        return seatGrid;
    }

    private void startRace(String mode, String assignmentString) {
        if (simManager != null) simManager.stopSimulation();
        
        // 1. PARSE USER INPUT
        List<int[]> assignments = parseAssignments(assignmentString);
        
        if (assignments.isEmpty()) {
            log("Error: No valid thread assignments found. Please use format T:SeatIndex (e.g., 1:9, 2:9).");
            return;
        }
        
        // 2. Setup map and visualization
        if (mode.equals("Locked")) {
            seatMap = new LockedSeatMap(TOTAL_SEATS);
        } else {
            seatMap = new LockFreeSeatMap(TOTAL_SEATS);
        }
        
        resetSeats(); 
        assignThreadColors(assignments); 
        
        // 3. Start the simulation
        simManager = new SimulationManager(assignments, seatMap, this);
        simManager.startSimulation();
    }
    
    // NEW Helper Method to parse the input
    private List<int[]> parseAssignments(String input) {
        List<int[]> assignments = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) return assignments;
        
        // Split by comma for each assignment (e.g., "1:9, 2:9")
        String[] parts = input.split(",");

        for (String part : parts) {
            try {
                // Split each assignment by colon (e.g., "1:9")
                String[] tokens = part.trim().split(":");
                if (tokens.length == 2) {
                    // tokens[0] is the Thread ID, tokens[1] is the Seat Index
                    int threadId = Integer.parseInt(tokens[0].trim());
                    int seatId = Integer.parseInt(tokens[1].trim());
                    
                    if (seatId >= 0 && seatId < TOTAL_SEATS && threadId > 0) {
                        assignments.add(new int[]{threadId, seatId});
                    } else {
                        log("Warning: Invalid ID or Seat Index (" + seatId + ") is out of bounds (0-" + (TOTAL_SEATS - 1) + "). Skipped.");
                    }
                }
            } catch (NumberFormatException e) {
                log("Warning: Invalid assignment format detected: " + part + ". Skipped.");
            }
        }
        return assignments;
    }

    private void assignThreadColors(List<int[]> assignments) {
        Random rand = new Random();
        threadColors.clear();
        
        for (int[] assignment : assignments) {
             int threadId = assignment[0];
             if (!threadColors.containsKey(threadId)) {
                 threadColors.put(threadId, Color.hsb(rand.nextDouble() * 360, 0.8, 0.8));
             }
        }
    }

    public void updateSeatButton(int seatId, int threadId) {
        int row = seatId / COLS;
        int col = seatId % COLS;
        Platform.runLater(() -> {
            Button btn = seatButtons[row][col];
            Color c = Color.WHITE;
            
            if (threadId > 0) { // Reserved by a thread
                c = threadColors.getOrDefault(threadId, Color.BLACK);
            }
            
            btn.setStyle("-fx-background-color: " + toRgbString(c) + "; -fx-border-color: black;");
        });
    }
    
    public void flashSeatButton(int seatId) {
        int row = seatId / COLS;
        int col = seatId % COLS;
        Platform.runLater(() -> {
            Button btn = seatButtons[row][col];
            btn.setStyle("-fx-background-color: yellow; -fx-border-color: red;");
        });
    }

    public void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    private void resetSimulation() {
        if (simManager != null) simManager.stopSimulation();
        resetSeats();
    }
    
    private void resetSeats() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seatButtons[i][j].setStyle("-fx-background-color: white; -fx-border-color: black;");
            }
        }
        logArea.clear();
    }

    private String toRgbString(Color c) {
        return "rgb(" + (int)(c.getRed()*255) + "," + (int)(c.getGreen()*255) + "," + (int)(c.getBlue()*255) + ")";
    }

    public static void main(String[] args) {
        launch(args);
    }
}