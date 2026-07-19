package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.requestresult.*;

import java.util.Collection;

public class GameService extends Service {

    public GameService(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ListGamesResult listGames(String authToken) throws ResponseException, DataAccessException {
        authenticate(authToken);
        Collection<GameData> games = dataAccess.listGames();
        ListGamesResult result = new ListGamesResult(games);
        return result;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws ResponseException, DataAccessException {
        authenticate(authToken);

        if (request == null) {
            throw new BadRequestException();
        }
        if (request.gameName() == null) {
            throw new BadRequestException();
        }

        GameData newGame = dataAccess.createGame(request.gameName());
        CreateGameResult result = new CreateGameResult(newGame.gameID());
        return result;
    }

    public void joinGame(String authToken, JoinGameRequest request) throws ResponseException, DataAccessException {
        AuthData auth = authenticate(authToken);

        if (request == null) {
            throw new BadRequestException();
        }
        if (request.playerColor() == null) {
            throw new BadRequestException();
        }

        GameData game = dataAccess.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException();
        }

        String username = auth.username();

        if (request.playerColor().equalsIgnoreCase("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException();
            }
            GameData updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            dataAccess.updateGame(updatedGame);
        } else if (request.playerColor().equalsIgnoreCase("BLACK")) {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException();
            }
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            dataAccess.updateGame(updatedGame);
        } else {
            throw new BadRequestException();
        }
    }
}
