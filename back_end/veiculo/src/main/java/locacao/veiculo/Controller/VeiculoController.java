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

import locacao.veiculo.DTO.VeiculoDTO;
import locacao.veiculo.Service.VeiculoService;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
    
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> cadastrar(@RequestBody VeiculoDTO veiculoDTO) {
        VeiculoDTO novoVeiculo = veiculoService.cadastrarVeiculo(veiculoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
    }

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listarTodos() {
        List<VeiculoDTO> veiculos = veiculoService.listarTodosVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarPorId(@PathVariable Long id) {
        VeiculoDTO veiculo = veiculoService.buscarVeiculoPorId(id);
        return ResponseEntity.ok(veiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizar(@PathVariable Long id, @RequestBody VeiculoDTO veiculoDTO) {
        VeiculoDTO veiculoAtualizado = veiculoService.atualizarVeiculo(id, veiculoDTO);
        return ResponseEntity.ok(veiculoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        veiculoService.excluirVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}

