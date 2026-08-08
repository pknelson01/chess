package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler {

    private final Gson gson = new Gson();
    private final DataAccess dataAccess;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void onMessage(WsMessageContext context) {
        String json = context.message();
        UserGameCommand command = gson.fromJson(json, UserGameCommand.class);

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            connect(command, context);
        }
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            MakeMoveCommand moveCommand = gson.fromJson(json, MakeMoveCommand.class);
            makeMove(moveCommand, context);
        }
        if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            leave(command, context);
        }
        if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            resign(command, context);
        }
    }

    public void onClose(WsCloseContext context) {
        connections.remove(context);
    }

    private void connect(UserGameCommand command, WsMessageContext context) {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(context, "Error: unauthorized");
                return;
            }

            GameData game = dataAccess.getGame(command.getGameID());
            if (game == null) {
                sendError(context, "Error: game not found");
                return;
            }

            String username = auth.username();
            int gameID = command.getGameID();
            connections.add(gameID, username, context);

            LoadGameMessage loadGame = new LoadGameMessage(game.game());
            context.send(gson.toJson(loadGame));

            String text = username + " joined the game as " + getRole(game, username);
            NotificationMessage notification = new NotificationMessage(text);
            connections.broadcast(gameID, username, gson.toJson(notification));
        } catch (Exception exception) {
            sendError(context, "Error: " + exception.getMessage());
        }
    }

    private void makeMove(MakeMoveCommand command, WsMessageContext context) {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(context, "Error: unauthorized");
                return;
            }

            GameData gameData = dataAccess.getGame(command.getGameID());
            if (gameData == null) {
                sendError(context, "Error: game not found");
                return;
            }

            String username = auth.username();
            ChessGame game = gameData.game();

            if (game.isGameOver()) {
                sendError(context, "Error: the game is already over");
                return;
            }

            ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);
            if (playerColor == null) {
                sendError(context, "Error: observers cannot make moves");
                return;
            }
            if (playerColor != game.getTeamTurn()) {
                sendError(context, "Error: it is not your turn");
                return;
            }

            game.makeMove(command.getMove());

            ChessGame.TeamColor otherColor = ChessGame.TeamColor.WHITE;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                otherColor = ChessGame.TeamColor.BLACK;
            }

            String endOfGameText = null;
            if (game.isInCheckmate(otherColor)) {
                game.setGameOver(true);
                endOfGameText = getOpponentName(gameData, playerColor) + " is in checkmate";
            } else if (game.isInStalemate(otherColor)) {
                game.setGameOver(true);
                endOfGameText = getOpponentName(gameData, playerColor) + " is in stalemate";
            } else if (game.isInCheck(otherColor)) {
                endOfGameText = getOpponentName(gameData, playerColor) + " is in check";
            }

            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), game);
            dataAccess.updateGame(updatedGame);

            int gameID = command.getGameID();
            LoadGameMessage loadGame = new LoadGameMessage(game);
            connections.broadcast(gameID, null, gson.toJson(loadGame));

            String moveText = "'" + username + "'" + " moved " + describeMove(command.getMove());
            connections.broadcast(gameID, username, gson.toJson(new NotificationMessage(moveText)));

            if (endOfGameText != null) {
                connections.broadcast(gameID, null, gson.toJson(new NotificationMessage(endOfGameText)));
            }
        } catch (InvalidMoveException exception) {
            sendError(context, "Error: invalid move");
        } catch (Exception exception) {
            sendError(context, "Error: " + exception.getMessage());
        }
    }

    private void leave(UserGameCommand command, WsMessageContext context) {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(context, "Error: unauthorized");
                return;
            }

            GameData gameData = dataAccess.getGame(command.getGameID());
            if (gameData == null) {
                sendError(context, "Error: game not found");
                return;
            }

            String username = auth.username();
            int gameID = command.getGameID();
            ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

            if (playerColor != null) {
                String whiteUsername = gameData.whiteUsername();
                String blackUsername = gameData.blackUsername();
                if (playerColor == ChessGame.TeamColor.WHITE) {
                    whiteUsername = null;
                } else {
                    blackUsername = null;
                }
                GameData updatedGame = new GameData(gameData.gameID(), whiteUsername, blackUsername,
                        gameData.gameName(), gameData.game());
                dataAccess.updateGame(updatedGame);
            }

            connections.remove(gameID, username);

            String text = username + " left the game";
            connections.broadcast(gameID, username, gson.toJson(new NotificationMessage(text)));
        } catch (Exception exception) {
            sendError(context, "Error: " + exception.getMessage());
        }
    }

    private void resign(UserGameCommand command, WsMessageContext context) {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(context, "Error: unauthorized");
                return;
            }

            GameData gameData = dataAccess.getGame(command.getGameID());
            if (gameData == null) {
                sendError(context, "Error: game not found");
                return;
            }

            String username = auth.username();
            ChessGame game = gameData.game();

            if (game.isGameOver()) {
                sendError(context, "Error: the game is already over");
                return;
            }

            ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);
            if (playerColor == null) {
                sendError(context, "Error: observers cannot resign");
                return;
            }

            game.setGameOver(true);
            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), game);
            dataAccess.updateGame(updatedGame);

            String text = username + " resigned the game";
            connections.broadcast(command.getGameID(), null, gson.toJson(new NotificationMessage(text)));
        } catch (Exception exception) {
            sendError(context, "Error: " + exception.getMessage());
        }
    }

    private ChessGame.TeamColor getPlayerColor(GameData game, String username) {
        if (username.equals(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        if (username.equals(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

    private String getOpponentName(GameData game, ChessGame.TeamColor moverColor) {
        if (moverColor == ChessGame.TeamColor.WHITE) {
            return game.blackUsername();
        }
        return game.whiteUsername();
    }

    private String describeMove(ChessMove move) {
        return describePosition(move.getStartPosition()) + " to " + describePosition(move.getEndPosition());
    }

    private String describePosition(ChessPosition position) {
        char column = (char) ('a' + position.getColumn() - 1);
        return "" + column + position.getRow();
    }

    private String getRole(GameData game, String username) {
        if (username.equals(game.whiteUsername())) {
            return "white";
        }
        if (username.equals(game.blackUsername())) {
            return "black";
        }
        return "an observer";
    }

    private void sendError(WsMessageContext context, String message) {
        ErrorMessage error = new ErrorMessage(message);
        context.send(gson.toJson(error));
    }
}
