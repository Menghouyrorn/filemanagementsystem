/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package HomePageadmin;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.fxml.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.TextField;
import helper.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import EditItem.EdititemController;
import java.io.IOException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javax.swing.JOptionPane;
import helper.user;

/**
 *
 * @author Dell
 */
public class HomepageadminController implements Initializable {

    private Label label;
    @FXML
    private Button txtaddnew;
    @FXML
    private TableView<Item> tableview;
    @FXML
    private TableColumn<Item, String> txtlistnumber;
    @FXML
    private TableColumn<Item, String> txttitle;
    @FXML
    private TableColumn<Item, String> txtdescription;
    @FXML
    private TableColumn<Item, String> txtdate;
    @FXML
    private TableColumn<Item, String> txtfilename;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Item itemdata;
    user datauser;
    @FXML
    private TextField txtsearchbox;
    ObservableList<Item> listProduct = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> txtcombox;
    String select;
    @FXML
    private MenuItem open;
    @FXML
    private MenuItem download;
    @FXML
    private MenuItem update;
    @FXML
    private MenuItem delete;
    String hide;
    boolean admin;
    @FXML
    private Button viewuser;
    @FXML
    private TableColumn<Item, String> txttypefile;
    private Label txtnameuser;
    String nameuser;
    private String URL = "jdbc:mysql://localhost:3306/filemanagement";
    private String USER = "root";
    private String PSD = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String type;
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("select `type` from `type`");
            rs = pst.executeQuery();
            while (rs.next()) {
                type = rs.getString("type");
                txtcombox.getItems().add(type);
            }
        } catch (Exception e) {
        }
        view();
        txtnameuser.setText(nameuser);
    }

    public void Name(String name) {
        this.nameuser = name;
    }

    public void view() {
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `listnumber`, `title`, `description`,`type`, `date`, `filename` FROM `uploadfile` ORDER BY date DESC limit 20");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new Item(
                        rs.getString("listnumber"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("filename"),
                        rs.getString("type")
                ));
            }
            tableview.setItems(listProduct);
            data();
        } catch (Exception e) {
        }
    }

    public void data() {
        txtlistnumber.setCellValueFactory(new PropertyValueFactory<>("No"));
        txttitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        txtdescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        txtdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        txtfilename.setCellValueFactory(new PropertyValueFactory<>("filename"));
        txttypefile.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
//        String current_user;
//        try {
//            con=DriverManager.getConnection(URL,USER,PSD);
//            pst=con.prepareStatement("SELECT USER()");
//            rs=pst.executeQuery();
//            if(rs.next()){
//                System.out.println("The current is : "+rs.getString("USER()"));
//            }
//        } catch (Exception e) {
//        }
//        Parent root = FXMLLoader.load(getClass().getResource("../report/test.fxml"));
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.show();
//        txtnameuser.setText(nameuser);
//        System.out.println("Name is : " + nameuser);
    }

    @FXML
    private void handleViewUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
        Stage stage = new Stage();
        stage.setTitle("View User");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearchbox.getScene().getWindow().hide();
    }

    @FXML
    private void handleAddNew(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../addnew/addnew.fxml"));
            Stage stage = new Stage();
            stage.setTitle("AddNew Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            txtaddnew.getScene().getWindow().hide();
        } catch (Exception e) {
        }

    }

    @FXML
    private void handleSignOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearchbox.getScene().getWindow().hide();
    }

    @FXML
    private void handleOpen(ActionEvent event) {
        try {
            itemdata = tableview.getSelectionModel().getSelectedItem();
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("select `file`, `filename`, `location` FROM `uploadfile` where listnumber='" + itemdata.getNo() + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                String path = rs.getString("location");
                String filename = rs.getString("filename");
                File file = new File(path);
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        itemdata = tableview.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../EditItem/edititem.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
        }
        EdititemController edit = loader.getController();
        edit.getUpdate(true);
        edit.setTextFiled(itemdata.getNo(), itemdata.getTitle(), itemdata.getDescription(), itemdata.getDate(), itemdata.getFilename());
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Edite");
        stage.setScene(new Scene(root));
        stage.show();
        txtsearchbox.getScene().getWindow().hide();
    }

    public void refressdata() {
        listProduct.clear();
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `listnumber`, `title`, `description`,`type`, `date`, `filename` FROM `uploadfile` ORDER BY date DESC limit 20");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new Item(
                        rs.getString("listnumber"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("filename"),
                        rs.getString("type")
                ));
                tableview.setItems(listProduct);
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            itemdata = tableview.getSelectionModel().getSelectedItem();
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("DELETE FROM `uploadfile` WHERE listnumber = '" + itemdata.getNo() + "'");
            pst.execute();
            if (pst != null) {
                System.out.println("Delete Successfull !");
                refressdata();
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }

        } catch (Exception e) {
        }

    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String search = txtsearchbox.getText();
        listProduct.clear();
        if (search.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please input the data");
        } else {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                pst = con.prepareStatement("SELECT `listnumber`, `title`, `description`,`type`, `date`, `filename` FROM `uploadfile` where listnumber=? or title=?");
                pst.setString(1, search);
                pst.setString(2, search);
                rs = pst.executeQuery();
                while (rs.next()) {
                    listProduct.add(new Item(
                            rs.getString("listnumber"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("date"),
                            rs.getString("filename"),
                            rs.getString("type")
                    ));
                }
                tableview.setItems(listProduct);
                data();
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void handleDownload(ActionEvent event) {
        try {
            itemdata = tableview.getSelectionModel().getSelectedItem();
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `file`,`filename` FROM `uploadfile` where listnumber='" + itemdata.getNo() + "'");
            rs = pst.executeQuery();
            while (rs.next()) {
                Blob blob = rs.getBlob("file");
                String filename = rs.getString("filename");
                byte byteArray[] = blob.getBytes(1, (int) blob.length());
                FileOutputStream outPutStream = new FileOutputStream("C:\\Users\\Dell\\Downloads\\" + filename);
                outPutStream.write(byteArray);
                System.out.println("Download is Successfull !");
                File file = new File("C:\\Users\\Dell\\Downloads\\" + filename);
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
        }

    }

    @FXML
    private void handleSelect(ActionEvent event) {
        String select = txtcombox.getValue();
        listProduct.clear();
        if (select.equals("")) {
            view();
        } else {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                pst = con.prepareStatement("SELECT `listnumber`, `title`, `description`,`type`,`date`, `filename` FROM `uploadfile` where type='" + select + "'");
                rs = pst.executeQuery();
                while (rs.next()) {
                    listProduct.add(new Item(
                            rs.getString("listnumber"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("date"),
                            rs.getString("filename"),
                            rs.getString("type")
                    ));
                }
                tableview.setItems(listProduct);
                data();
            } catch (Exception e) {
            }
        }
    }

    public void admin(boolean b) {
        this.admin = b;
    }
}
