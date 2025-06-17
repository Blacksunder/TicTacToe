package app.web.mapper;

import app.domain.model.DomainGameField;
import app.domain.model.DomainGameModel;
import app.domain.model.DomainUser;
import app.domain.model.FieldTypes;
import app.web.model.WebGameField;
import app.web.model.WebGameModel;
import app.web.model.WebUser;

public class MapperWebToDomain {
    public static DomainGameModel toDomainGameModel(WebGameModel webGameModel) {
        DomainGameModel toReturn = new DomainGameModel();
        toReturn.setUuid(webGameModel.getUuid());
        toReturn.setField(toDomainGameField(webGameModel.getWebGameField()));
        toReturn.setGameState(webGameModel.getGameState());
        toReturn.setPlayer1Uuid(webGameModel.getPlayer1Uuid());
        toReturn.setPlayer2Uuid(webGameModel.getPlayer2Uuid());
        toReturn.setMultiPlayer(webGameModel.isMultiPlayer());
        toReturn.setCreateDate(webGameModel.getCreateDate());
        return toReturn;
    }

    private static DomainGameField toDomainGameField(WebGameField webGameField) {
        DomainGameField toReturn = new DomainGameField();
        for (int i = 0; i < toReturn.getField().length; ++i) {
            for (int j = 0; j < toReturn.getField()[i].length; ++j) {
                if (webGameField.getField()[i][j] == 'X') {
                    toReturn.getField()[i][j] = FieldTypes.X;
                } else if (webGameField.getField()[i][j] == 'O') {
                    toReturn.getField()[i][j] = FieldTypes.O;
                } else {
                    toReturn.getField()[i][j] = FieldTypes.EMPTY;
                }
            }
        }
        return toReturn;
    }

    private static DomainUser toDomainUser(WebUser webUser) {
        return new DomainUser(webUser.getUuid(), webUser.getPassword(),
                webUser.getLogin());
    }
}
