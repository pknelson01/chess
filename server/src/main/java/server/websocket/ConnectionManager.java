package server.websocket;

import io.javalin.websocket.WsContext;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, WsContext>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, WsContext context) {
        connections.putIfAbsent(gameID, new ConcurrentHashMap<>());
        connections.get(gameID).put(username, context);
    }

    public void remove(int gameID, String username) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).remove(username);
        }
    }

    public void remove(WsContext context) {
        for (Integer gameID : connections.keySet()) {
            ConcurrentHashMap<String, WsContext> gameConnections = connections.get(gameID);
            for (String username : gameConnections.keySet()) {
                if (gameConnections.get(username) == context) {
                    gameConnections.remove(username);
                }
            }
        }
    }

    public void broadcast(int gameID, String excludeUsername, String message) {
        if (!connections.containsKey(gameID)) {
            return;
        }
        ConcurrentHashMap<String, WsContext> gameConnections = connections.get(gameID);
        for (String username : gameConnections.keySet()) {
            if (!username.equals(excludeUsername)) {
                WsContext context = gameConnections.get(username);
                if (context.session.isOpen()) {
                    context.send(message);
                }
            }
        }
    }
}
