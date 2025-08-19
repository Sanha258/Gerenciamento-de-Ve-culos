package locacao.veiculo.Service.Impl;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import locacao.veiculo.DTO.VeiculoDTO;
import locacao.veiculo.Entity.VeiculoEntity;
import locacao.veiculo.Mapper.VeiculoMapper;
import locacao.veiculo.Repository.VeiculoRepository;
import locacao.veiculo.Service.VeiculoService;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoMapper veiculoMapper;

    @Override
    @Transactional
    public VeiculoDTO cadastrarVeiculo(VeiculoDTO veiculoDTO) {
        validarVeiculo(veiculoDTO);
        if (veiculoRepository.existsByPlaca(veiculoDTO.getPlaca())) {
            throw new RuntimeException("Placa já existe!");
        }

        VeiculoEntity veiculoEntity = veiculoMapper.toEntity(veiculoDTO);
        VeiculoEntity veiculoSalvo = veiculoRepository.save(veiculoEntity);
        return veiculoMapper.toDTO(veiculoSalvo);
    }

    @Override
    public List<VeiculoDTO> listarTodosVeiculos() {
        return veiculoRepository.findAll().stream()
                .map(veiculoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VeiculoDTO buscarVeiculoPorId(Long id) {
        Optional<VeiculoEntity> veiculoOptional = veiculoRepository.findById(id);
        return veiculoOptional.map(veiculoMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) {
        validarVeiculo(veiculoDTO);
        VeiculoEntity veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com o ID: " + id));

        if (!veiculoExistente.getPlaca().equals(veiculoDTO.getPlaca()) && 
            veiculoRepository.existsByPlaca(veiculoDTO.getPlaca())) {
            throw new RuntimeException("Placa já existe!");
        }

        veiculoExistente.setMarca(veiculoDTO.getMarca());
        veiculoExistente.setModelo(veiculoDTO.getModelo());
        veiculoExistente.setAno(veiculoDTO.getAno());
        veiculoExistente.setPlaca(veiculoDTO.getPlaca());
        veiculoExistente.setCor(veiculoDTO.getCor());

        VeiculoEntity veiculoAtualizado = veiculoRepository.save(veiculoExistente);
        return veiculoMapper.toDTO(veiculoAtualizado);
    }

    @Override
    @Transactional
    public void excluirVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new IllegalArgumentException("Veículo não encontrado com o ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }

    private void validarVeiculo(VeiculoDTO veiculoDTO) {
        if (veiculoDTO.getMarca() == null || veiculoDTO.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("Marca do veículo é obrigatória.");
        }
        if (veiculoDTO.getModelo() == null || veiculoDTO.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo do veículo é obrigatório.");
        }
        if (veiculoDTO.getAno() == null || veiculoDTO.getAno() < 1900 || veiculoDTO.getAno() > Year.now().getValue()) {
            throw new IllegalArgumentException("Ano do veículo deve estar entre 1900 e o ano atual.");
        }
        if (veiculoDTO.getPlaca() == null || veiculoDTO.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa do veículo é obrigatória.");
        }
        if (veiculoDTO.getCor() == null || veiculoDTO.getCor().trim().isEmpty()) {
            throw new IllegalArgumentException("Cor do veículo é obrigatória.");
        }
    }
    
}
