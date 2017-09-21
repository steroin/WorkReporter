package pl.workreporter.web.beans.entities.team;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */

public interface TeamDao {
    Team getTeamById(long id);
    List<Team> getAllTeamsInSolution(long solutionId);
    List<Team> getAllTeamsManagedBy(long userId);
    List<Team> getTeams(List<Long> teamsIds);
    Team addTeam(long solutionId, String name, Long leaderId);
    void removeTeam(long solutionId, long teamId);
    void removeTeams(long solutionId, List<Long> teamIds);
    Team updateTeam(long teamId, Map<String, String> map);
}
