package app.http.response;

import app.http.HTTPUtils;
import app.http.Session;
import app.http.request.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseProvider {
    private static Logger LOGGER = Logger.getLogger(ResponseProvider.class.getName());

    public Response getResponse(Request request, File file, ConcurrentMap<String, Session> sessions) throws FileNotFoundException {
        Response response;
        if (file.isFile()) {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_OK, HTTPUtils.RM_OK, file.getAbsolutePath());
            addSuccessHeaders(file, response);
        } else {
            response = new Response(HTTPUtils.HTTP_1_1_PROTOCOL, HTTPUtils.RC_NOT_FOUND, HTTPUtils.RM_FILE_NOT_FOUND, null);
            addErrorHeaders(response);
        }
        setUuidCookie(request, sessions, response);
        return response;
    }

    private void setUuidCookie(Request request, ConcurrentMap<String, Session> sessions, Response response) {
        String UUID = request.getParamValueFromHeader("Cookie", "UUID");
        if (UUID == null) {
            Session session = new Session();
            sessions.putIfAbsent(session.getUuid().toString(), session);
            response.setHeader("Set-Cookie", "UUID=" + session.getUuid().toString());
            return;
        }
        if (sessions.containsKey(UUID)) {
            Session session = sessions.get(UUID);
            session.update();
        } else {
            LOGGER.log(Level.WARNING, "The UUID =" + UUID + " is not found in available sessions.");
        }
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