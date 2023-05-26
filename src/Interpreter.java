import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Interpreter {
    public Interpreter(){

    }
    public ArrayList<String> readProgram(String filePath) {
        ArrayList<String> output = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                output.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return output;
    }

    public ArrayList<String[]> readProgramRetArr(String filePath) {
        ArrayList<String[]> output = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] instruction = data.split(" ");
                output.add(instruction);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return output;
    }

    public void parseInstruction(String in, int pid, OS os) {
        String[] instruction = in.split(" ");
        switch (instruction[0]) {
            case "print":
                os.printOutput(instruction[1], pid);
                break;
            case "assign":
                if (instruction.length == 4 && instruction[2].equals("readFile")) {
                    ArrayList<String> temp = os.ReadFromFile(instruction[3], pid);
                    String t2="";
                    for (int i = 0; i < temp.size() ; i++) {
                        t2 += temp.get(i);
                    }
                    os.assignVariable(instruction[1], t2, os, pid);
                } else {
                    if (instruction.length > 3) {
                        String v="";
                        for (int i = 2; i < instruction.length ; i++) {
                            v+= instruction[i];
                            if(i != (instruction.length-1)) {
                                v+=" ";
                            }
                        }
                        os.assignVariable(instruction[1], v, os, pid);
                    } else {
                        os.assignVariable(instruction[1], instruction[2], os, pid);
                    }
                }
                break;
            case "writeFile":
                os.writeToFile(instruction[1], instruction[2], pid);
                break;
            case "readFile":
                os.ReadFromFile(instruction[1], pid);
                break;
            case "printFromTo":
                os.printFromTo(instruction[1], instruction[2],pid);
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