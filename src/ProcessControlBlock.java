public class ProcessControlBlock {
    private int processID;
    private State processState;

    private int programCounter;

    private int lowerBound;

    private int upperBound;

    public ProcessControlBlock(int processID, int lowerBound, int upperBound) {
        this.processID = processID;
        processState = State.READY;
        programCounter = 0;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

    }


}
