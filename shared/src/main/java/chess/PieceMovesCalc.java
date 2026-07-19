package chess;

import java.util.Collection;
import java.util.ArrayList;

public interface PieceMovesCalc {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    class PawnMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece myPiece = board.getPiece(position);
            ChessGame.TeamColor myColor = myPiece.getTeamColor();

            int direction;
            int startRow;
            int promotionRow;

            if (myColor == ChessGame.TeamColor.WHITE) {
                direction = 1;
                startRow = 2;
                promotionRow = 8;
            } else {
                direction = -1;
                startRow = 7;
                promotionRow = 1;
            }

            int forwardRow = row + direction;

            if (forwardRow >= 1 && forwardRow <= 8) {
                ChessPosition forwardPos = new ChessPosition(forwardRow, col);
                ChessPiece pieceAtForward = board.getPiece(forwardPos);

                if (pieceAtForward == null) {
                    if (forwardRow == promotionRow) {
                        moves.add(new ChessMove(position, forwardPos, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, forwardPos, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, forwardPos, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, forwardPos, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, forwardPos, null));
                    }

                    if (row == startRow) {
                        int doubleRow = row + (2 * direction);
                        ChessPosition doublePos = new ChessPosition(doubleRow, col);
                        ChessPiece pieceAtDouble = board.getPiece(doublePos);
                        if (pieceAtDouble == null) {
                            moves.add(new ChessMove(position, doublePos, null));
                        }
                    }
                }
            }

            int captureColLeft = col - 1;
            if (forwardRow >= 1 && forwardRow <= 8 && captureColLeft >= 1 && captureColLeft <= 8) {
                ChessPosition capturePosLeft = new ChessPosition(forwardRow, captureColLeft);
                ChessPiece pieceAtLeft = board.getPiece(capturePosLeft);
                if (pieceAtLeft != null && pieceAtLeft.getTeamColor() != myColor) {
                    if (forwardRow == promotionRow) {
                        moves.add(new ChessMove(position, capturePosLeft, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, capturePosLeft, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, capturePosLeft, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, capturePosLeft, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, capturePosLeft, null));
                    }
                }
            }

            int captureColRight = col + 1;
            if (forwardRow >= 1 && forwardRow <= 8 && captureColRight >= 1 && captureColRight <= 8) {
                ChessPosition capturePosRight = new ChessPosition(forwardRow, captureColRight);
                ChessPiece pieceAtRight = board.getPiece(capturePosRight);
                if (pieceAtRight != null && pieceAtRight.getTeamColor() != myColor) {
                    if (forwardRow == promotionRow) {
                        moves.add(new ChessMove(position, capturePosRight, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, capturePosRight, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, capturePosRight, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, capturePosRight, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, capturePosRight, null));
                    }
                }
            }

            return moves;
        }
    }

    class KingMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece myPiece = board.getPiece(position);
            ChessGame.TeamColor myColor = myPiece.getTeamColor();

            int[][] offsets = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
            for (int[] offset : offsets) {
                int newRow = row + offset[0];
                int newCol = col + offset[1];
                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition newPos = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtPos = board.getPiece(newPos);
                    if (pieceAtPos == null || pieceAtPos.getTeamColor() != myColor) {
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
            }

            return moves;
        }
    }

    class RookMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece myPiece = board.getPiece(position);
            ChessGame.TeamColor myColor = myPiece.getTeamColor();

            int r;
            int c;
            ChessPosition newPos;
            ChessPiece pieceAtPos;

            r = row + 1;
            c = col;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r++;
            }

            r = row - 1;
            c = col;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r--;
            }

            r = row;
            c = col + 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                c++;
            }

            r = row;
            c = col - 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                c--;
            }

            return moves;
        }
    }

    class KnightMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece myPiece = board.getPiece(position);
            ChessGame.TeamColor myColor = myPiece.getTeamColor();

            int[][] offsets = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
            for (int[] offset : offsets) {
                int newRow = row + offset[0];
                int newCol = col + offset[1];
                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    ChessPosition newPos = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtPos = board.getPiece(newPos);
                    if (pieceAtPos == null || pieceAtPos.getTeamColor() != myColor) {
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
            }

            return moves;
        }
    }

    class QueenMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            moves.addAll(new RookMC().pieceMoves(board, position));
            moves.addAll(new BishopMC().pieceMoves(board, position));
            return moves;
        }
    }

    class BishopMC implements PieceMovesCalc {
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
            ArrayList<ChessMove> moves = new ArrayList<>();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece myPiece = board.getPiece(position);
            ChessGame.TeamColor myColor = myPiece.getTeamColor();

            int r;
            int c;
            ChessPosition newPos;
            ChessPiece pieceAtPos;

            r = row + 1;
            c = col + 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r++;
                c++;
            }

            r = row + 1;
            c = col - 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r++;
                c--;
            }

            r = row - 1;
            c = col + 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r--;
                c++;
            }

            r = row - 1;
            c = col - 1;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                newPos = new ChessPosition(r, c);
                pieceAtPos = board.getPiece(newPos);
                if (pieceAtPos == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (pieceAtPos.getTeamColor() != myColor) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }
                r--;
                c--;
            }

            return moves;
        }
    }
}
