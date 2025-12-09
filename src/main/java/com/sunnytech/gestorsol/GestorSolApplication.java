package com.sunnytech.gestorsol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 1. Essa anotação configura TUDO (Banco, Web, Injeção) automaticamente.
public class GestorSolApplication {

    public static void main(String[] args) {
        // 2. Aqui o Spring 'boot' (inicia) a aplicação
        SpringApplication.run(GestorSolApplication.class, args);
    }
}