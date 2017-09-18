package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 17.09.2017.
 */
public class RestResponseRemovingFailedError<T> extends RestResponseError<T> {
    public RestResponseRemovingFailedError() {
        super("Nie udało się usunąć zawartości.");
    }
}
