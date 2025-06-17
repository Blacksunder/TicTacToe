package app.datasource.service;

import app.datasource.model.DatasourceGameModel;
import app.datasource.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataGameService {

    private final GameRepository gameRepository;

    @Autowired
    public DataGameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void synchronizeData(DatasourceGameModel datasourceModel) {
        gameRepository.save(datasourceModel);
    }

    public DatasourceGameModel getGameByUuid(String uuid) {
        if (gameRepository.findById(uuid).isPresent()) {
            return gameRepository.findById(uuid).get();
        }
        return null;
    }

    public ArrayList<DatasourceGameModel> getGames() {
        return (ArrayList<DatasourceGameModel>) gameRepository.findAll();
    }

    public void saveGame(DatasourceGameModel game) {
        gameRepository.save(game);
    }

    public void deleteGameByUuid(String uuid) {
        gameRepository.deleteById(uuid);
    }

    public List<DatasourceGameModel> getGamesByUserUuid(String uuid) {
        return gameRepository.getGamesByUserUuid(uuid);
    }
}
