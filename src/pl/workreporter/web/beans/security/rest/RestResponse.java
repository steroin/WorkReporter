package pl.workreporter.web.beans.security.rest;

import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

/**
 * Created by Sergiusz on 13.09.2017.
 */
public abstract class RestResponse<T> {
    @JsonView(JsonDataView.Common.class)
    protected T response;
    @JsonView(JsonDataView.Common.class)
    protected int responseCode;

    public int getResponseCode() {
        return responseCode;
    }
    public T getResponse() {
        return response;
    }
}
