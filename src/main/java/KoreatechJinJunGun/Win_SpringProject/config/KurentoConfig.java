package KoreatechJinJunGun.Win_SpringProject.config;

import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KurentoConfig {

    @Value("${kurento-url}")
    private String kurentoUrl;

    @Bean
    public KurentoClient kurentoClient() {
        //kurento 미디어 서버 연결
        return KurentoClient.create(kurentoUrl);
    }
}
