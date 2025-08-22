package locacao.veiculo.Health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.client.RestTemplate;

public class ResponseTimeHealthIndicator implements HealthIndicator {

    @Override
    public Health health () {
        String url = "https://api.github.com";

    RestTemplate restTemplate = new RestTemplate();
        long start = System.currentTimeMillis();

        try {
            restTemplate.getForObject(url, String.class);
            long elapsed= System.currentTimeMillis() - start;
            String qualidade;
            if (elapsed < 200) {
                qualidade = "otimo";
            } else if(elapsed <400){
                qualidade = "bom";
                
            }else {
                qualidade = "ruim";
            }
        return Health.up()
            .withDetail("API externa", url)
            .withDetail("Tempo de Resposta (ms)", elapsed)
            .withDetail("classificação", qualidade)
            .withDetail("status", "Api externa ok")
            .withDetail("Origem", "API")
            .build();
        } catch (Exception e) {
            long elapsed= System.currentTimeMillis() -start;
        return Health.down()
            .withDetail("API externa", url)
            .withDetail("Tempo de Resposta (ms)", elapsed)
            .withDetail("status", "Falha ao acessar API externa")
            .withDetail("Origem", "API")
            .build();
        }

    }
    
}
