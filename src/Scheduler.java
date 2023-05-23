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

    public void schedule(int timeQuantum, int[] arrivalTimes, OS os) {

        ArrayList<ArrayList<String[]>> programs = new ArrayList<>();
        ArrayList<String[]> instructions1 = os.getInterpreter().readProgram("src/programs/Program_1.txt");
        ArrayList<String[]> instructions2 = os.getInterpreter().readProgram("src/programs/Program_2.txt");
        ArrayList<String[]> instructions3 = os.getInterpreter().readProgram("src/programs/Program_3.txt");
        programs.add(instructions1);
        programs.add(instructions2);
        programs.add(instructions3);

        Memory memory = os.getMemory();

        while (true) {
            for (int i = 0; i < arrivalTimes.length; i++) {
                int t = arrivalTimes[i];
                if (currentTime == t) {
                    int lowerBound = 10;
                    if (memory.getWords()[10] == null) {
                        lowerBound = 10;
                    } else if (memory.getWords()[25] == null) {
                        lowerBound = 25;
                    } else {
                        // not enough space
                    }

                    ProcessControlBlock pcb = new ProcessControlBlock(i+1, lowerBound, lowerBound + 14,
                            State.READY, 0);

                    if (memory.getWords()[0] == null) {
                        // skull emoji 💀 skull emoji 💀 skull emoji 💀
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
                        // not enough space
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

            if (readyQueue.isEmpty()) {
                break;
            } else {
                int currentProcess = readyQueue.remove();
                if ((int) memory.getWords()[0].getValue() == currentProcess) {

//                    os.getInterpreter().parseInstruction(os.getMemory().getWords()[]);
                } else if ((int)memory.getWords()[5].getValue() == currentProcess) {

                }
            }



            currentTime++;
        }




//        while () {
//
//            interpreter.parseInstruction(arr1.get(pcb1.getProgramCounter()), pcb1.getProcessID());
//
//        }






    }


}
