package client;

import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public void run () {
        System.out.println(" ♕ Welcome to Parker's Chess Application. Sign in to start.\n");
        ClientMain.help();

        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit") && !line.equals("q")) {
            System.out.print("[LOGGED_OUT] >>> ");
            line = scanner.nextLine().trim().toLowerCase();
            String[] tokens = line.split(" ");

            if (tokens[0].equals("help") || tokens[0].equals("h")) {
                ClientMain.help();
            } else if (tokens[0].equals("login") || tokens[0].equals("l")) {
                return;
            } else if (tokens[0].equals("register") || tokens[0].equals("r")) {
                return;
            } else if (tokens[0].equals("quit") || tokens[0].equals("q")) {
                    System.out.println("Goodbye, pawn!");
            } else {
                System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }
}
