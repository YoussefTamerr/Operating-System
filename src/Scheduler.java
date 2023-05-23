import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private int currentTime;
    private int timeSlice;

    public Scheduler(int timeSlice) {
        readyQueue = new LinkedList<Integer>();
        blockedQueue = new LinkedList<Integer>();
        this.timeSlice = timeSlice;
        currentTime = 0;
    }



    public void schedule(int timeQuantum, ArrayList<Integer> arrivalTimes, OS os) {

        ArrayList<ArrayList<String[]>> programs = new ArrayList<>();
        ArrayList<String[]> instructions1 = os.getInterpreter().readProgram("src/programs/Program_1.txt");
        ArrayList<String[]> instructions2 = os.getInterpreter().readProgram("src/programs/Program_2.txt");
        ArrayList<String[]> instructions3 = os.getInterpreter().readProgram("src/programs/Program_3.txt");
        programs.add(instructions1);
        programs.add(instructions2);
        programs.add(instructions3);

        Memory memory = os.getMemory();

        while (true) {
            for (int i = 0; i < arrivalTimes.size(); i++) {
                int t = arrivalTimes.get(i);
                if (currentTime >= t) {
                    arrivalTimes.remove(arrivalTimes.get(i));
                    int lowerBound = 10;
                    if (memory.getWords()[10] == null) {
                        lowerBound = 10;
                    } else if (memory.getWords()[25] == null) {
                        lowerBound = 25;
                    }

                    ProcessControlBlock pcb = new ProcessControlBlock(i+1, lowerBound, lowerBound + 14,
                            State.READY, 0);

                    if (memory.getWords()[0] == null) {
                        memory.getWords()[0] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                        memory.getWords()[1] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                        memory.getWords()[2] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                        memory.getWords()[3] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                        memory.getWords()[4] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());
                    } else if (memory.getWords()[5] == null) {
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
                            os.writeToDisk("src/Disk.txt",  memory.getWords()[j].getKey()+" "+memory.getWords()[j].getValue().toString());
                        }

                        memory.getWords()[0] = new Word("pid" + pcb.getProcessID()  , pcb.getProcessID());
                        memory.getWords()[1] = new Word("lowerBound" + pcb.getProcessID(), pcb.getLowerBound());
                        memory.getWords()[2] = new Word("upperBound" + pcb.getProcessID(), pcb.getUpperBound());
                        memory.getWords()[3] = new Word("state" + pcb.getProcessID(), pcb.getProcessState());
                        memory.getWords()[4] = new Word("pc" + pcb.getProcessID(), pcb.getProgramCounter());
                    }

                    int b = pcb.getLowerBound();
                    ArrayList<String[]> instructions = programs.get(pcb.getProcessID() - 1);
                    for (String[] instruction : instructions) {
                        memory.getWords()[b] = new Word(pcb.getProcessID() + "" + b, instruction);
                        b++;
                    }

                    readyQueue.add(pcb.getProcessID());
                }
            }
            boolean f = false;
            if (readyQueue.isEmpty()) {
                break;
            } else {
                int currentProcess = readyQueue.remove();
                if ((int) memory.getWords()[0].getValue() == currentProcess) {
                    ProcessControlBlock pcb = new ProcessControlBlock((int)memory.getWords()[0].getValue(), (int)memory.getWords()[1].getValue(),(int)memory.getWords()[2].getValue()
                            ,(State) memory.getWords()[3].getValue(),(int)memory.getWords()[4].getValue());

                    for (int i = 0; i < timeSlice ; i++) {
                        int index = (pcb.getLowerBound()) + (pcb.getProgramCounter());
                        if(os.getMemory().getWords()[index].getValue() == null) {
                            pcb.setProcessState(State.FINISHED);
                        } else {
                            os.getInterpreter().parseInstruction((String[])os.getMemory().getWords()[index].getValue(), pcb.getProcessID(), os);
                            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
                            currentTime++;
                            f = true;
                        }
                    }
                    memory.getWords()[3].setValue(pcb.getProcessState());
                    memory.getWords()[4].setValue(pcb.getProgramCounter());
                } else if ((int)memory.getWords()[5].getValue() == currentProcess) {
                    ProcessControlBlock pcb = new ProcessControlBlock((int)memory.getWords()[5].getValue(), (int)memory.getWords()[6].getValue(),(int)memory.getWords()[7].getValue()
                            ,(State) memory.getWords()[8].getValue(),(int)memory.getWords()[9].getValue());


                    for (int i = 0; i < timeSlice ; i++) {
                        int index = (pcb.getLowerBound()) + (pcb.getProgramCounter());
                        if(os.getMemory().getWords()[index].getValue() == null) {
                            pcb.setProcessState(State.FINISHED);
                        } else {
                            os.getInterpreter().parseInstruction((String[])os.getMemory().getWords()[index].getValue(), pcb.getProcessID(), os);
                            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
                            currentTime++;
                            f = true;
                        }
                    }
                    memory.getWords()[8].setValue(pcb.getProcessState());
                    memory.getWords()[9].setValue(pcb.getProgramCounter());
                } else {
                    ArrayList<String[]> diskContent = os.getInterpreter().readProgram("src/Disk.txt");
                    int pid = 0;
                    int lower = 0;
                    int upper = 0;
                    int pc = 0;
                    State state = null;
                    for (int i = 0; i < diskContent.size() ; i++) {
                        if (diskContent.get(i)[0].equals("pid"+currentProcess)) {
                            pid = Integer.parseInt(diskContent.get(i)[1]);
                        }
                        if (diskContent.get(i)[0].equals("lowerBound"+currentProcess)) {
                            lower = Integer.parseInt(diskContent.get(i)[1]);
                        }
                        if (diskContent.get(i)[0].equals("upperBound"+currentProcess)) {
                            upper = Integer.parseInt(diskContent.get(i)[1]);
                        }
                        if (diskContent.get(i)[0].equals("state"+currentProcess)) {
                           switch (diskContent.get(i)[1]) {
                               case "RUNNING" :
                                   state = State.RUNNING;
                                   break;
                               case "READY":
                                   state = State.READY;
                                   break;
                               case "FINISHED":
                                   state = State.FINISHED;
                                   break;
                                case "BLOCKED":
                                    state = State.BLOCKED;
                                   break;
                           }
                        }
                        if (diskContent.get(i)[0].equals("pc"+currentProcess)) {
                            pc = Integer.parseInt(diskContent.get(i)[1]);
                        }
                    }
                    ProcessControlBlock pcb = new ProcessControlBlock(pid, lower, upper, state, pc);

                    //hna nkamel law mal2tsh el process fy memory kda gebnaha men disk 3ayzeen n7otha fy memory w 3ayzeen ngeeb el instructions men memory Lsa matgaboosh



                }
                if (f) {
                    currentTime--;
                    f = false;
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
