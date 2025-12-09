package com.sunnytech.gestorsol;

public class GestorSolLauncher {
    
    public static void main(String[] args) {
        // Este é o truque: chamamos a classe principal daqui.
        // Isso dá tempo para o Classpath carregar as DLLs do JavaFX.
        GestorSolApplication.main(args);
    }
}