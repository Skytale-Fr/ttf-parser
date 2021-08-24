package fr.skytale.ttfparser.tables;

public class TTFTableParseException extends RuntimeException {

    public TTFTableParseException(String attributeName, Object value, Object expectedValue) {
        super(String.format("Constant parsed value is incorrect for attribute '%s' (value: '%s', expected: '%s').", attributeName, value.toString(), expectedValue.toString()));
    }
    public TTFTableParseException(String attributeName, int value, int expectedValue) {
        this(attributeName, Integer.valueOf(value), Integer.valueOf(expectedValue));
    }
    public TTFTableParseException(String attributeName, short value, short expectedValue) {
        this(attributeName, Short.valueOf(value), Short.valueOf(expectedValue));
    }

    public TTFTableParseException(String message) {
        super(message);
    }

    public TTFTableParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TTFTableParseException(Throwable cause) {
        super(cause);
    }

    public TTFTableParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
