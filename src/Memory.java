import java.util.ArrayList;
import java.util.Hashtable;

public class Memory {
    private ArrayList<Word> words;

    private final int maxCapacity = 40;

    public Memory() {
        words = new ArrayList<Word>(maxCapacity);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Word getWord(String s) {
        Word w = null;
        for (int i = 0; i < words.size() ; i++) {
            if(words.get(i).getKey().equals(s)) {
                w = getWord(s);
            }
        }
        return w;
    }

}
