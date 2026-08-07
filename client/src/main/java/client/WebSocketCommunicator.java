package client;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    private final Gson gson = new Gson();
    private final ServerMessageObserver observer;
    private final Session session;

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws Exception {
        this.observer = observer;

        URI uri = new URI(serverUrl.replace("http", "ws") + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String json) {
                handleMessage(json);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    private void handleMessage(String json) {
        ServerMessage message = gson.fromJson(json, ServerMessage.class);

        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage loadGame = gson.fromJson(json, LoadGameMessage.class);
            observer.loadGame(loadGame.getGame());
        }
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notification = gson.fromJson(json, NotificationMessage.class);
            observer.showNotification(notification.getMessage());
        }
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage error = gson.fromJson(json, ErrorMessage.class);
            observer.showError(error.getErrorMessage());
        }
    }

    public void sendCommand(UserGameCommand command) throws Exception {
        session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void close() throws Exception {
        session.close();
    }
}
