/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Register;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField txtfname;
    @FXML
    private TextField txtlname;
    @FXML
    private TextField txtemail;
    @FXML
    private TextField txtpassword;
    @FXML
    private TextField txtcpassword;
    private String Fname, Lname,email, password,CPassword;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    @FXML
    private Hyperlink txtToLogin;
    @FXML
    private Hyperlink txtLinkForgetPassword;
    private String URL="jdbc:mysql://localhost:3306/filemanagement";
    private String USER="root";
    private String PSD="";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Fname = "";
        Lname = "";
        email = "";
        password = null;
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        Fname = txtfname.getText();
        Lname = txtlname.getText();
        email = txtemail.getText();
        password = txtpassword.getText();
        CPassword = txtcpassword.getText();
        
        if (Fname.isEmpty() || Lname.isEmpty() || email.isEmpty() || password.isEmpty() || CPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Input data in form");} 
        else {
            if (password.equals(CPassword)) {
                try {
                    con = DriverManager.getConnection(URL, USER, PSD);
                    pst = con.prepareStatement("select * from user where email=?");
                    pst.setString(1, email);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "The Email in use!(Please Input the new Email)");
                    } else {
                        try {
                            con = DriverManager.getConnection(URL, USER, PSD);
                            pst = con.prepareStatement("Insert into user (firstname,lastname,email,password) values (?,?,?,?)");
                            pst.setString(1, Fname);
                            pst.setString(2, Lname);
                            pst.setString(3, email);
                            pst.setString(4, password);
                            int status = pst.executeUpdate();
                            if (status == 1) {
                                JOptionPane.showMessageDialog(null, "Registered!");
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
                                    Stage stage = new Stage();
                                    stage.setTitle("Login Page");
                                    Scene scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    txtpassword.getScene().getWindow().hide();
                                } catch (Exception e) {
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        } catch (Exception e) {
                        }
                        txtfname.getScene().getWindow().hide();

                    }
                } catch (Exception e) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password is not correct!");
            }
        }
    }

    @FXML
    private void handleToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
        txtToLogin.getScene().getWindow().hide();
    }
    @FXML
    private void handleLinkForgetPassword(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../ForgetPassword/ForgetPassword.fxml"));
            Stage stage = new Stage();
            stage.setTitle("ForGetPassword page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
        }
    txtemail.getScene().getWindow().hide();
    }

}

