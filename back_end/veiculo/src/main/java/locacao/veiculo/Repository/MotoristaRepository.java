package locacao.veiculo.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import locacao.veiculo.Entity.MotoristaEntity;

public interface MotoristaRepository extends JpaRepository< MotoristaEntity, Long>{

    Optional<MotoristaEntity> findByCpf(String cpf);
    Optional<MotoristaEntity> findByCnh(String cnh);
    boolean existsByCpf(String cpf);
    boolean existsByCnh(String cnh);
    
}
