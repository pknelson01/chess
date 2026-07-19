package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;

public abstract class Service {

    protected final DataAccess dataAccess;

    protected Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    protected AuthData authenticate(String authToken) throws ResponseException, DataAccessException {
        if (authToken == null) {
            throw new UnauthorizedException();
        }
        AuthData auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        return auth;
    }
}
