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
}
