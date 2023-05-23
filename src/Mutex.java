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

    public void semWait(int owner, Scheduler sch) {
        if (available) {
            available = false;
            this.owner = owner;
        } else {
            blockedQueue.add(owner);
            sch.getBlockedQueue().add(owner);
        }
    }

    public void semSignal(int owner, Scheduler sch) {
        if (this.owner == owner) {
            if (!blockedQueue.isEmpty()) {
                this.owner = blockedQueue.remove();
                sch.getBlockedQueue().remove(owner);
                sch.getReadyQueue().add(owner);
            } else {
                available = true;
                this.owner = -1;
            }
        }
    }
}
