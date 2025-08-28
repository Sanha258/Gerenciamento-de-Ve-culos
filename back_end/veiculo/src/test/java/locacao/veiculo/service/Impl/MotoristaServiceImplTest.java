package locacao.veiculo.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import locacao.veiculo.DTO.EnderecoDTO;
import locacao.veiculo.DTO.MotoristaDTO;
import locacao.veiculo.Entity.EnderecoEntity;
import locacao.veiculo.Entity.MotoristaEntity;
import locacao.veiculo.Mapper.MotoristaMapper;
import locacao.veiculo.Repository.MotoristaRepository;
import locacao.veiculo.Service.Impl.MotoristaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MotoristaServiceImplTest {
    
    @Mock
    private MotoristaRepository motoristaRepository;

    @Mock
    private MotoristaMapper motoristaMapper;

    @InjectMocks
    private MotoristaServiceImpl motoristaService;

    private MotoristaDTO motoristaDTO;
    private MotoristaEntity motoristaEntity;
    private EnderecoDTO enderecoDTO;
    private EnderecoEntity enderecoEntity;
    private AutoCloseable closeable;
   
     @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        
        // Configurar dados de teste
        enderecoDTO = new EnderecoDTO(
            1L, "Rua Teste", "123", "Apto 1", "Centro", 
            "São Paulo", "SP", "01234-567"
        );
        
        enderecoEntity = new EnderecoEntity(
            "Rua Teste", "123", "Apto 1", "Centro", 
            "São Paulo", "SP", "01234-567"
        );
        enderecoEntity.setId(1L);
        
        motoristaDTO = new MotoristaDTO(
            1L, "João", "Silva", "12345678901", "35785765498", 
            LocalDate.now().plusYears(5), "11999999999", enderecoDTO
        );
        
        motoristaEntity = new MotoristaEntity(
            "João", "Silva", "12345678901", "CNH123456", 
            LocalDate.now().plusYears(5), "11999999999"
        );
        motoristaEntity.setId(1L);
        motoristaEntity.setEndereco(enderecoEntity);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSalvarMotorista() {

        when(motoristaMapper.toEntity(any(MotoristaDTO.class))).thenReturn(motoristaEntity);
        when(motoristaRepository.save(any(MotoristaEntity.class))).thenReturn(motoristaEntity);
        when(motoristaMapper.toDTO(any(MotoristaEntity.class))).thenReturn(motoristaDTO);

        MotoristaDTO resultado = motoristaService.cadastrarMotorista(motoristaDTO);

        assertNotNull(resultado);
        assertEquals(motoristaDTO.getId(), resultado.getId());
        assertEquals(motoristaDTO.getNome(), resultado.getNome());
        assertEquals(motoristaDTO.getCpf(), resultado.getCpf());
        
        verify(motoristaMapper, times(1)).toEntity(any(MotoristaDTO.class));
        verify(motoristaRepository, times(1)).save(any(MotoristaEntity.class));
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }

     @Test
    void testBuscarMotoristaPorId() {
      
        Long id = 1L;
        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaEntity));
        when(motoristaMapper.toDTO(any(MotoristaEntity.class))).thenReturn(motoristaDTO);

       
        MotoristaDTO resultado = motoristaService.buscarMotoristaPorId(id);

        
        assertNotNull(resultado);
        assertEquals(motoristaDTO.getId(), resultado.getId());
        assertEquals(motoristaDTO.getNome(), resultado.getNome());
        assertEquals(motoristaDTO.getCpf(), resultado.getCpf());
        assertEquals(motoristaDTO.getCnh(), resultado.getCnh());
        
        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }


    
}
