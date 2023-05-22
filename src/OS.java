import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OS {

    private Memory memory;

    private File disk;

    public OS() {
        memory = new Memory();
        disk = new File("src/disk.txt");
    }




    public static void writeToDisk(String s, String txt) {
        try {
            FileWriter writer = new FileWriter(s);
            writer.write(txt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> ReadFromFile(String filename) {
        ArrayList<String> res = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void printOutput(String x) {
        System.out.println(x);
    }

    public static void printFromTo(String s, String s1) {
    }

    public static String takeInput() {
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

    public static void assignVariable(String x, String y) {
        if (y.equals("input")) {
            y = takeInput();
        }
        try {
            int i = Integer.parseInt(y);
        } catch (NumberFormatException e) {
            // zizo
        }
    }

    public static void main(String[] args) {

    }

}
