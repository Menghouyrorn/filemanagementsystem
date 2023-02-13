/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package viewuser;


import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.*;
import javafx.fxml.*;
import javafx.collections.*;
import helper.user;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.JOptionPane;
import Edituser.UserController;

/**
 *
 * FXML Controller class
 *
 * @author Dell
 */
public class ViewuserController implements Initializable {

    @FXML
    private TextField txtsearch;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    ObservableList<user> listProduct = FXCollections.observableArrayList();
    @FXML
    private Button btnhome;
    @FXML
    private TableView<user> tableview;
    @FXML
    private TableColumn<user, String> txtid;
    @FXML
    private TableColumn<user, String> txtfname;
    @FXML
    private TableColumn<user, String> txtlastname;
    @FXML
    private TableColumn<user, String> txtemail;
    user datauser;
    private String URL="jdbc:mysql://localhost:3306/filemanagement";
    private String USER="root";
    private String PSD="";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        view();
    }

    public void view() {
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `id`, `firstname`, `lastname`, `email` FROM `user` ORDER BY `id` ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new user(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email")
                ));
            }
            tableview.setItems(listProduct);
            data();
        } catch (Exception e) {
        }
    }

    public void data() {
        txtid.setCellValueFactory(new PropertyValueFactory<>("id"));
        txtfname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        txtlastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        txtemail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../HomePageadmin/Homepageadmin.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
        }
        Parent root =loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Home Page");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearch.getScene().getWindow().hide();
    }

    @FXML
    private void handleAddnew(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../addnew/addnew.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Home Page");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearch.getScene().getWindow().hide();
    }

    @FXML
    private void handleSigout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearch.getScene().getWindow().hide();
    }

    public void refressdata() {
        listProduct.clear();
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `id`, `firstname`, `lastname`, `email`, `password` FROM `user` ORDER BY `id` ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new user(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email")
                ));
                tableview.setItems(listProduct);
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String search = txtsearch.getText();
        listProduct.clear();
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `id`, `firstname`, `lastname`, `email` FROM `user` WHERE id=? or firstname=?");
            pst.setString(1, search);
            pst.setString(2, search);
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new user(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email")
                ));
                tableview.setItems(listProduct);
                data();
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void handleAddUser(ActionEvent event) throws IOException {
        Parent root =FXMLLoader.load(getClass().getResource("../adduser/adduser.fxml"));
        Stage stage = new Stage();
        stage.setTitle("AddUser");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearch.getScene().getWindow().hide();
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        datauser = tableview.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Edituser/user.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
        }
        UserController edit = loader.getController();
        edit.setUpdate(true);
        edit.setTextFiled(datauser.getId(), datauser.getFirstname(), datauser.getLastname(), datauser.getEmail());
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Edit");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearch.getScene().getWindow().hide();
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            datauser = tableview.getSelectionModel().getSelectedItem();
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("DELETE FROM `user` WHERE id= '" + datauser.getId() + "'");
            pst.execute();
            if (pst != null) {
                System.out.println("Delete");
                refressdata();
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }

        } catch (Exception e) {
        }
    }

}
