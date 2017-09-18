package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 17.09.2017.
 */
public class RestResponseUpdateFailedError<T> extends RestResponseError<T> {
    public RestResponseUpdateFailedError() {
        super("Nie udało się edytować zawartości.");
    }
}
