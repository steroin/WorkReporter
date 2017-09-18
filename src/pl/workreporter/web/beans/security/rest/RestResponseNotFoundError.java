package pl.workreporter.web.beans.security.rest;

/**
 * Created by Sergiusz on 17.09.2017.
 */
public class RestResponseNotFoundError<T> extends RestResponseError<T> {
    public RestResponseNotFoundError() {
        super("Nie odnaleziono żadnej zawartości.");
    }
}
