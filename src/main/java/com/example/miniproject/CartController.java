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
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML
    private Button BuyBtn;

    @FXML
    private Button CartBackBtn;
    @FXML
    private ScrollPane Scroller;

    @FXML
    private VBox ScrollerVbox;

    @FXML
    private Label total_price;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            box_fill_products();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();
        String query = "SELECT SUM(Price) FROM Cart";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                total_price.setText("Total: " + rs.getString(1));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void BuyBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentMode.fxml"));
            Parent root = loader.load();
            PaymentModeController paymentModeController = loader.getController();

            Stage stage = (Stage) BuyBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CartBackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BuyerPage.fxml"));
            Parent root = loader.load();
            BuyerPageController buyerPageController = loader.getController();

            Stage stage = (Stage) CartBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

void box_fill_products() throws SQLException {
    ScrollerVbox.getChildren().clear();

    List<Book> ls = new ArrayList<>(book_list());
    System.out.println(ls.size());

    for (int j = 0; j < ls.size(); j++) {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("book.fxml"));
        try {
            HBox hbox = fxmlloader.load();
            ScrollPane scrollpane = new ScrollPane();
            scrollpane.setContent(hbox);
            BookController bc = fxmlloader.getController();
            bc.setText_to_Delete_products();
            bc.setData(ls.get(j));
            bc.setCartController(this);
            ScrollerVbox.getChildren().add(scrollpane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    public List<Book> book_list() throws SQLException {
        List<Book> ls = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();
        try{
            String BookList ="SELECT * FROM Cart";
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
    public void refreshCart() throws SQLException {
        box_fill_products();
    }

}
