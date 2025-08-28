package locacao.veiculo.Service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import locacao.veiculo.DTO.EnderecoDTO;
import locacao.veiculo.DTO.MotoristaDTO;
import locacao.veiculo.Entity.EnderecoEntity;
import locacao.veiculo.Entity.MotoristaEntity;
import locacao.veiculo.Mapper.MotoristaMapper;
import locacao.veiculo.Repository.MotoristaRepository;
import locacao.veiculo.Service.MotoristaService;

@Service
public class MotoristaServiceImpl implements MotoristaService {

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private MotoristaMapper motoristaMapper;

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern CNH_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\d{10,11}$");

    @Override
    @Transactional
    public MotoristaDTO cadastrarMotorista(MotoristaDTO motoristaDTO) {
        validarMotorista(motoristaDTO);
        
        // Verificar se CPF já existe
        if (motoristaRepository.existsByCpf(motoristaDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        // Verificar se CNH já existe
        if (motoristaRepository.existsByCnh(motoristaDTO.getCnh())) {
            throw new IllegalArgumentException("CNH já cadastrada!");
        }

        MotoristaEntity motoristaEntity = motoristaMapper.toEntity(motoristaDTO);
        MotoristaEntity motoristaSalvo = motoristaRepository.save(motoristaEntity);
        return motoristaMapper.toDTO(motoristaSalvo);
    }

    @Override
    public List<MotoristaDTO> listarTodosMotoristas() {
        return motoristaRepository.findAll().stream()
                .map(motoristaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MotoristaDTO buscarMotoristaPorId(Long id) {
        Optional<MotoristaEntity> motoristaOptional = motoristaRepository.findById(id);
        return motoristaOptional.map(motoristaMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado com o ID: " + id));
    }

    @Override
    public MotoristaDTO buscarMotoristaPorCpf(String cpf) {
        Optional<MotoristaEntity> motoristaOptional = motoristaRepository.findByCpf(cpf);
        return motoristaOptional.map(motoristaMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado com o CPF: " + cpf));
    }

    @Override
    @Transactional
    public MotoristaDTO atualizarMotorista(Long id, MotoristaDTO motoristaDTO) {
        validarMotorista(motoristaDTO);
        
        MotoristaEntity motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado com o ID: " + id));

        // Verificar se CPF já existe em outro motorista
        if (!motoristaExistente.getCpf().equals(motoristaDTO.getCpf()) && 
            motoristaRepository.existsByCpf(motoristaDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado em outro motorista!");
        }

        // Verificar se CNH já existe em outro motorista
        if (!motoristaExistente.getCnh().equals(motoristaDTO.getCnh()) && 
            motoristaRepository.existsByCnh(motoristaDTO.getCnh())) {
            throw new IllegalArgumentException("CNH já cadastrada em outro motorista!");
        }

        motoristaExistente.setNome(motoristaDTO.getNome());
        motoristaExistente.setSobrenome(motoristaDTO.getSobrenome());
        motoristaExistente.setCpf(motoristaDTO.getCpf());
        motoristaExistente.setCnh(motoristaDTO.getCnh());
        motoristaExistente.setValidadeCnh(motoristaDTO.getValidadeCnh());
        motoristaExistente.setTelefone(motoristaDTO.getTelefone());

        // Atualizar endereço
        if (motoristaDTO.getEndereco() != null) {
            EnderecoEntity endereco = motoristaExistente.getEndereco();
            if (endereco == null) {
                endereco = new EnderecoEntity();
            }
            EnderecoDTO enderecoDTO = motoristaDTO.getEndereco();
            endereco.setLogradouro(enderecoDTO.getLogradouro());
            endereco.setNumero(enderecoDTO.getNumero());
            endereco.setComplemento(enderecoDTO.getComplemento());
            endereco.setBairro(enderecoDTO.getBairro());
            endereco.setCidade(enderecoDTO.getCidade());
            endereco.setEstado(enderecoDTO.getEstado());
            endereco.setCep(enderecoDTO.getCep());
            motoristaExistente.setEndereco(endereco);
        }

        MotoristaEntity motoristaAtualizado = motoristaRepository.save(motoristaExistente);
        return motoristaMapper.toDTO(motoristaAtualizado);
    }

    @Override
    @Transactional
    public void excluirMotorista(Long id) {
        if (!motoristaRepository.existsById(id)) {
            throw new IllegalArgumentException("Motorista não encontrado com o ID: " + id);
        }
        motoristaRepository.deleteById(id);
    }

    private void validarMotorista(MotoristaDTO motoristaDTO) {
        if (motoristaDTO.getNome() == null || motoristaDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do motorista é obrigatório.");
        }
        if (motoristaDTO.getSobrenome() == null || motoristaDTO.getSobrenome().trim().isEmpty()) {
            throw new IllegalArgumentException("Sobrenome do motorista é obrigatório.");
        }
        if (motoristaDTO.getCpf() == null || !CPF_PATTERN.matcher(motoristaDTO.getCpf()).matches()) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos.");
        }
        if (motoristaDTO.getCnh() == null || !CNH_PATTERN.matcher(motoristaDTO.getCnh()).matches()) {
            throw new IllegalArgumentException("CNH inválida. Deve conter 11 dígitos.");
        }
        if (motoristaDTO.getValidadeCnh() == null) {
            throw new IllegalArgumentException("Validade da CNH é obrigatória.");
        }
        if (motoristaDTO.getTelefone() == null || !TELEFONE_PATTERN.matcher(motoristaDTO.getTelefone()).matches()) {
            throw new IllegalArgumentException("Telefone inválido. Deve conter 10 ou 11 dígitos.");
        }
        
        // Validar endereço
        if (motoristaDTO.getEndereco() != null) {
            validarEndereco(motoristaDTO.getEndereco());
        }
    }

    private void validarEndereco(EnderecoDTO enderecoDTO) {
        if (enderecoDTO.getLogradouro() == null || enderecoDTO.getLogradouro().trim().isEmpty()) {
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        }
        if (enderecoDTO.getNumero() == null || enderecoDTO.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Número é obrigatório.");
        }
        if (enderecoDTO.getBairro() == null || enderecoDTO.getBairro().trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro é obrigatório.");
        }
        if (enderecoDTO.getCidade() == null || enderecoDTO.getCidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }
        if (enderecoDTO.getEstado() == null || enderecoDTO.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado é obrigatório.");
        }
        if (enderecoDTO.getCep() == null || enderecoDTO.getCep().trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }
    }
    
}
