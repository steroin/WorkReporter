package pl.workreporter.web.controllers.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.entities.reports.*;
import pl.workreporter.web.beans.security.rest.RestResponse;

import java.util.Date;

/**
 * Created by Sergiusz on 27.10.2017.
 */
@RestController
public class ReportsRestController {

    @Autowired
    private ReportDaoWrapper reportDaoWrapper;

    @RequestMapping(value = "/solution/reports/{object}/{sortBy}/{sortingType}/{dateFrom}/{dateTo}")
    public RestResponse<Report> getReport(@PathVariable("object") int object,
                                          @PathVariable("sortBy") int sortBy,
                                          @PathVariable("sortingType") int sortingType,
                                          @PathVariable("dateFrom") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateFrom,
                                          @PathVariable("dateTo") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateTo) {
        ReportObject reportObject = ReportObject.fromValue(object);
        ReportSortingObject sortingObject = ReportSortingObject.fromTypeAndValue(reportObject, sortBy);
        ReportSortingType sortType = ReportSortingType.fromValue(sortingType);
        return reportDaoWrapper.generateReport(reportObject, sortingObject, sortType, dateFrom, dateTo);
    }
}
