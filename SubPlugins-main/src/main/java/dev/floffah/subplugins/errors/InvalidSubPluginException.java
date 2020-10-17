package dev.floffah.subplugins.errors;

public class InvalidSubPluginException extends Exception {
    public InvalidSubPluginException(Throwable thro, String msg) {
        super(msg, thro);
    }

    public InvalidSubPluginException(String msg) {
        super(msg);
    }
}
