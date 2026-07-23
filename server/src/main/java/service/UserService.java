package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requestresult.*;

import java.util.UUID;

public class UserService extends Service {

    public UserService(DataAccess dataAccess) {
        super(dataAccess);
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException, DataAccessException {
        if (request == null) {
            throw new BadRequestException();
        }
        if (request.username() == null) {
            throw new BadRequestException();
        }
        if (request.password() == null) {
            throw new BadRequestException();
        }
        if (request.email() == null) {
            throw new BadRequestException();
        }

        UserData existingUser = dataAccess.getUser(request.username());
        if (existingUser != null) {
            throw new AlreadyTakenException();
        }

        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        UserData newUser = new UserData(request.username(), hashedPassword, request.email());
        dataAccess.createUser(newUser);

        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, request.username());
        dataAccess.createAuth(newAuth);

        RegisterResult result = new RegisterResult(request.username(), authToken);
        return result;
    }

    public LoginResult login(LoginRequest request) throws ResponseException, DataAccessException {
        if (request == null) {
            throw new BadRequestException();
        }
        if (request.username() == null) {
            throw new BadRequestException();
        }
        if (request.password() == null) {
            throw new BadRequestException();
        }

        UserData user = dataAccess.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedException();
        }
        if (!BCrypt.checkpw(request.password(), user.password())) {
            throw new UnauthorizedException();
        }

        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, request.username());
        dataAccess.createAuth(newAuth);

        LoginResult result = new LoginResult(request.username(), authToken);
        return result;
    }

    public void logout(String authToken) throws ResponseException, DataAccessException {
        authenticate(authToken);
        dataAccess.deleteAuth(authToken);
    }
}
