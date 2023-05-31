package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class BuyerPageController implements Initializable {
    @FXML
    private Button BuyerBackBtn;

    @FXML
    private ToggleGroup Category;

    @FXML
    private RadioButton CivilRadio;

    @FXML
    private RadioButton CommonRadio;

    @FXML
    private RadioButton CompRadio;

    @FXML
    private RadioButton ITRadio;

    @FXML
    private RadioButton MechRadio;
    @FXML
    private RadioButton ALL;

    @FXML
    private TextField SearchBar;

    @FXML
    private VBox book_box;
    @FXML
    private Button CartBtn;


    String txt_pressed = null;
    String Catg_select = null;

    public void BuyerBackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();

            Stage stage = (Stage) BuyerBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void box_fill_products() throws SQLException {
        book_box.getChildren().clear();

        List<Book> ls = new ArrayList<>(book_list());
        System.out.println(ls.size());

        for(int j=0; j<ls.size(); j++)
        {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("book.fxml"));

            try{
                HBox hbox = fxmlloader.load();
                ScrollPane scrollpane = new ScrollPane();
                scrollpane.setContent(hbox);
                BookController bc = fxmlloader.getController();
                bc.setText_to_view_details();
                bc.setData(ls.get(j));
                book_box.getChildren().add(scrollpane);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public List<Book> book_list() throws SQLException {
        List<Book> ls = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        try{
            String BookList ;
            if(txt_pressed == null && Catg_select ==null || Catg_select =="ALL"){
                BookList="SELECT * FROM Books";
            }
            else if (txt_pressed == null && Catg_select !=null) {
                BookList="SELECT * FROM Books WHERE category LIKE '" + Catg_select +"'";
            }
            else if (txt_pressed != null && Catg_select ==null){
                BookList="SELECT * FROM Books WHERE LOWER(name_of_Book) LIKE '" + txt_pressed + "'";
            } else  {
                BookList="SELECT * FROM Books WHERE LOWER(name_of_Book) LIKE '" + txt_pressed + "'AND category LIKE '" + Catg_select +"'";
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(BookList);
            while(rs.next()){
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
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        con.close();
        return ls;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            box_fill_products();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void CmnRadioOnAction(ActionEvent event) throws SQLException {
        Catg_select = "Common";
        box_fill_products();
    }

    public void MechRadioOnAction(ActionEvent event) throws SQLException {
        Catg_select = "Mech";
        box_fill_products();
    }

    public void ITRadioOnAction(ActionEvent event) {
        Catg_select = "IT";
    }

    public void CompRadioOnAction(ActionEvent event) throws SQLException {
        Catg_select = "Comp";
        box_fill_products();
    }

    public void CivilRadioOnAction(ActionEvent event) throws SQLException {
        Catg_select = "Civil";
        box_fill_products();
    }

    public void Searched(KeyEvent keyEvent) throws SQLException {
        txt_pressed = "%" + SearchBar.getText() +"%";
        box_fill_products();
    }
    public void CartBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();

            Stage stage = (Stage) CartBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ALLRadioOnAction(ActionEvent event) throws SQLException {
        Catg_select="ALL";
        box_fill_products();
    }
}