package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySqlDataAccessTests {

    private MySqlDataAccess dataAccess;

    @BeforeEach
    public void setup() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    public void clearPositive() throws DataAccessException {
        dataAccess.createUser(new UserData("user1", "pass1", "u1@mail.com"));
        dataAccess.createAuth(new AuthData("token1", "user1"));
        dataAccess.createGame("game1");

        dataAccess.clear();

        assertNull(dataAccess.getUser("user1"));
        assertNull(dataAccess.getAuth("token1"));
        assertTrue(dataAccess.listGames().isEmpty());
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);
        assertEquals(user, dataAccess.getUser("user1"));
    }

    @Test
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);
        assertThrows(DataAccessException.class, () -> dataAccess.createUser(user));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("user1", "pass1", "u1@mail.com");
        dataAccess.createUser(user);
        assertEquals(user, dataAccess.getUser("user1"));
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        assertNull(dataAccess.getUser("nonexistent"));
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);
        assertEquals(auth, dataAccess.getAuth("token1"));
    }

    @Test
    public void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);
        assertThrows(DataAccessException.class, () -> dataAccess.createAuth(auth));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);
        assertEquals(auth, dataAccess.getAuth("token1"));
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        assertNull(dataAccess.getAuth("nonexistent"));
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);
        dataAccess.deleteAuth("token1");
        assertNull(dataAccess.getAuth("token1"));
    }

    @Test
    public void deleteAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token1", "user1");
        dataAccess.createAuth(auth);
        dataAccess.deleteAuth("nonexistent");
        assertNotNull(dataAccess.getAuth("token1"));
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        GameData game = dataAccess.createGame("game1");
        assertNotNull(dataAccess.getGame(game.gameID()));
        assertEquals("game1", game.gameName());
    }

    @Test
    public void createGameNegative() {
        assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        GameData game = dataAccess.createGame("game1");
        assertEquals(game.gameID(), dataAccess.getGame(game.gameID()).gameID());
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        assertNull(dataAccess.getGame(9999));
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");
        Collection<GameData> games = dataAccess.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        Collection<GameData> games = dataAccess.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void updateGamePositive() throws DataAccessException {
        GameData game = dataAccess.createGame("game1");
        GameData updated = new GameData(game.gameID(), "white", "black", "game1", new ChessGame());
        dataAccess.updateGame(updated);
        GameData result = dataAccess.getGame(game.gameID());
        assertEquals("white", result.whiteUsername());
        assertEquals("black", result.blackUsername());
    }

    @Test
    public void updateGameNegative() throws DataAccessException {
        GameData game = new GameData(9999, "white", "black", "game1", new ChessGame());
        dataAccess.updateGame(game);
        assertNull(dataAccess.getGame(9999));
    }
}
