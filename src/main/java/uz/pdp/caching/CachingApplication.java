package uz.pdp.caching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import uz.pdp.caching.entity.Post;
import uz.pdp.caching.repository.PostRepository;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Working with Caching | G58 Open Specification | Swagger",
                version = "1.0",
                description = "Cache",
                contact = @Contact(
                        name = "Salohiddin Yunusov",
                        email = "salohiddinyunusov377@gmail.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Server"
                )
        }
)

@ConfigurationPropertiesScan
@EnableCaching
public class CachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CachingApplication.class, args);
    }

//    @Bean
//    public CacheManager cacheManager() {
//        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
//        cacheManager.setCacheNames(List.of("posts", "comments", "users"));
//
//        return cacheManager;
//    }

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Spring Swagger Example")
                        .description("Spring Boot + Swagger + MapStruct Example")
                        .version("1.0")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Salohiddin Yunusov")
                                .email("salohiddinyunusov377@gmail.com")
                                .url("https://github.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation")
                        .url("https://springdoc.org"))
                .servers(List.of(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("http://localhost:8080")
                                .description("Production"),
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("http://localhost:9090")
                                .description("Test Server")
                ));
    }

    @Bean
    public ApplicationRunner applicationRunner(ObjectMapper objectMapper, PostRepository postRepository) {
        return args -> {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts");
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            List<Post> posts = objectMapper.readValue(bytes, new TypeReference<List<Post>>() {});

            postRepository.saveAll(posts);
        };
    }
}


