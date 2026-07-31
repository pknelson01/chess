package client;

import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private String authToken;

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public void run() {
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
                if (tokens.length < 3) {
                    System.out.println("Correct Usage: login <USERNAME> <PASSWORD>");
                } else {
                    try {
                        var result = facade.login(tokens[1], tokens[2]);
                        authToken = result.authToken();
                        System.out.println("Logged in successfully!");
                        postloginLoop(scanner);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } else if (tokens[0].equals("register") || tokens[0].equals("r")) {
                if (tokens.length < 4) {
                    System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
                } else {
                    try {
                        var result = facade.register(tokens[1], tokens[2], tokens[3]);
                        authToken = result.authToken();
                        System.out.println("Registered and logged in successfully!");
                        postloginLoop(scanner);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } else if (tokens[0].equals("quit") || tokens[0].equals("q")) {
                System.out.println("Goodbye, chess master!");
            } else {
                System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }

    private void postloginLoop(Scanner scanner) {
        String line = "";
        while (!line.equals("logout") && !line.equals("lo")) {
            System.out.print("[LOGGED_IN] >>> ");
            line = scanner.nextLine().trim().toLowerCase();
            String[] tokens = line.split(" ");

            if (tokens[0].equals("help") || tokens[0].equals("h")) {
                ClientMain.postloginHelp();
            } else if (tokens[0].equals("logout") || tokens[0].equals("lo")) {
                try {
                    facade.logout(authToken);
                    authToken = null;
                    System.out.println("Logged out successfully!");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (tokens[0].equals("create") || tokens[0].equals("c")) {
            } else if (tokens[0].equals("list") || tokens[0].equals("ls")) {
            } else if (tokens[0].equals("play") || tokens[0].equals("p")) {
            } else if (tokens[0].equals("observe") || tokens[0].equals("o")) {
            } else {
                System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }
}
