import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Handler {

    private static File levels;
    private static FileWriter fileWriter;
    private static Scanner fileReader;
    private static int currentMax;
    public static void init() throws IOException {
        levels = new File("./src/levels.txt");

        fileReader = new Scanner(levels);
        currentMax = Integer.parseInt(fileReader.nextLine());
        //System.out.println(currentMax);
        fileWriter = new FileWriter(levels);
        fileWriter.write(currentMax+"");
        fileWriter.close();
    }

    public static void setMaxLevel(int maxLevel) throws IOException {
        if(maxLevel>currentMax){
            fileWriter = new FileWriter(levels);
            fileWriter.write(maxLevel+"");
            fileWriter.close();
            fileReader = new Scanner(levels);
            currentMax = Integer.parseInt(fileReader.nextLine());
        }
    }

    public static int getCurrentMax(){
        return currentMax;
    }

    public static void resetGame() throws IOException {
        fileWriter = new FileWriter(levels);
        fileWriter.write(1+"");
        fileWriter.close();
        System.exit(0);
    }
}
