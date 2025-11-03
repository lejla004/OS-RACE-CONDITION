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
 * Provides a thread-safe pause/step flag for agents to check.
 */


public class PauseController {
    private boolean paused = false;
    private final Object lock = new Object();

    public void pause() {
        synchronized (lock) {
            paused = true;
        }
    }

    public void step() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll(); // allow threads to continue
        }
    }

    public void waitIfPaused() throws InterruptedException {
        synchronized (lock) {
            while (paused) {
                lock.wait();
            }
        }
    }
}
