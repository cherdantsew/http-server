package app.factories;

import app.responses.*;
import java.io.File;
import app.httpUtils.*;
public class ResponseFactory {
    public Response getResponse(String method, File file) {
        if (!HttpStatusLineUtils.METHOD_GET.equals(method)) {
            return new ResponseError(HttpStatusLineUtils.HTTP_1_1_PROTOCOL, HttpStatusLineUtils.RC_METHOD_NOT_ALLOWED, HttpStatusLineUtils.RM_METHOD_NOT_ALLOWED, HttpErrorResponse.RM_405_HTML_TEXT);
        }
        if (file.isFile()) {
            return new ResponseSuccess(HttpStatusLineUtils.HTTP_1_1_PROTOCOL, HttpStatusLineUtils.RC_OK, HttpStatusLineUtils.RM_OK, file);
        } else {
            return new ResponseError(HttpStatusLineUtils.HTTP_1_1_PROTOCOL, HttpStatusLineUtils.RC_NOT_FOUND, HttpStatusLineUtils.RM_FILE_NOT_FOUND, HttpErrorResponse.RM_404_HTML_TEXT);
        }
    }
}
