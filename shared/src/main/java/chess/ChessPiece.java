package chess;

import java.util.Collection;
import java.util.List;

/*
Design Structure and Plan:
    - Create 6 sub-classes (1 for each piece type) that overrides the ChessPiece method
 */

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN}

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalc calculator = null;

        if (this.type == PieceType.KING) {
            calculator = new KingMovesCalc();
        } else if (this.type == PieceType.QUEEN) {
            calculator = new QueenMovesCalc();
        } else if (this.type == PieceType.PAWN) {
            calculator = new PawnMovesCalc();
        } else if (this.type == PieceType.ROOK) {
            calculator = new RookMovesCalc();
        } else if (this.type == PieceType.KNIGHT) {
            calculator = new KnightMovesCalc();
        } else if (this.type == PieceType.BISHOP) {
            calculator = new BishopMovesCalc();
        }

        if (calculator == null) {
            return List.of();
        }
        return calculator.pieceMoves(board, position);
    }

    public Collection<ChessMove> KingMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
    public Collection<ChessMove> QueenMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
    public Collection<ChessMove> PawnMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
    public Collection<ChessMove> RookMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
    public Collection<ChessMove> KnightMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
    public Collection<ChessMove> BishopMovesCalc() {
        throw new RuntimeException("Not implemented");
    }
}
