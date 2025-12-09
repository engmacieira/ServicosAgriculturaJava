package com.sunnytech.gestorsol.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import com.sunnytech.gestorsol.model.Produtor;
import com.sunnytech.gestorsol.service.ProdutorService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // Importante: Diz pro Spring que ele gerencia essa classe!
public class ListaProdutoresController {

    @Autowired
    private ProdutorService produtorService;

    @Autowired
    private ApplicationContext context; // Para carregar novas telas

    @FXML private TextField txtBusca;
    @FXML private TableView<Produtor> tabelaProdutores;

    @FXML
    public void initialize() {
        // Esse método roda automaticamente quando a tela abre
        carregarDados();
    }

    private void carregarDados() {
        // Por enquanto, trazendo a primeira página (Page 0, 100 itens)
        // No futuro implementamos a paginação visual
        Page<Produtor> pagina = produtorService.listarTodos(PageRequest.of(0, 100), null);
        List<Produtor> lista = pagina.getContent();
        
        // O JavaFX precisa de uma ObservableList
        tabelaProdutores.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    public void aoBuscar() {
        String termo = txtBusca.getText();
        Page<Produtor> pagina = produtorService.listarTodos(PageRequest.of(0, 100), termo);
        tabelaProdutores.setItems(FXCollections.observableArrayList(pagina.getContent()));
    }

    @FXML
    public void aoCriarNovo() {
        abrirFormulario(null); // Passa null para criar novo
    }

    @FXML
    public void aoEditar() {
        Produtor selecionado = tabelaProdutores.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            abrirFormulario(selecionado); // Passa o selecionado para editar
        } else {
            mostrarAlerta("Atenção", "Selecione um produtor na tabela.");
        }
    }

    @FXML
    public void aoExcluir() {
        Produtor selecionado = tabelaProdutores.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            produtorService.inativar(selecionado.getId());
            carregarDados(); // Atualiza a tabela
            mostrarAlerta("Sucesso", "Produtor inativado.");
        } else {
            mostrarAlerta("Atenção", "Selecione um produtor para excluir.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void abrirFormulario(Produtor produtor) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form_produtor.fxml"));
        // O Pulo do Gato: Usar o contexto do Spring para criar o Controller do formulário
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        // Pegamos o controller para passar os dados
        FormProdutorController controller = loader.getController();
        // Passamos o produtor (ou null) e uma função para recarregar a tabela ao fechar
        controller.setProdutor(produtor, this::carregarDados); 

        Stage stage = new Stage();
        stage.setTitle(produtor == null ? "Novo Produtor" : "Editar Produtor");
        stage.setScene(new Scene(root));

        // Modality.WINDOW_MODAL bloqueia a janela de trás até fechar essa
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tabelaProdutores.getScene().getWindow());

        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Erro", "Não foi possível abrir o formulário.");
    }
}
}