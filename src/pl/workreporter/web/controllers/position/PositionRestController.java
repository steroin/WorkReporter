package pl.workreporter.web.controllers.position;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.position.PositionDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@RestController
public class PositionRestController {
    @Autowired
    private PositionDaoWrapper positionDaoWrapper;

    @JsonView(JsonDataView.SolutionManager.class)
    @RequestMapping(value = "/solution/positions", method = GET)
    public @ResponseBody
    RestResponse<List<Position>> getAllPositions(@RequestParam("id") long solutionId) {
        return positionDaoWrapper.getAllPositionsInSolution(solutionId);
    }

    @RequestMapping(value = "/solution/positions/{id}", method = DELETE)
    public RestResponse<Void> removePosition(@PathVariable("id") long positionId) {
        return positionDaoWrapper.removePosition(positionId);
    }

    @RequestMapping(value = "/solution/positions", method = DELETE)
    public RestResponse<Void> removeSelectedPositions(@RequestParam("positions") List<Long> positions) {
        return positionDaoWrapper.removePositions(positions);
    }

    @RequestMapping(value = "/solution/positions/{id}", method = PATCH)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Position> updatePosition(@PathVariable("id") long positionId, @RequestBody Map<String, String> map) {
        return positionDaoWrapper.updatePosition(positionId, map);
    }

    @RequestMapping(value="/solution/positions", method = POST)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Position> addPosition(@RequestBody Map<String, String> position) {
        return positionDaoWrapper.addPosition(Long.parseLong(position.get("solutionid")), position.get("name"));
    }
}
