import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class HttpUtils {
    public static Request readRequestFromInputStream(InputStream inputStream) {
        try {
            ArrayList<String> clientRequestList = new ArrayList<>();
            Request request = new Request();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String currentString = in.readLine();
                if (currentString == null || currentString.trim().length() == 0) {
                    System.err.println("have read all request");
                    break;
                }
                clientRequestList.add(currentString);
            }
            formRequestStatusLine(clientRequestList, request);
            formRequestHeaders(clientRequestList, request);
            return request;
        } catch (Exception e) {
            System.err.println("Другая ошибка чтения входных параметров от клиента");
            e.printStackTrace();
        }
        return null;
    }

    public static void writeResponse(Response response, OutputStream outputStream) throws IOException {
        outputStream.write(response.getResponse().getBytes());
        outputStream.write(response.getBody());
        outputStream.flush();
    }

    public static Response getResponse(Request request, File requestedFile) throws IOException {
        if ("GET".equals(request.getMethod())) {
            if (!requestedFile.isFile()) {
                requestedFile = new File(ResourcesRepository.C_RESOURCES + ResourcesRepository.ERROR_404_PAGE_ADDRESS);
                return form404response(request, requestedFile);
            }
            return form200Response(request, requestedFile);
        } else return form405response(request);
    }

    private static void formRequestStatusLine(ArrayList<String> clientRequestList, Request request) {
        String[] statusLineArray = clientRequestList.get(0).split(" ");
        request.setMethod(statusLineArray[0]);
        request.setResource(statusLineArray[1]);
        request.setProtocolVersion(statusLineArray[2]);
    }

    private static void formRequestHeaders(ArrayList<String> clientRequestList, Request request) {
        for (int i = 1; i < clientRequestList.size(); i++) {
            String headerLine = clientRequestList.get(i);
            String headerName = headerLine.split(":", 2)[0];
            String headerValues;
            if (headerLine.split(":", 2).length > 1) {
                headerValues = headerLine.split(":", 2)[1];
                for (String headerValue : headerValues.split(";")) {
                    request.setHeader(headerName, headerValue);
                }
            }
        }
    }

    private static Response form200Response(Request request, File requestedFile) throws IOException {
        Response response = new Response();
        response.setProtocolVersion(request.getProtocolVersion());
        response.setStatusCode(Response.RC_OK);
        response.setStatusMessage(Response.RM_OK);
        response.addHeader("Server", "VladServer/08.12.2021");
        response.addHeader("Content-Type", "text/html");
        response.addHeader("Connection", "close");
        if (requestedFile.getAbsolutePath().endsWith("png")) {
            String base64Img = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(requestedFile.getAbsolutePath())));
            String imageHtmlCode = "<img src=\"data:image/png;base64," + base64Img + "\">";
            response.addHeader("Content-Length", String.valueOf(imageHtmlCode.length()));
            response.addBody(imageHtmlCode.getBytes(StandardCharsets.UTF_8));
            return response;
        }
        response.addHeader("Content-Length", String.valueOf(requestedFile.length()));

        response.addBody(Files.readAllBytes(Paths.get(requestedFile.getAbsolutePath())));
        return response;
    }

    private static Response form404response(Request request, File requestedFile) throws IOException {
        Response response = new Response();
        response.setProtocolVersion(request.getProtocolVersion());
        response.setStatusCode(Response.RC_NOT_FOUND);
        response.setStatusMessage(Response.RM_FILE_NOT_FOUND);
        response.addHeader("Server", "VladServer/08.12.2021");
        response.addHeader("Content-Type", "text/html");
        response.addHeader("Content-Length", String.valueOf(requestedFile.length()));
        response.addHeader("Connection", "close");
        response.addBody(Files.readAllBytes(Paths.get(requestedFile.getAbsolutePath())));
        return response;
    }

    private static Response form405response(Request request) {
        Response response = new Response();
        String body405error = "<html><body><h1>My server doesnt handle methods other then GET</h1></body></html>";
        response.setProtocolVersion(request.getProtocolVersion());
        response.setStatusCode(Response.RC_METHOD_NOT_ALLOWED);
        response.setStatusMessage(Response.RM_METHOD_NOT_ALLOWED);
        response.addHeader("Server", "VladServer/08.12.2021");
        response.addHeader("Content-Type", "text/html");
        response.addHeader("Content-Length", String.valueOf(body405error.length()));
        response.addHeader("Connection", "close");
        response.addHeader("Allow", "GET");
        response.addBody(body405error.getBytes());
        return response;
    }
}
