package pl.workreporter.web.beans.entities.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 20.09.2017.
 */
@Repository
public class PositionDaoWrapper {
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<Position> getPositionById(long id) {

        Position position;
        try {
            position = positionDao.getPositionById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateSolutionAdministrator(position.getSolution().getId()) ||
                authenticator.authenticatePositionId(position.getId())) {
            return new RestResponseSuccess<>(position);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<Position>> getAllPositionsInSolution(long solutionId) {

        try {
            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(positionDao.getAllPositionsInSolution(solutionId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Position> addPosition(long solutionId, String name) {
        try {
            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(positionDao.addPosition(solutionId, name));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseAdditionFailedError<>();
        }
    }

    public RestResponse<Void> removePosition(long positionId) {
        try {
            Position position = positionDao.getPositionById(positionId);
            if (authenticator.authenticateSolutionAdministrator(position.getSolution().getId())) {
                positionDao.removePosition(positionId);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseRemovingFailedError<>();
        }
    }

    public RestResponse<Void> removePositions(List<Long> positionsIds) {

        List<Position> positions;

        try {
            positions = positionDao.getPositions(positionsIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (Position position : positions) {
            if (!authenticator.authenticateSolutionAdministrator(position.getSolution().getId())) {
                authenticated = false;
            }
        }
        if (!authenticated) {
            return new RestResponseAuthenticationError<>();
        }
        try {
            positionDao.removePositions(positionsIds);
            return new RestResponseSuccess<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }
    public RestResponse<Position> updatePosition(long positionId, Map<String, String> map) {
        try {
            Position position = positionDao.getPositionById(positionId);
            if (authenticator.authenticateSolutionAdministrator(position.getSolution().getId())) {
                return new RestResponseSuccess<>(positionDao.updatePosition(positionId, map));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            return new RestResponseUpdateFailedError<>();
        }
    }
}
