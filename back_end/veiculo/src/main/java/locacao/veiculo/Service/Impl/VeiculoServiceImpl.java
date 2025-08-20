package locacao.veiculo.Service.Impl;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

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

    private static final Pattern PLACA_PATTERN = 
        Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", Pattern.CASE_INSENSITIVE);

    @Override
    @Transactional
    public VeiculoDTO cadastrarVeiculo(VeiculoDTO veiculoDTO) {
        validarVeiculo(veiculoDTO);
        
        String placaLimpa = limparPlaca(veiculoDTO.getPlaca());
        validarFormatoPlaca(placaLimpa);
        
        veiculoDTO.setPlaca(placaLimpa);
        
        if (veiculoRepository.existsByPlaca(placaLimpa)) {
            throw new RuntimeException("Placa já cadastrada!");
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
        
        String placaLimpa = limparPlaca(veiculoDTO.getPlaca());
        validarFormatoPlaca(placaLimpa);
        
        veiculoDTO.setPlaca(placaLimpa);
        
        VeiculoEntity veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com o ID: " + id));

        if (!veiculoExistente.getPlaca().equals(placaLimpa) && 
            veiculoRepository.existsByPlaca(placaLimpa)) {
            throw new RuntimeException("Placa já cadastrada!");
        }

        veiculoExistente.setMarca(veiculoDTO.getMarca());
        veiculoExistente.setModelo(veiculoDTO.getModelo());
        veiculoExistente.setAno(veiculoDTO.getAno());
        veiculoExistente.setPlaca(placaLimpa);

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
    }
    
    private void validarFormatoPlaca(String placa) {
        if (placa == null) {
            throw new IllegalArgumentException("Placa não pode ser nula");
        }
        
        if (placa.length() != 7) {
            throw new IllegalArgumentException("Placa deve ter 7 caracteres (ex: ABC1234 ou ABC1D23)");
        }
        
        if (!PLACA_PATTERN.matcher(placa).matches()) {
            throw new IllegalArgumentException(
                "Formato de placa inválido. Use padrão brasileiro: " +
                "Antigo (AAA1234) ou Mercosul (AAA1D23)");
        }
    }
    
    private String limparPlaca(String placa) {
        return placa.replaceAll("[-\\s]", "").toUpperCase();
    }
}