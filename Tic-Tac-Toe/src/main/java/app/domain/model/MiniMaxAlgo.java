package app.domain.model;

import java.util.ArrayList;

public class MiniMaxAlgo {
    private ArrayList<Turn> possibleTurns;
    private DomainGameField fieldCopy;

    public MiniMaxAlgo(DomainGameField field) {
        fieldCopy = new DomainGameField();
        for (int i = 0; i < fieldCopy.getField().length; ++i) {
            for (int j = 0; j < fieldCopy.getField()[i].length; ++j) {
                fieldCopy.getField()[i][j] = field.getField()[i][j];
            }
        }
        possibleTurns = new ArrayList<>();
    }

    public Turn findBestTurn() {
        return recursiveAlgo(1);
    }

    private Turn recursiveAlgo(int recursiveNumber) {
        DomainGameField copy = new DomainGameField();
        for (int i = 0; i < copy.getField().length; ++i) {
            for (int j = 0; j < copy.getField()[i].length; ++j) {
                copy.getField()[i][j] = fieldCopy.getField()[i][j];
            }
        }
        boolean gameOver = false;
        switch (checkGameState(copy)) {
            case AI_VICTORY -> {
                return new Turn(0, 0, 10 - recursiveNumber);
            } case PLAYER_1_VICTORY -> {
                return new Turn(0, 0, -10 + recursiveNumber);
            } case DRAW -> {
                return new Turn(0, 0, 0);
            }
            default -> {}
        }
        for (int i = 0; !gameOver && i < copy.getField().length; ++i) {
            for (int j = 0; j < copy.getField()[i].length; ++j) {
                if (copy.getField()[i][j] == FieldTypes.EMPTY) {
                    Turn turn = new Turn(i, j, 0);
                    copy.getField()[i][j] = defineCurrentSign(copy);
                    MiniMaxAlgo algo = new MiniMaxAlgo(copy);
                    turn.changeValue(algo.recursiveAlgo(recursiveNumber + 1).getValue());
                    copy.getField()[i][j] = FieldTypes.EMPTY;
                    possibleTurns.add(turn);
                }
            }
        }
        Turn bestTurn = possibleTurns.get(0);
        if (defineCurrentSign(copy) == FieldTypes.O) {
            for (Turn turn : possibleTurns) {
                if (turn != null && turn.getValue() > bestTurn.getValue()) {
                    bestTurn = turn;
                }
            }
        } else {
            for (Turn turn : possibleTurns) {
                if (turn != null && turn.getValue() < bestTurn.getValue()) {
                    bestTurn = turn;
                }
            }
        }
        return bestTurn;
    }

    private GameState checkGameState(DomainGameField field) {
        boolean found = false;
        GameState foundState = GameState.PLAYER_2_TURN;
        FieldTypes[][] gameField = field.getField();
        for (int i = 0; !found && i < field.getField().length; ++i) {
            if (gameField[i][0] == gameField[i][1] && gameField[i][1] == gameField[i][2]) {
                if (gameField[i][0] == FieldTypes.O) {
                    found = true;
                    foundState = GameState.AI_VICTORY;
                } else if (gameField[i][0] == FieldTypes.X) {
                    found = true;
                    foundState = GameState.PLAYER_1_VICTORY;
                }
            }
            if (!found && gameField[0][i] == gameField[1][i] && gameField[1][i] == gameField[2][i]) {
                if (gameField[0][i] == FieldTypes.O) {
                    found = true;
                    foundState = GameState.AI_VICTORY;
                } else if (gameField[0][i] == FieldTypes.X) {
                    found = true;
                    foundState = GameState.PLAYER_1_VICTORY;
                }
            }
        }
        if (!found && ((gameField[0][0] == gameField[1][1] && gameField[1][1] == gameField[2][2]) ||
                (gameField[0][2] == gameField[1][1] && gameField[1][1] == gameField[2][0]))) {
            if (gameField[1][1] == FieldTypes.O) {
                found = true;
                foundState = GameState.AI_VICTORY;
            } else if (gameField[1][1] == FieldTypes.X) {
                found = true;
                foundState = GameState.PLAYER_1_VICTORY;
            }
        }
        if (!found) {
            int empty = 0;
            for (FieldTypes[] line : gameField) {
                for (FieldTypes ceil : line) {
                    if (ceil == FieldTypes.EMPTY) {
                        ++empty;
                    }
                }
            }
            if (empty == 0) {
                foundState = GameState.DRAW;
            }
        }
        return foundState;
    }

    private FieldTypes defineCurrentSign(DomainGameField field) {
        int x = 0;
        int o = 0;
        for (FieldTypes[] line : field.getField()) {
            for (FieldTypes ceil : line) {
                if (ceil == FieldTypes.X) {
                    ++x;
                } else if (ceil == FieldTypes.O) {
                    ++o;
                }
            }
        }
        return x > o ? FieldTypes.O : FieldTypes.X;
    }

}
