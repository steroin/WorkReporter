package pl.workreporter.web.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.workreporter.security.authentication.CompleteUserDetails;

/**
 * Created by Sergiusz on 14.09.2017.
 */
@Service
public class PrincipalAuthenticator {
    public boolean authenticateUserId(Long userId) {
        return userId != null && getPrincipal().getUserId() == userId;
    }

    public boolean authenticateSolutionId(Long solutionId) {
        return solutionId != null && getPrincipal().getSolutionId() == solutionId;
    }

    public boolean authenticateSolutionAdministrator(Long solutionId) {
        return solutionId != null && getPrincipal().getManagedSolutions().contains(solutionId);
    }

    public boolean authenticateTeamAdministrator(Long teamId) {
        return teamId != null && getPrincipal().getManagedTeams().contains(teamId);
    }

    public boolean authenticatePositionId(Long positionId) {
        return positionId != null && positionId.equals(getPrincipal().getPositionId());
    }

    public boolean authenticateTeamId(Long teamId) {
        return teamId != null && teamId.equals(getPrincipal().getTeamId());
    }

    private CompleteUserDetails getPrincipal() {
        return (CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
