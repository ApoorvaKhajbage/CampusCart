package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookController {
    Book b2;
    String nmbook;
    String prc;
    String yearp;
    String edtn;
    String catg;
    String sellerphn;
    String isbn;

    @FXML
    private Label book_name;

    @FXML
    private Label price;

    @FXML
    private Button view_details;
    @FXML
    private HBox BookBox;

    private CartController cartController;
    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }


    @FXML
    void view_details_of_book(ActionEvent event) throws SQLException {
        if((view_details.getText()).equals("View Details")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductDetails.fxml"));
                Parent root = loader.load();
                ProductDetailsController  productDetailsController = loader.getController();
                productDetailsController.setData(nmbook,prc,edtn,yearp,isbn);
                Stage stage = (Stage) view_details.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
                FXMLLoader fxmlloader = new FXMLLoader();
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                String deleteQuery = "Delete FROM Cart WHERE ISBN_NO ='"+ isbn +"'";
                Statement st = con.createStatement();
                int r = st.executeUpdate(deleteQuery);
            if (r > 0) {
                System.out.println("Successfully Deleted");
                cartController.refreshCart();
            }

            st.close();
                con.close();


        }

    }
    public void setText_to_view_details(){
        view_details.setText("View Details");
    }
    public void setText_to_Delete_products(){
        view_details.setText("Remove");
    }
    public void hideViewDetailsButton() {
        view_details.setVisible(false);
    }

    public void setData(Book b){
        //b2=b;
        book_name.setText(b.getBook_name());
        price.setText(b.getBook_price());
        nmbook=b.getBook_name();
        prc =b.getBook_price();
        edtn =b.getEdt();
        yearp = b.getYop();
        isbn = b.getIsbn();
        catg = b.getCatg();
        sellerphn=b.getSellerphn();
    }

}
