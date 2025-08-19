package locacao.veiculo.Mapper;

import org.springframework.stereotype.Component;

import locacao.veiculo.DTO.VeiculoDTO;
import locacao.veiculo.Entity.VeiculoEntity;

@Component
public class VeiculoMapper {

     public VeiculoEntity toEntity(VeiculoDTO dto) {
        VeiculoEntity entity = new VeiculoEntity();
        entity.setId(dto.getId());
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setAno(dto.getAno());
        entity.setPlaca(dto.getPlaca());

        return entity;
    }

    public VeiculoDTO toDTO(VeiculoEntity entity) {
        return new VeiculoDTO(
                entity.getId(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                entity.getPlaca()
                
        );
    }
    
}
