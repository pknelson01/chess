package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return teamTurn; }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { this.teamTurn = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        Collection<ChessMove> moveOptions = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        for (ChessMove move : moveOptions) {

            // temporary copy of the board
            ChessBoard copyBoard = new ChessBoard();

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece pieceAtPos = board.getPiece(pos);

                    if (pieceAtPos != null) {
                        copyBoard.addPiece(pos, pieceAtPos);
                    }
                }
            }

            ChessPiece movingPiece = copyBoard.getPiece(move.getStartPosition());

            if (move.getPromotionPiece() != null) {
                ChessPiece promotedPiece = new ChessPiece(pieceColor, move.getPromotionPiece());
                copyBoard.addPiece(move.getEndPosition(), promotedPiece);
            } else {
                copyBoard.addPiece(move.getEndPosition(), movingPiece);
            }

            copyBoard.addPiece(move.getStartPosition(), null);
            ChessBoard ogBoard = this.board;
            this.board = copyBoard;
            boolean leavesKingInCheck = isInCheck(pieceColor);
            this.board = ogBoard;

            if (!leavesKingInCheck) {
                legalMoves.add(move);
            }
        }

        return legalMoves;

    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null) {
            throw new InvalidMoveException("There is no piece at the start position");
        }

        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It is not that team's turn");
        }

        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());

        if (!legalMoves.contains(move)) {
            throw new InvalidMoveException("That move is not a legal move");
        }

        if (move.getPromotionPiece() != null) {
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promotedPiece);
        } else {
            board.addPiece(move.getEndPosition(), piece);
        }

        board.addPiece(move.getStartPosition(), null);

        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPos = null;

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPos = pos;
                }
            }
        }

        // Check with TA or someone who plays chess more often than me for the following block of code but I am adding it as a safety net even though I am not sure if it is even possible...
        if (kingPos == null) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMoves = piece.pieceMoves(board, pos);

                    for (ChessMove move : enemyMoves) {
                        if (move.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }


        // Not in Check...
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> legalMoves = validMoves(pos);

                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if (isInCheck(teamColor)) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove>legalMoves = validMoves(pos);

                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board; }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return board; }
}