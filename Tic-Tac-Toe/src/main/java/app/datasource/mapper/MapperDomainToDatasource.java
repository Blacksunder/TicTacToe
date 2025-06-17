package app.datasource.mapper;

import app.datasource.model.DatasourceGameField;
import app.datasource.model.DatasourceGameModel;
import app.datasource.model.DatasourceUser;
import app.domain.model.DomainGameField;
import app.domain.model.DomainGameModel;
import app.domain.model.DomainUser;

public class MapperDomainToDatasource {
    public static DatasourceGameModel toDataSourceGameModel(DomainGameModel domainGameModel) {
        DatasourceGameModel toReturn = new DatasourceGameModel();
        toReturn.setUuid(domainGameModel.getUuid());
        toReturn.setDataField(toDataSourceGameField(domainGameModel.getField()));
        toReturn.setGameState(domainGameModel.getGameState());
        toReturn.setPlayer1Uuid(domainGameModel.getPlayer1Uuid());
        toReturn.setPlayer2Uuid(domainGameModel.getPlayer2Uuid());
        toReturn.setMultiPlayer(domainGameModel.isMultiPlayer());
        return toReturn;
    }

    private static DatasourceGameField toDataSourceGameField(DomainGameField domainGameField) {
        DatasourceGameField toReturn = new DatasourceGameField();
        StringBuilder toSet = new StringBuilder();
        for (int i = 0; i < domainGameField.getField().length; ++i) {
            for (int j = 0; j < domainGameField.getField()[i].length; ++j) {
                switch (domainGameField.getField()[i][j]) {
                    case O -> toSet.append("O");
                    case X -> toSet.append("X");
                    default -> toSet.append("-");
                }

            }
        }
        toReturn.setField(toSet.toString());
        return toReturn;
    }

    public static DatasourceUser toDataSourceUser(DomainUser domainUser) {
        return new DatasourceUser(domainUser.getUuid(), domainUser.getPassword(),
                domainUser.getLogin());
    }
}
