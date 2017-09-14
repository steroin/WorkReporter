package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 13.09.2017.
 */
public class RestResponseError<T> extends RestResponse<T> {
    private String message;

    public RestResponseError(String message) {
        this.message = message;
        response = null;
    }

    public String getMessage() {
        return message;
    }
}
