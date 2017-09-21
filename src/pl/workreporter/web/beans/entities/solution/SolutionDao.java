package pl.workreporter.web.beans.entities.solution;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 12.08.2017.
 */
public interface SolutionDao {
    Solution loadSolution(long id);
    Solution updateSolution(Solution solution);
}
