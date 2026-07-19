package service;

public class BadRequestException extends ResponseException {

    public BadRequestException() {
        super(400, "Error: bad request");
    }
}
