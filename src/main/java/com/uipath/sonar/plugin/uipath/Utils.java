package com.uipath.sonar.plugin.uipath;

import java.net.URI;
import java.net.URISyntaxException;

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
}
