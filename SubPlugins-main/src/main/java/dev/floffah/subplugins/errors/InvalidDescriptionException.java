package dev.floffah.subplugins.errors;

public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(Throwable thro, String msg) {
        super(msg, thro);
    }

    public InvalidDescriptionException(String msg) {
        super(msg);
    }
}
