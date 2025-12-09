package com.sunnytech.gestorsol.controller;

import com.sunnytech.gestorsol.model.Produtor;
import com.sunnytech.gestorsol.service.ProdutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Diz ao Spring: "Isso responde a requisições HTTP (REST)"
@RequestMapping("/api/produtores") // Prefixo da URL (igual ao Flask)
// @CrossOrigin(origins = "*") // Habilita o CORS se precisar (Electron as vezes pede)
public class ProdutorController {

    @Autowired
    private ProdutorService service;

    // GET /api/produtores?page=0&size=10&q=termo
    @GetMapping
    public ResponseEntity<Page<Produtor>> listar(
            @RequestParam(defaultValue = "0") int page, // Spring começa página em 0, Flask era 1
            @RequestParam(defaultValue = "10") int per_page, // mantive o nome do parâmetro pra compatibilidade
            @RequestParam(required = false) String q // 'q' igual ao seu frontend envia
    ) {
        // Criamos o objeto de paginação
        // Sort.by("nome") ordena alfabeticamente
        PageRequest pageRequest = PageRequest.of(page, per_page, Sort.by("nome"));
        
        Page<Produtor> produtores = service.listarTodos(pageRequest, q);
        return ResponseEntity.ok(produtores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produtor> buscarPorId(@PathVariable Long id) {
        Produtor produtor = service.buscarPorId(id);
        return ResponseEntity.ok(produtor);
    }

    @PostMapping
    public ResponseEntity<Produtor> criar(@RequestBody Produtor produtor) {
        Produtor novo = service.salvar(produtor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produtor> atualizar(@PathVariable Long id, @RequestBody Produtor produtor) {
        Produtor atualizado = service.atualizar(id, produtor);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}