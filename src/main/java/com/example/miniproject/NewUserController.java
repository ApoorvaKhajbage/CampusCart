package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NewUserController {
    @FXML
    private Button AddUserBtn;

    @FXML
    private Button BackBtnNewUser;

    @FXML
    private TextField NewUserEmail;

    @FXML
    private TextField NewUserName;

    @FXML
    private TextField NewUserPhone;

    @FXML
    private Label EmailError;

    @FXML
    private Label NameError;

    @FXML
    private Label PhoneError;

    public void AddUserBtnOnAction(ActionEvent event) throws IOException {
        String ph_no = NewUserPhone.getText();
        String email = NewUserEmail.getText();
        String name = NewUserName.getText();
        NameError.setText("");
        EmailError.setText("");
        PhoneError.setText("");
        boolean success = false;

        boolean isNameValid = validateName(name);
        boolean isEmailValid = validateEmail(email);
        boolean isPhoneValid = validatePhoneNumber(ph_no);
        boolean isAlreadyExists = checkInDB(ph_no);

        if (!isNameValid) {
            NameError.setText("Invalid name format");
        }
        if (!isEmailValid) {
            EmailError.setText("Invalid email format");
        }
        if (!isPhoneValid) {
            PhoneError.setText("Invalid phone number format");
        }
        if(isAlreadyExists){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("This Phone number already exists Try Searching this User.");
            alert.showAndWait();
        }
        if (isNameValid && isEmailValid && isPhoneValid && !isAlreadyExists) {
            try {
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                String add_user = "INSERT INTO Users (ph_no, name, email) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(add_user);
                preparedStatement.setString(1, ph_no);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    success = true;
                    write_in_file(name, ph_no);
                }
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (success) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHome.fxml"));
                Parent root = loader.load();
                UserHomeController userhomeController = loader.getController();
                Stage stage = (Stage) AddUserBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                System.out.println("Invalid Inputs");
            }
        }

    }

    private boolean validateName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private boolean validateEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean validatePhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }
    private boolean checkInDB(String phone) {
        boolean phoneNumberExists = false;

        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();

            String checkQuery = "SELECT * FROM Users WHERE ph_no = ?";
            PreparedStatement checkSt = con.prepareStatement(checkQuery);
            checkSt.setString(1, phone);
            ResultSet rs = checkSt.executeQuery();

            if (rs.next()) {
                phoneNumberExists = true;
            }

            rs.close();
            checkSt.close();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return phoneNumberExists;
    }

    public void BackBtnNewUserOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
            Parent root = loader.load();
            AdminHomeController adminHomeController = loader.getController();

            Stage stage = (Stage) BackBtnNewUser.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void reloadPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SellerPage.fxml"));
        Parent root = loader.load();
        SellerPageController sellerPageController = loader.getController();

        Stage stage = (Stage) AddUserBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    public void write_in_file(String name, String phone_no) throws IOException {
        FileOutputStream fos = new FileOutputStream("details.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(name);
        dos.writeUTF(phone_no);
    }
}