package client;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        if (request != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                String json = new Gson().toJson(request);
                os.write(json.getBytes());
            }
        }
        connection.connect();
        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP Error: " + status);
        } try (InputStream is = connection.getInputStream();
               InputStreamReader reader = new InputStreamReader(is)) {
            return new Gson().fromJson(reader, Object.class);
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
