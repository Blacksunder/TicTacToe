package app.domain.model;

import java.util.Date;
import java.util.UUID;

public class DomainGameModel {
    private String uuid;
    private DomainGameField field;
    private GameState gameState;
    private FieldTypes firstPlayerSym = FieldTypes.X;
    private FieldTypes secondPlayerSym = FieldTypes.O;
    private String player1Uuid;
    private String player2Uuid;
    private boolean multiPlayer;
    private Date createDate;

    public DomainGameModel() {
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
        field = new DomainGameField();
    }

    public DomainGameField getField() {
        return field;
    }

    public void setField(DomainGameField field) {
        this.field = field;
    }

    public void applyUserTurn(String uuid, int y, int x) {
        gameState = defineGameState();
        if (multiPlayer && checkForCorrectPlayer(uuid) && y >= 0 && y < 3 && x >= 0 && x < 3 &&
                field.getField()[y][x] == FieldTypes.EMPTY) {
            field.getField()[y][x] = definePlayerSym(uuid);
        } else if (checkForCorrectPlayer(uuid) && y >= 0 && y < 3 && x >= 0 && x < 3 &&
                field.getField()[y][x] == FieldTypes.EMPTY) {
           field.getField()[y][x] = FieldTypes.X;
        }
        gameState = defineGameState();
    }

    public DomainGameField applyAiTurn() {
        gameState = defineGameState();
        if (gameState != GameState.PLAYER_2_TURN) {
            return field;
        }
        MiniMaxAlgo algo = new MiniMaxAlgo(field);
        Turn aiTurn = algo.findBestTurn();
        DomainGameField field = new DomainGameField();
        for (int i = 0; i < field.getField().length; ++i) {
            for (int j = 0; j < field.getField()[i].length; ++j) {
                field.getField()[i][j] = this.field.getField()[i][j];
            }
        }
        field.getField()[aiTurn.getY()][aiTurn.getX()] = FieldTypes.O;
        gameState = defineGameState();
        return field;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GameState defineGameState() {
        GameState foundState = GameState.PLAYER_1_TURN;
        boolean found = false;
        for (int i = 0; !found && i < field.getField().length; ++i) {
            if (field.getField()[i][0] == field.getField()[i][1] &&
                    field.getField()[i][1] == field.getField()[i][2]) {
                if (field.getField()[i][0] == firstPlayerSym) {
                    found = true;
                    foundState = GameState.PLAYER_1_VICTORY;
                } else if (field.getField()[i][0] == secondPlayerSym) {
                    found = true;
                    foundState = GameState.PLAYER_2_VICTORY;
                }
            }
            if (field.getField()[0][i] == field.getField()[1][i] &&
                    field.getField()[1][i] == field.getField()[2][i]) {
                if (field.getField()[0][i] == firstPlayerSym) {
                    found = true;
                    foundState = GameState.PLAYER_1_VICTORY;
                } else if (field.getField()[0][i] == secondPlayerSym) {
                    found = true;
                    foundState = GameState.PLAYER_2_VICTORY;
                }
            }
        }
        if (!found && ((field.getField()[0][0] == field.getField()[1][1] &&
                field.getField()[1][1] == field.getField()[2][2]) ||
                (field.getField()[0][2]) == field.getField()[1][1] &&
                field.getField()[1][1] == field.getField()[2][0])) {
            if (field.getField()[1][1] == firstPlayerSym) {
                foundState = GameState.PLAYER_1_VICTORY;
                found = true;
            } else if (field.getField()[1][1] == secondPlayerSym) {
                foundState = GameState.PLAYER_2_VICTORY;
                found = true;
            }
        }
        if (!found && countPlayerCeil(player1Uuid) + countPlayerCeil(player2Uuid) == 9) {
            foundState = GameState.DRAW;
            found = true;
        }
        if (!found && countPlayerCeil(player1Uuid) > countPlayerCeil(player2Uuid)) {
            foundState = GameState.PLAYER_2_TURN;
            found = true;
        }
        if (!found && (player1Uuid == null || player2Uuid == null)) {
            foundState = GameState.PLAYERS_AWAITING;
        }
        return foundState;
    }

    private int countPlayerCeil(String uuid) {
        int amount = 0;
        for (FieldTypes[] line : field.getField()) {
            for (FieldTypes ceil : line) {
                if (ceil == definePlayerSym(uuid)) {
                    ++amount;
                }
            }
        }
        return amount;
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

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    public void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }

    private boolean checkForCorrectPlayer(String uuid) {
        return (uuid.equals(player1Uuid) && gameState == GameState.PLAYER_1_TURN) ||
                (uuid.equals(player2Uuid) && gameState == GameState.PLAYER_2_TURN);
    }

    private FieldTypes definePlayerSym(String uuid) {
        if (uuid.equals(player1Uuid)) {
            return firstPlayerSym;
        } else {
            return secondPlayerSym;
        }
    }

    public void setPlayer1Uuid(String player1Uuid) {
        this.player1Uuid = player1Uuid;
    }

    public String getPlayer1Uuid() {
        return player1Uuid;
    }

    public void setPlayer2Uuid(String player2Uuid) {
        this.player2Uuid = player2Uuid;
    }

    public String getPlayer2Uuid() {
        return player2Uuid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
