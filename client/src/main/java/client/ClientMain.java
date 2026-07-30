package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        String login = "Login as an existing user: \"l\", \"login\" <USERNAME> <PASSWORD>";
        String register = "Register a new user: \"r\", \"register\" <USERNAME> <PASSWORD> <EMAIL>";
        String exit = "Exit the program: \"q\", \"quit\"";
        String help = "Print this message: \"h\", \"help\"";
        // System.out.println("♕ 240 Chess Client: " + piece);

        System.out.printf(" ♕ Welcome to Parker's Chess Application. Sign in to start. ♕\nOptions:\n%s\n%s\n%s\n%s", login, register, exit, help);
    }
}
