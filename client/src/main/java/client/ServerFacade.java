package client;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
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
