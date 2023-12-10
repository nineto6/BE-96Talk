package nineto6.Talk;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@OpenAPIDefinition(servers = {
		@Server(url = "http://localhost:8080/api", description = "Local Server URL"),
		@Server(url = "http://nineto6.kro.kr:8080/api", description = "Develop Server URL"),
		@Server(url = "https://nineto6.p-e.kr/api", description = "Product Server URL")})
@SpringBootApplication
@EnableMongoRepositories
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
