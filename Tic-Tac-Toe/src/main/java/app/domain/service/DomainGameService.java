package app.domain.service;

import app.domain.model.DomainGameField;
import app.domain.model.DomainGameModel;
import app.domain.model.FieldTypes;
import app.domain.model.GameState;
import org.springframework.stereotype.Service;

@Service
public class DomainGameService {
    private DomainGameModel gameModel;

    public DomainGameService() {
        gameModel = new DomainGameModel();
    }

    public void setGameModel(DomainGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public DomainGameModel getGameModel() {
        return gameModel;
    }

    public DomainGameField getNextAiTurn() {
        return gameModel.applyAiTurn();
    }

    public boolean validateField(DomainGameField newField) {
        int i = 0;
        int x = 0;
        int o = 0;
        for (FieldTypes[] line : gameModel.getField().getField()) {
            int j = 0;
            for (FieldTypes ceil : line) {
                if (ceil != FieldTypes.EMPTY && ceil != newField.getField()[i][j]) {
                    return false;
                } else if (newField.getField()[i][j] == FieldTypes.X) {
                    ++x;
                } else if (newField.getField()[i][j] == FieldTypes.O) {
                    ++o;
                }
                ++j;
            }
            ++i;
        }
        if (o > x || x - o > 1) {
            return false;
        }
        gameModel.setField(newField);
        return true;
    }

    public boolean isGameOver() {
        GameState currentState = gameModel.defineGameState();
        return currentState != GameState.PLAYER_1_TURN &&
                currentState != GameState.PLAYERS_AWAITING &&
                currentState != GameState.PLAYER_2_TURN;
    }

    public void applyUserTurn(String uuid, int y, int x) {
        gameModel.applyUserTurn(uuid, y, x);
    }

    public void updateGameState() {
        gameModel.setGameState(gameModel.defineGameState());
    }
}
