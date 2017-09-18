package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 17.09.2017.
 */
public class RestResponseAdditionFailedError<T> extends RestResponseError<T> {
    public RestResponseAdditionFailedError() {
        super("Nie udało się dodać zawartości.");
    }
}
