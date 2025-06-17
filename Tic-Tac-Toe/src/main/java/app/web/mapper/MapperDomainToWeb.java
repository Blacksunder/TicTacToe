package app.web.mapper;

import app.datasource.model.DatasourceUser;
import app.domain.model.DomainGameField;
import app.domain.model.DomainGameModel;
import app.domain.model.DomainUser;
import app.domain.model.FieldTypes;
import app.web.model.WebGameField;
import app.web.model.WebGameModel;
import app.web.model.WebUser;

public class MapperDomainToWeb {
    public static WebGameModel toWebGameModel(DomainGameModel domainGameModel) {
        WebGameModel toReturn = new WebGameModel();
        toReturn.setUuid(domainGameModel.getUuid());
        toReturn.setField(toWebGameField(domainGameModel.getField()));
        toReturn.setGameState(domainGameModel.getGameState());
        toReturn.setPlayer1Uuid(domainGameModel.getPlayer1Uuid());
        toReturn.setPlayer2Uuid(domainGameModel.getPlayer2Uuid());
        toReturn.setMultiPlayer(domainGameModel.isMultiPlayer());
        toReturn.setCreateDate(domainGameModel.getCreateDate());
        return toReturn;
    }

    private static WebGameField toWebGameField(DomainGameField domainGameField) {
        WebGameField toReturn = new WebGameField();
        for (int i = 0; i < toReturn.getField().length; ++i) {
            for (int j = 0; j < toReturn.getField()[i].length; ++j) {
                if (domainGameField.getField()[i][j] == FieldTypes.X) {
                    toReturn.getField()[i][j] = 'X';
                } else if (domainGameField.getField()[i][j] == FieldTypes.O) {
                    toReturn.getField()[i][j] = 'O';
                } else {
                    toReturn.getField()[i][j] = '-';
                }
            }
        }
        return toReturn;
    }

    private static WebUser toWebUser(DomainUser domainUser) {
        return new WebUser(domainUser.getUuid(), domainUser.getPassword(),
                domainUser.getLogin());
    }
}
