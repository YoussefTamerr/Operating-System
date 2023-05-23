import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OS {

    private Memory memory;
    private File disk;
    private Mutex userInput;
    private Mutex userOutput;
    private Mutex file;

    public OS() {
        memory = new Memory();
        disk = new File("src/disk.txt");
        userOutput = new Mutex();
        userInput = new Mutex();
        file = new Mutex();
    }

    public void SchedSemWait(String s, int pid) {
        if(s.equals("userInput")) {
            userInput.semWait(pid);
        } else  if(s.equals("userOutput")) {
            userOutput.semWait(pid);
        } else {
            file.semWait(pid);
        }
    }
    public void SchedSemSignal(String s, int pid) {
        if(s.equals("userInput")) {
            userInput.semSignal(pid);
        } else  if(s.equals("userOutput")) {
            userOutput.semSignal(pid);
        } else {
            file.semSignal(pid);
        }
    }


    public void writeToDisk(String s, String txt) {
        try {
            FileWriter writer = new FileWriter(s);
            writer.write(txt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> ReadFromFile(String filename) {
        ArrayList<String> res = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void printOutput(String x) {

        System.out.println(x);
    }

    public void printFromTo(String s, String s1) {
    }

    public String takeInput() {
        System.out.println("Please enter a value");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public Object readFromMemory(String s) {
        return this.memory.getWords().getVal(s);
    }

    public void writeToMemory(String s, Object o) {
        Word w = new Word(s, o);
        if(memory.getWords().size() <= 40) {
            this.memory.getWords().add(w);
        }
    }

    public void assignVariable(String x, String y) {
        if (y.equals("input")) {
            y = takeInput();
        }
        try {
            int i = Integer.parseInt(y);
        } catch (NumberFormatException e) {
            // zizo
        }
    }

    public static void main(String[] args) {

    }

}
