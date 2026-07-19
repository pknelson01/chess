package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.Javalin;
import io.javalin.http.Context;
import service.*;
import service.requestresult.*;

public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();

    private final DataAccess dataAccess = new MemoryDataAccess();
    private final ClearService clearService = new ClearService(dataAccess);
    private final UserService userService = new UserService(dataAccess);
    private final GameService gameService = new GameService(dataAccess);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clear);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);

        javalin.exception(ResponseException.class, this::handleResponseException);
        javalin.exception(Exception.class, this::handleException);
    }

    private void clear(Context ctx) throws Exception {
        clearService.clear();
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }

    private void register(Context ctx) throws Exception {
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        String json = gson.toJson(result);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(json);
    }

    private void login(Context ctx) throws Exception {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        String json = gson.toJson(result);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(json);
    }

    private void logout(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }

    private void listGames(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        ListGamesResult result = gameService.listGames(authToken);
        String json = gson.toJson(result);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(json);
    }

    private void createGame(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(authToken, request);
        String json = gson.toJson(result);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(json);
    }

    private void joinGame(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
        gameService.joinGame(authToken, request);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }

    private void handleResponseException(ResponseException exception, Context ctx) {
        ErrorResult error = new ErrorResult(exception.getMessage());
        String json = gson.toJson(error);
        ctx.status(exception.statusCode());
        ctx.contentType("application/json");
        ctx.result(json);
    }

    private void handleException(Exception exception, Context ctx) {
        ErrorResult error = new ErrorResult("Error: " + exception.getMessage());
        String json = gson.toJson(error);
        ctx.status(500);
        ctx.contentType("application/json");
        ctx.result(json);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
