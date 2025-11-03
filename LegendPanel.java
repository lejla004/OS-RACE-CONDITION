/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LegendPanel {

    // Map each thread ID to a unique color
    private final Map<Integer, Color> threadColors = new HashMap<>();

    // Assign random colors to each thread in the assignments
    public void assignColors(List<Assignment> assignments) {
        Random rand = new Random();
        threadColors.clear();
        for (Assignment a : assignments) {
            int threadId = a.getThreadId();
            if (!threadColors.containsKey(threadId)) {
                threadColors.put(threadId, Color.hsb(rand.nextDouble() * 360, 0.8, 0.8));
            }
        }
    }

    // Get the color for a thread, default to black if missing
    public Color getColor(int threadId) {
        return threadColors.getOrDefault(threadId, Color.BLACK);
    }
}
