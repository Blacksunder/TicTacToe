package app.datasource.repository;

import app.datasource.model.DatasourceGameModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<DatasourceGameModel, String> {
    @Query("SELECT game FROM DatasourceGameModel game WHERE game.gameState IN (0, 1, 2)" +
            " AND (game.player1Uuid = :uuid OR game.player2Uuid = :uuid)")
    List<DatasourceGameModel> getGamesByUserUuid(@Param("uuid") String uuid);
}
