package chess;

import java.util.Collection;
import java.util.ArrayList;

public interface PieceMovesCalc {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    class KingMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }

    class QueenMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }

    class PawnMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }

    class RookMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }

    class KnightMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }

    class BishopMovesCalc implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            return new ArrayList<>();
        }
    }
}