package com.uipath.sonar.plugin.uipath;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public final class Utils {

    private Utils(){}

    /**
     * Creates a URI for a specified path. Convenience method for URI constructor.
     * @param path The path to the workflow.
     * @return A URI object.
     */
    public static URI getURI(String path) throws URISyntaxException {
        return new URI(path.replace('\\', '/'));
    }

    /**
     * Determines if a text node in a XAML document is plain text (Plain text is not surrounded by []s in the XAML.)
     * @param value The String representation of the XAML node.
     * @return True if the node represents plaintext.
     */
    public static boolean nodeIsPlainText(String value){
        return !nodeIsCode(value);
    }

     /**
     * Determines if a text node in a XAML document is code (Code is surrounded by []s in the XAML.)
     * @param value The String representation of the XAML node.
     * @return True if the node represents code.
     */
    public static boolean nodeIsCode(String value){
        return value.startsWith("[")  && value.endsWith("]");
    }

    // Regex patterns for conventions
    public static final String CAMELCASE_IDENTIFIER = "[camelCase]";
    private static final String CAMELCASE_PATTERN = "^[a-z][\\w\\d]+$";
    public static final String PASCALCASE_IDENTIFIER = "[PascalCase]";
    private static final String PASCALCASE_PATTERN = "^[A-Z][\\w\\d]+$";
    public static final String UPPERCASE_IDENTIFIER = "[UPPERCASE]";
    private static final String UPPERCASE_PATTERN = "^[A-Z\\d]+$";
    public static final String LOWER_IDENTIFIER = "[lowercase]";
    private static final String LOWERCASE_PATTERN = "^[a-z\\d]+$";

    /**
     * Generates a regex pattern to match the specified convention.
     * @param convention The convention to match.
     * @return A regex pattern that matches the convention.
     */
    public static Pattern createRegexPatternForConvention(String convention){
        return Pattern.compile(
            convention
                .replace(CAMELCASE_IDENTIFIER, CAMELCASE_PATTERN)
                .replace(PASCALCASE_IDENTIFIER, PASCALCASE_PATTERN)
                .replace(UPPERCASE_IDENTIFIER, UPPERCASE_PATTERN)
                .replace(LOWER_IDENTIFIER, LOWERCASE_PATTERN));
    }

    /**
     * Checks that a string matches the specified convention.
     * @param string The string to test
     * @param convention The convention to use for the test
     * @return True if the string follows the convention, otherwise False. Works with '[camelCase]', '[PascalCase]', '[UPPERCASE]', and '[lowercase]' magic strings.
     */
    public static boolean stringFollowsConvention(String string, String convention){
        return createRegexPatternForConvention(convention).matcher(string).find();
    }
}
