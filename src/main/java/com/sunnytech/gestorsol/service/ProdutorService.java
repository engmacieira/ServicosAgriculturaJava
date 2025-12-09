package com.sunnytech.gestorsol.service;

import com.sunnytech.gestorsol.model.Produtor;
import com.sunnytech.gestorsol.repository.ProdutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Diz ao Spring: "Aqui tem regras de negócio"
public class ProdutorService {

    @Autowired // Injeção de Dependência: O Spring traz o Repository pronto pra gente
    private ProdutorRepository repository;

    // Listagem com Paginação e Busca (Refatorando sua lógica do Python)
    public Page<Produtor> listarTodos(Pageable paginacao, String termoBusca) {
        if (termoBusca != null && !termoBusca.isBlank()) {
            // Se tiver busca, usamos um método customizado (vamos adicionar no Repo rapidinho)
            // Por enquanto, vamos buscar pelo nome para simplificar a migração inicial
            return repository.findByNomeContainingIgnoreCase(termoBusca, paginacao);
        }
        // Se não, traz tudo paginado (filtrando os ativos, claro)
        return repository.findByAtivoTrue(paginacao);
    }

    public Produtor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produtor não encontrado")); 
                // Numa aplicação real, usaríamos uma Exception personalizada (ResourceNotFoundException)
    }

    public Produtor salvar(Produtor produtor) {
        // Regra de Negócio: Verificar se CPF já existe antes de salvar?
        // O banco já tem a constraint unique, mas aqui poderíamos tratar melhor.
        return repository.save(produtor);
    }

    public Produtor atualizar(Long id, Produtor dadosAtualizados) {
        Produtor produtorExistente = buscarPorId(id);
        
        // Atualizamos apenas os campos permitidos
        produtorExistente.setNome(dadosAtualizados.getNome());
        produtorExistente.setCpfCnpj(dadosAtualizados.getCpfCnpj());
        produtorExistente.setTelefone(dadosAtualizados.getTelefone());
        produtorExistente.setEndereco(dadosAtualizados.getEndereco());
        // Não atualizamos dataCadastro nem ID, por segurança
        
        return repository.save(produtorExistente);
    }

    public void inativar(Long id) {
        Produtor produtor = buscarPorId(id);
        produtor.setAtivo(false); // Soft Delete: Apenas marcamos como inativo
        repository.save(produtor);
    }
}