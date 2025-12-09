package com.sunnytech.gestorsol.repository;

import com.sunnytech.gestorsol.model.Produtor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    
    // Busca paginada apenas dos ativos (onde deletadoEm é nulo)
    Page<Produtor> findByDeletadoEmIsNull(Pageable pageable);

    // Busca por nome (LIKE) apenas nos ativos
    Page<Produtor> findByNomeContainingIgnoreCaseAndDeletadoEmIsNull(String nome, Pageable pageable);

    // Opcional: Busca por CPF para validações futuras
    Optional<Produtor> findByCpfCnpjAndDeletadoEmIsNull(String cpfCnpj);
}