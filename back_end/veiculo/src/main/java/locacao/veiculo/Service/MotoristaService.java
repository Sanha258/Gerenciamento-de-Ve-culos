package locacao.veiculo.Service;

import java.util.List;

import locacao.veiculo.DTO.MotoristaDTO;

public interface MotoristaService {
    
    MotoristaDTO cadastrarMotorista(MotoristaDTO motoristaDTO);
    List<MotoristaDTO> listarTodosMotoristas();
    MotoristaDTO buscarMotoristaPorId(Long id);
    MotoristaDTO atualizarMotorista(Long id, MotoristaDTO motoristaDTO);
    void excluirMotorista(Long id);
    MotoristaDTO buscarMotoristaPorCpf(String cpf);
}
