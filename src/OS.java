import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OS {
    private Memory memory;
    private File disk;
    private Mutex userInput;
    private Mutex userOutput;
    private Mutex file;
    private Interpreter interpreter;
    private Scheduler scheduler;

    public OS(int timeSlice) {
        memory = new Memory();
        disk = new File("src/Disk.txt");
        userOutput = new Mutex();
        userInput = new Mutex();
        file = new Mutex();
        interpreter = new Interpreter();
        scheduler = new Scheduler(timeSlice);
    }

    public void SchedSemWait(String s, int pid) {
        if(s.equals("userInput")) {
            userInput.semWait(pid, scheduler);
        } else  if(s.equals("userOutput")) {
            userOutput.semWait(pid, scheduler);
        } else {
            file.semWait(pid, scheduler);
        }
    }
    public void SchedSemSignal(String s, int pid) {
        if(s.equals("userInput")) {
            userInput.semSignal(pid, scheduler);
        } else  if(s.equals("userOutput")) {
            userOutput.semSignal(pid, scheduler);
        } else {
            file.semSignal(pid, scheduler);
        }
    }


    public void writeToDisk(String filename, String txt) {
        try {
            FileWriter writer = new FileWriter(filename);
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
        return this.memory.getWord(s).getValue();
    }

    public void writeToMemory(String s, Object o) {
//        Word w = new Word(s, o);
//        this.memory.getWords().add(w);
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

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public File getDisk() {
        return disk;
    }

    public void setDisk(File disk) {
        this.disk = disk;
    }

    public Mutex getUserInput() {
        return userInput;
    }

    public void setUserInput(Mutex userInput) {
        this.userInput = userInput;
    }

    public Mutex getUserOutput() {
        return userOutput;
    }

    public void setUserOutput(Mutex userOutput) {
        this.userOutput = userOutput;
    }

    public Mutex getFile() {
        return file;
    }

    public void setFile(Mutex file) {
        this.file = file;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static void main(String[] args) {

    }

}
