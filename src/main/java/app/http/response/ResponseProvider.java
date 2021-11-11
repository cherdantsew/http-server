package app.http.response;

import app.http.HTTPUtils;
import app.http.Session;
import app.http.request.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.logging.Logger;

public class ResponseProvider {

    private static final Logger LOGGER = Logger.getLogger(ResponseProvider.class.getName());

    public Response getResponse(Request request) throws FileNotFoundException {
        Session session = request.getSession();
        if (session == null) {
            return handleExpiredSession();
        }
        return handleActiveSession(request, session);
    }

    private Response handleExpiredSession() {
        Response response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_UNAUTHORIZED, HTTPUtils.RM_UNAUTHORIZED, HttpErrorResponse.RM_401_HTML_TEXT);
        addErrorHeaders(response, HttpErrorResponse.RM_401_HTML_TEXT);
        return response;
    }

    private Response handleActiveSession(Request request, Session session) {
        Response response;
        File file = new File(request.getResource());
        if (file.isFile()) {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_OK, HTTPUtils.RM_OK, file.getAbsolutePath());
            addSuccessHeaders(file, response, session);
        } else {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_NOT_FOUND, HTTPUtils.RM_FILE_NOT_FOUND, null);
            addErrorHeaders(response, HttpErrorResponse.RM_404_HTML_TEXT);
        }
        return response;
    }

    private void addSuccessHeaders(File file, Response response, Session session) {
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(file.getName()));
        if (session.isNew())
            response.setHeader("Set-Cookie", "UUID=" + session.getUuid().toString());
    }

    private void addErrorHeaders(Response response, String errorHtmlText) {
        response.setHeader("Content-Length", String.valueOf(errorHtmlText.length()));
        response.setHeader("Content-Type", "text/html");
        if (response.getStatusCode() == HTTPUtils.RC_UNAUTHORIZED)
            response.setHeader("Set-Cookie", "UUID=; Max-age=-1");
    }
}