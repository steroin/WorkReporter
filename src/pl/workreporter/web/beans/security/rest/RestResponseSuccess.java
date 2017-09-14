package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 14.09.2017.
 */
public class RestResponseSuccess<T> extends RestResponse<T> {
    public RestResponseSuccess() {
        this.response = null;
    }

    public RestResponseSuccess(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }
}
