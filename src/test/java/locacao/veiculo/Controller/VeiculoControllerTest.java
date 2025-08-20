package locacao.veiculo.Controller;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import locacao.veiculo.DTO.VeiculoDTO;
import locacao.veiculo.Service.VeiculoService;

@WebMvcTest(VeiculoController.class)
public class VeiculoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VeiculoService veiculoService;

    private VeiculoDTO veiculoDTO;
    private List<VeiculoDTO> veiculosList;

    @BeforeEach
    void setUp() {
        veiculoDTO = new VeiculoDTO();
        veiculoDTO.setId(1L);
        veiculoDTO.setMarca("Fiat");
        veiculoDTO.setModelo("Uno");
        veiculoDTO.setAno(2020);
        veiculoDTO.setPlaca("ABC1234");

        VeiculoDTO veiculoDTO2 = new VeiculoDTO();
        veiculoDTO2.setId(2L);
        veiculoDTO2.setMarca("Volkswagen");
        veiculoDTO2.setModelo("Gol");
        veiculoDTO2.setAno(2021);
        veiculoDTO2.setPlaca("XYZ5678");

        veiculosList = Arrays.asList(veiculoDTO, veiculoDTO2);
    }

     @Test
    void cadastrar_ComDadosValidos_DeveRetornar201() throws Exception {
        when(veiculoService.cadastrarVeiculo(any(VeiculoDTO.class))).thenReturn(veiculoDTO);

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.marca").value("Fiat"))
                .andExpect(jsonPath("$.modelo").value("Uno"))
                .andExpect(jsonPath("$.ano").value(2020))
                .andExpect(jsonPath("$.placa").value("ABC1234"));
    }

    @Test
    void cadastrar_ComDadosInvalidos_DeveRetornar400() throws Exception {
        
        VeiculoDTO veiculoInvalido = new VeiculoDTO();
        veiculoInvalido.setMarca(""); 
        veiculoInvalido.setModelo(""); 
        veiculoInvalido.setAno(1899); 
        veiculoInvalido.setPlaca(""); 

        when(veiculoService.cadastrarVeiculo(any(VeiculoDTO.class)))
                .thenThrow(new IllegalArgumentException("Marca do veículo é obrigatória."));

        mockMvc.perform(post("/api/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(veiculoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Marca do veículo é obrigatória."));
    }

    @Test
    void listarTodos_ComVeiculosExistentes_DeveRetornar200() throws Exception {
        when(veiculoService.listarTodosVeiculos()).thenReturn(veiculosList);

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].marca").value("Fiat"))
                .andExpect(jsonPath("$[1].marca").value("Volkswagen"));
    }

     @Test
    void listarTodos_SemVeiculos_DeveRetornar200() throws Exception {
        when(veiculoService.listarTodosVeiculos()).thenReturn(List.of());

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
