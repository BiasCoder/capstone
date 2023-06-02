package academy.doku.da3duawebserviceapi.mekaniku.workshop.config;

import com.amazonaws.ClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfig {
    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private int proxyPort;

    @Bean
    @ConditionalOnProperty(name = "proxy.enable", havingValue = "true")
    public ClientConfiguration clientConfigurationProxy() {
        final ClientConfiguration clientConfiguration = new ClientConfiguration();

        clientConfiguration.setProxyHost(proxyHost);
        clientConfiguration.setProxyPort(proxyPort);

        return clientConfiguration;
    }

    @Bean
    @ConditionalOnProperty(name = "proxy.enable", havingValue = "false")
    public ClientConfiguration clientConfiguration() {
        return new ClientConfiguration();
    }

}
