import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtils {

    private static final Logger logger = Logger.getLogger(HttpUtils.class.getName());

    public static Request readRequestFromInputStream(InputStream inputStream) {
        try {
            List<String> clientRequestList = new ArrayList<>();
            Request request = new Request();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            readRequestLines(clientRequestList, in);
            formRequestStatusLine(clientRequestList, request);
            formRequestHeaders(clientRequestList, request);
            return request;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while reading request from input stream.", e);
        }
        return null;
    }

    public static Response getResponse(Request request, File requestedFile) throws IOException {
        if ("GET".equals(request.getMethod())) {
            if (!requestedFile.isFile()) {
                return formResponse(requestedFile, Response.RC_NOT_FOUND, Response.RM_FILE_NOT_FOUND);
            }
            return formResponse(requestedFile, Response.RC_OK, Response.RM_OK);
        }
        return formResponse(requestedFile, Response.RC_METHOD_NOT_ALLOWED, Response.RM_METHOD_NOT_ALLOWED);
    }

    public static void writeResponse(Response response, OutputStream outputStream) throws IOException {
        outputStream.write(response.getResponse().getBytes());
        outputStream.write(response.getBody());
        outputStream.flush();
    }

    private static void formRequestStatusLine(List<String> clientRequestList, Request request) {
        String[] statusLineArray = clientRequestList.get(0).split(" ");
        request.setMethod(statusLineArray[0]);
        request.setResource(statusLineArray[1]);
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

    private static Response formResponse(File requestedFile, int statusCode, String statusMessage) throws IOException {
        Response response = new Response(Response.HTTP_1_1_PROTOCOL, statusCode, statusMessage);
        //If file not found
        if (statusCode != Response.RC_OK) {
            addBasicHeaders(requestedFile, response);
            response.addBody(Response.RM_404_HTML_TEXT.getBytes());
            return response;
        }
        //If image is requested
        if (requestedFile.getAbsolutePath().endsWith("png")) {
            return handleImageRequest(requestedFile, response);
        }
        //If any other existing file is requested
        response.addBody(Files.readAllBytes(Paths.get(requestedFile.getAbsolutePath())));
        return response;
    }

    private static void addBasicHeaders(File requestedFile, Response response) {
        response.addHeader("Server", "VladServer/08.12.2021");
        response.addHeader("Content-Type", "text/html");
        if (response.getStatusCode() == Response.RC_OK)
            response.addHeader("Content-Length", String.valueOf(requestedFile.length()));
        else if (response.getStatusCode() == Response.RC_NOT_FOUND)
            response.addHeader("Content-Length", String.valueOf(Response.RM_404_HTML_TEXT.length()));
        else response.addHeader("Content-Length", String.valueOf(Response.RM_405_HTML_TEXT.length()));
        response.addHeader("Connection", "close");
        response.addHeader("Allow", "GET");
    }

    private static Response handleImageRequest(File requestedFile, Response response) throws IOException {
        String base64Img = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(requestedFile.getAbsolutePath())));
        String imageHtmlCode = "<img src=\"data:image/png;base64," + base64Img + "\">";
        response.addHeader("Content-Length", String.valueOf(imageHtmlCode.length()));
        response.addBody(imageHtmlCode.getBytes(StandardCharsets.UTF_8));
        return response;
    }
}