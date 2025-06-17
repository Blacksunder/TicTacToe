package app.datasource.mapper;

import app.datasource.model.DatasourceGameField;
import app.datasource.model.DatasourceGameModel;
import app.datasource.model.DatasourceUser;
import app.domain.model.DomainGameField;
import app.domain.model.DomainGameModel;
import app.domain.model.DomainUser;
import app.domain.model.FieldTypes;

public class MapperDatasourceToDomain {
    public static DomainGameModel toDomainGameModel(DatasourceGameModel datasourceGameModel) {
        DomainGameModel toReturn = new DomainGameModel();
        toReturn.setUuid(datasourceGameModel.getUuid());
        toReturn.setField(toDomainGameField(datasourceGameModel.getDataField()));
        toReturn.setGameState(datasourceGameModel.getGameState());
        toReturn.setPlayer1Uuid(datasourceGameModel.getPlayer1Uuid());
        toReturn.setPlayer2Uuid(datasourceGameModel.getPlayer2Uuid());
        toReturn.setMultiPlayer(datasourceGameModel.isMultiPlayer());
        toReturn.setCreateDate(datasourceGameModel.getCreateDate());
        return toReturn;
    }

    private static DomainGameField toDomainGameField(DatasourceGameField datasourceGameField) {
        DomainGameField toReturn = new DomainGameField();
        char[] convertedField = datasourceGameField.getField().toCharArray();
        for (int i = 0; i < toReturn.getField().length; ++i) {
            for (int j = 0; j < toReturn.getField()[i].length; ++j) {
                switch (convertedField[i * 3 + j]) {
                    case 'X' -> toReturn.getField()[i][j] = FieldTypes.X;
                    case 'O' -> toReturn.getField()[i][j] = FieldTypes.O;
                    default -> toReturn.getField()[i][j] = FieldTypes.EMPTY;
                }
            }
        }
        return toReturn;
    }

    private static DomainUser toDomainUser(DatasourceUser datasourceUser) {
        return new DomainUser(datasourceUser.getUuid(), datasourceUser.getPassword(),
                datasourceUser.getLogin());
    }
}
