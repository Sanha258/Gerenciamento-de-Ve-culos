package locacao.veiculo.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import locacao.veiculo.DTO.VeiculoDTO;
import locacao.veiculo.Entity.VeiculoEntity;
import locacao.veiculo.Mapper.VeiculoMapper;
import locacao.veiculo.Repository.VeiculoRepository;
import locacao.veiculo.Service.Impl.VeiculoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceImplTest {
    
    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VeiculoMapper veiculoMapper;

    @InjectMocks
    private VeiculoServiceImpl veiculoService;

    private VeiculoDTO veiculoDTO;
    private VeiculoEntity veiculoEntity;

    @BeforeEach
    void setUp() {
        veiculoDTO = new VeiculoDTO();
        veiculoDTO.setMarca("Fiat");
        veiculoDTO.setModelo("Uno");
        veiculoDTO.setAno(2020);
        veiculoDTO.setPlaca("ABC1234");

        veiculoEntity = new VeiculoEntity();
        veiculoEntity.setId(1L);
        veiculoEntity.setMarca("Fiat");
        veiculoEntity.setModelo("Uno");
        veiculoEntity.setAno(2020);
        veiculoEntity.setPlaca("ABC1234");
    }

    @Test
    void cadastrarVeiculo_ComDadosValidos_DeveRetornarVeiculoDTO() {
        
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(veiculoMapper.toEntity(veiculoDTO)).thenReturn(veiculoEntity);
        when(veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoEntity);
        when(veiculoMapper.toDTO(veiculoEntity)).thenReturn(veiculoDTO);

        
        VeiculoDTO resultado = veiculoService.cadastrarVeiculo(veiculoDTO);

        
        assertNotNull(resultado);
        assertEquals("Fiat", resultado.getMarca());
        assertEquals("Uno", resultado.getModelo());
        assertEquals(2020, resultado.getAno());
        assertEquals("ABC1234", resultado.getPlaca());

        verify(veiculoRepository).existsByPlaca("ABC1234");
        verify(veiculoRepository).save(any(VeiculoEntity.class));
    }


}
