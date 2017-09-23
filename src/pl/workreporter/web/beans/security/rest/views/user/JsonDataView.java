package pl.workreporter.web.beans.security.rest.views.user;

/**
 * Created by Sergiusz on 20.09.2017.
 */
public interface JsonDataView {
    interface Common {}
    interface User extends Common {}
    interface Myself extends User {}
    interface SolutionManager extends Myself {}
}
