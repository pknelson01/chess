package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class BoardDrawer {
    private static final String RESET = "\u001B[0m";
    private static final String LIGHT_BG = "\u001B[43m";
    private static final String DARK_BG = "\u001B[42m";
    private static final String BORDER_BG = "\u001B[100m";
    private static final String WHITE_PIECE_COLOR = "\u001B[31m";
    private static final String BLACK_PIECE_COLOR = "\u001B[34m";
    private static final String BORDER_TEXT_COLOR = "\u001B[97m";

    private static final String SELECTED_BG = "\u001B[105m";
    private static final String MOVE_BG = "\u001B[106m";

    public static void draw(ChessBoard board, boolean whitePerspective) {
        draw(board, whitePerspective, null, null);
    }

    public static void draw(ChessBoard board, boolean whitePerspective, ChessPosition selected,
                            Collection<ChessPosition> moves) {
        System.out.println();
        printColumnHeaders(whitePerspective);
        if (whitePerspective) {
            for (int row = 8; row >= 1; row--) {
                printRow(board, row, true, selected, moves);
            }
        } else {
            for (int row = 1; row <= 8; row++) {
                printRow(board, row, false, selected, moves);
            }
        }
        printColumnHeaders(whitePerspective);
        System.out.println();
    }

    private static void printColumnHeaders(boolean whitePerspective) {
        System.out.print(BORDER_BG + BORDER_TEXT_COLOR + "   ");
        if (whitePerspective) {
            for (char c = 'a'; c <= 'h'; c++) {
                System.out.print(" " + c + " ");
            }
        } else {
            for (char c = 'h'; c >= 'a'; c--) {
                System.out.print(" " + c + " ");
            }
        }
        System.out.println("   " + RESET);
    }

    private static void printRow(ChessBoard board, int row, boolean whitePerspective, ChessPosition selected,
                                 Collection<ChessPosition> moves) {
        System.out.print(BORDER_BG + BORDER_TEXT_COLOR + " " + row + " ");
        if (whitePerspective) {
            for (int col = 1; col <= 8; col++) {
                printSquare(board, row, col, selected, moves);
            }
        } else {
            for (int col = 8; col >= 1; col--) {
                printSquare(board, row, col, selected, moves);
            }
        }
        System.out.println(BORDER_BG + BORDER_TEXT_COLOR + " " + row + " " + RESET);
    }

    private static void printSquare(ChessBoard board, int row, int col, ChessPosition selected,
                                    Collection<ChessPosition> moves) {
        ChessPosition position = new ChessPosition(row, col);
        String bg;
        if (selected != null && selected.equals(position)) {
            bg = SELECTED_BG;
        } else if (moves != null && moves.contains(position)) {
            bg = MOVE_BG;
        } else if ((row + col) % 2 == 1) {
            bg = LIGHT_BG;
        } else {
            bg = DARK_BG;
        }
        System.out.print(bg);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            System.out.print("   ");
        } else {
            String pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR;
            System.out.print(pieceColor + " " + getPieceLetter(piece) + " ");
        }
    }

    private static String getPieceLetter(ChessPiece piece) {
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return "K";
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return "Q";
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return "R";
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return "B";
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return "N";
        } else {
            return "P";
        }
    }
}
