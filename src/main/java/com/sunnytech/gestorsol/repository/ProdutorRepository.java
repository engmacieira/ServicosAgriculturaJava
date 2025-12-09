package com.sunnytech.gestorsol.repository;

import com.sunnytech.gestorsol.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository // Diz ao Spring: "Isso é um componente que acessa dados"
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {

    // --- MÉTODOS MÁGICOS (Query Methods) ---
    
    // Só de declarar o método com esse nome, o Spring cria o SQL:
    // "SELECT * FROM produtores WHERE cpf_cnpj = ?"
    Optional<Produtor> findByCpfCnpj(String cpfCnpj);

    // SQL gerado: "SELECT * FROM produtores WHERE ativo = true"
    List<Produtor> findByAtivoTrue();

    // SQL gerado: "SELECT * FROM produtores WHERE nome LIKE %?%"
    List<Produtor> findByNomeContainingIgnoreCase(String nome);
    
    Page<Produtor> findByAtivoTrue(Pageable pageable);
    Page<Produtor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    // Você não precisa implementar nada! O Spring faz o corpo do método pra você.
}
