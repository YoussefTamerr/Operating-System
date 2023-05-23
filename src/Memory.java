import java.util.ArrayList;
import java.util.Hashtable;

public class Memory {
    private Word[] words;


    public Memory() {
        words = new Word[40];
    }

    public Word[] getWords() {
        return words;
    }

    public void setWords(Word[] words) {
        this.words = words;
    }

    public Word getWord(String s) {
        for (Word word : words) {
            if(word.getKey().equals(s)) {
                return word;
            }
        }
        return null;
    }

}
