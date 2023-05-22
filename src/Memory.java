import java.util.Hashtable;

public class Memory {
    private Hashtable<String, Object> words;

    private final int maxCapacity = 40;

    public Memory() {
        words = new Hashtable<>(maxCapacity);
    }

    public Hashtable<String, Object> getWords() {
        return words;
    }

    public void setWords(Hashtable<String, Object> words) {
        this.words = words;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

}
