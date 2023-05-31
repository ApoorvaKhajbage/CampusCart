package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable {

    @FXML
    private Button BuyBtn;

    @FXML
    private Button SellBtn;

    @FXML
    private Button SignOut;

    @FXML
    private Label Acc_name;

    String name = null;
    String phone_no = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FileInputStream fis = new FileInputStream("details.txt");
            DataInputStream dis = new DataInputStream(fis);
            name = dis.readUTF();
            phone_no = dis.readUTF();
            Acc_name.setText(name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void BuyBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BuyerPage.fxml"));
            Parent root = loader.load();
            BuyerPageController buyerPageController = loader.getController();

            Stage stage = (Stage) BuyBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SellBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SellerPage.fxml"));
            Parent root = loader.load();
            SellerPageController sellerPageController = loader.getController();

            Stage stage = (Stage) SellBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SignOutOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
            Parent root = loader.load();
            AdminHomeController adminHomeController = loader.getController();

            Stage stage = (Stage) SignOut.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
