package locacao.veiculo.Mapper;

import org.springframework.stereotype.Component;

import locacao.veiculo.DTO.EnderecoDTO;
import locacao.veiculo.DTO.MotoristaDTO;
import locacao.veiculo.Entity.EnderecoEntity;
import locacao.veiculo.Entity.MotoristaEntity;

@Component
public class MotoristaMapper {
    
    public MotoristaEntity toEntity(MotoristaDTO dto) {
        MotoristaEntity entity = new MotoristaEntity();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setSobrenome(dto.getSobrenome());
        entity.setCpf(dto.getCpf());
        entity.setCnh(dto.getCnh());
        entity.setValidadeCnh(dto.getValidadeCnh());
        entity.setTelefone(dto.getTelefone());
        
        if (dto.getEndereco() != null) {
            entity.setEndereco(toEnderecoEntity(dto.getEndereco()));
        }
        
        return entity;
    }

    public MotoristaDTO toDTO(MotoristaEntity entity) {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setSobrenome(entity.getSobrenome());
        dto.setCpf(entity.getCpf());
        dto.setCnh(entity.getCnh());
        dto.setValidadeCnh(entity.getValidadeCnh());
        dto.setTelefone(entity.getTelefone());
        
        if (entity.getEndereco() != null) {
            dto.setEndereco(toEnderecoDTO(entity.getEndereco()));
        }
        
        return dto;
    }

    private EnderecoEntity toEnderecoEntity(EnderecoDTO dto) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(dto.getId());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        entity.setCep(dto.getCep());
        return entity;
    }

    private EnderecoDTO toEnderecoDTO(EnderecoEntity entity) {
        return new EnderecoDTO(
            entity.getId(),
            entity.getLogradouro(),
            entity.getNumero(),
            entity.getComplemento(),
            entity.getBairro(),
            entity.getCidade(),
            entity.getEstado(),
            entity.getCep()
        );
    }
}
