package app.datasource.model;

import app.web.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "users")
public class DatasourceUser {
    @Id
    @Column(name = "uuid", length = 50)
    private String uuid;

    @Column(name = "passw", length = 50)
    private String password;

    @Column(name = "login", length = 50, unique = true)
    private String login;

    @Column(name = "role")
    private final List<Role> roles = new ArrayList<>();

    public DatasourceUser() {}

    public DatasourceUser(String uuid, String password, String login) {
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
