package pl.workreporter.web.beans.entities.team;

import java.util.List;

/**
 * Created by Sergiusz on 19.08.2017.
 */

public interface TeamDao {
    Team getTeamById(long id);
    List<Team> getAllTeamsInSolution(long solutionId);
    Team addTeam(long solutionId, String name, Long leaderId);
    void removeTeam(long solutionId, long teamId);
    void removeTeams(long solutionId, List<Long> teamIds);
    void updateTeam(Team team);
}
