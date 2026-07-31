package client;

import chess.ChessBoard;
import model.GameData;

import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private String authToken;
    private GameData[] lastGameList;

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
                if (tokens.length < 2) {
                    System.out.println("Usage: create <GAME_NAME>");
                } else {
                    try {
                        facade.createGame(authToken, tokens[1]);
                        System.out.println("Game '" + tokens[1] + "' created successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } else if (tokens[0].equals("list") || tokens[0].equals("ls")) {
                try {
                    lastGameList = facade.listGames(authToken);
                    if (lastGameList.length == 0) {
                        System.out.println("No games available.");
                    } else {
                        for (int i = 0; i < lastGameList.length; i++) {
                            GameData game = lastGameList[i];
                            String white = game.whiteUsername() != null ? game.whiteUsername() : "(open)";
                            String black = game.blackUsername() != null ? game.blackUsername() : "(open)";
                            System.out.println((i + 1) + ". " + game.gameName() + " | White: " + white + " | Black: " + black);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (tokens[0].equals("play") || tokens[0].equals("p")) {
                if (tokens.length < 3) {
                    System.out.println("Usage: play <GAME_NUMBER> <WHITE|BLACK>");
                } else if (lastGameList == null) {
                    System.out.println("Please list games first.");
                } else {
                    try {
                        int gameNum = Integer.parseInt(tokens[1]);
                        if (gameNum < 1 || gameNum > lastGameList.length) {
                            System.out.println("Invalid game number.");
                        } else {
                            String color = tokens[2].toUpperCase();
                            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                                System.out.println("Color must be WHITE or BLACK.");
                            } else {
                                int gameId = lastGameList[gameNum - 1].gameID();
                                facade.joinGame(authToken, gameId, color);
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                boolean whitePerspective = color.equals("WHITE");
                                BoardDrawer.draw(board, whitePerspective);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game number.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } else if (tokens[0].equals("observe") || tokens[0].equals("o")) {
                if (tokens.length < 2) {
                    System.out.println("Usage: observe <GAME_NUMBER>");
                } else if (lastGameList == null) {
                    System.out.println("Please list games first.");
                } else {
                    try {
                        int gameNum = Integer.parseInt(tokens[1]);
                        if (gameNum < 1 || gameNum > lastGameList.length) {
                            System.out.println("Invalid game number.");
                        } else {
                            ChessBoard board = new ChessBoard();
                            board.resetBoard();
                            BoardDrawer.draw(board, true);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game number.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }
}
