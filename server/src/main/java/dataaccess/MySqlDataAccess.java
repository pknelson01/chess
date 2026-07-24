package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlDataAccess implements DataAccess {

    private final Gson gson = new Gson();

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        String createUserTable = "CREATE TABLE IF NOT EXISTS user (" +
                "username VARCHAR(256) NOT NULL, " +
                "password VARCHAR(256) NOT NULL, " +
                "email VARCHAR(256) NOT NULL, " +
                "PRIMARY KEY (username))";
        String createAuthTable = "CREATE TABLE IF NOT EXISTS auth (" +
                "authToken VARCHAR(256) NOT NULL, " +
                "username VARCHAR(256) NOT NULL, " +
                "PRIMARY KEY (authToken))";
        String createGameTable = "CREATE TABLE IF NOT EXISTS game (" +
                "gameID INT NOT NULL AUTO_INCREMENT, " +
                "whiteUsername VARCHAR(256), " +
                "blackUsername VARCHAR(256), " +
                "gameName VARCHAR(256) NOT NULL, " +
                "game TEXT NOT NULL, " +
                "PRIMARY KEY (gameID))";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(createUserTable)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(createAuthTable)) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement(createGameTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE game")) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to clear database: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to create user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM user WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    UserData user = new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"));
                    return user;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        String json = gson.toJson(newGame);
        String statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, gameName);
            preparedStatement.setString(4, json);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                int gameID = generatedKeys.getInt(1);
                GameData newGameData = new GameData(gameID, null, null, gameName, newGame);
                return newGameData;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to create game: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, auth.authToken());
            preparedStatement.setString(2, auth.username());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to create auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    AuthData auth = new AuthData(
                            rs.getString("authToken"),
                            rs.getString("username"));
                    return auth;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to delete auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                    GameData game = new GameData(
                            rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            chessGame);
                    return game;
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read game: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<>();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                GameData game = new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName"),
                        chessGame);
                games.add(game);
            }
            return games;
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to list games: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, gson.toJson(game.game()));
            preparedStatement.setInt(5, game.gameID());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to update game: " + ex.getMessage(), ex);
        }
    }
}
