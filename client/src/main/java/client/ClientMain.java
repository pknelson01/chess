package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var serverUrl = "http://localhost:8080";
        if(args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run(); // Create a REPL class either here or on its own page

    }

    public static void help() {
        String login = "Login as an existing user: \"l\", \"login\" <USERNAME> <PASSWORD>";
        String register = "Register a new user: \"r\", \"register\" <USERNAME> <PASSWORD> <EMAIL>";
        String exit = "Exit the program: \"q\", \"quit\"";
        String help = "Print this message: \"h\", \"help\"";
        System.out.printf("Options:\n%s\n%s\n%s\n%s\n", login, register, exit, help);
    }

    public static void postloginHelp() {
        String help = "Print this message: \"h\", \"help\"";
        String logout = "Logout: \"lo\", \"logout\"";
        String create = "Create a game: \"c\", \"create\" <GAME_NAME>";
        String list = "List all games: \"ls\", \"list\"";
        String play = "Play a game: \"p\", \"play\" <GAME_NUMBER> <WHITE|BLACK>";
        String observe = "Observe a game: \"o\", \"observe\" <GAME_NUMBER>";
        System.out.printf("Options:\n%s\n%s\n%s\n%s\n%s\n%s\n", help, logout, create, list, play, observe);
    }
}
