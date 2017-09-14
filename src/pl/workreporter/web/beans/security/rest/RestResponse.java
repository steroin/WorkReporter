package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 13.09.2017.
 */
public abstract class RestResponse<T> {
    protected T response;
    protected int responseCode;

    public int getResponseCode() {
        return responseCode;
    }
}
