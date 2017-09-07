package pl.workreporter.web.beans.entities.position;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
public interface PositionDao {
    Position getPositionById(long id);
    List<Position> getAllPositionsInSolution(long solutionId);
    Position addPosition(long solutionId, String name);
    void removePosition(long solutionId, long positionId);
    void removePositions(long solutionId, List<Long> positionsIds);
    Position updatePosition(long positionId, Map<String, String> map);
}
