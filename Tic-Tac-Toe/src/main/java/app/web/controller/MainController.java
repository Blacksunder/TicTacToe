package app.web.controller;

import app.datasource.mapper.MapperDatasourceToDomain;
import app.datasource.mapper.MapperDomainToDatasource;
import app.datasource.model.DatasourceGameModel;
import app.datasource.model.DatasourceUser;
import app.datasource.service.DataGameService;
import app.datasource.service.UserService;
import app.domain.model.GameState;
import app.domain.service.DomainGameService;
import app.web.mapper.MapperDomainToWeb;
import app.web.model.WebGameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MainController {

    private final DataGameService dataGameService;
    private final DomainGameService domainGameService;
    private final UserService userService;

    @Autowired
    public MainController(DomainGameService domainGameService, DataGameService dataGameService,
                          UserService userService) {
        this.domainGameService = domainGameService;
        this.dataGameService = dataGameService;
        this.userService = userService;
    }

    @GetMapping ("/game")
    public ResponseEntity<?> getAvailableGames() {
        ArrayList<DatasourceGameModel> allGames = dataGameService.getGames();
        ArrayList<String> availableGames = new ArrayList<>();
        availableGames.add("Available games:");
        String playerUuid = getUuidFromSecurityContext();
        for (DatasourceGameModel game : allGames) {
            if (game.gameIsAvailable()) {
                availableGames.add(game.getUuid() + " (MultiPlayer)");
            } else if (!game.isMultiPlayer() &&
                    game.getPlayer1Uuid().equals(playerUuid) &&
                    game.getGameState() == GameState.PLAYER_1_TURN) {
                availableGames.add(game.getUuid() + " (SinglePlayer)");
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(availableGames);
    }

    @PostMapping ("/game/create")
    public String createGame(@RequestParam boolean online) {
        String playerUuid = getUuidFromSecurityContext();
        DatasourceGameModel newGame = new DatasourceGameModel();
        newGame.setMultiPlayer(online);
        if (online) {
            newGame.setGameState(GameState.PLAYERS_AWAITING);
        } else {
            newGame.setPlayer2Uuid("AI");
            newGame.setGameState(GameState.PLAYER_1_TURN);
        }
        newGame.setPlayer1Uuid(playerUuid);
        dataGameService.saveGame(newGame);
        return "redirect:/game/" + newGame.getUuid();
    }

    @PostMapping ("/game/connect/{uuid}")
    public String connectGame(@PathVariable String uuid) {
        DatasourceGameModel foundGame = dataGameService.getGameByUuid(uuid);
        if (foundGame != null && foundGame.gameIsAvailable()) {
            foundGame.setGameState(GameState.PLAYER_1_TURN);
            String playerUuid = getUuidFromSecurityContext();
            foundGame.setPlayer2Uuid(playerUuid);
            dataGameService.saveGame(foundGame);
            return "redirect:/game/" + foundGame.getUuid();
        } else {
            return "redirect:/game";
        }
    }

    @GetMapping ("/game/{uuid}")
    public ResponseEntity<?> game(@PathVariable String uuid) {
        DatasourceGameModel foundGame = dataGameService.getGameByUuid(uuid);
        String playerUuid = getUuidFromSecurityContext();
        if (foundGame != null && !foundGame.gameIsOver() &&
                (foundGame.getPlayer1Uuid().equals(playerUuid) ||
                        foundGame.getPlayer2Uuid().equals(playerUuid))) {
            domainGameService.setGameModel(MapperDatasourceToDomain.toDomainGameModel(foundGame));
            WebGameModel webGameModel = MapperDomainToWeb.toWebGameModel(domainGameService.getGameModel());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(webGameModel);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid game UUID");
        }
    }

    @PostMapping ("/game/{uuid}")
    public ResponseEntity<?> game(@PathVariable String uuid,
                                  @RequestParam int y, @RequestParam int x) {
        DatasourceGameModel foundGame = dataGameService.getGameByUuid(uuid);
        String playerUuid = getUuidFromSecurityContext();
        if (foundGame != null && !foundGame.gameIsOver() &&
                (foundGame.getPlayer1Uuid().equals(playerUuid) ||
                        foundGame.getPlayer2Uuid().equals(playerUuid))) {
            domainGameService.setGameModel(MapperDatasourceToDomain.toDomainGameModel(foundGame));
            domainGameService.applyUserTurn(playerUuid, y, x);
            if (foundGame.isMultiPlayer()) {
                dataGameService.synchronizeData(MapperDomainToDatasource.
                    toDataSourceGameModel(domainGameService.getGameModel()));
                WebGameModel webGameModel = MapperDomainToWeb.
                        toWebGameModel(domainGameService.getGameModel());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(webGameModel);
            } else {
                if (!domainGameService.validateField(domainGameService.getNextAiTurn())) {
                    dataGameService.deleteGameByUuid(domainGameService.getGameModel().getUuid());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Invalid field");
                }
                domainGameService.updateGameState();
                dataGameService.synchronizeData(MapperDomainToDatasource.
                    toDataSourceGameModel(domainGameService.getGameModel()));
                WebGameModel webGameModel = MapperDomainToWeb.
                        toWebGameModel(domainGameService.getGameModel());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(webGameModel);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid game UUID");
    }

    @GetMapping ("/game/user/{uuid}")
    public ResponseEntity<?> userInfo(@PathVariable String uuid) {
        DatasourceUser user = userService.findUserByUuid(uuid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user UUID");
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user.getLogin());
        }
    }

    @GetMapping ("/game/user_games/{uuid}")
    public ResponseEntity<?> getGamesByUserUuid(@PathVariable String uuid) {
        if (userService.findUserByUuid(uuid) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user UUID");
        }
        ArrayList<DatasourceGameModel> games =
                (ArrayList<DatasourceGameModel>) dataGameService.getGamesByUserUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(games);
    }

    @PostMapping ("game/leader_board")
    public ResponseEntity<?> getLeaderBoard(@RequestParam int n) {
        if (n <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Number must be >= 0");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getLeaderBoard(n));
    }

    private String getUuidFromSecurityContext() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
