package app.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLConnection;

public class ResponseProvider {

    public Response getResponse(File file, int bufferSize) throws FileNotFoundException {
        Response response;
        if (file.isFile()) {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_OK, HTTPUtils.RM_OK, new BufferedInputStream(new FileInputStream(file), 1048576), true);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(file.getName()));
            return response;
        }
        response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_NOT_FOUND, HTTPUtils.RM_FILE_NOT_FOUND, null, false);
        response.setHeader("Content-Length", String.valueOf(HttpErrorResponse.RM_404_HTML_TEXT.length()));
        response.setHeader("Content-Type", "text/html");
        return response;
    }
}