package locacao.veiculo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import locacao.veiculo.DTO.MotoristaDTO;
import locacao.veiculo.Service.MotoristaService;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {
    
    private final MotoristaService motoristaService;

    public MotoristaController(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    @PostMapping
    public ResponseEntity<MotoristaDTO> cadastrar(@RequestBody MotoristaDTO motoristaDTO) {
        MotoristaDTO novoMotorista = motoristaService.cadastrarMotorista(motoristaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMotorista);
    }

    @GetMapping
    public ResponseEntity<List<MotoristaDTO>> listarTodos() {
        List<MotoristaDTO> motoristas = motoristaService.listarTodosMotoristas();
        return ResponseEntity.ok(motoristas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoristaDTO> buscarPorId(@PathVariable Long id) {
        MotoristaDTO motorista = motoristaService.buscarMotoristaPorId(id);
        return ResponseEntity.ok(motorista);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<MotoristaDTO> buscarPorCpf(@PathVariable String cpf) {
        MotoristaDTO motorista = motoristaService.buscarMotoristaPorCpf(cpf);
        return ResponseEntity.ok(motorista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoristaDTO> atualizar(@PathVariable Long id, @RequestBody MotoristaDTO motoristaDTO) {
        MotoristaDTO motoristaAtualizado = motoristaService.atualizarMotorista(id, motoristaDTO);
        return ResponseEntity.ok(motoristaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        motoristaService.excluirMotorista(id);
        return ResponseEntity.noContent().build();
    }
}
