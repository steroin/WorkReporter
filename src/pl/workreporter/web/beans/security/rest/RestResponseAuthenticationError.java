package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 14.09.2017.
 */
public class RestResponseAuthenticationError<T> extends RestResponseError<T> {
    public RestResponseAuthenticationError() {
        super("Nie posiadasz uprawnień do wykonania tej operacji.");
    }
}
