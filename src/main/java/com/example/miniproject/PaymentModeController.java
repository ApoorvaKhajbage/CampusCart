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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentModeController implements Initializable {

    @FXML
    private Button cashBtn;

    @FXML
    private Button debitCreditBtn;

    @FXML
    private Label total_price;
    @FXML
    private Button BackBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection dc = new DatabaseConnection();
        Connection con = dc.getConnection();
        String query = "SELECT SUM(Price) FROM Cart";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                total_price.setText("The total Price to pay is: " + rs.getString(1));
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

    @FXML
    void cashBtnOnAction(ActionEvent event) {
        try {
            insertTransaction();
            deleteItemsFromBooksTable();
            clearCartTable();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Paymentsuccessful.fxml"));
            Parent root = loader.load();
            PaymentsuccessfulController paymentsuccessfulController = loader.getController();
            Stage stage = (Stage) cashBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void BackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();

            Stage stage = (Stage) BackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void debitCreditBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CardPay.fxml"));
            Parent root = loader.load();
            CardPayController cardPayController = loader.getController();

            Stage stage = (Stage) debitCreditBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertTransaction() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        try {
            String totalPriceQuery = "SELECT SUM(Price) AS TotalPrice FROM Cart";
            Statement priceStmt = con.createStatement();
            ResultSet priceRs = priceStmt.executeQuery(totalPriceQuery);

            double totalPrice = 0.0;
            if (priceRs.next()) {
                totalPrice = priceRs.getDouble("TotalPrice");
            }

            priceRs.close();
            priceStmt.close();

            String buyerPhNo = getBuyerPhoneNumber();

            String transactionCountQuery = "SELECT COUNT(*) AS TransactionCount FROM Transactions";
            Statement countStmt = con.createStatement();
            ResultSet countRs = countStmt.executeQuery(transactionCountQuery);

            int transactionCount = 0;
            if (countRs.next()) {
                transactionCount = 1+countRs.getInt("TransactionCount");
            }

            countRs.close();
            countStmt.close();

            // Insert into Transactions table
            String transactionInsertQuery = "INSERT INTO Transactions (TransactionID, Buyer_Ph_no, Price) " +
                    "VALUES (" + transactionCount + ", '" + buyerPhNo + "', " + totalPrice + ")";
            Statement insertStmt = con.createStatement();
            insertStmt.executeUpdate(transactionInsertQuery);
            insertStmt.close();

            // Insert into Transaction_details table
            String transactionDetailsInsertQuery = "INSERT INTO Transaction_details (TransactionID, Name_of_Book, Seller_Ph_no) " +
                    "SELECT " + transactionCount + ", Name_of_Book, Seller_Ph_no FROM Cart";
            Statement detailsInsertStmt = con.createStatement();
            detailsInsertStmt.executeUpdate(transactionDetailsInsertQuery);
            detailsInsertStmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
        }
    }

    private String getBuyerPhoneNumber() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        try {
            String buyerPhNoQuery = "SELECT DISTINCT(Buyer_Ph_no) FROM Cart";
            Statement buyerPhNoStmt = con.createStatement();
            ResultSet buyerPhNoRs = buyerPhNoStmt.executeQuery(buyerPhNoQuery);

            String buyerPhNo = null;
            if (buyerPhNoRs.next()) {
                buyerPhNo = buyerPhNoRs.getString("Buyer_Ph_no");
            }

            buyerPhNoRs.close();
            buyerPhNoStmt.close();

            return buyerPhNo;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
        }
    }

    private void deleteItemsFromBooksTable() throws SQLException {
        List<Book> cartItems = getCartItems();
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        try {
            String query = "DELETE FROM Books WHERE ISBN_NO = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);

            for (Book book : cartItems) {
                preparedStatement.setString(1, book.getIsbn());
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
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

    private List<Book> getCartItems() throws SQLException {
        List<Book> cartItems = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnection();

        try {
            String cartQuery = "SELECT * FROM Cart";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(cartQuery);

            while (resultSet.next()) {
                Book book = new Book();
                book.setIsbn(resultSet.getString("ISBN_NO"));
                book.setBook_price(resultSet.getString("Price"));
                book.setBook_name(resultSet.getString("Name_of_Book"));
                book.setSellerphn(resultSet.getString("Seller_Ph_no"));
                book.setEdt(resultSet.getString("Edition"));
                book.setYop(resultSet.getString("Year"));
                book.setCatg(resultSet.getString("category"));
                book.setAddedToCart(true); // Set addedToCart to true

                cartItems.add(book);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
        }
        return cartItems;
    }
}
