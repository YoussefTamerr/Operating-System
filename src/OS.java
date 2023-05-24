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

    private ArrayList<ProcessControlBlock> processes;

    public OS(int timeSlice) {
        memory = new Memory();
        disk = new File("src/Disk.txt");
        userOutput = new Mutex();
        userInput = new Mutex();
        file = new Mutex();
        interpreter = new Interpreter();
        scheduler = new Scheduler(timeSlice);
        processes = new ArrayList<ProcessControlBlock>();
    }

    public ArrayList<ProcessControlBlock> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<ProcessControlBlock> processes) {
        this.processes = processes;
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
            FileWriter writer = new FileWriter(filename, true);
            writer.write(txt+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emptyDisk() {
        try {
            // Open the file in write mode
            File file = new File("src/Disk.txt");
            FileWriter writer = new FileWriter(file);

            // Close the file to empty its contents
            writer.close();

            System.out.println("File emptied successfully.");
        } catch(IOException e) {
            System.out.println("An error occurred.");
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

    public void printFromTo(String s, String s1,int pid) {
        //hena el mafrood ygeeb el value mn el memory el bt correspond l lel string s w s1
        int i1 = 0;
        int i2 = 0;
        for(int i=0;i<memory.getWords().length;i++){
            if(memory.getWords()[i].getKey().equals(pid+" "+s)){
               i1=(int) memory.getWords()[i].getValue();
            }
            if(memory.getWords()[i].getKey().equals(pid+" "+s1)){
                i2=(int) memory.getWords()[i].getValue();
            }
        }

        for (int i = i1; i <= i2 ; i++) {
            System.out.println(i);
        }
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

    public void assignVariable(String x, String y, OS os, int pid) {

        ProcessControlBlock pcb = null;
        for (int i = 0; i < processes.size() ; i++) {
            if(pid == processes.get(i).getProcessID()) {
                pcb = processes.get(i);
            }
        }

        if (y.equals("input")) {
            y = takeInput();
        }
        int i = 0;
        try {
            i = Integer.parseInt(y);
        } catch (NumberFormatException e) {

            //if((int)os.memory.getWords()[0].getValue()  == pid) {
                for (int j = pcb.getLowerBound(); j < pcb.getUpperBound(); j++) {
                    if (os.memory.getWords()[j] == null) {
                        Word w = new Word("" + pid +" "+x, y);
                        os.memory.getWords()[j] = w;
                        break;
                    }
                    if (os.memory.getWords()[j].getKey().toString().equals(x)) {
                        os.memory.getWords()[j].setValue(y);
                        break;
                    }

                }
//            } else if((int)os.memory.getWords()[5].getValue()  == pid) {
//                for (int j = 25; j < 40 ; j++) {
//                    if (os.memory.getWords()[j].getKey().toString().equals(x)) {
//                        os.memory.getWords()[j].setValue(y);
//                        break;
//                    }
//                    if(os.memory.getWords()[j].getKey() == null) {
//                        Word w = new Word(x, y);
//                        os.memory.getWords()[j] = w;
//                        break;
//                    }
//                }
//            }
            return;
        }

        for (int j = pcb.getLowerBound(); j < pcb.getUpperBound(); j++) {
            if (os.memory.getWords()[j] == null) {
                Word w = new Word(x, y);
                os.memory.getWords()[j] = w;
                break;
            }
            if (os.memory.getWords()[j].getKey().equals(x)) {
                os.memory.getWords()[j].setValue(y);
                break;
            }

        }

//        if((int)os.memory.getWords()[0].getValue()  == pid) {
//            for (int j = 10; j < 25 ; j++) {
//                if (os.memory.getWords()[j].getKey().toString().equals(x)) {
//                    os.memory.getWords()[j].setValue(i);
//                    break;
//                }
//                if(os.memory.getWords()[j].getKey() == null) {
//                    Word w = new Word(x, i);
//                    os.memory.getWords()[j] = w;
//                    break;
//                }
//            }
//        } else if((int)os.memory.getWords()[5].getValue()  == pid) {
//            for (int j = 25; j < 40 ; j++) {
//                if (os.memory.getWords()[j].getKey().toString().equals(x)) {
//                    os.memory.getWords()[j].setValue(i);
//                    break;
//                }
//                if(os.memory.getWords()[j].getKey() == null) {
//                    Word w = new Word(x, i);
//                    os.memory.getWords()[j] = w;
//                    break;
//                }
//            }
//        }
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
        OS os = new OS(2);
        ArrayList<Integer> x = new ArrayList<>();
        x.add(0);
        x.add(2);
        x.add(4);
        os.getScheduler().schedule(x, os);
    }

}
