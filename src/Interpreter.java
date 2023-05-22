import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {
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

    public void parseInstruction(String[] instruction) {
        // Convert the array to a list
        List<String> list = Arrays.asList(instruction);
        Collections.reverse(list);
        instruction = list.toArray(new String[list.size()]);




     }

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
//        switch (instruction[i]) {
//            case "print":
//                printOutput(instruction);
//                break;
//            case "assign":
//                assignVariable(instruction[1], instruction[2]);
//                break;
//            case "writeFile":
//                writeFile(instruction[1], instruction[2]);
//                break;
//            case "readFile":
//                readFile(instruction[1]);
//                break;
//            case "printFromTo":
//                printFromTo(instruction[1], instruction[2]);
//                break;
//            case "semWait":
//                semWait(instruction[1]);
//                break;
//            case "semSignal":
//                semSignal(instruction[1]);
//                break;
//            case   :
//
//        }
//        }
//    }
}
