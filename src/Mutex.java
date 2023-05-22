import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    private int owner;
    private boolean available;
    private Queue<Integer> blockedQueue;

    public Mutex() {
        owner = -1;
        available = true;
        blockedQueue = new LinkedList<Integer>();
    }

    public void semWait(int owner) {
        if (available) {
            available = false;
            this.owner = owner;
        } else {
            blockedQueue.add(owner);
            // add to scheduler blocked
        }
    }

    public void semSignal(int owner) {
        if (this.owner == owner) {
            if (!blockedQueue.isEmpty()) {
                this.owner = blockedQueue.remove();
                // remove from scheduler blocked
            } else {
                available = true;
                this.owner = -1;
            }
        }
    }
}
