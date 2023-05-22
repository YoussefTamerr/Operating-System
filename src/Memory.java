import java.util.Hashtable;

public class Memory {
    private Hashtable<String, Object> words;

    private final int maxCapacity = 40;

    public Memory() {
        words = new Hashtable<>(maxCapacity);
    }


}
