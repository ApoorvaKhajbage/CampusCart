package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminLoginController {
    @FXML
    private Label CautionLabel;
    @FXML
    private Button AdminCancelBtn;

    @FXML
    private Button AdminLoginBtn;

    @FXML
    private PasswordField AdminPswd;

    @FXML
    private TextField AdminUsername;

    public void AdminCancelOnAction(ActionEvent event) {
        Stage stage =(Stage) AdminCancelBtn.getScene().getWindow();
        stage.close();
    }

    public void AdminLoginOnAction(ActionEvent event) throws SQLException, IOException {
        String username = AdminUsername.getText();
        String password = AdminPswd.getText();

        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();
        boolean check = false;
        try{
            Statement st = con.createStatement();
            String verifyAdmin = "SELECT * FROM Admins WHERE Username = '" + username +"' AND password = '" + password + "'";

            ResultSet rs = st.executeQuery(verifyAdmin);
            while(rs.next()){
                check = true;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        con.close();

        if(check){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
                Parent root = loader.load();
                AdminHomeController adminHomeController = loader.getController();

                Stage stage = (Stage) AdminLoginBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        else
        {
            // Login failed, display an error message
            System.out.println("Invalid username or password.");
            CautionLabel.setText("Invalid username or password.");
        }


    }
}