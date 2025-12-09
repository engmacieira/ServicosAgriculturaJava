package com.sunnytech.gestorsol;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

// 1. Não usamos mais @SpringBootApplication aqui na classe principal diretamente
//    Nós gerenciamos o contexto manualmente.
public class GestorSolApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        // 2. O JavaFX chama o init() primeiro. Aqui nós "ligamos" o Spring Boot.
        applicationContext = new SpringApplicationBuilder(GestorSolBoot.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // 3. Aqui o Spring já carregou. Vamos carregar a primeira tela.
        
        // Usamos o Contexto do Spring para carregar o FXML. 
        // Isso permite usar @Autowired dentro dos Controllers da tela!
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lista_produtores.fxml"));
        
        // Esta linha mágica diz: "Se o Controller precisar de injeção, use o Spring"
        loader.setControllerFactory(applicationContext::getBean);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle("Gestor Sol - Sistema Agrícola");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // 4. Quando fechar a janela, mata o Spring e o Banco
        applicationContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        // O ponto de entrada padrão do Java chama o JavaFX
        Application.launch(GestorSolApplication.class, args);
    }
}