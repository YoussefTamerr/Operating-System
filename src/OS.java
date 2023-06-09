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

    public void writeToFile(String filename, String txt, int pid) {

        ProcessControlBlock pcb = null;
        for (int i = 0; i < processes.size() ; i++) {
            if(pid == processes.get(i).getProcessID()) {
                pcb = processes.get(i);
            }
        }

        String i1 = "";
        String i2 = "";
        for(int i=pcb.getLowerBound();i<pcb.getUpperBound();i++){
            if(memory.getWords()[i] == null) {
                break;
            }

            if(memory.getWords()[i].getKey().equals(""+pid+""+filename)){
                i1= memory.getWords()[i].getValue().toString();
            }
            if(memory.getWords()[i].getKey().equals(""+pid+""+txt)){
                i2= memory.getWords()[i].getValue().toString();
            }
        }

        try {
            FileWriter writer = new FileWriter(i1, true);
            writer.write(i2+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> ReadFromFile(String filename, int pid) {

        ProcessControlBlock pcb = null;
        for (int i = 0; i < processes.size() ; i++) {
            if(pid == processes.get(i).getProcessID()) {
                pcb = processes.get(i);
            }
        }

        String i1 = "";
        for(int i=pcb.getLowerBound();i<pcb.getUpperBound();i++){
            if(memory.getWords()[i] == null) {
                break;
            }

            if(memory.getWords()[i].getKey().equals(""+pid+""+filename)){
                i1= memory.getWords()[i].getValue().toString();
            }
        }

        ArrayList<String> res = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(i1);
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

    public void printOutput(String x, int pid) {
        ProcessControlBlock pcb = null;
        for (int i = 0; i < processes.size() ; i++) {
            if(pid == processes.get(i).getProcessID()) {
                pcb = processes.get(i);
            }
        }

        String i1 = "";
        for(int i=pcb.getLowerBound();i<pcb.getUpperBound();i++){
            if(memory.getWords()[i] == null) {
                break;
            }

            if(memory.getWords()[i].getKey().equals(""+pid+""+x)){
                i1= memory.getWords()[i].getValue().toString();
            }

        }
        System.out.println(i1);
    }

    public void printFromTo(String s, String s1,int pid) {
        ProcessControlBlock pcb = null;
        for (int i = 0; i < processes.size() ; i++) {
            if(pid == processes.get(i).getProcessID()) {
                pcb = processes.get(i);
            }
        }

        int i1 = 0;
        int i2 = 0;
        //for(int i=0;i<memory.getWords().length;i++){
        for(int i=pcb.getLowerBound();i<pcb.getUpperBound();i++){
            if(memory.getWords()[i] == null) {
                break;
            }

            if(memory.getWords()[i].getKey().equals(""+pid+""+s)){
               i1= Integer.parseInt(memory.getWords()[i].getValue().toString());
            }
            if(memory.getWords()[i].getKey().equals(""+pid+""+s1)){
                i2= Integer.parseInt(memory.getWords()[i].getValue().toString());
            }
        }

        if(i1 == 0 && i2 == 0) {
            System.out.println("fy 7aga ghalat");
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
        Integer i;
        try {
            i = Integer.parseInt(y);
        } catch (NumberFormatException e) {
                for (int j = pcb.getLowerBound(); j < pcb.getUpperBound(); j++) {
                    if (os.memory.getWords()[j] == null) {
                        Word w = new Word("" + pid +""+x, y);

                        os.memory.getWords()[j] = w;
                        break;
                    }
                    if (os.memory.getWords()[j].getKey().toString().equals(""+pid+""+x)) {
                        os.memory.getWords()[j].setValue(y);
                        break;
                    }

                }
            return;
        }

        for (int j = pcb.getLowerBound(); j < pcb.getUpperBound(); j++) {
            if (os.memory.getWords()[j] == null) {
                Word w = new Word("" + pid +""+x, y);
                os.memory.getWords()[j] = w;
                break;
            }
            if (os.memory.getWords()[j].getKey().equals("" + pid +""+x)) {
                os.memory.getWords()[j].setValue(y);
                break;
            }

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
        OS os = new OS(2);
        ArrayList<Integer> x = new ArrayList<>();
        x.add(0);
        x.add(1);
        x.add(4);

        ArrayList<String> instructions1 = os.getInterpreter().readProgram("src/programs/Program_1.txt");
        ArrayList<String> instructions2 = os.getInterpreter().readProgram("src/programs/Program_2.txt");
        ArrayList<String> instructions3 = os.getInterpreter().readProgram("src/programs/Program_3.txt");

        ArrayList<ArrayList<String>> programsOrder = new ArrayList<ArrayList<String>>();
        programsOrder.add(instructions1);
        programsOrder.add(instructions2);
        programsOrder.add(instructions3);

        os.getScheduler().schedule(x, os, programsOrder);
    }

}
