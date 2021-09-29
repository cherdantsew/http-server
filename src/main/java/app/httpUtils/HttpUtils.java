package app.httpUtils;

import app.Request;
import app.responses.*;
import app.factories.*;

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtils {

    private static final Logger logger = Logger.getLogger(HttpUtils.class.getName());
    private static final ResponseFactory responseFactory = new ResponseFactory();

    public static Request readRequestFromInputStream(InputStream inputStream, String pathToResources) {
        try {
            List<String> clientRequestList = new ArrayList<>();
            Request request = new Request();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            readRequestLines(clientRequestList, in);
            formRequestStatusLine(clientRequestList, request, pathToResources);
            formRequestHeaders(clientRequestList, request);
            return request;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while reading request from input stream.", e);
        }
        return null;
    }

    public static Response buildResponse(Request request) throws IOException {
        File requestedFile = new File(request.getResource());
        return responseFactory.getResponse(request.getMethod(), requestedFile);
    }

    private static void formRequestStatusLine(List<String> clientRequestList, Request request, String pathToResources) {
        String[] statusLineArray = clientRequestList.get(0).split(" ");
        request.setMethod(statusLineArray[0]);
        request.setResource(pathToResources + statusLineArray[1].replace('/', '\\'));
        request.setProtocolVersion(statusLineArray[2]);
    }

    private static void formRequestHeaders(List<String> clientRequestList, Request request) {
        for (int i = 1; i < clientRequestList.size(); i++) {
            String headerLine = clientRequestList.get(i);
            String headerName = headerLine.split(":", 2)[0];
            if (headerLine.split(":", 2).length > 1) {
                String headerValues = headerLine.split(":", 2)[1];
                for (String headerValue : headerValues.split(";")) {
                    request.setHeader(headerName, headerValue);
                }
            }
        }
    }

    private static void readRequestLines(List<String> clientRequestList, BufferedReader in) throws IOException {
        while (true) {
            String currentString = in.readLine();
            if (currentString == null || currentString.trim().length() == 0) {
                break;
            }
            clientRequestList.add(currentString);
        }
    }

}