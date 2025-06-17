package app.datasource.service;

import app.datasource.model.DatasourceUser;
import app.datasource.model.PlayerStat;
import app.datasource.model.SignUpRequest;
import app.datasource.repository.UserRepository;
import app.web.security.JwtRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean registerUser(SignUpRequest request) throws Exception {
        if (request.getLogin() == null || request.getPassword() == null) {
            throw new Exception("login/password can't be empty");
        }
        if (!checkForUniqueLogin(request.getLogin())) {
            return false;
        }
        try {
            UUID uuid = UUID.randomUUID();
            DatasourceUser newUser = new DatasourceUser(uuid.toString(),
                    request.getPassword(), request.getLogin());
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            return false;
        }
        return true;
    }

    public DatasourceUser authorizeUser(JwtRequest request) {
        Optional<DatasourceUser> optional = userRepository.findUserByLoginAndPassword(request.getLogin(),
                request.getPassword());
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    private boolean checkForUniqueLogin(String login) {
        return userRepository.checkForUniqueLogin(login).isEmpty();
    }

    public DatasourceUser findUserByUuid(String uuid) {
        Optional<DatasourceUser> foundUser = userRepository.findById(uuid);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        return null;
    }

    public List<PlayerStat> getLeaderBoard(int n) {
        return userRepository.getLeaderBoard(n);
    }
}
