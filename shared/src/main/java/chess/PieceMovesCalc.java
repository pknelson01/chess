package chess;

import java.util.Collection;

public class PieceMovesCalc {
    public interface PieceMovesCalc {
        Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
    }
}
