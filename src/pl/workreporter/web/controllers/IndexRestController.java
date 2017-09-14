package pl.workreporter.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Sergiusz on 22.08.2017.
 */
@RestController
public class IndexRestController {
    @RequestMapping(value = "/empty", method = GET)
    public @ResponseBody
    RestResponse<List<Long>> emptyRequest() {
        return new RestResponseSuccess<>(new ArrayList<>());
    }

    @RequestMapping(value = "/currentdate", method = GET, produces="text/plain")
    @ResponseBody
    public RestResponse<String> getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return new RestResponseSuccess<>(sdf.format(new Date()));
    }
}
