import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private int currentTime;
    private int timeSlice;

    private static int toto=1;

    public Scheduler(int timeSlice) {
        readyQueue = new LinkedList<Integer>();
        blockedQueue = new LinkedList<Integer>();
        this.timeSlice = timeSlice;
        currentTime = 0;
    }



    public void schedule(ArrayList<Integer> arrivalTimes, OS os) {

        ArrayList<ArrayList<String>> programs = new ArrayList<>();
        ArrayList<String> instructions1 = os.getInterpreter().readProgram("src/programs/Program_1.txt");
        ArrayList<String> instructions2 = os.getInterpreter().readProgram("src/programs/Program_2.txt");
        ArrayList<String> instructions3 = os.getInterpreter().readProgram("src/programs/Program_3.txt");
        programs.add(instructions1);
        programs.add(instructions2);
        programs.add(instructions3);

        Memory memory = os.getMemory();
        boolean inMemory = false;
        while (true) {

            for (int i = 0; i < arrivalTimes.size(); i++) {
                int t = arrivalTimes.get(i);
                if (currentTime >= t) {
                    arrivalTimes.remove(i);
                    i--;
                    int lowerBound = 10;
                    if (memory.getWords()[10] == null) {
                        lowerBound = 10;
                    } else if (memory.getWords()[25] == null) {
                        lowerBound = 25;
                    }

                    ProcessControlBlock pcb = new ProcessControlBlock(toto, lowerBound, lowerBound + 14,
                            State.READY, 0);
                    toto++;

                    os.getProcesses().add(pcb);
                    if (memory.getWords()[0] == null) {
                        inMemory = true;
                        memory.getWords()[0] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                        memory.getWords()[1] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                        memory.getWords()[2] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                        memory.getWords()[3] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                        memory.getWords()[4] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());
                    } else if (memory.getWords()[5] == null) {
                        inMemory = true;
                        memory.getWords()[5] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                        memory.getWords()[6] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                        memory.getWords()[7] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                        memory.getWords()[8] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                        memory.getWords()[9] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());
                    } else {
                        os.writeToDisk("src/Disk.txt", memory.getWords()[0].getKey()+" "+memory.getWords()[0].getValue());
                        os.writeToDisk("src/Disk.txt", memory.getWords()[1].getKey()+" "+memory.getWords()[1].getValue());
                        os.writeToDisk("src/Disk.txt", memory.getWords()[2].getKey()+" "+memory.getWords()[2].getValue());
                        os.writeToDisk("src/Disk.txt", memory.getWords()[3].getKey()+" "+memory.getWords()[3].getValue());
                        os.writeToDisk("src/Disk.txt", memory.getWords()[4].getKey()+" "+memory.getWords()[4].getValue());

                        for (int j = 10; j < 25 ; j++) {
                            if(memory.getWords()[j]==null) {
                                break;
                            }
                            os.writeToDisk("src/Disk.txt", memory.getWords()[j].getKey() + " " + j + " "+ memory.getWords()[j].getValue().toString());
                        }

                        memory.getWords()[0] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                        memory.getWords()[1] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                        memory.getWords()[2] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                        memory.getWords()[3] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                        memory.getWords()[4] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());
                        inMemory = true;
                    }

                    if(inMemory) {
                        int b = pcb.getLowerBound();
                        ArrayList<String> instructions = programs.get(pcb.getProcessID()-1 );
                        for (String instruction : instructions) {
                            memory.getWords()[b] = new Word(""+pcb.getProcessID(), instruction);
                            b++;
                        }
                        inMemory = false;
                    }

                    if(t < currentTime) {
                        Queue<Integer> temp = new LinkedList<Integer>();
                        while(!readyQueue.isEmpty()) {
                            temp.add(readyQueue.remove());
                        }
                        readyQueue.add(pcb.getProcessID());
                        while (!temp.isEmpty()) {
                            readyQueue.add(temp.remove());
                        }
                    }
                    else {
                        readyQueue.add(pcb.getProcessID());
                    }
                }
            }

            if (readyQueue.isEmpty()) {
                break;
            } else {
                System.out.println("ready queue : ");
                System.out.println(readyQueue);
                System.out.println("blocked queue : ");
                System.out.println(blockedQueue);
                int currentProcess = readyQueue.remove();
                if ((int) memory.getWords()[0].getValue() == currentProcess) {
                    ProcessControlBlock pcb = new ProcessControlBlock((int)memory.getWords()[0].getValue(), (int)memory.getWords()[1].getValue(),(int)memory.getWords()[2].getValue()
                            ,(State) memory.getWords()[3].getValue(),(int)memory.getWords()[4].getValue());
                    os.getProcesses().remove(pcb);
                    for (int i = 0; i < timeSlice ; i++) {
                        int index = (pcb.getLowerBound()) + (pcb.getProgramCounter());
                        if(os.getMemory().getWords()[index] == null) {
                            pcb.setProcessState(State.FINISHED);
                        } else {
                            // check int then finish
                            if(os.getMemory().getWords()[index].getValue() instanceof Integer){
                                pcb.setProcessState(State.FINISHED);
                                break;
                            }
                            else {System.out.println("instruction : ");
                            System.out.println((String)os.getMemory().getWords()[index].getValue());
                            os.getInterpreter().parseInstruction((String)os.getMemory().getWords()[index].getValue(), pcb.getProcessID(), os);
                            if(blockedQueue.contains(pcb.getProcessID())) {
                                pcb.setProcessState(State.BLOCKED);
                                break;
                            } else {
                                pcb.setProcessState(State.READY);
                            }
                            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
                            currentTime++;}
                        }
                    }
                    if(blockedQueue.contains(pcb.getProcessID())) {
                        pcb.setProcessState(State.BLOCKED);
                    } else if(pcb.getProcessState().equals(State.READY)) {
                        readyQueue.add(pcb.getProcessID());
                    }
                    currentTime--;
                    memory.getWords()[3].setValue(pcb.getProcessState());
                    memory.getWords()[4].setValue(pcb.getProgramCounter());
                    os.getProcesses().add(pcb);
                } else if ((int)memory.getWords()[5].getValue() == currentProcess) {
                    ProcessControlBlock pcb = new ProcessControlBlock((int)memory.getWords()[5].getValue(), (int)memory.getWords()[6].getValue(),(int)memory.getWords()[7].getValue()
                            ,(State) memory.getWords()[8].getValue(),(int)memory.getWords()[9].getValue());

                    os.getProcesses().remove(pcb);
                    for (int i = 0; i < timeSlice ; i++) {
                        int index = (pcb.getLowerBound()) + (pcb.getProgramCounter());
                        if(os.getMemory().getWords()[index] == null) {
                            pcb.setProcessState(State.FINISHED);
                        } else {
                            // check int then finish before the only difference that the below if didnt exist
                            if(os.getMemory().getWords()[index].getValue() instanceof Integer){
                                pcb.setProcessState(State.FINISHED);
                                break;
                            }
                            else {System.out.println("instruction : ");
                            System.out.println((String)os.getMemory().getWords()[index].getValue());
//                                for (int j = 0; j < memory.getWords().length ; j++) {
//                                    if( os.getMemory().getWords()[j] == null) {
//                                        System.out.println("null");
//                                        continue;
//                                    }
//                                    System.out.println(os.getMemory().getWords()[j].getKey()+" : "+os.getMemory().getWords()[j].getValue());
//                                }
                            os.getInterpreter().parseInstruction((String)os.getMemory().getWords()[index].getValue(), pcb.getProcessID(), os);
                            if(blockedQueue.contains(pcb.getProcessID())) {
                                pcb.setProcessState(State.BLOCKED);
                                break;
                            } else {
                                pcb.setProcessState(State.READY);
                            }
                            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
                            currentTime++;}
                        }
                    }
                    if(blockedQueue.contains(pcb.getProcessID())) {
                        pcb.setProcessState(State.BLOCKED);
                    } else if(pcb.getProcessState().equals(State.READY)) {
                        readyQueue.add(pcb.getProcessID());
                    }
                    os.getProcesses().add(pcb);
                    currentTime--;
                    memory.getWords()[8].setValue(pcb.getProcessState());
                    memory.getWords()[9].setValue(pcb.getProgramCounter());
                } else {

                    os.writeToDisk("src/Disk.txt", memory.getWords()[0].getKey()+" "+memory.getWords()[0].getValue());
                    os.writeToDisk("src/Disk.txt", memory.getWords()[1].getKey()+" "+memory.getWords()[1].getValue());
                    os.writeToDisk("src/Disk.txt", memory.getWords()[2].getKey()+" "+memory.getWords()[2].getValue());
                    os.writeToDisk("src/Disk.txt", memory.getWords()[3].getKey()+" "+memory.getWords()[3].getValue());
                    os.writeToDisk("src/Disk.txt", memory.getWords()[4].getKey()+" "+memory.getWords()[4].getValue());

                    for (int j = 10; j < 25 ; j++) {
                        if(memory.getWords()[j]==null){
                            break;
                        }
                        os.writeToDisk("src/Disk.txt",  memory.getWords()[j].getKey()+" " + j + " "+memory.getWords()[j].getValue().toString());
                    }

                    ArrayList<String[]> diskContent = os.getInterpreter().readProgramRetArr("src/Disk.txt");
                    ArrayList<String> diskContent1 = os.getInterpreter().readProgram("src/Disk.txt");
                    int pid = 0;
                    int lower = 0;
                    int upper = 0;
                    int pc = 0;
                    State state = null;
                    Boolean kimo= false;
                    int memoryIndex;
                    String value = "";
                    for (int i = 0; i < diskContent.size() ; i++) {
                        if (diskContent.get(i)[0].equals("pid"+currentProcess)) {
                            pid = Integer.parseInt(diskContent.get(i)[1]);
                            diskContent.remove(i);
                            diskContent1.remove(i);
                            kimo=true;
                            i--;
                        }
                        else if (diskContent.get(i)[0].equals("lowerBound"+currentProcess)) {
                            lower = Integer.parseInt(diskContent.get(i)[1]);
                            diskContent.remove(i);
                            diskContent1.remove(i);
                            kimo=true;
                            i--;
                        }
                        else if (diskContent.get(i)[0].equals("upperBound"+currentProcess)) {
                            upper = Integer.parseInt(diskContent.get(i)[1]);
                            diskContent.remove(i);
                            diskContent1.remove(i);
                            kimo=true;
                            i--;
                        }
                        else if (diskContent.get(i)[0].equals("state"+currentProcess)) {
                           switch (diskContent.get(i)[1]) {
                               case "RUNNING" :
                                   state = State.RUNNING;
                                   diskContent.remove(i);
                                   diskContent1.remove(i);
                                   kimo=true;
                                   i--;
                                   break;
                               case "READY":
                                   state = State.READY;
                                   diskContent.remove(i);
                                   diskContent1.remove(i);
                                   kimo=true;
                                   i--;
                                   break;
                               case "FINISHED":
                                   state = State.FINISHED;
                                   diskContent.remove(i);
                                   diskContent1.remove(i);
                                   kimo=true;
                                   i--;
                                   break;
                               case "BLOCKED":
                                   state = State.BLOCKED;
                                   diskContent.remove(i);
                                   diskContent1.remove(i);
                                   kimo=true;
                                   i--;
                                   break;
                           }
                        }
                        else if (diskContent.get(i)[0].equals("pc"+currentProcess)) {
                            pc = Integer.parseInt(diskContent.get(i)[1]);
                            diskContent.remove(i);
                            diskContent1.remove(i);
                            kimo=true;
                            i--;
                        }
                        else if(diskContent.get(i)[0].length() == 2) {
                            String temp = ""+diskContent.get(i)[0].charAt(0);
                            if (temp.equals(""+currentProcess)) {
                                memoryIndex = Integer.parseInt(diskContent.get(i)[1]);
                                Word w = new Word(diskContent.get(i)[0], diskContent.get(i)[2]);
                                diskContent.remove(i);
                                diskContent1.remove(i);
                                kimo = true;
                                i--;
                                memory.getWords()[memoryIndex] = w;
                            }
                        }
                        else if(diskContent.get(i)[0].equals(""+currentProcess)) {
                            memoryIndex = Integer.parseInt(diskContent.get(i)[1]);
                            for (int k = 2; k < diskContent.get(i).length; k++) {
                                value += diskContent.get(i)[k];
                                if(k != (diskContent.get(i).length - 1)) {
                                    value += " ";
                                }
                            }
//                            System.out.println("val : "+value);
                            Word w = new Word(diskContent.get(i)[0], value);
                            diskContent.remove(i);
                            diskContent1.remove(i);
                            kimo = true;
                            i--;
                            memory.getWords()[memoryIndex] = w;
                            value = "";
                        }

//                        for (int j = lower; j < upper  ; j++) {
//                            //String temp = ""+diskContent.get(i)[0].charAt(0);
////                            if (temp.equals(""+currentProcess)) {
////                                Word w = new Word(diskContent.get(i)[0], diskContent.get(i)[1]);
////                                diskContent.remove(i);
////                                diskContent1.remove(i); //el ghalta enk mesh bt7otha fy memory
////                            }
//                            if(diskContent.get(i)[0].equals(""+currentProcess)) {
//                               //Word w = new Word(diskContent.get(i)[0]+" "+diskContent.get(i)[1], diskContent.get(i)[2]); elly kimo katabo
//                                Word w = new Word(diskContent.get(i)[0], diskContent.get(i)[1]); //we need to handle if its an instruction
//                                if(diskContent.get(i).length > 2 ) { //if bigger than 2 then its an instruction
////                                    String value = "";
//                                    for (int k = 1; k < diskContent.get(i).length; k++) {
//                                        value += diskContent.get(i)[k];
//                                        if(k != (diskContent.get(i).length - 1)) {
//                                            value += " ";
//                                        }
//                                    } //variable written as 1a as a key and 3 as value left
//                                    w.setValue(value);
//                                }
//                                System.out.println(diskContent.get(i)[0]);
//                                diskContent.remove(i);
//                                diskContent1.remove(i);
//// ABOUZ
//                                memory.getWords()[j] = w;
//                            }
//                      }
//                      if(kimo){
//                          kimo = false;
//                          i--;
//                      }
                    }
                    File f = new File("src/Disk.txt");
                    f.delete();
                    System.gc();
                   // os.emptyDisk();
                    for (int i = 0; i < diskContent.size() ; i++) {
                        os.writeToDisk("src/Disk.txt", diskContent1.get(i));
                    }
                    ProcessControlBlock pcb = new ProcessControlBlock(pid, lower, upper, state, pc);
                    os.getProcesses().remove(pcb);


                    memory.getWords()[0] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                    memory.getWords()[1] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                    memory.getWords()[2] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                    memory.getWords()[3] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                    memory.getWords()[4] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());



                    //hna nkamel law mal2tsh el process fy memory kda gebnaha men disk 3ayzeen n7otha fy memory w 3ayzeen ngeeb el instructions men disk Lsa matgaboosh

                    for (int i = 0; i < timeSlice ; i++) {
                        int index = (pcb.getLowerBound()) + (pcb.getProgramCounter());
                        if(os.getMemory().getWords()[index] == null) {
                            pcb.setProcessState(State.FINISHED);
                        } else {

                            if(os.getMemory().getWords()[index].getValue() instanceof Integer){
                                pcb.setProcessState(State.FINISHED);
                                break;
                            }
                           else{
                                System.out.println("instruction : ");
                                System.out.println((String) os.getMemory().getWords()[index].getValue());
//                                for (int j = 0; j < memory.getWords().length ; j++) {
//                                    if( os.getMemory().getWords()[j] == null) {
//                                        System.out.println("null");
//                                        continue;
//                                    }
//                                    System.out.println(os.getMemory().getWords()[j].getKey()+" : "+os.getMemory().getWords()[j].getValue());
//                                }
                               os.getInterpreter().parseInstruction((String)os.getMemory().getWords()[index].getValue(), pcb.getProcessID(), os);
                                if(blockedQueue.contains(pcb.getProcessID())) {
                                    pcb.setProcessState(State.BLOCKED);
                                    break;
                                } else {
                                    pcb.setProcessState(State.READY);
                                }
                            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
                            currentTime++;}
                        }
                    }
                    if(blockedQueue.contains(pcb.getProcessID())) {
                        pcb.setProcessState(State.BLOCKED);
                    } else if(pcb.getProcessState().equals(State.READY)) {
                        readyQueue.add(pcb.getProcessID());
                    }
                    os.getProcesses().add(pcb);
                    currentTime--;
                    memory.getWords()[3].setValue(pcb.getProcessState());
                    memory.getWords()[4].setValue(pcb.getProgramCounter());

                }
            }
            currentTime++;
        }
    }


    public Queue<Integer> getReadyQueue() {
        return readyQueue;
    }

    public void setReadyQueue(Queue<Integer> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public Queue<Integer> getBlockedQueue() {
        return blockedQueue;
    }

    public void setBlockedQueue(Queue<Integer> blockedQueue) {
        this.blockedQueue = blockedQueue;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }
}
