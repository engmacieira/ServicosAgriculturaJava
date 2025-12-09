package com.sunnytech.gestorsol.service;

import com.sunnytech.gestorsol.model.Produtor;
import com.sunnytech.gestorsol.repository.ProdutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProdutorService {

    @Autowired
    private ProdutorRepository repository;

    /**
     * Lista produtores com paginação e filtro de busca (Nome).
     * Ignora registros marcados como deletados (deletadoEm != null).
     */
    public Page<Produtor> listarTodos(Pageable paginacao, String termoBusca) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            // Busca por nome ignorando maiúsculas/minúsculas E garantindo que não foi deletado
            return repository.findByNomeContainingIgnoreCaseAndDeletadoEmIsNull(termoBusca, paginacao);
        }
        // Traz todos que não têm data de deleção
        return repository.findByDeletadoEmIsNull(paginacao);
    }

    public Produtor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produtor não encontrado com ID: " + id));
    }

    public Produtor salvar(Produtor produtor) {
        // Aqui você pode adicionar validações extras antes de salvar (ex: verificar CPF duplicado manualmente)
        return repository.save(produtor);
    }

    public Produtor atualizar(Long id, Produtor dadosAtualizados) {
        Produtor produtorExistente = buscarPorId(id);
        
        // Atualizamos apenas os dados cadastrais, preservando ID e datas
        produtorExistente.setNome(dadosAtualizados.getNome());
        produtorExistente.setApelido(dadosAtualizados.getApelido());
        produtorExistente.setCpfCnpj(dadosAtualizados.getCpfCnpj());
        produtorExistente.setTelefone(dadosAtualizados.getTelefone());
        produtorExistente.setRegiao(dadosAtualizados.getRegiao());
        produtorExistente.setReferencia(dadosAtualizados.getReferencia());
        
        return repository.save(produtorExistente);
    }

    public void inativar(Long id) {
        Produtor produtor = buscarPorId(id);
        
        // Lógica de Soft Delete compatível com seu banco legado:
        // Define a data atual na coluna 'deletado_em' em vez de apagar o registro.
        produtor.setDeletadoEm(LocalDateTime.now());
        
        repository.save(produtor);
    }
}