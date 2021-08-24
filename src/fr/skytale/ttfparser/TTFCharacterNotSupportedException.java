package fr.skytale.ttfparser;

public class TTFCharacterNotSupportedException extends RuntimeException {

    public TTFCharacterNotSupportedException(char character) {
        super(String.format("The character '%c' is not supported by the loaded font.", character));
    }

    public TTFCharacterNotSupportedException(String message) {
        super(message);
    }

    public TTFCharacterNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TTFCharacterNotSupportedException(Throwable cause) {
        super(cause);
    }

    public TTFCharacterNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
