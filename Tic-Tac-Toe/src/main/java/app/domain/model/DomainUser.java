package app.domain.model;

import app.web.security.Role;

import java.util.ArrayList;
import java.util.List;

public class DomainUser {
    private String uuid;
    private String password;
    private String login;
    private final List<Role> roles = new ArrayList<>();

    public DomainUser(String uuid, String password, String login) {
        this.login = login;
        this.uuid = uuid;
        this.password = password;
        roles.add(Role.ROLE_USER);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
