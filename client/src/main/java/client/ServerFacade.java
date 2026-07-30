package client;

import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private Object makeRequest(String method, String path, Object request, String authToken) throws Exception{
        var url = new URI(serverUrl + path).toUrl();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }
    }


    public Object register(String username, String password, String email) {
        return null;
    }

    public Object login(String username, String password) {
        return null;
    }

    public void logout(String authToken) {
        return;
    }

    public Object listGames(String authToken) {
        return null;
    }

    public Object createGame(String authToken, String gameName) {
        return null;
    }

    public void joinGame(String authToken, int gameId, String playerColor) {
        return;
    }
}
