package locacao.veiculo.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import locacao.veiculo.Entity.VeiculoEntity;

public interface VeiculoRepository extends JpaRepository<VeiculoEntity, Long> {
    boolean existsByPlaca(String placa);
    Optional<VeiculoEntity> findByPlaca(String placa);
    
}
