/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import HomePageadmin.HomepageadminController;
import java.io.IOException;
import helper.user;
import HomePageadmin.HomepageadminController;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtpassword;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String email, password;
    private String URL="jdbc:mysql://localhost:3306/filemanagement";
    private String USER="root";
    private String PSD="";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        HomepageadminController admin = new HomepageadminController();
        email = txtemail.getText();
        password = txtpassword.getText();
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Input the data to form");
        } else {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                pst = con.prepareStatement("select * from `user` where email=? and password=?");
                pst.setString(1, email);
                pst.setString(2, password);
                rs = pst.executeQuery();
                if (rs.next()) {
                    admin.Name(rs.getString("firstname"));
                    if (email.equals("admin@gmail.com") && password.equals("admin")) {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("../HomePageadmin/Homepageadmin.fxml"));
                        try {
                            loader.load();
                        } catch (Exception e) {
                        }
                        HomepageadminController test=loader.getController();
                        test.Name(rs.getString("email"));
                        Parent root =loader.getRoot();
                        Stage stage = new Stage();
                        stage.setTitle("The Home Page");
                        stage.setScene(new Scene(root));
                        stage.show();
                        txtemail.getScene().getWindow().hide();
                    } else {
                        Parent root = FXMLLoader.load(getClass().getResource("../homeuser/homeuser.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("The Home Page");
                        stage.setScene(new Scene(root));
                        stage.show();
                        txtemail.getScene().getWindow().hide();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Email is not must !");
                }
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Register/Register.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Register");
        stage.setScene(new Scene(root));
        stage.show();
        txtemail.getScene().getWindow().hide();
    }

    @FXML
    private void handleForgetpassword(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../ForgetPassword/ForgetPassword.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Register");
        stage.setScene(new Scene(root));
        stage.show();
        txtemail.getScene().getWindow().hide();
    }

}
