package locacao.veiculo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import locacao.veiculo.DTO.VeiculoDTO;

@Service
public interface VeiculoService {

    VeiculoDTO cadastrarVeiculo(VeiculoDTO veiculoDTO);
    List<VeiculoDTO> listarTodosVeiculos();
    VeiculoDTO buscarVeiculoPorId(Long id);
    VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO);
    void excluirVeiculo(Long id);
    
}
