package com.sunnytech.gestorsol.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity // 1. Diz ao Spring: "Isso é uma tabela no banco"
@Table(name = "produtores") // 2. Nome da tabela (opcional, mas boa prática)
@Data // 3. LOMBOK: Gera Getters, Setters, toString, Equals e HashCode automaticamente!
@NoArgsConstructor // 4. Cria um construtor vazio (obrigatório para o JPA)
@AllArgsConstructor // 5. Cria um construtor com todos os campos
public class Produtor {

    @Id // Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment (como o serial/autoincrement)
    private Long id;

    @Column(nullable = false, length = 100) // Validação direto no banco: não pode ser nulo, max 100 chars
    private String nome;

    @Column(name = "cpf_cnpj", unique = true, nullable = false, length = 18)
    private String cpfCnpj;

    private String telefone;

    private String endereco;

    // --- REGRAS DE NEGÓCIO DA NOSSA ARQUITETURA ---
    
    // Lembra da nossa regra de Soft Delete? Não deletamos o registro, apenas marcamos como inativo.
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    // Um "gatilho" do Java para preencher a data antes de salvar a primeira vez
    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}
