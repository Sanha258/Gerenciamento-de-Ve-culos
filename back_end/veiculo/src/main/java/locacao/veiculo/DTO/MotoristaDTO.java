package locacao.veiculo.DTO;

import java.time.LocalDate;

public class MotoristaDTO {
    
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String cnh;
    private LocalDate validadeCnh;
    private String telefone;
    private EnderecoDTO endereco;
    
    public MotoristaDTO() {
    }

    public MotoristaDTO(Long id, String nome, String sobrenome, String cpf, String cnh, LocalDate validadeCnh,
            String telefone, EnderecoDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public LocalDate getValidadeCnh() {
        return validadeCnh;
    }

    public void setValidadeCnh(LocalDate validadeCnh) {
        this.validadeCnh = validadeCnh;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    
}
