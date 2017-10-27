package pl.workreporter.web.beans.entities.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import pl.workreporter.security.authentication.CompleteUserDetails;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseAuthenticationError;
import pl.workreporter.web.beans.security.rest.RestResponseNotFoundError;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.Date;

/**
 * Created by Sergiusz on 27.10.2017.
 */
@Repository
public class ReportDaoWrapper {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<Report> generateReport(ReportObject object, ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to) {
        Report report;
        Long solutionId = ((CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSolutionId();

        if (!authenticator.authenticateSolutionAdministrator(solutionId)) {
            return new RestResponseAuthenticationError<>();
        }

        try {
            report = reportDao.generateReport(object, sortBy, sortType, from, to, solutionId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        return new RestResponseSuccess<>(report);
    }

}
