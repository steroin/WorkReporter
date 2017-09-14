package pl.workreporter.web.controllers.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.position.PositionDao;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@RestController
public class PositionRestController {
    @Autowired
    private PositionDao positionDao;

    @RequestMapping(value = "/solution/positions", method = GET)
    public @ResponseBody
    RestResponse<List<Position>> getAllPositions(@RequestParam("id") long solutionId) {
        List<Position> result = positionDao.getAllPositionsInSolution(solutionId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/positions/{id}", method = DELETE)
    public RestResponse<Void> removePosition(@RequestParam("solutionid") long solutionId, @PathVariable("id") long positionId) {
        positionDao.removePosition(solutionId, positionId);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/positions", method = DELETE)
    public RestResponse<Void> removeSelectedPositions(@RequestParam("solutionid") long solutionId, @RequestParam("positions") List<Long> positions) {
        positionDao.removePositions(solutionId, positions);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/positions/{id}", method = PATCH)
    public RestResponse<Position> updatePosition(@PathVariable("id") long positionId, @RequestBody Map<String, String> map) {
        return new RestResponseSuccess<>(positionDao.updatePosition(positionId, map));
    }

    @RequestMapping(value="/solution/positions", method = POST)
    public RestResponse<Position> addPosition(@RequestBody Map<String, String> position) {
        return new RestResponseSuccess<>(positionDao.addPosition(Long.parseLong(position.get("solutionid")), position.get("name")));
    }
}
