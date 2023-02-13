/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Edituser;

import java.io.IOException;
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
public class UserController implements Initializable {

    @FXML
    private TextField txtfname;
    @FXML
    private TextField txtlname;
    @FXML
    private TextField txtemail;
    boolean update;
    int UserID;
    Connection con;
    PreparedStatement pst;

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
    private void handleUpdateUser(ActionEvent event) {
        datauser();
    }
    
    public void datauser(){
        if(update==true){
            String fname=txtfname.getText();
            String lname=txtlname.getText();
            String email=txtemail.getText();
            try {
                con=DriverManager.getConnection(URL, USER, PSD);
                pst=con.prepareStatement("UPDATE `user` SET `firstname`=?,`lastname`=?,`email`=? WHERE id='"+UserID+"'");
                pst.setString(1, fname);
                pst.setString(2, lname);
                pst.setString(3, email);
                int rs =pst.executeUpdate();
                if(rs==1){
                    JOptionPane.showMessageDialog(null, "Update Successfull !");
                    Parent root =FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
                    Stage stage=new Stage();
                    stage.setTitle("View User");
                    stage.setScene(new Scene(root));
                    stage.show();
                    txtfname.getScene().getWindow().hide();
                }else{
                    System.out.println("Error");
                }
            } catch (Exception e) {
            }
        }
    }
    
    public void setUpdate(boolean b){
        this.update=b;
    }
    
    public void setTextFiled(int id, String fname, String lname, String email) {
        UserID=id;
        txtfname.setText(fname);
        txtlname.setText(lname);
        txtemail.setText(email);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root =FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
        Stage stage = new Stage();
        stage.setTitle("View User");
        stage.setScene(new Scene(root));
        stage.show();
        txtfname.getScene().getWindow().hide();
    }
}
