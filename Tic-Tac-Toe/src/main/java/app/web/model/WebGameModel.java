package app.web.model;

import app.domain.model.FieldTypes;
import app.domain.model.GameState;

import java.util.Date;

public class WebGameModel {
    private WebGameField webGameField;
    private String uuid;
    private GameState gameState;
    private FieldTypes firstPlayerSym = FieldTypes.X;
    private FieldTypes secondPlayerSym = FieldTypes.O;
    private String player1Uuid;
    private String player2Uuid;
    private boolean multiPlayer;
    private Date createDate;

    public WebGameField getWebGameField() {
        return webGameField;
    }

    public void setField(WebGameField webGameField) {
        this.webGameField = webGameField;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        StringBuilder field = new StringBuilder();
        for (char[] line : webGameField.getField()) {
            for (char ceil : line) {
                field.append(ceil).append(" ");
            }
            field.append("\n");
        }
        return field.toString();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public FieldTypes getFirstPlayerSym() {
        return firstPlayerSym;
    }

    public void setFirstPlayerSym(FieldTypes firstPlayerSym) {
        this.firstPlayerSym = firstPlayerSym;
    }

    public FieldTypes getSecondPlayerSym() {
        return secondPlayerSym;
    }

    public void setSecondPlayerSym(FieldTypes secondPlayerSym) {
        this.secondPlayerSym = secondPlayerSym;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
