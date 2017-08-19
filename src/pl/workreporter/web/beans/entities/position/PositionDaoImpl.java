package pl.workreporter.web.beans.entities.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
public class PositionDaoImpl implements PositionDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Position getPositionById(long id) {
        String query = "select * from position where id="+id;
        SimpleDateFormat srcSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        SimpleDateFormat destSdf = new SimpleDateFormat("dd.MM.yyyy");
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        Position position = new Position();
        position.setId(Long.parseLong(result.get("id").toString()));
        position.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        position.setName(result.get("name").toString());
        try {
            position.setCreationDate(destSdf.format(srcSdf.parse(result.get("creation_date").toString())));
            position.setLastEditionDate(destSdf.format(srcSdf.parse(result.get("last_edition_date").toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return position;
    }

    @Override
    public List<Position> getAllPositionsInSolution(long solutionId) {
        String query = "select * from position where solutionid="+solutionId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Position> positions = new ArrayList<>();

        for (Map<String, Object> map : result) {
            Position position = new Position();
            position.setId(Long.parseLong(map.get("id").toString()));
            position.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            position.setName(map.get("name").toString());
            position.setCreationDate(map.get("creation_date").toString());
            position.setLastEditionDate(map.get("last_edition_date").toString());
            positions.add(position);
        }
        return positions;
    }

    @Override
    public Position addPosition(long solutionId, String name) {
        String query = "select positionseq.nextval from dual";
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long id = Long.parseLong(result.get("nextval").toString());

        query = "insert into position(id, solutionid, name, creation_date, last_edition_date) values (?, ?, ?, sysdate, sysdate)";
        jdbcTemplate.update(query, id, solutionId, name);
        query = "select * from position where id = ?";
        result = jdbcTemplate.queryForMap(query, id);
        Position position = new Position();
        position.setId(Long.parseLong(result.get("id").toString()));
        position.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        position.setName(result.get("name").toString());
        position.setCreationDate(result.get("creation_date").toString());
        position.setLastEditionDate(result.get("last_edition_date").toString());
        return position;
    }

    @Override
    public void removePosition(long solutionId, long positionId) {
        String query = "delete from position where id="+positionId+" and solutionid="+solutionId;
        jdbcTemplate.execute(query);
    }

    @Override
    public void removePositions(long solutionId, List<Long> positionsIds) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from position where solutionid="+solutionId);
        queryBuilder.append(" and (");
        for (int i = 0; i < positionsIds.size(); i++) {
            queryBuilder.append("id="+positionsIds.get(i));
            if (i < positionsIds.size() - 1) {
                queryBuilder.append(" or ");
            }
        }
        queryBuilder.append(")");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void updatePosition(Position position) {
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";
        String query = "update position " +
                "set solutionid = ?, name = ?, creation_date = to_timestamp(?, ?), last_edition_date = sysdate " +
                "where id= ?";
        jdbcTemplate.update(query, position.getSolutionId(), position.getName(), position.getCreationDate(),
                dateFormat, position.getId());
    }
}
