import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Exceptions {
    public static void main(String[] args) {
        File file = new File(args[0]);
        processFile(file);
    }

    private static void processFile(File file) {
        Scanner scan = null;

        try (Scanner scan = new Scanner(file)) {
                // ... use Scanner for something useful
            scan.clos();
        }
        catch (FileNotFoundException ex){
            System.out.printf("Could not find this file: %s", file);
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (scan != null) {
                scan.close();
            }
        }
    }
}