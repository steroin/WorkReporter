package pl.workreporter.web.beans.entities.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Repository
public class TeamDaoImpl implements TeamDao{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Team getTeamById(long id) {
        String query = "select * from team where id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        Team team = new Team();
        team.setId(Long.parseLong(result.get("id").toString()));
        team.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        team.setName(result.get("name").toString());
        team.setCreationDate(result.get("creation_date").toString());
        team.setLastEditionDate(result.get("last_edition_date").toString());

        query = "select userid from team_administrator where teamid="+id;
        List<Map<String, Object>> adminsResult = jdbcTemplate.queryForList(query);
        List<Long> admins = new ArrayList<>();

        for (Map<String, Object> map : adminsResult) {
            admins.add(Long.parseLong(map.get("userid").toString()));
        }
        team.setTeamAdministrators(admins);
        return team;
    }

    @Override
    public List<Team> getAllTeamsInSolution(long solutionId) {
        String query = "select ta.userid as userid, ta.teamid as teamid from team_administrator ta join appuser au on ta.userid=au.id where au.solutionid="+solutionId;
        List<Map<String, Object>> adminsResult = jdbcTemplate.queryForList(query);
        Map<Long, List<Long>> adminsByTeam = new HashMap<Long, List<Long>>();

        for (Map<String, Object> map : adminsResult) {
            long teamId = Long.parseLong(map.get("teamid").toString());
            long userId = Long.parseLong(map.get("userid").toString());

            if (!adminsByTeam.containsKey(teamId)) {
                adminsByTeam.put(teamId, new ArrayList<>());
            }
            adminsByTeam.get(teamId).add(userId);
        }
        List<Team> teams = new ArrayList<>();

        query = "select * from team where solutionid="+solutionId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            Team team = new Team();
            long id = Long.parseLong(map.get("id").toString());
            team.setId(id);
            team.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            team.setName(map.get("name").toString());
            team.setCreationDate(map.get("creation_date").toString());
            team.setLastEditionDate(map.get("last_edition_date").toString());
            team.setTeamAdministrators(adminsByTeam.get(id));
            teams.add(team);
        }
        return teams;
    }

    @Override
    public Team addTeam(long solutionId, String name, List<Long> admins) {
        String query = "select teamseq.nextval from dual";
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long id = Long.parseLong(result.get("nextval").toString());

        query = "insert into team(id, solutionid, name, creation_date, last_edition_date) values(?, ?, ?, sysdate, sydate)";
        jdbcTemplate.update(query, id, solutionId, name);

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN \n");
        for (long adminId : admins) {
            queryBuilder.append("insert into team_administrator(id, teamid, userid) values(teamadministratorseq.nextval, "+id+", "+adminId+"); \n");
        }
        jdbcTemplate.execute(queryBuilder.toString());
        return getTeamById(id);
    }

    @Override
    public void removeTeam(long solutionId, long teamId) {
        String query = "delete from team where id="+teamId+" and solutionid="+solutionId;
        jdbcTemplate.execute(query);
    }

    @Override
    public void removeTeams(long solutionId, List<Long> teamIds) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from team where solutionid="+solutionId);
        queryBuilder.append(" and (");
        for (int i = 0; i < teamIds.size(); i++) {
            queryBuilder.append("id="+teamIds.get(i));
            if (i < teamIds.size() - 1) {
                queryBuilder.append(" or ");
            }
        }
        queryBuilder.append(")");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void updateTeam(Team team) {
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";
        String query = "select userid from team_administrator where teamid="+team.getId();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Long> adminsToRemove = new ArrayList<>();
        List<Long> adminsToAdd = new ArrayList<>();
        List<Long> oldAdmins = new ArrayList<>();
        List<Long> newAdmins = team.getTeamAdministrators();

        for (Map<String, Object> map : result) {
            long adminId = Long.parseLong(map.get("userid").toString());
            oldAdmins.add(adminId);
            if (!newAdmins.contains(adminId)) {
                adminsToRemove.add(adminId);
            }
        }

        for (long id : newAdmins) {
            if (!oldAdmins.contains(id)) {
                adminsToAdd.add(id);
            }
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN \n");

        for (long id : adminsToRemove) {
            queryBuilder.append("delete from team_administrator where teamid="+team.getId()+" and userid"+id+"; \n");
        }
        for (long id : adminsToAdd) {
            queryBuilder.append("insert into team(id, teamid, userid) values(teamadministratorseq.nextval, "+team.getId()+", "+id+"); \n");
        }
        queryBuilder.append("END;");
        jdbcTemplate.execute(queryBuilder.toString());
        query = "update team set solutionid=?, name=?, creation_date=to_timestamp(?, ?), last_edition_date=sysdate where id=?";
        jdbcTemplate.update(query, team.getSolutionId(), team.getName(), team.getCreationDate(), dateFormat, team.getId());
    }
}
