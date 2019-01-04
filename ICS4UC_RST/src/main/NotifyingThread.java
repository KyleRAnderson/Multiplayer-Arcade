package main;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Kyle Anderson
 */
public abstract class NotifyingThread extends Thread {
    private final Set<ThreadCompleteListener> listeners
            = new CopyOnWriteArraySet<ThreadCompleteListener>();
    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }
    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }
    public abstract void doRun();

    public static void main(String[] args) {
        NotifyingThread thread1 = new NotifyingThread() {
            @Override
            public void doRun() {
                System.out.println("Ran");
            }
        };

        thread1.addListener(thread -> {
            System.out.println("Exited thread.");
        });

        thread1.start();
    }
}
