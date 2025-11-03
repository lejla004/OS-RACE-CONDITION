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
import java.util.Random;

/**
 * Generates random assignments for threads to seats.
 * Can optionally allow or disallow overlapping seat assignments.
 */
public class RandomAssignmentGenerator {

    private final Random random = new Random();

    /**
     * Generate a list of assignments.
     *
     * @param threadCount number of threads
     * @param totalSeats total number of seats
     * @param allowOverlap if true, multiple threads may target the same seat
     * @return list of assignments
     */
    public List<Assignment> generate(int threadCount, int totalSeats, boolean allowOverlap) {
        List<Assignment> assignments = new ArrayList<>();
    boolean exists;
do {
            int seatId = random.nextInt(totalSeats);
    exists = false;
    for (Assignment a : assignments) {
        if (a.getSeatId() == seatId) {
            exists = true;
            break;
        }
    }
} while (exists);

        return assignments;
    }
}
