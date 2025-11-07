import java.util.*;
import java.io.*;

public class Waitlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<WaitlistItem> waitlistItems;

    public Waitlist() {
        this.waitlistItems = new LinkedList<>();
    }

    public void addToWaitlist(WaitlistItem item) {
        waitlistItems.add(item);
    }

    public void removeFromWaitlist(WaitlistItem item) {
        waitlistItems.remove(item);
    }

    public Iterator<WaitlistItem> getWaitlistIterator() {
        return waitlistItems.iterator();
    }

    public boolean isEmpty() {
        return waitlistItems.isEmpty();
    }   
}   