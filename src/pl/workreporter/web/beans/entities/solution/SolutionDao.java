package pl.workreporter.web.beans.entities.solution;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 12.08.2017.
 */
public interface SolutionDao {
    Solution loadSolution(long id);
    List<Integer> getSolutionProjects(long id);
    List<Integer> getSolutionTeams(long id);
    List<Integer> getSolutionAdministrators(long id);
    Map<Long, String> getSolutionIdNameMap(long userId);
}
