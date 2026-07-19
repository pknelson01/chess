package dataaccess;

import chess.ChessGame;
import model.UserData;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> auths = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    private int nextGameId = 1;

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
        nextGameId = 1;
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        UserData user = users.get(username);
        return user;
    }

    @Override
    public void createAuth(AuthData auth) {
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        AuthData auth = auths.get(authToken);
        return auth;
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public GameData createGame(String gameName) {
        int newId = nextGameId;
        nextGameId = nextGameId + 1;
        GameData newGame = new GameData(newId, null, null, gameName, new ChessGame());
        games.put(newId, newGame);
        return newGame;
    }

    @Override
    public GameData getGame(int gameID) {
        GameData game = games.get(gameID);
        return game;
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }
}
