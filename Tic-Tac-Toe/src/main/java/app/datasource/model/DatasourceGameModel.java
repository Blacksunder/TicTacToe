package app.datasource.model;

import app.domain.model.GameState;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table (name = "games")
public class DatasourceGameModel {
    @Column (name = "field")
    private DatasourceGameField dataField;

    @Id
    @Column (name = "uuid", length = 50, unique = true)
    private String uuid;

    @Column (name = "game_state")
    private GameState gameState;

    @Column (name = "player1_uuid")
    private String player1Uuid;

    @Column (name = "player2_uuid")
    private String player2Uuid;

    @Column (name = "is_online")
    private boolean multiPlayer;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private final Date createDate;

    public DatasourceGameModel() {
        dataField = new DatasourceGameField();
        uuid = UUID.randomUUID().toString();
        createDate = new Date();
    }

    public DatasourceGameField getDataField() {
        return dataField;
    }

    public void setDataField(DatasourceGameField dataField) {
        this.dataField = dataField;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getPlayer1Uuid() {
        return player1Uuid;
    }

    public void setPlayer1Uuid(String player1Uuid) {
        this.player1Uuid = player1Uuid;
    }

    public String getPlayer2Uuid() {
        return player2Uuid;
    }

    public void setPlayer2Uuid(String player2Uuid) {
        this.player2Uuid = player2Uuid;
    }

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    public void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }

    public boolean gameIsAvailable() {
        return multiPlayer && gameState == GameState.PLAYERS_AWAITING;
    }

    public boolean gameIsOver() {
        return gameState == GameState.PLAYER_1_VICTORY || gameState == GameState.PLAYER_2_VICTORY ||
                gameState == GameState.DRAW;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
