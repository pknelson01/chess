package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import service.requestresult.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClearServiceTest {

    @Test
    public void clearRemovesAllData() throws Exception {
        DataAccess data = new MemoryDataAccess();
        UserService userService = new UserService(data);
        ClearService clearService = new ClearService(data);

        RegisterResult reg = userService.register(new RegisterRequest("alice", "pw", "a@b.com"));

        clearService.clear();

        Assertions.assertNull(data.getUser("alice"));
        Assertions.assertNull(data.getAuth(reg.authToken()));
    }
}
