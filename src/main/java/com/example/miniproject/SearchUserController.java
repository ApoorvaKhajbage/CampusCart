package com.example.miniproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class SearchUserController {
    @FXML
    private Button SearchUser;

    @FXML
    private TextField UserPhone;

    @FXML
    private Button SearchUserBackBtn;

    @FXML
    private Label  SearchResult;

    public void SearchUserOnAction(ActionEvent event) throws IOException {
        String phone_no = UserPhone.getText();
        boolean success = false;

        try {
            DatabaseConnection db2 = new DatabaseConnection();
            Connection con = db2.getConnection();
            Statement st = con.createStatement();
            String search_user = "SELECT * FROM Users WHERE Ph_no = '" + phone_no + "'";
            ResultSet rs = st.executeQuery(search_user);
            while(rs.next()){
                success = true;
                write_in_file(rs.getString(2), phone_no);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(success){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController homeController = loader.getController();
            Stage stage = (Stage) SearchUserBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }else{
            System.out.println("Invalid Phone Number");
            SearchResult.setText("User Not Found.");
        }
    }

    public void SearchUserBackBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
            Parent root = loader.load();
            AdminHomeController adminHomeController = loader.getController();

            Stage stage = (Stage) SearchUserBackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write_in_file(String name, String phone_no) throws IOException {
        FileOutputStream fos = new FileOutputStream("details.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(name);
        dos.writeUTF(phone_no);
    }

}
