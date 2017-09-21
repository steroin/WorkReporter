package pl.workreporter.web.beans.entities.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

/**
 * Created by Sergiusz on 21.09.2017.
 */
@Repository
public class SolutionDaoWrapper {
    @Autowired
    private SolutionDao solutionDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<Solution> loadSolution(long id) {
        Solution solution;

        try {
            solution = solutionDao.loadSolution(id);
            if (authenticator.authenticateSolutionId(id)) {
                return new RestResponseSuccess<>(solution);
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Solution> updateSolution(Solution solution) {
        try {
            if (authenticator.authenticateSolutionAdministrator(solution.getId())) {
                return new RestResponseSuccess<>(solutionDao.updateSolution(solution));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseUpdateFailedError<>();
        }
    }
}
