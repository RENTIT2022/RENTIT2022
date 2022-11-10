package kg.neobis.rentit;

import kg.neobis.rentit.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class RentitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentitApplication.class, args);
    }

}
