package pl.workreporter.web.beans.entities.reports;

import java.util.Date;

/**
 * Created by Sergiusz on 27.10.2017.
 */
public interface ReportDao {
    Report generateReport(ReportObject object, ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId);
}
