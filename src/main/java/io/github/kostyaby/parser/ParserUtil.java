package io.github.kostyaby.parser;

/**
 * Created by kostya_by on 2/24/16.
 */
public class ParserUtil {
    public final static int DEFAULT_NUMBER = 9999;

    public static int parseNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return DEFAULT_NUMBER;
        }
    }
}
