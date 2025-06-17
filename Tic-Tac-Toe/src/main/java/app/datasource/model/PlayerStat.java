package app.datasource.model;

public class PlayerStat {
    private String userId;
    private String login;
    private double victoryPercent;

    public PlayerStat(String userId, String login, double victoryPercent) {
        this.userId = userId;
        this.login = login;
        this.victoryPercent = victoryPercent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getVictoryPercent() {
        return victoryPercent;
    }

    public void setVictoryPercent(double victoryPercent) {
        this.victoryPercent = victoryPercent;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
