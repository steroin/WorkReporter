package pl.workreporter.web.beans.entities.solution;

import java.util.List;

/**
 * Created by Sergiusz on 12.08.2017.
 */
public interface SolutionDao {
    Solution loadSolution(int id);
    List<Integer> getSolutionProjects(int id);
    List<Integer> getSolutionTeams(int id);
    List<Integer> getSolutionAdministrators(int id);
}
