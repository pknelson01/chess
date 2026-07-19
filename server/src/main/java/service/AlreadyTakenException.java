package service;

public class AlreadyTakenException extends ResponseException {

    public AlreadyTakenException() {
        super(403, "Error: already taken");
    }
}
