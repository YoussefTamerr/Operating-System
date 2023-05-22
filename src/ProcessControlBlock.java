public class ProcessControlBlock {
    private int processID;
    private State processState;

    private int programCounter;

    private int lowerBound;

    private int upperBound;

    public ProcessControlBlock(int processID, int lowerBound, int upperBound, State s, int pc) {
        this.processID = processID;
        this.processState = s;
        this.programCounter = pc;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public State getProcessState() {
        return processState;
    }

    public void setProcessState(State processState) {
        this.processState = processState;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getProcessID() {
        return processID;
    }


}
