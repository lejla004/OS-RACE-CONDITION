/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Parses user-provided assignment strings like "1:9, 2:9, 3:10"
 * into List<Assignment>.
 */
public class AssignmentParser {

    /**
     * Parse input string into assignments.
     *
     * @param input comma-separated string of "threadId:seatId"
     * @param maxSeats maximum valid seat index
     * @return list of valid Assignment objects
     */
    public static List<Assignment> parse(String input, int maxSeats) {
        List<Assignment> assignments = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) return assignments;

        String[] parts = input.split(",");
        for (String part : parts) {
            String[] tokens = part.trim().split(":");
            if (tokens.length != 2) continue;

            try {
                int threadId = Integer.parseInt(tokens[0].trim());
                int seatId = Integer.parseInt(tokens[1].trim());
                if (threadId > 0 && seatId >= 0 && seatId < maxSeats) {
                    assignments.add(new Assignment(threadId, seatId));
                }
            } catch (NumberFormatException ignored) {
                // skip invalid format
            }
        }
        return assignments;
    }
}
