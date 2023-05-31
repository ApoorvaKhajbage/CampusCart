package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {

    @FXML
    private Button AddUser;

    @FXML
    private Button AvailaBtn;

    @FXML
    private AnchorPane CardPaneForDefault;

    @FXML
    private Button DashBtn;

    @FXML
    private Button SearchUser;

    @FXML
    private Button SignOut;

    @FXML
    private VBox book_box;

    @FXML
    private ScrollPane scroller;
    @FXML
    private Label TotalBooks;

    @FXML
    private Label TotalCust;
    @FXML
    private TextField SearchBar;
    @FXML
    private HBox SearchBox;
    @FXML
    private HBox tablebar;

    String txt_pressed = null;

    public void SearchUserOnAction(ActionEvent event) {
        try {
            clearCartTable();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchUser.fxml"));
            Parent root = loader.load();
            SearchUserController searchUserController = loader.getController();
            Stage stage = (Stage) SearchUser.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void box_fill_products(String searchText) throws SQLException {
        book_box.getChildren().clear();

        List<Book> ls = new ArrayList<>(book_list(searchText));
        System.out.println(ls.size());

        for (int j = 0; j < ls.size(); j++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("book.fxml"));

            try {
                HBox hbox = fxmlloader.load();
                ScrollPane scrollpane = new ScrollPane();
                scrollpane.setContent(hbox);
                BookController bc = fxmlloader.getController();
                bc.hideViewDetailsButton();
                //bc.setText_to_view_details();
                bc.setData(ls.get(j));
                book_box.getChildren().add(scrollpane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void SignOutOnAction(ActionEvent event) {
        try {
            clearCartTable();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
            Parent root = loader.load();
            AdminLoginController adminLoginController = loader.getController();
            // Pass any necessary data to the admin login controller if needed
            // adminLoginController.setData(...);

            Stage stage = (Stage) SignOut.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void AddUserOnAction(ActionEvent event) {
        try {
            clearCartTable();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewUser.fxml"));
            Parent root = loader.load();
            NewUserController newUserController = loader.getController();


            Stage stage = (Stage) AddUser.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> book_list(String searchText) throws SQLException {
        List<Book> ls = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        try {
            String bookListQuery;
            if (searchText != null && !searchText.isEmpty()) {
                bookListQuery = "SELECT * FROM Books WHERE LOWER(name_of_Book) LIKE '%" + searchText.toLowerCase() + "%'";
            } else {
                bookListQuery = "SELECT * FROM Books";
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(bookListQuery);
            while (rs.next()) {
                Book bk = new Book();
                bk.setBook_name(rs.getString(3));
                bk.setBook_price(rs.getString(2));
                bk.setEdt(rs.getString(5));
                bk.setYop(rs.getString(6));
                bk.setIsbn(rs.getString(1));
                bk.setSellerphn(rs.getString(4));
                bk.setCatg(rs.getString(7));
                ls.add(bk);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        con.close();
        return ls;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            box_fill_products("");
            scroller.setVisible(false);
            SearchBox.setVisible(false);
            tablebar.setVisible(false);
            CardPaneForDefault.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();
        String totalBooksQuery = "SELECT count(*) FROM Books";
        String totalCustQuery ="SELECT count(*) FROM Users";
        try {
            Statement st1 = con.createStatement();
            Statement st2 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(totalBooksQuery);
            ResultSet rs2 = st2.executeQuery(totalCustQuery);
            while(rs1.next()){
                TotalBooks.setText(rs1.getString(1));
            }
            while(rs2.next()){
                TotalCust.setText(rs2.getString(1));
            }
            st1.close();
            st2.close();
            rs1.close();
            rs2.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void DashBtnOnAction(ActionEvent event) {
        scroller.setVisible(false);
        tablebar.setVisible(false);
        SearchBox.setVisible(false);
        CardPaneForDefault.setVisible(true);
    }

    public void SearchBarOnKeyPressed(KeyEvent event) throws SQLException {
        String searchText = SearchBar.getText();
        box_fill_products(searchText);
    }


    public void AvailaBtnOnAction(ActionEvent event) {
        scroller.setVisible(true);
        SearchBox.setVisible(true);
        tablebar.setVisible(true);
        CardPaneForDefault.setVisible(false);
    }
    private void clearCartTable() throws SQLException {
        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();
        String query = "TRUNCATE TABLE Cart";
        Statement st = con.createStatement();
        int rs = st.executeUpdate(query);
        if (rs > 0) {
            System.out.println("Cart table empty!");
        }
        st.close();
        con.close();
    }
}



