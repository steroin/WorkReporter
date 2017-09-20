package pl.workreporter.web.beans.security.rest;

import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.security.rest.views.user.JsonCommonView;

/**
 * Created by Sergiusz on 13.09.2017.
 */
public abstract class RestResponse<T> {
    @JsonView(JsonCommonView.class)
    protected T response;
    @JsonView(JsonCommonView.class)
    protected int responseCode;

    public int getResponseCode() {
        return responseCode;
    }
}
