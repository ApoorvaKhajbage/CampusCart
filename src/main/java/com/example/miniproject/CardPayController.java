//package com.example.miniproject;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//import java.io.IOException;
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//
//public class CardPayController {
//
//    @FXML
//    private Button BackBtn;
//
//    @FXML
//    private Button PayBtn;
//    @FXML
//    private Label CVVError;
//
//    @FXML
//    private Label CardNoError;
//
//    @FXML
//    private Label FirstNameError;
//
//    @FXML
//    private Label LastNameError;
//
//    @FXML
//    private Label ValidityError;
//
//    @FXML
//    private TextField cardNumberField;
//
//    @FXML
//    private DatePicker cardValidityField;
//
//    @FXML
//    private TextField cvvField;
//
//    @FXML
//    private TextField firstNameField;
//
//    @FXML
//    private TextField lastNameField;
//
//    @FXML
//    private AnchorPane rootPane;
//
//    @FXML
//    void PayBtnOnAction(ActionEvent event) throws SQLException, IOException {
//        CardNoError.setText("");
//        FirstNameError.setText("");
//        LastNameError.setText("");
//        CVVError.setText("");
//        ValidityError.setText("");
//        List<String> errorMessages = new ArrayList<>();
//
//        try {
//            // Get card details
//            String cardNumber = cardNumberField.getText();
//            String firstName = firstNameField.getText();
//            String lastName = lastNameField.getText();
//            String cvv = cvvField.getText();
//            LocalDate cardValidityDate = cardValidityField.getValue();
//
//            // Validate card details
//            String cardNumberPattern = "\\d{16}"; // 16 digits only
//            if (!cardNumber.matches(cardNumberPattern)) {
//                errorMessages.add("INVALID CARD Number.");
//            }
//
//            String namePattern = "[a-zA-Z]+"; // Only characters
//            if (!firstName.matches(namePattern)) {
//                errorMessages.add("First Name should only have characters.");
//            }
//
//            if (!lastName.matches(namePattern)) {
//                errorMessages.add("Last Name should only have characters.");
//            }
//
//            if (cvv.length() != 3) {
//                errorMessages.add("CVV Number should only be of 3 length.");
//            }
//
//            if (cardValidityDate == null || cardValidityDate.isBefore(LocalDate.now())) {
//                errorMessages.add("Card Validity Expired.");
//            }
//
//            if (!errorMessages.isEmpty()) {
//                // Display error messages
//                CardNoError.setText(errorMessages.contains("INVALID CARD Number.") ? "INVALID CARD Number." : "");
//                FirstNameError.setText(errorMessages.contains("First Name should only have characters.") ? "First Name should only have characters." : "");
//                LastNameError.setText(errorMessages.contains("Last Name should only have characters.") ? "Last Name should only have characters." : "");
//                CVVError.setText(errorMessages.contains("CVV Number should only be of 3 length.") ? "CVV Number should only be of 3 length." : "");
//                ValidityError.setText(errorMessages.contains("Card Validity Expired.") ? "Card Validity Expired." : "");
//
//                return; // Stop execution if there are errors
//            }
//        insertTransaction();
//        deleteItemsFromBooksTable();
//        DatabaseConnection dc = new DatabaseConnection();
//        Connection con = dc.getConnection();
//        String query = "TRUNCATE TABLE Cart";
//        Statement st = con.createStatement();
//        int rs = st.executeUpdate(query);
//        if (rs > 0){
//            System.out.println("Cart table empty!");
//        }
//        st.close();
//        con.close();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Paymentsuccessful.fxml"));
//        Parent root = loader.load();
//        PaymentsuccessfulController paymentsuccessfulController = loader.getController();
//        Stage stage = (Stage) PayBtn.getScene().getWindow();
//        stage.setScene(new Scene(root));
//    } catch (SQLException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//}
//    public void insertTransaction() throws SQLException {
//        DatabaseConnection db = new DatabaseConnection();
//        Connection con = db.getConnection();
//        try {
//
//            String cartQuery = "SELECT DISTINCT(Buyer_Ph_no), SUM(Price) AS TotalPrice FROM Cart";
//            Statement cartStmt = con.createStatement();
//            ResultSet cartRs = cartStmt.executeQuery(cartQuery);
//
//            String buyerPhNo = null;
//            double totalPrice = 0.0;
//
//            if (cartRs.next()) {
//                buyerPhNo = cartRs.getString("Buyer_Ph_no");
//                totalPrice = cartRs.getDouble("TotalPrice");
//            }
//
//            cartRs.close();
//            cartStmt.close();
//
//            String transactionCountQuery = "SELECT COUNT(*) AS TransactionCount FROM Transactions";
//            Statement countStmt = con.createStatement();
//            ResultSet countRs = countStmt.executeQuery(transactionCountQuery);
//
//            int transactionCount = 0;
//
//            if (countRs.next()) {
//                transactionCount = countRs.getInt("TransactionCount");
//            }
//
//            countRs.close();
//            countStmt.close();
//
//            // Insert into Transactions table
//            String transactionInsertQuery = "INSERT INTO Transactions (TransactionID, Buyer_Ph_no, Price) " +
//                    "VALUES (" + transactionCount + ", '" + buyerPhNo + "', " + totalPrice + ")";
//            Statement insertStmt = con.createStatement();
//            insertStmt.executeUpdate(transactionInsertQuery);
//            insertStmt.close();
//
//            // Insert into Transaction_details table
//            String transactionDetailsInsertQuery = "INSERT INTO Transaction_details (TransactionID, Name_of_Book, Seller_Ph_no) " +
//                    "SELECT " + transactionCount + ", Name_of_Book, Seller_Ph_no FROM Cart";
//            Statement detailsInsertStmt = con.createStatement();
//            detailsInsertStmt.executeUpdate(transactionDetailsInsertQuery);
//            detailsInsertStmt.close();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            con.close();
//        }
//    }
//
//    public void BackBtnOnAction(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentMode.fxml"));
//            Parent root = loader.load();
//            PaymentModeController paymentModeController = loader.getController();
//
//            Stage stage = (Stage) BackBtn.getScene().getWindow();
//            stage.setScene(new Scene(root));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private void deleteItemsFromBooksTable() throws SQLException {
//        List<Book> cartItems = getCartItems();
//        DatabaseConnection db = new DatabaseConnection();
//        Connection con = db.getConnection();
//
//        try {
//            String query = "DELETE FROM Books WHERE ISBN_NO = ?";
//            PreparedStatement preparedStatement = con.prepareStatement(query);
//
//            for (Book book : cartItems) {
//                preparedStatement.setString(1, book.getIsbn());
//                preparedStatement.executeUpdate();
//            }
//
//            preparedStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            con.close();
//        }
//    }
//    private List<Book> getCartItems() throws SQLException {
//        List<Book> cartItems = new ArrayList<>();
//        DatabaseConnection db = new DatabaseConnection();
//        Connection con = db.getConnection();
//
//        try {
//            String cartQuery = "SELECT * FROM Cart";
//            Statement statement = con.createStatement();
//            ResultSet resultSet = statement.executeQuery(cartQuery);
//
//            while (resultSet.next()) {
//                Book book = new Book();
//                book.setIsbn(resultSet.getString("ISBN_NO"));
//                book.setBook_price(resultSet.getString("Price"));
//                book.setBook_name(resultSet.getString("Name_of_Book"));
//                book.setSellerphn(resultSet.getString("Seller_Ph_no"));
//                book.setEdt(resultSet.getString("Edition"));
//                book.setYop(resultSet.getString("Year"));
//                book.setCatg(resultSet.getString("category"));
//                book.setAddedToCart(true); // Set addedToCart to true
//
//                cartItems.add(book);
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            con.close();
//        }
//        return cartItems;
//    }
//}
//
//
package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CardPayController {

    @FXML
    private Button BackBtn;

    @FXML
    private Button PayBtn;

    @FXML
    private Label CVVError;

    @FXML
    private Label CardNoError;

    @FXML
    private Label FirstNameError;

    @FXML
    private Label LastNameError;

    @FXML
    private Label ValidityError;

    @FXML
    private TextField cardNumberField;

    @FXML
    private DatePicker cardValidityField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private AnchorPane rootPane;

    @FXML
    void PayBtnOnAction(ActionEvent event) throws SQLException, IOException {
        CardNoError.setText("");
        FirstNameError.setText("");
        LastNameError.setText("");
        CVVError.setText("");
        ValidityError.setText("");
        String cardNumber = cardNumberField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String cvv = cvvField.getText();
        LocalDate cardValidityDate = cardValidityField.getValue();
        boolean success = false;
        Boolean isCardNoValid = ValidateCardNo(cardNumber);
        Boolean isFirstNameValid = ValidateName(firstName);
        Boolean isLastNameValid = ValidateName(lastName);
        Boolean isCvvValid = ValidateCVV(cvv);
        Boolean isCardValid = ValidateCard(cardValidityDate);
        //List<String> errorMessages = new ArrayList<>();
        if(!isCardNoValid){
            CardNoError.setText("INVALID CARD NUMBER!");
        }
        if(!isCardValid){
            ValidityError.setText("CARD VALIDITY EXPIRED!");
        }
        if(!isCvvValid){
            CVVError.setText("INVALID CVV Number!");
        }
        if(!isFirstNameValid){
            FirstNameError.setText("INCORRECT FIRST NAME!");
        }
        if(!isLastNameValid){
            LastNameError.setText("INCORRECT LAST NAME!");
        }
        if(isCardValid && isCardNoValid && isCvvValid && isFirstNameValid && isLastNameValid){
            try {
                // Get card details



//            // Validate card details
//            String cardNumberPattern = "\\d{16}"; // 16 digits only
//            if (!cardNumber.matches(cardNumberPattern)) {
//                errorMessages.add("INVALID CARD Number.");
//            }
//
//            String namePattern = "[a-zA-Z]+"; // Only characters
//            if (!firstName.matches(namePattern)) {
//                errorMessages.add("First Name should only have characters.");
//            }
//
//            if (!lastName.matches(namePattern)) {
//                errorMessages.add("Last Name should only have characters.");
//            }
//
//            if (cvv.length() != 3) {
//                errorMessages.add("CVV Number should only be of 3 length.");
//            }
//
//            if (cardValidityDate == null || cardValidityDate.isBefore(LocalDate.now())) {
//                errorMessages.add("Card Validity Expired.");
//            }
//
//            if (!errorMessages.isEmpty()) {
//                // Display error messages
//                CardNoError.setText(errorMessages.contains("INVALID CARD Number.") ? "INVALID CARD Number." : "");
//                FirstNameError.setText(errorMessages.contains("First Name should only have characters.") ? "First Name should only have characters." : "");
//                LastNameError.setText(errorMessages.contains("Last Name should only have characters.") ? "Last Name should only have characters." : "");
//                CVVError.setText(errorMessages.contains("CVV Number should only be of 3 length.") ? "CVV Number should only be of 3 length." : "");
//                ValidityError.setText(errorMessages.contains("Card Validity Expired.") ? "Card Validity Expired." : "");
//
//                return; // Stop execution if there are errors
//            }

                insertTransaction();
                deleteItemsFromBooksTable();
                clearCartTable();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Paymentsuccessful.fxml"));
                Parent root = loader.load();
                PaymentsuccessfulController paymentsuccessfulController = loader.getController();
                Stage stage = (Stage) PayBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("ERROR!!");
        }

    }

    private Boolean ValidateCVV(String cvv) {
        if (cvv.length() != 3) {
              return false;
            }
        return true;
    }

    private Boolean ValidateName(String Name) {
        String namePattern = "[a-zA-Z]+";
        if (!Name.matches(namePattern)) {
                return false;
            }

        return true;
    }

    private Boolean ValidateCardNo(String cardNumber) {
        String cardNumberPattern = "\\d{16}"; // 16 digits only
           if (!cardNumber.matches(cardNumberPattern)) {
                return false;
                }
           return true;
    }

    private Boolean ValidateCard(LocalDate cardValidityDate) {
        if (cardValidityDate == null || cardValidityDate.isBefore(LocalDate.now())) {
                return false;
           }
        return true;
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

    public void BackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentMode.fxml"));
            Parent root = loader.load();
            PaymentModeController paymentModeController = loader.getController();

            Stage stage = (Stage) BackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
