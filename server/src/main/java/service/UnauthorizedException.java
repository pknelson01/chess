package service;

public class UnauthorizedException extends ResponseException {

    public UnauthorizedException() {
        super(401, "Error: unauthorized");
    }
}
