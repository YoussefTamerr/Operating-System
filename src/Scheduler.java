import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;


    public Scheduler() {
        readyQueue = new LinkedList<Integer>();
        blockedQueue = new LinkedList<Integer>();
    }

//    public static void SchedSemWait(String s, int pid) {
//        if(s.equals("userInput")) {
//            userInput.semWait(pid);
//        } else  if(s.equals("userOutput")) {
//            userOutput.semWait(pid);
//        } else {
//            file.semWait(pid);
//        }
//    }
//    public static void SchedSemSignal(String s, int pid) {
//        if(s.equals("userInput")) {
//            userInput.semSignal(pid);
//        } else  if(s.equals("userOutput")) {
//            userOutput.semSignal(pid);
//        } else {
//            file.semSignal(pid);
//        }
//    }

    public void schedule() {
        int[] arrivalTimes = {0, 1, 4};
        int[] burstTimes = {7, 7, 9};

        int timeQuantum = 2;
        int currentTime = 0;

        Interpreter interpreter = new Interpreter();

        ArrayList<String[]> arr1 = interpreter.readProgram("src/programs/Program_1.txt");
        ProcessControlBlock pcb1 = new ProcessControlBlock(0, 0, 14, State.READY, 0);

        ArrayList<String[]> arr2 = interpreter.readProgram("src/programs/Program_2.txt");
        ProcessControlBlock pcb2 = new ProcessControlBlock(1, 15, 29, State.READY, 0);

        ArrayList<String[]> arr3 = interpreter.readProgram("src/programs/Program_3.txt");
        ProcessControlBlock pcb3 = new ProcessControlBlock(2, 0, 14, State.READY, 0);

        while (currentTime % 2 != 0) {

            interpreter.parseInstruction(arr1.get(pcb1.getProgramCounter()), pcb1.getProcessID());
            
        }






    }


}
