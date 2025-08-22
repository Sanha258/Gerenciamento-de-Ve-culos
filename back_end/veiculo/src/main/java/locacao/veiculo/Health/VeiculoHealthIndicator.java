package locacao.veiculo.Health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import locacao.veiculo.Repository.VeiculoRepository;

@Component
public class VeiculoHealthIndicator implements HealthIndicator {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Override
    public Health health () {

        try {
            long count = veiculoRepository.count();
        return Health.up()
            .withDetail("Aplicação", "veiculos API está Saudavel")
            .withDetail("Versão", "1.0.0")
            .withDetail("Quantidade de veiculos", count)
            .withDetail("Banco de dados", "ok")
            .withDetail("Origem", "API")
            .build();
        } catch (Exception e) {
        return Health.down(e)
             .withDetail("Aplicação", "Veiculo API está Saudavel")
             .withDetail("Origem", "API")
             .build();
        }

    }
    
    
}
