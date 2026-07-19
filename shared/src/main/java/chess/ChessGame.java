package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    // This holds the current state of the chess board (where all the pieces are)
    private ChessBoard board;

    // This tracks whose turn it is (WHITE or BLACK)
    private TeamColor teamTurn;

    public ChessGame() {
        // Create a brand new chess board
        this.board = new ChessBoard();

        // Set up all the pieces in their starting positions
        this.board.resetBoard();

        // White always goes first in chess
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        // Just return whichever team's turn it currently is
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        // Update whose turn it is to whatever team is passed in
        this.teamTurn = team;
    }

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

        // Get the piece sitting at the given position
        ChessPiece piece = board.getPiece(startPosition);

        // If there is no piece at this position, return null as the instructions say
        if (piece == null) {
            return null;
        }

        // Save the team color of the piece we're working with
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        // Get all the moves this piece could physically make (ignoring check rules for now)
        Collection<ChessMove> candidateMoves = piece.pieceMoves(board, startPosition);

        // This list will hold only the moves that are actually legal (don't leave king in check)
        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        // Test each candidate move one by one
        for (ChessMove move : candidateMoves) {

            // Create a blank copy of the board to safely simulate this move on
            ChessBoard copyBoard = new ChessBoard();

            // Copy every piece from the real board onto the copy board square by square
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece pieceAtPos = board.getPiece(pos);

                    // Only copy the square if there is actually a piece there
                    if (pieceAtPos != null) {
                        copyBoard.addPiece(pos, pieceAtPos);
                    }
                }
            }

            // Get the piece that is moving on the copy board
            ChessPiece movingPiece = copyBoard.getPiece(move.getStartPosition());

            // Check if this move is a pawn promotion (has a promotion piece type)
            if (move.getPromotionPiece() != null) {
                // Place the newly promoted piece at the destination square
                ChessPiece promotedPiece = new ChessPiece(pieceColor, move.getPromotionPiece());
                copyBoard.addPiece(move.getEndPosition(), promotedPiece);
            } else {
                // Just move the piece normally to the destination square
                copyBoard.addPiece(move.getEndPosition(), movingPiece);
            }

            // Remove the piece from the square it started on
            copyBoard.addPiece(move.getStartPosition(), null);

            // Temporarily swap the real board out for the copy board
            // This lets us reuse isInCheck() which looks at this.board
            ChessBoard realBoard = this.board;
            this.board = copyBoard;

            // Check if our king is still in check after making this move
            boolean leavesKingInCheck = isInCheck(pieceColor);

            // Swap the real board back in so we don't permanently change anything
            this.board = realBoard;

            // If the move does NOT leave our king in check, it is a legal move
            if (!leavesKingInCheck) {
                legalMoves.add(move);
            }
        }

        // Return the list of all moves that are actually legal
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // Get the piece that is sitting at the start position of the move
        ChessPiece piece = board.getPiece(move.getStartPosition());

        // If there is no piece at the start position, this move can't be made
        if (piece == null) {
            throw new InvalidMoveException("There is no piece at the start position");
        }

        // If it is not this piece's team's turn, the move is not allowed
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It is not that team's turn");
        }

        // Get all the legal moves for the piece at the start position
        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());

        // If the move the user wants is NOT in the list of legal moves, it's invalid
        if (!legalMoves.contains(move)) {
            throw new InvalidMoveException("That move is not legal");
        }

        // At this point the move is legal, so we apply it to the real board.
        // Check if this move is a pawn promotion (it has a promotion piece type)
        if (move.getPromotionPiece() != null) {
            // Make the new promoted piece using this piece's team color
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            // Place the promoted piece on the end position
            board.addPiece(move.getEndPosition(), promotedPiece);
        } else {
            // Not a promotion, so just move the same piece to the end position
            board.addPiece(move.getEndPosition(), piece);
        }

        // Remove the piece from the square it started on (set that square to empty)
        board.addPiece(move.getStartPosition(), null);

        // Now flip the turn to the other team
        if (teamTurn == TeamColor.WHITE) {
            // White just moved, so now it is black's turn
            teamTurn = TeamColor.BLACK;
        } else {
            // Black just moved, so now it is white's turn
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

        // First I need to find where the king of the given team is on the board
        ChessPosition kingPosition = null;

        // Loop through every row on the board (1 through 8)
        for (int row = 1; row <= 8; row++) {

            // Loop through every column on the board (1 through 8)
            for (int col = 1; col <= 8; col++) {

                // Get the piece sitting at this square
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                // Check if this square has a piece, it belongs to our team, and it's the king
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    // Found the king, save its position
                    kingPosition = pos;
                }
            }
        }

        // Now I need to check if any enemy piece can attack the king's position
        // Loop through every square again, this time looking for opponent pieces
        for (int row = 1; row <= 8; row++) {

            for (int col = 1; col <= 8; col++) {

                // Get the piece at this square
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                // Only care about pieces that belong to the opposing team
                if (piece != null && piece.getTeamColor() != teamColor) {

                    // Get all the moves this enemy piece can make
                    Collection<ChessMove> enemyMoves = piece.pieceMoves(board, pos);

                    // Check each move to see if it lands on the king's square
                    for (ChessMove move : enemyMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            // An enemy piece can reach the king, so we are in check
                            return true;
                        }
                    }
                }
            }
        }

        // No enemy piece can reach the king, so we are not in check
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        // Checkmate can only happen if the team is currently in check.
        // If they are NOT in check, it is definitely not checkmate.
        if (!isInCheck(teamColor)) {
            return false;
        }

        // Now we need to check if this team has ANY legal move at all.
        // Loop through every row on the board (1 through 8)
        for (int row = 1; row <= 8; row++) {

            // Loop through every column on the board (1 through 8)
            for (int col = 1; col <= 8; col++) {

                // Get the piece sitting at this square
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                // Only look at pieces that belong to the team we are checking
                if (piece != null && piece.getTeamColor() == teamColor) {

                    // Get all the legal moves for this piece
                    Collection<ChessMove> legalMoves = validMoves(pos);

                    // If this piece has at least one legal move, it is NOT checkmate
                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // The team is in check and has no legal moves anywhere, so it is checkmate
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

        // Stalemate only happens when the team is NOT in check.
        // If the team IS in check, it cannot be stalemate (it might be checkmate instead).
        if (isInCheck(teamColor)) {
            return false;
        }

        // Now check whether this team has ANY legal move at all.
        // Loop through every row on the board (1 through 8)
        for (int row = 1; row <= 8; row++) {

            // Loop through every column on the board (1 through 8)
            for (int col = 1; col <= 8; col++) {

                // Get the piece sitting at this square
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                // Only look at pieces that belong to the team we are checking
                if (piece != null && piece.getTeamColor() == teamColor) {

                    // Get all the legal moves for this piece
                    Collection<ChessMove> legalMoves = validMoves(pos);

                    // If this piece has at least one legal move, it is NOT stalemate
                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // The team is not in check but has no legal moves anywhere, so it is stalemate
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        // Replace the current board with the new board that was passed in
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        // Just return the current board
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
