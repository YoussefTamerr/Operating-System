import java.util.Scanner;

public class OS {
    public void printOutput(String x) {
        System.out.println(x);
    }

    public void assignVariable(String x, String y) {
        if (y.equals("input")) {
            System.out.println("Please enter a value");
            Scanner sc = new Scanner(System.in);
            y = sc.nextLine();
        }
        try {
            int i = Integer.parseInt(y);

        } catch (NumberFormatException e) {
            // zizo
        }

    }

}
