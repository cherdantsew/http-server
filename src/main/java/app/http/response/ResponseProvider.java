package app.http.response;

import app.http.HTTPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;

public class ResponseProvider {

    public Response getResponse(File file) throws FileNotFoundException {
        Response response;
        if (file.isFile()) {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_OK, HTTPUtils.RM_OK, file.getAbsolutePath());
            addSuccessHeaders(file, response);
            return response;
        }
        response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_NOT_FOUND, HTTPUtils.RM_FILE_NOT_FOUND, null);
        addErrorHeaders(response);
        return response;
    }

    private void addSuccessHeaders(File file, Response response) {
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(file.getName()));
    }

    private void addErrorHeaders(Response response) {
        response.setHeader("Content-Length", String.valueOf(HttpErrorResponse.RM_404_HTML_TEXT.length()));
        response.setHeader("Content-Type", "text/html");
    }
}