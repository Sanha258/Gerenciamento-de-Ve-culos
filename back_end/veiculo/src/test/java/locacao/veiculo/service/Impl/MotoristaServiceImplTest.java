package locacao.veiculo.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    @Test
    void testBuscarTodosMotoristas() {
   
        List<MotoristaEntity> entities = Arrays.asList(motoristaEntity);
        
        when(motoristaRepository.findAll()).thenReturn(entities);
        when(motoristaMapper.toDTO(motoristaEntity)).thenReturn(motoristaDTO);

        List<MotoristaDTO> resultado = motoristaService.listarTodosMotoristas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(motoristaDTO.getId(), resultado.get(0).getId());
        
        verify(motoristaRepository, times(1)).findAll();
        verify(motoristaMapper, times(1)).toDTO(motoristaEntity);
    }

    @Test
    void testBuscarMotoristaPorCpf() {
        // Arrange
        String cpf = "12345678901";
        when(motoristaRepository.findByCpf(cpf)).thenReturn(Optional.of(motoristaEntity));
        when(motoristaMapper.toDTO(any(MotoristaEntity.class))).thenReturn(motoristaDTO);

        // Act - Remove o Optional, pois retorna MotoristaDTO diretamente
        MotoristaDTO resultado = motoristaService.buscarMotoristaPorCpf(cpf);

        // Assert - Testa o objeto diretamente
        assertNotNull(resultado);
        assertEquals(motoristaDTO.getCpf(), resultado.getCpf());
        
        verify(motoristaRepository, times(1)).findByCpf(cpf);
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }

    @Test
    void testAtualizarMotoristaSemAlterarCpf() {
        Long id = 1L;
      
        MotoristaDTO motoristaAtualizadoDTO = new MotoristaDTO(
            id, "João Atualizado", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(5), "11988888888", enderecoDTO
        );
        
        MotoristaEntity motoristaExistente = new MotoristaEntity(
            "João", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(3), "11999999999"
        );
        motoristaExistente.setId(id);
        motoristaExistente.setEndereco(enderecoEntity);

        MotoristaEntity motoristaAtualizadoEntity = new MotoristaEntity(
            "João Atualizado", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(5), "11988888888"
        );
        motoristaAtualizadoEntity.setId(id);
        motoristaAtualizadoEntity.setEndereco(enderecoEntity);

        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaExistente));
        when(motoristaRepository.save(any(MotoristaEntity.class))).thenReturn(motoristaAtualizadoEntity);
        when(motoristaMapper.toDTO(motoristaAtualizadoEntity)).thenReturn(motoristaAtualizadoDTO);

        MotoristaDTO resultado = motoristaService.atualizarMotorista(id, motoristaAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
       
        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaRepository, never()).existsByCpf(anyString()); 
        verify(motoristaRepository, never()).existsByCnh(anyString()); 
        verify(motoristaRepository, times(1)).save(any(MotoristaEntity.class));
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }

    @Test
    void testAtualizarMotoristaComCpfECnhDiferentes() {
        Long id = 1L;
        
        MotoristaDTO motoristaAtualizadoDTO = new MotoristaDTO(
            id, "João Atualizado", "Silva", "12345678902", "67459547533", 
            LocalDate.now().plusYears(5), "11988888888", enderecoDTO
        );
        
        MotoristaEntity motoristaExistente = new MotoristaEntity(
            "João", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(3), "11999999999"
        );
        motoristaExistente.setId(id);
        motoristaExistente.setEndereco(enderecoEntity);

      
        MotoristaEntity motoristaAtualizadoEntity = new MotoristaEntity(
            "João Atualizado", "Silva", "12345678902", "67459547533", 
            LocalDate.now().plusYears(5), "11988888888"
        );
        motoristaAtualizadoEntity.setId(id);
        motoristaAtualizadoEntity.setEndereco(enderecoEntity);

        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaExistente));
        when(motoristaRepository.existsByCpf("12345678902")).thenReturn(false); 
        when(motoristaRepository.save(any(MotoristaEntity.class))).thenReturn(motoristaAtualizadoEntity);
        when(motoristaMapper.toDTO(motoristaAtualizadoEntity)).thenReturn(motoristaAtualizadoDTO);

        MotoristaDTO resultado = motoristaService.atualizarMotorista(id, motoristaAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
        assertEquals("12345678902", resultado.getCpf()); 
        assertEquals("11988888888", resultado.getTelefone());
        
        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaRepository, times(1)).existsByCpf("12345678902");
        verify(motoristaRepository, times(1)).existsByCnh("67459547533");
        verify(motoristaRepository, times(1)).save(any(MotoristaEntity.class));
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }

    @Test
    void testAtualizarMotoristaComCpfExistenteDeveLancarExcecao() {
        Long id = 1L;
        
        MotoristaDTO motoristaAtualizadoDTO = new MotoristaDTO(
            id, "João Atualizado", "Silva", "12345678902", "67459547532", 
            LocalDate.now().plusYears(5), "11988888888", enderecoDTO
        );
        
        MotoristaEntity motoristaExistente = new MotoristaEntity(
            "João", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(3), "11999999999"
        );
        motoristaExistente.setId(id);
        motoristaExistente.setEndereco(enderecoEntity);

        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaExistente));
        when(motoristaRepository.existsByCpf("12345678902")).thenReturn(true); 

       
        assertThrows(IllegalArgumentException.class, () -> {
            motoristaService.atualizarMotorista(id, motoristaAtualizadoDTO);
        });

        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaRepository, times(1)).existsByCpf("12345678902");
        verify(motoristaRepository, never()).existsByCnh(anyString());
        verify(motoristaRepository, never()).save(any(MotoristaEntity.class));
        }

   @Test
    void testAtualizarMotoristaSemAlterarCpfECnh() {
        Long id = 1L;
        
        MotoristaDTO motoristaAtualizadoDTO = new MotoristaDTO(
            id, "João Atualizado", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(5), "11988888888", enderecoDTO
        );
        
        MotoristaEntity motoristaExistente = new MotoristaEntity(
            "João", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(3), "11999999999"
        );
        motoristaExistente.setId(id);
        motoristaExistente.setEndereco(enderecoEntity);

        MotoristaEntity motoristaAtualizadoEntity = new MotoristaEntity(
            "João Atualizado", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(5), "11988888888"
        );
        motoristaAtualizadoEntity.setId(id);
        motoristaAtualizadoEntity.setEndereco(enderecoEntity);

        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaExistente));
        when(motoristaRepository.save(any(MotoristaEntity.class))).thenReturn(motoristaAtualizadoEntity);
        when(motoristaMapper.toDTO(motoristaAtualizadoEntity)).thenReturn(motoristaAtualizadoDTO);

        MotoristaDTO resultado = motoristaService.atualizarMotorista(id, motoristaAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
        
        
        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaRepository, never()).existsByCpf(anyString());
        verify(motoristaRepository, never()).existsByCnh(anyString());
        verify(motoristaRepository, times(1)).save(any(MotoristaEntity.class));
        verify(motoristaMapper, times(1)).toDTO(any(MotoristaEntity.class));
    }

    @Test
    void testAtualizarMotoristaComCnhExistenteDeveLancarExcecao() {
        Long id = 1L;
        
        
        MotoristaDTO motoristaAtualizadoDTO = new MotoristaDTO(
            id, "João Atualizado", "Silva", "12345678902", "67459547533", 
            LocalDate.now().plusYears(5), "11988888888", enderecoDTO
        );
        
        MotoristaEntity motoristaExistente = new MotoristaEntity(
            "João", "Silva", "12345678901", "67459547532", 
            LocalDate.now().plusYears(3), "11999999999"
        );
        motoristaExistente.setId(id);
        motoristaExistente.setEndereco(enderecoEntity);

        when(motoristaRepository.findById(id)).thenReturn(Optional.of(motoristaExistente));
        when(motoristaRepository.existsByCpf("12345678902")).thenReturn(false); 
        when(motoristaRepository.existsByCnh("67459547533")).thenReturn(true); 
        
        assertThrows(IllegalArgumentException.class, () -> {
            motoristaService.atualizarMotorista(id, motoristaAtualizadoDTO);
        });

        verify(motoristaRepository, times(1)).findById(id);
        verify(motoristaRepository, times(1)).existsByCpf("12345678902"); 
        verify(motoristaRepository, times(1)).existsByCnh("67459547533");
        verify(motoristaRepository, never()).save(any(MotoristaEntity.class));
    }

    
    
}
