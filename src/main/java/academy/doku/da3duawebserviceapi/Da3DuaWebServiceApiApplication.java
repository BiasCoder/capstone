package academy.doku.da3duawebserviceapi;

import academy.doku.da3duawebserviceapi.mekaniku.user.config.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyProperties.class)
public class Da3DuaWebServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Da3DuaWebServiceApiApplication.class, args);
    }

}
