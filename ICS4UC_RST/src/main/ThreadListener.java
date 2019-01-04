package main;

/**
 * ThreadListener interface. Credit to <a href="https://www.algosome.com/articles/knowing-when-threads-stop.html">Greg Cope's Article</a>
 * @author Kyle Anderson
 * ICS4U RST
 */
public interface ThreadListener {
    /**
     * Called when the thread runnable has completed its work.
     * @param runner The runner whose work has been completed.
     */
    void threadComplete(Runnable runner);
}
