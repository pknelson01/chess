package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    private DataAccess data;
    private UserService userService;

    @BeforeEach
    public void setup() {
        data = new MemoryDataAccess();
        userService = new UserService(data);
    }

    @Test
    public void registerSuccess() throws Exception {
        RegisterResult result = userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        Assertions.assertEquals("alice", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertNotNull(data.getUser("alice"));
    }

    @Test
    public void registerDuplicateUsernameFails() throws Exception {
        userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> userService.register(new RegisterRequest("alice", "other", "c@d.com")));
    }

    @Test
    public void loginSuccess() throws Exception {
        userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        LoginResult result = userService.login(new LoginRequest("alice", "pw"));

        Assertions.assertEquals("alice", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginWrongPasswordFails() throws Exception {
        userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.login(new LoginRequest("alice", "wrong")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        RegisterResult reg = userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        userService.logout(reg.authToken());

        Assertions.assertNull(data.getAuth(reg.authToken()));
    }

    @Test
    public void logoutBadTokenFails() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.logout("not-a-real-token"));
    }
}
