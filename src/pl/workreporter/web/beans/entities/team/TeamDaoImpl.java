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
        String query = "select t.id, t.name, t.solutionid, t.leaderid, t.creation_date, t.last_edition_date, pd.firstname, pd.lastname, ac.login\n" +
                "from team t \n" +
                "  left join appuser au on t.leaderid = au.id \n" +
                "  left join personal_data pd on au.personaldataid=pd.id\n" +
                "  left join account ac on au.accountid = ac.id " +
                "where t.id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        String leaderName = "";
        if (result.get("firstname") != null && result.get("lastname") != null && result.get("login") != null) {
            leaderName = result.get("firstname").toString()+" "+result.get("lastname").toString()+" ("+result.get("login").toString()+")";
        }
        Team team = new Team();
        team.setId(Long.parseLong(result.get("id").toString()));
        team.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        team.setLeaderId(result.get("leaderid") == null ? null : Long.parseLong(result.get("leaderid").toString()));
        team.setLeaderName(leaderName);
        team.setName(result.get("name").toString());
        team.setCreationDate(result.get("creation_date").toString());
        team.setLastEditionDate(result.get("last_edition_date").toString());
        return team;
    }

    @Override
    public List<Team> getAllTeamsInSolution(long solutionId) {
        String query = "select t.id, t.name, t.solutionid, t.leaderid, t.creation_date, t.last_edition_date, pd.firstname, pd.lastname, ac.login\n" +
                "from team t \n" +
                "  left join appuser au on t.leaderid = au.id \n" +
                "  left join personal_data pd on au.personaldataid=pd.id\n" +
                "  left join account ac on au.accountid = ac.id " +
                "where t.solutionid="+solutionId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Team> teams = new ArrayList<>();

        for (Map<String, Object> map : result) {
            String leaderName = "";
            if (map.get("firstname") != null && map.get("lastname") != null && map.get("login") != null) {
                leaderName = map.get("firstname").toString()+" "+map.get("lastname").toString()+" ("+map.get("login").toString()+")";
            }
            Team team = new Team();
            long id = Long.parseLong(map.get("id").toString());
            team.setId(id);
            team.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            team.setLeaderId(map.get("leaderid") == null ? null : Long.parseLong(map.get("leaderid").toString()));
            team.setLeaderName(leaderName);
            team.setName(map.get("name").toString());
            team.setCreationDate(map.get("creation_date").toString());
            team.setLastEditionDate(map.get("last_edition_date").toString());
            teams.add(team);
        }
        return teams;
    }

    @Override
    public Team addTeam(long solutionId, String name, Long leaderId) {
        String query = "select teamseq.nextval from dual";
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long id = Long.parseLong(result.get("nextval").toString());

        query = "insert into team(id, solutionid, leaderid, name, creation_date, last_edition_date) values(?, ?, ?, ?, sysdate, sydate)";
        jdbcTemplate.update(query, id, solutionId, leaderId, name);
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
        String query = "update team set solutionid=?, leaderid=?, name=?, creation_date=to_timestamp(?, ?), last_edition_date=sysdate where id=?";
        jdbcTemplate.update(query, team.getSolutionId(), team.getLeaderId(), team.getName(), team.getCreationDate(), dateFormat, team.getId());
    }
}
