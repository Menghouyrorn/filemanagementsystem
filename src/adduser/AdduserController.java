/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package adduser;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class AdduserController implements Initializable {

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
    private String URL = "jdbc:mysql://localhost:3306/filemanagement";
    private String USER = "root";
    private String PSD = "";
    private String fname, lname, email, password, cpassword;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleAdd(ActionEvent event) {
        fname = txtfname.getText();
        lname = txtlname.getText();
        email = txtemail.getText();
        password = txtpassword.getText();
        cpassword = txtcpassword.getText();
        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Input data in form");
        } else {
            if (password.equals(cpassword)) {
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
                            pst.setString(1, fname);
                            pst.setString(2, lname);
                            pst.setString(3, email);
                            pst.setString(4, password);
                            int status = pst.executeUpdate();
                            if (status == 1) {
                                JOptionPane.showMessageDialog(null, "Add Successfull !");
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
                                    Stage stage = new Stage();
                                    stage.setTitle("View User");
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
    private void handleback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
            Stage stage = new Stage();
            stage.setTitle("View User");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            txtpassword.getScene().getWindow().hide();
        } catch (Exception e) {
        }
    }

}
