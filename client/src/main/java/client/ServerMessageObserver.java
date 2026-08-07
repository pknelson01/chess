package client;

import chess.ChessGame;

public interface ServerMessageObserver {

    void loadGame(ChessGame game);

    void showNotification(String message);

    void showError(String errorMessage);
}
