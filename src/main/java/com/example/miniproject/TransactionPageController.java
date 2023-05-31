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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TransactionPageController implements Initializable {

    @FXML
    private Button BackBtn;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label priceLabel;

    @FXML
    private Label tid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();

            // Fetch the most recent transaction
            String query  = "SELECT * FROM (SELECT t.TransactionID, t.Buyer_Ph_no, t.Price, u.Name " +
                    "FROM Transactions t " +
                    "JOIN Users u ON t.Buyer_Ph_no = u.Ph_no " +
                    "ORDER BY t.TransactionID DESC) " +
                    "WHERE ROWNUM = 1";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String transactionID = rs.getString(1);
                String buyerPhone = rs.getString(2);
                String price = rs.getString(3);
                String buyerName = rs.getString(4);

                // Set the values to the labels
                tid.setText("Transaction ID: " + transactionID);
                phone.setText("Buyer Contact: " + buyerPhone);
                priceLabel.setText("Amount Paid: " + price);
                name.setText("Name of Buyer: " + buyerName);
            }

            rs.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
}
