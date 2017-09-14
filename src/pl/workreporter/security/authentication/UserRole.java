package pl.workreporter.security.authentication;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Sergiusz on 10.08.2017.
 */
public class UserRole implements GrantedAuthority {
    private String roleName;

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}
