package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

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

    /**
     * Creates the database (if needed) and all tables (if needed). Safe to run on
     * every startup because every statement uses IF NOT EXISTS.
     */
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : CREATE_STATEMENTS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage(), ex);
        }
    }

    private static final String[] CREATE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(256) NOT NULL,
                password VARCHAR(256) NOT NULL,
                email    VARCHAR(256) NOT NULL,
                PRIMARY KEY (username)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(256) NOT NULL,
                username  VARCHAR(256) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID        INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(256),
                blackUsername VARCHAR(256),
                gameName      VARCHAR(256) NOT NULL,
                game          TEXT NOT NULL,
                PRIMARY KEY (gameID)
            )
            """
    };

    @Override
    public void clear() throws DataAccessException {
        // Order doesn't matter here since there are no foreign keys. TRUNCATE also
        // resets game's AUTO_INCREMENT counter, which the tests expect after a clear.
        try (var conn = DatabaseManager.getConnection()) {
            for (var table : new String[]{"auth", "game", "user"}) {
                try (var ps = conn.prepareStatement("TRUNCATE TABLE " + table)) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to clear database: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        // NOTE: user.password() must already be a BCrypt hash by the time it gets here.
        // Hashing belongs in UserService.register, not in the DAO.
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, user.username());
            ps.setString(2, user.password());
            ps.setString(3, user.email());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to create user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"));
                }
                return null; // no such user — matches MemoryDataAccess behavior
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        // The DB assigns gameID via AUTO_INCREMENT. We ask for the generated key back
        // so we can return a fully-populated GameData, matching MemoryDataAccess.
        var newGame = new ChessGame();
        var json = gson.toJson(newGame);
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, null);
            ps.setString(2, null);
            ps.setString(3, gameName);
            ps.setString(4, json);
            ps.executeUpdate();

            try (var generatedKeys = ps.getGeneratedKeys()) {
                generatedKeys.next();
                int gameID = generatedKeys.getInt(1);
                return new GameData(gameID, null, null, gameName, newGame);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to create game: " + ex.getMessage(), ex);
        }
    }

    // ----------------------------------------------------------------------------------
    // TODO: the methods below are yours to implement. Guidance for each is in the comment.
    // The four above cover every pattern you need — copy their shape.
    // ----------------------------------------------------------------------------------

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        // Like createUser: a plain INSERT into auth (authToken, username). No hashing.
        throw new DataAccessException("createAuth not implemented");
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        // Like getUser: SELECT ... WHERE authToken = ?, build an AuthData or return null.
        throw new DataAccessException("getAuth not implemented");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        // A DELETE FROM auth WHERE authToken = ?. Same try/prepare/executeUpdate shape.
        throw new DataAccessException("deleteAuth not implemented");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readGame(rs);
                }
                return null; // no such game — matches MemoryDataAccess behavior
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to read game: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(readGame(rs));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to list games: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = """
                UPDATE game
                SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?
                WHERE gameID = ?
                """;
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, game.whiteUsername());
            ps.setString(2, game.blackUsername());
            ps.setString(3, game.gameName());
            ps.setString(4, gson.toJson(game.game()));
            ps.setInt(5, game.gameID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to update game: " + ex.getMessage(), ex);
        }
    }

    /**
     * Builds a GameData from the current row of a ResultSet. The 'game' column holds
     * the ChessGame as a JSON string, so it is deserialized back into an object here.
     * Shared by getGame and listGames.
     */
    private GameData readGame(ResultSet rs) throws SQLException {
        var chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
        return new GameData(
                rs.getInt("gameID"),
                rs.getString("whiteUsername"),
                rs.getString("blackUsername"),
                rs.getString("gameName"),
                chessGame);
    }
}
