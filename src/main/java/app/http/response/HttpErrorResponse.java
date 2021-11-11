package app.http.response;

public class HttpErrorResponse {
    public static final String RM_404_HTML_TEXT = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>404 Error</title>\n" +
            "</head>\n" +
            "<body><h2>File not found on the server.</h2></body>\n" +
            "</html>";
    public static final String RM_405_HTML_TEXT = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>405 Error</title>\n" +
            "</head>\n" +
            "<body><h2>Method is not supported yet.</h2></body>\n" +
            "</html>";

    public static final String RM_401_HTML_TEXT = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>401 Error</title>\n" +
            "</head>\n" +
            "<body><h2>The session is expired.</h2></body>\n" +
            "</html>";
}