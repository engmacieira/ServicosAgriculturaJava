package com.sunnytech.gestorsol.view;

import com.sunnytech.gestorsol.model.Produtor;
import com.sunnytech.gestorsol.service.ProdutorService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormProdutorController {

    @Autowired
    private ProdutorService service;

    @FXML private TextField txtNome;
    @FXML private TextField txtApelido;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtRegiao;
    @FXML private TextField txtReferencia;

    private Produtor produtorAtual;
    private Runnable onSaveCallback; // Uma função para chamar quando salvar (ex: atualizar a lista)

    // Método chamado por quem abre a tela para passar o produtor (ou null se for novo)
    public void setProdutor(Produtor produtor, Runnable onSaveCallback) {
        this.produtorAtual = produtor;
        this.onSaveCallback = onSaveCallback;

        if (produtor != null) {
            // Modo Edição: Preenche os campos
            txtNome.setText(produtor.getNome());
            txtApelido.setText(produtor.getApelido());
            txtCpf.setText(produtor.getCpfCnpj());
            txtTelefone.setText(produtor.getTelefone());
            txtRegiao.setText(produtor.getRegiao());
            txtReferencia.setText(produtor.getReferencia());
        } else {
            // Modo Criação: Garante objeto novo
            this.produtorAtual = new Produtor();
        }
    }

    @FXML
    public void aoSalvar() {
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            mostrarAlerta("Erro", "O nome é obrigatório.");
            return;
        }

        // Coleta dados da tela para o objeto
        produtorAtual.setNome(txtNome.getText());
        produtorAtual.setApelido(txtApelido.getText());
        produtorAtual.setCpfCnpj(txtCpf.getText());
        produtorAtual.setTelefone(txtTelefone.getText());
        produtorAtual.setRegiao(txtRegiao.getText());
        produtorAtual.setReferencia(txtReferencia.getText());

        try {
            // O Service decide se salva (novo ID) ou atualiza (ID existente)
            service.salvar(produtorAtual);
            
            mostrarAlerta("Sucesso", "Produtor salvo com sucesso!");
            
            // Roda a função de atualizar a lista lá da outra tela
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            
            fecharJanela();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao salvar: " + e.getMessage());
        }
    }

    @FXML
    public void aoCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        // Pega a referência da janela atual (Stage) e fecha
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}