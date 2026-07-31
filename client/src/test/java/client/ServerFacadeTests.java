package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception {
        facade.clear();
    }

    @Test
    void registerSuccess() throws Exception {
        AuthData result = facade.register("testuser", "password", "test@email.com");
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    void registerDuplicateUsername() throws Exception {
        facade.register("testuser", "password", "test@email.com");
        Assertions.assertThrows(Exception.class, () -> facade.register("testuser", "password", "test@email.com"));
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("testuser", "password", "test@email.com");
        AuthData result = facade.login("testuser", "password");
        Assertions.assertEquals("testuser", result.username());
    }

    @Test
    void loginWrongPassword() throws Exception {
        facade.register("testuser", "password", "test@email.com");
        Assertions.assertThrows(Exception.class, () -> facade.login("testuser", "wrongpassword"));
    }

    @Test
    void logoutSuccess() throws Exception {
        AuthData auth = facade.register("testuser", "password", "test@email.com");
        facade.logout(auth.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.listGames(auth.authToken()));
    }

    @Test
    void logoutInvalidToken() {
        Assertions.assertThrows(Exception.class, () -> facade.logout("invalidtoken"));
    }

    @Test
    void listGamesSuccess() throws Exception {
        AuthData auth = facade.register("testuser", "password", "test@email.com");
        facade.createGame(auth.authToken(), "TestGame");
        GameData[] games = facade.listGames(auth.authToken());
        Assertions.assertEquals(1, games.length);
    }

    @Test
    void listGamesInvalidToken() {
        Assertions.assertThrows(Exception.class, () -> facade.listGames("invalidtoken"));
    }

    @Test
    void createGameSuccess() throws Exception {
        AuthData auth = facade.register("testuser", "password", "test@email.com");
        facade.createGame(auth.authToken(), "MyGame");
        Assertions.assertEquals(1, facade.listGames(auth.authToken()).length);
    }

    @Test
    void createGameInvalidToken() {
        Assertions.assertThrows(Exception.class, () -> facade.createGame("invalidtoken", "MyGame"));
    }

    @Test
    void joinGameSuccess() throws Exception {
        AuthData auth = facade.register("testuser", "password", "test@email.com");
        facade.createGame(auth.authToken(), "TestGame");
        GameData[] games = facade.listGames(auth.authToken());
        facade.joinGame(auth.authToken(), games[0].gameID(), "WHITE");
        Assertions.assertEquals("testuser", facade.listGames(auth.authToken())[0].whiteUsername());
    }

    @Test
    void joinGameColorTaken() throws Exception {
        AuthData auth1 = facade.register("user1", "password", "user1@email.com");
        AuthData auth2 = facade.register("user2", "password", "user2@email.com");
        facade.createGame(auth1.authToken(), "TestGame");
        GameData[] games = facade.listGames(auth1.authToken());
        facade.joinGame(auth1.authToken(), games[0].gameID(), "WHITE");
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(auth2.authToken(), games[0].gameID(), "WHITE"));
    }
}
