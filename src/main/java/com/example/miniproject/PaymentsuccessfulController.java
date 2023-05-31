package com.example.miniproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentsuccessfulController {

    @FXML
    private Button BackBtn;

    @FXML
    private Button TransacBtn;

    @FXML
    void BackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();

            Stage stage = (Stage) BackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void TransacBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionPage.fxml"));
            Parent root = loader.load();
            TransactionPageController transactionPageController = loader.getController();

            Stage stage = (Stage) BackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

