package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class MySqlDataAccessTests {

    private MySqlDataAccess dataAccess;

    @BeforeEach
    public void setup() throws Exception {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    public void clearSuccess() throws Exception {
        dataAccess.createUser(new UserData("user1", "pass1", "u1@mail.com"));
        dataAccess.createAuth(new AuthData("token1", "user1"));
        dataAccess.createGame("game1");

        dataAccess.clear();

        Assertions.assertNull(dataAccess.getUser("user1"));
        Assertions.assertNull(dataAccess.getAuth("token1"));
        Assertions.assertTrue(dataAccess.listGames().isEmpty());
    }

    @Test
    public void createUserSuccess() throws Exception {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);

        Assertions.assertEquals(user, dataAccess.getUser("user1"));
    }

    @Test
    public void createUserDuplicateFails() throws Exception {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createUser(user));
    }

    @Test
    public void getUserSuccess() throws Exception {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);

        Assertions.assertEquals(user, dataAccess.getUser("user1"));
    }

    @Test
    public void getUserMissingReturnsNull() throws Exception {
        Assertions.assertNull(dataAccess.getUser("nonexistent"));
    }

    @Test
    public void createAuthSuccess() throws Exception {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);

        Assertions.assertEquals(auth, dataAccess.getAuth("token1"));
    }

    @Test
    public void createAuthDuplicateFails() throws Exception {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);

        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createAuth(auth));
    }

    @Test
    public void getAuthSuccess() throws Exception {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);

        Assertions.assertEquals(auth, dataAccess.getAuth("token1"));
    }

    @Test
    public void getAuthMissingReturnsNull() throws Exception {
        Assertions.assertNull(dataAccess.getAuth("nonexistent"));
    }

    @Test
    public void deleteAuthSuccess() throws Exception {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);

        dataAccess.deleteAuth("token1");

        Assertions.assertNull(dataAccess.getAuth("token1"));
    }

    @Test
    public void deleteAuthMissingKeepsOthers() throws Exception {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);

        dataAccess.deleteAuth("nonexistent");

        Assertions.assertNotNull(dataAccess.getAuth("token1"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        GameData game = dataAccess.createGame("game1");

        Assertions.assertNotNull(dataAccess.getGame(game.gameID()));
        Assertions.assertEquals("game1", game.gameName());
    }

    @Test
    public void createGameNullNameFails() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }

    @Test
    public void getGameSuccess() throws Exception {
        GameData game = dataAccess.createGame("game1");

        Assertions.assertEquals(game.gameID(), dataAccess.getGame(game.gameID()).gameID());
    }

    @Test
    public void getGameMissingReturnsNull() throws Exception {
        Assertions.assertNull(dataAccess.getGame(9999));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");

        Collection<GameData> games = dataAccess.listGames();

        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void listGamesEmptyReturnsEmpty() throws Exception {
        Collection<GameData> games = dataAccess.listGames();

        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    public void updateGameSuccess() throws Exception {
        GameData game = dataAccess.createGame("game1");
        GameData updated = new GameData(game.gameID(), "white", "black", "game1", new ChessGame());

        dataAccess.updateGame(updated);

        GameData result = dataAccess.getGame(game.gameID());
        Assertions.assertEquals("white", result.whiteUsername());
        Assertions.assertEquals("black", result.blackUsername());
    }

    @Test
    public void updateGameMissingDoesNothing() throws Exception {
        GameData game = new GameData(9999, "white", "black", "game1", new ChessGame());

        dataAccess.updateGame(game);

        Assertions.assertNull(dataAccess.getGame(9999));
    }
}
