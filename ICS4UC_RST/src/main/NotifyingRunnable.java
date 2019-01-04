package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This abstract class implements the Runnable interface and can be used to notify listeners
 * <p>
 * when the runnable thread has completed. To use this class, first extend it and implement
 * <p>
 * the doRun function - the doRun function is where all work should be performed. Add any listener to update upon completion, then
 * <p>
 * create a new thread with this new object and run.
 *
 * @author Greg Cope with modifications made by Kyle Anderson
 */
public abstract class NotifyingRunnable implements Runnable {
    /**
     * An abstract function that children must implement. This function is where
     * <p>
     * all work - typically placed in the run of runnable - should be placed.
     */
    public abstract void doWork();


    /**
     * List of listeners to be notified upon thread completion.
     */
    private final List<ThreadListener> listeners = Collections.synchronizedList(new ArrayList<>());


    /**
     * Adds a listener to this object.
     *
     * @param listener Adds a new listener to this object.
     */
    public void addListener(ThreadListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a particular listener from this object, or does nothing if the listener
     * <p>
     * is not registered.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(ThreadListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that the thread has completed.
     */
    private void notifyListeners() {
        synchronized (listeners) {
            for (ThreadListener listener : listeners) {
                listener.threadComplete(this);
            }
        }
    }

    /**
     * Implementation of the Runnable interface. This function first calls doRun(), then
     * <p>
     * notifies all listeners of completion.
     */
    public final void run() {
        doWork();
        notifyListeners();
    }
}