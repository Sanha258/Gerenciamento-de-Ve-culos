package locacao.veiculo.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.Optional;

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

    @Test
    void cadastrarVeiculo_ComPlacaExistente_DeveLancarExcecao() {
        
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertEquals("Placa já cadastrada!", exception.getMessage());
        verify(veiculoRepository, never()).save(any(VeiculoEntity.class));
    }

    @Test
    void cadastrarVeiculo_ComPlacaInvalida_DeveLancarExcecao() {
        
        veiculoDTO.setPlaca("ABC12"); 

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertTrue(exception.getMessage().contains("Placa deve ter 7 caracteres"));
    }

     @Test
    void cadastrarVeiculo_ComPlacaFormatoInvalido_DeveLancarExcecao() {
        
        veiculoDTO.setPlaca("123ABCD"); 

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertTrue(exception.getMessage().contains("Formato de placa inválido"));
    }

    @Test
    void cadastrarVeiculo_ComPlacaComTraco_DeveLimparPlaca() {
        
        veiculoDTO.setPlaca("ABC-1234");
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(veiculoMapper.toEntity(any(VeiculoDTO.class))).thenReturn(veiculoEntity);
        when(veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoEntity);
        when(veiculoMapper.toDTO(veiculoEntity)).thenReturn(veiculoDTO);

        VeiculoDTO resultado = veiculoService.cadastrarVeiculo(veiculoDTO);

        assertNotNull(resultado);
        verify(veiculoRepository).existsByPlaca("ABC1234"); 
    }

    @Test
    void cadastrarVeiculo_ComMarcaNula_DeveLancarExcecao() {
        
        veiculoDTO.setMarca(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertEquals("Marca do veículo é obrigatória.", exception.getMessage());
    }

    @Test
    void cadastrarVeiculo_ComModeloVazio_DeveLancarExcecao() {
        
        veiculoDTO.setModelo("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertEquals("Modelo do veículo é obrigatório.", exception.getMessage());
    }

     @Test
    void cadastrarVeiculo_ComAnoInvalido_DeveLancarExcecao() {
        
        veiculoDTO.setAno(1899); 

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertEquals("Ano do veículo deve estar entre 1900 e o ano atual.", exception.getMessage());
    }

     @Test
    void cadastrarVeiculo_ComAnoFuturo_DeveLancarExcecao() {
    
        int anoFuturo = Year.now().getValue() + 1;
        veiculoDTO.setAno(anoFuturo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> veiculoService.cadastrarVeiculo(veiculoDTO));
        
        assertEquals("Ano do veículo deve estar entre 1900 e o ano atual.", exception.getMessage());
    }

    @Test
    void cadastrarVeiculo_ComPlacaMercosulValida_DeveCadastrarComSucesso() {
        
        veiculoDTO.setPlaca("ABC1D23");
        when(veiculoRepository.existsByPlaca("ABC1D23")).thenReturn(false);
        when(veiculoMapper.toEntity(any(VeiculoDTO.class))).thenReturn(veiculoEntity);
        when(veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoEntity);
        when(veiculoMapper.toDTO(veiculoEntity)).thenReturn(veiculoDTO);

        VeiculoDTO resultado = veiculoService.cadastrarVeiculo(veiculoDTO);

        assertNotNull(resultado);
        verify(veiculoRepository).existsByPlaca("ABC1D23");
        verify(veiculoRepository).save(any(VeiculoEntity.class));
    }

    @Test
    void buscarVeiculoPorId_ComIdExistente_DeveRetornarVeiculo() {
        
        Long id = 1L;
        when(veiculoRepository.findById(id)).thenReturn(Optional.of(veiculoEntity));
        when(veiculoMapper.toDTO(veiculoEntity)).thenReturn(veiculoDTO);

        VeiculoDTO resultado = veiculoService.buscarVeiculoPorId(id);

        assertNotNull(resultado);
        assertEquals("Fiat", resultado.getMarca());
        verify(veiculoRepository).findById(id);
    }
}
