package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductDetailsController implements Initializable {
    String isbn_no;
    String name;
    String phone_no;
    String nmbook = null;
    String prc = null;
    String yearp = null;
    String edtn = null;
    String catg = null;
    String sellerphn = null;

    @FXML
    private Button AddToCart;

    @FXML
    private Button BackBtn;

    @FXML
    private Label BookName;

    @FXML
    private Label Price;

    @FXML
    private Label edition;

    @FXML
    private Label yop;

    public void AddToCartOnAction(ActionEvent event) throws SQLException {
        // Retrieve the data for the cart entry
        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();

        String Query = "SELECT * FROM Books WHERE ISBN_NO = ?";
        PreparedStatement st = con.prepareStatement(Query);
        st.setString(1, isbn_no);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            nmbook = rs.getString(3);
            prc = rs.getString(2);
            yearp = rs.getString(6);
            edtn = rs.getString(5);
            catg = rs.getString(7);
            sellerphn = rs.getString(4);
        }

        rs.close();

        String checkQuery = "SELECT * FROM Cart WHERE ISBN_NO = ? ";
        PreparedStatement checkSt = con.prepareStatement(checkQuery);
        checkSt.setString(1, isbn_no);
        ResultSet checkRs = checkSt.executeQuery();

        if (checkRs.next()) {
            // Book already added to cart, show a pop-up message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Book already added to cart");
            alert.showAndWait();
        } else {
            // Book not yet added to cart, insert the entry
            String add = "INSERT INTO Cart VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertSt = con.prepareStatement(add);
            insertSt.setString(1, isbn_no);
            insertSt.setString(2, prc);
            insertSt.setString(3, nmbook);
            insertSt.setString(4, sellerphn);
            insertSt.setString(5, edtn);
            insertSt.setString(6, yearp);
            insertSt.setString(7, catg);
            insertSt.setString(8, phone_no);
            int rows = insertSt.executeUpdate();

            if (rows > 0) {
                AddToCart.setText("Added To Cart");

                // Show a pop-up message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Book added to cart");
                alert.showAndWait();
            }

            insertSt.close();
        }

        checkRs.close();
        checkSt.close();
        st.close();
        con.close();
    }


    public void ProductDetailsBackBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BuyerPage.fxml"));
            Parent root = loader.load();
            BuyerPageController buyerPageController = loader.getController();

            Stage stage = (Stage) BackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setData(String bkname, String price, String edt, String yp, String isbn) {
        BookName.setText(bkname);
        Price.setText(price);
        edition.setText(edt);
        yop.setText(yp);
        isbn_no = isbn;
        System.out.println(isbn_no);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FileInputStream fis = new FileInputStream("details.txt");
            DataInputStream dis = new DataInputStream(fis);
            name = dis.readUTF();
            phone_no = dis.readUTF();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
