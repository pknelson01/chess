package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
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
