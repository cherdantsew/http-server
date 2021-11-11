package app.http.request;

import app.http.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestReader {

    private static final Logger LOGGER = Logger.getLogger(RequestReader.class.getName());
    private static final String COOKIE = "Cookie";
    private static final String UUID = "UUID";
    private static final int STATUS_LINE_INDEX = 0;
    private static final int HTTP_METHOD_INDEX = 0;
    private static final int HTTP_RESOURCE_INDEX = 1;
    private static final int HTTP_PROTOCOL_INDEX = 2;
    private static final int HEADER_NAME_INDEX = 0;
    private static final int HEADER_VALUE_INDEX = 1;
    private static final int SESSION_TIMER_DELAY = 1000;
    private static final int SESSION_TIMER_PERIOD = 1000;

    public Request readRequest(InputStream inputStream, String pathToResources, ConcurrentMap<String, Session> sessions, Timer timer) {
        try {
            Request request = new Request();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            List<String> requestLines = readRequestLines(in);
            String firstLine = requestLines.get(STATUS_LINE_INDEX);
            request.setMethod(getMethod(firstLine));
            request.setProtocolVersion(getProtocolVersion(firstLine));
            request.setResource(getResource(pathToResources, firstLine));
            formRequestHeaders(requestLines, request);
            request.setSession(getSession(request, sessions, timer));
            return request;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while reading request from input stream.", e);
        }
        return null;
    }

    private Session getSession(Request request, ConcurrentMap<String, Session> sessions, Timer timer) {
        String UUID = request.getParamValueFromHeader(COOKIE, RequestReader.UUID);
        if (UUID == null) {
            Session session = new Session();
            sessions.putIfAbsent(session.getUuid().toString(), session);
            timer.schedule(session, SESSION_TIMER_DELAY, SESSION_TIMER_PERIOD);
            return session;
        }
        if (sessions.containsKey(UUID)) {
            Session session = sessions.get(UUID);
            if (session.isValid()) {
                session.update();
                return session;
            } else return null;
        }
        return null;
    }

    private String getMethod(String firstLine) {
        return firstLine.split(" ")[HTTP_METHOD_INDEX];
    }

    private String getResource(String pathToResources, String firstLine) {
        return pathToResources + firstLine.split(" ")[HTTP_RESOURCE_INDEX].replace('/', '\\');
    }

    private String getProtocolVersion(String firstLine) {
        return firstLine.split(" ")[HTTP_PROTOCOL_INDEX];
    }

    private void formRequestHeaders(List<String> clientRequestList, Request request) {
        for (int i = 1; i < clientRequestList.size(); i++) {
            String headerLine = clientRequestList.get(i);
            String headerName = headerLine.split(":", 2)[HEADER_NAME_INDEX];
            if (headerLine.split(":", 2).length > 1) {
                String headerValues = headerLine.split(":", 2)[HEADER_VALUE_INDEX];
                for (String headerValue : headerValues.split(";")) {
                    request.setHeader(headerName, headerValue.replaceAll("\\s+", ""));
                }
            }
        }
    }

    private List<String> readRequestLines(BufferedReader in) throws IOException {
        List<String> requestLines = new ArrayList<>();
        String currentString = in.readLine();
        while (!(currentString == null || currentString.isBlank())) {
            requestLines.add(currentString);
            currentString = in.readLine();
        }
        return requestLines;
    }
}
