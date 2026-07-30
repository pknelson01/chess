package client;

import java.util.Scanner;

public class Repl {
    public Repl(String serverUrl) {
    }

    public void run () {
        System.out.println(" ♕ Welcome to Parker's Chess Application. Sign in to start.\n");
        ClientMain.help();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
        }
    }
}
