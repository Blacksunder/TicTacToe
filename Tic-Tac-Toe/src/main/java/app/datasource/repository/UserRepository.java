package app.datasource.repository;

import app.datasource.model.DatasourceUser;
import app.datasource.model.PlayerStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<DatasourceUser, String> {
    @Query("FROM DatasourceUser WHERE login = :login AND password = :password")
    Optional<DatasourceUser> findUserByLoginAndPassword(@Param("login") String login,
                                                        @Param("password") String password);

    @Query("FROM DatasourceUser WHERE login = :login")
    Optional<DatasourceUser> checkForUniqueLogin(@Param("login") String login);

    @Query(value = "WITH user_games AS (" +
            "    SELECT u.uuid AS user_id, u.login," +
            "    SUM(CASE WHEN g.is_online = true THEN 1 ELSE 0 END) AS total_games," +
            "    SUM (" +
            "        CASE" +
            "        WHEN (g.is_online = true AND ((g.game_state = 0 AND g.player1_uuid = u.uuid)" +
            " OR (g.game_state = 1 AND g.player2_uuid = u.uuid)))" +
            "        THEN 1 ELSE 0 END" +
            "    ) AS victories" +
            "    FROM users u LEFT JOIN games g ON u.uuid = g.player1_uuid OR u.uuid = g.player2_uuid" +
            "    GROUP BY u.uuid, u.login" +
            ")" +
            " SELECT user_id AS uuid, login," +
            " CASE WHEN total_games = 0 THEN 0" +
            " ELSE CAST(ROUND(victories::numeric / total_games * 100, 2) AS double precision)" +
            " END AS victoryPercent" +
            " FROM user_games" +
            " ORDER BY victoryPercent DESC" +
            " LIMIT :N;", nativeQuery = true)
    List<PlayerStat> getLeaderBoard(@Param("N") int n);
}
