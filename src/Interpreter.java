import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {

    OS os;

    public Interpreter(){
        os = new OS();
    }
    public ArrayList<String[]> readProgram(String filePath) {
        ArrayList<String[]> output = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] terms = data.split(" ");
                output.add(terms);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return output;
    }

    public void parseInstruction(String[] instruction, int pid) {
        switch (instruction[0]) {
            case "print":
                os.printOutput(instruction[1]);
                break;
            case "assign":
                if (instruction.length == 4) {
                    ArrayList<String> temp = OS.ReadFromFile(instruction[3]);
                    os.assignVariable(instruction[1], temp.get(0));
                } else {
                    os.assignVariable(instruction[1], instruction[2]);
                }
                break;
            case "writeFile":
                os.writeToDisk(instruction[1], instruction[2]);
                break;
            case "readFile":
                os.ReadFromFile(instruction[1]);
                break;
            case "printFromTo":
                os.printFromTo(instruction[1], instruction[2]);
                break;
            case "semWait":
                os.SchedSemWait(instruction[1], pid);
                break;
            case "semSignal":
                os.SchedSemSignal(instruction[1], pid);
                break;
        }
    }
}


// Convert the array to a list
//        List<String> list = Arrays.asList(instruction);
//        Collections.reverse(list);
//        instruction = list.toArray(new String[list.size()]);


//    public void parseInstruction(String[] instruction) {
//        String[] rev = new String[instruction.length];
//        for (int i = 0; i < instruction.length; i++) {
//            rev[i]=instruction[instruction.length-i];
//        }
//        for (int i = 0; i < rev.length; i++) {
//            switch (rev[i]) {
//                case rev[0]:
//                try {
//                   int firstint = Integer.parseInt(rev[0]);
//
//                } catch (NumberFormatException e) {
//                    // zizo
//                }
//                case rev[1]:
//                    try {
//                        int secondint = Integer.parseInt(rev[1]);
//
//                    } catch (NumberFormatException e) {
//                        // zizo
//                    }
//            }
//
//