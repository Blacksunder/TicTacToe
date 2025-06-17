package app.di;

import app.datasource.repository.GameRepository;
import app.datasource.repository.UserRepository;
import app.datasource.service.DataGameService;
import app.datasource.service.UserService;
import app.domain.service.DomainGameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfigurations {
    @Bean
    public DomainGameService gameService() {
        return new DomainGameService();
    }

    @Bean
    public DataGameService dataService(GameRepository gameRepository) {
        return new DataGameService(gameRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
