package com.sunnytech.gestorsol.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtores") // Nome da tabela igual ao Python
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produtor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produtor_id") // <--- O PULO DO GATO! Mapeia 'produtor_id' do banco para 'id' do Java
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    private String apelido; // O nome é igual no banco e no Java, não precisa de @Column

    // No banco Python chama "cpf", no Java chamamos "cpfCnpj". Mapeamos aqui:
    @Column(name = "cpf", unique = true, length = 18) 
    private String cpfCnpj;

    private String regiao;
    
    private String referencia;

    private String telefone;

    // --- A questão do Soft Delete ---
    // No Python você usava a coluna 'deletado_em' (Timestamp) para saber se estava deletado.
    // Vamos manter essa lógica para não quebrar o banco legado.
    
    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    // Campo auxiliar para o Java saber se está ativo (não salvo no banco, calculado na hora)
    // Se deletadoEm for nulo, está ativo.
    public boolean isAtivo() {
        return deletadoEm == null;
    }
}