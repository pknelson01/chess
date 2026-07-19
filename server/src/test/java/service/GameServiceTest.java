package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import service.requestresult.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {

    private DataAccess data;
    private UserService userService;
    private GameService gameService;
    private String authToken;

    @BeforeEach
    public void setup() throws Exception {
        data = new MemoryDataAccess();
        userService = new UserService(data);
        gameService = new GameService(data);
        authToken = userService.register(new RegisterRequest("alice", "pw", "a@b.com")).authToken();
    }

    @Test
    public void createGameSuccess() throws Exception {
        CreateGameResult result = gameService.createGame(authToken, new CreateGameRequest("My Game"));

        Assertions.assertTrue(result.gameID() > 0);
        Assertions.assertNotNull(data.getGame(result.gameID()));
    }

    @Test
    public void createGameBadTokenFails() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> gameService.createGame("bad-token", new CreateGameRequest("My Game")));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        gameService.createGame(authToken, new CreateGameRequest("Game A"));
        gameService.createGame(authToken, new CreateGameRequest("Game B"));

        ListGamesResult result = gameService.listGames(authToken);

        Assertions.assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesBadTokenFails() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> gameService.listGames("bad-token"));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        int gameID = gameService.createGame(authToken, new CreateGameRequest("My Game")).gameID();

        gameService.joinGame(authToken, new JoinGameRequest("WHITE", gameID));

        Assertions.assertEquals("alice", data.getGame(gameID).whiteUsername());
    }

    @Test
    public void joinGameColorAlreadyTakenFails() throws Exception {
        int gameID = gameService.createGame(authToken, new CreateGameRequest("My Game")).gameID();
        gameService.joinGame(authToken, new JoinGameRequest("WHITE", gameID));

        String otherToken = userService.register(new RegisterRequest("bob", "pw", "b@b.com")).authToken();

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> gameService.joinGame(otherToken, new JoinGameRequest("WHITE", gameID)));
    }
}
