import java.util.Scanner;

public class OS {

    private Memory memory;

    public OS() {
        memory = new Memory();
    }

//    public Object readData(String file) {
//
//    }

    public void printOutput(String x) {
        System.out.println(x);
    }

    public String takeInput() {
        System.out.println("Please enter a value");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public Object readFromMemory(String s) {
        return this.memory.getWords().get(s);
    }

    public void writeToMemory(String s, Object o) {
        this.memory.getWords().put(s, o);
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

}
