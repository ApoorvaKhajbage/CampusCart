package com.example.miniproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Label;

public class SellerPageController {

    @FXML
    private Button AddBtn;

    @FXML
    private Label CatgError;

    @FXML
    private Label ISBNError;

    @FXML
    private TextField ISBNno;

    @FXML
    private TextField NameofBook;

    @FXML
    private TextField Price;

    @FXML
    private Button SellerBackBtn;

    @FXML
    private TextField category;

    @FXML
    private TextField edition;

    @FXML
    private Label priceError;

    @FXML
    private Label yopError;

    @FXML
    private TextField yrofpurchase;

    @FXML
    void SellerBackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();

            Stage stage = (Stage) SellerBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AddBtnOnAction(ActionEvent event) throws IOException {
        String Isbnno = ISBNno.getText();
        String Nameofbook = NameofBook.getText();
        String price = Price.getText();
        String edt = edition.getText();
        String y = yrofpurchase.getText();
        String ct = category.getText();
        boolean success = false;
        FileInputStream fis = new FileInputStream("details.txt");
        DataInputStream dis = new DataInputStream(fis);
        String name = dis.readUTF();
        String phone_no = dis.readUTF();
        CatgError.setText("");
        ISBNError.setText("");
        priceError.setText("");
        yopError.setText("");
        boolean isNameValid = validateCharacters(ct);
        boolean isISBNValid =  validateNumber(Isbnno);
        boolean isPriceValid = validateNumber(price);
        boolean isYearValid =  validateNumber(y);
        if (!isNameValid) {
            CatgError.setText("Invalid name format");
        }
        if (!isISBNValid) {
            ISBNError.setText("Invalid ISBN format Only digits accepted");
        }
        if (!isPriceValid) {
            priceError.setText("Invalid Price format Only digits accepted");
        }
        if(!isYearValid){
            yopError.setText("Invalid Year format Only digits accepted");
        }
        if(isNameValid && isISBNValid && isPriceValid && isYearValid){
            try {

                DatabaseConnection oj = new DatabaseConnection();
                Connection con = oj.getConnection();
                String add_user = "INSERT INTO Books (ISBN_NO, Price, Name_of_Book, Ph_no, Edition, Year, category) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(add_user);
                preparedStatement.setString(1, Isbnno);
                preparedStatement.setString(2, price);
                preparedStatement.setString(3, Nameofbook);
                preparedStatement.setString(4, phone_no);
                preparedStatement.setString(5, edt);
                preparedStatement.setString(6, y);
                preparedStatement.setString(7, ct);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    success = true;
                }
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (success) {
                AddBtn.setText("Added");

                // Show pop-up message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book added");
                alert.showAndWait();

                // Reload the page
                reloadPage();
            } else {
                System.out.println("Invalid Details");
            }
        }

    }
    private void reloadPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SellerPage.fxml"));
        Parent root = loader.load();
        SellerPageController sellerPageController = loader.getController();

        Stage stage = (Stage) AddBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    private boolean validateCharacters(String name) {
        return name.matches("[a-zA-Z]+");
    }
    private boolean validateNumber(String n) {return n.matches("[0-9]+");  }

}
