/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package addnew;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.*;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javax.swing.JOptionPane;
import java.sql.*;
import java.sql.DriverManager;
import javafx.fxml.FXMLLoader;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class addnew implements Initializable {

    @FXML
    private ImageView txtimage;
    @FXML
    private Label txtfilename;
    @FXML
    private DatePicker txtdate;
    @FXML
    private TextField txtListNumber;
    @FXML
    private TextField txttitle;
    @FXML
    private TextField txtDescription;
    @FXML
    private ComboBox<String> txtcombo;
    @FXML
    private Button txtfile;
    ObservableList<String> list;
    public File file;
    private String type, listnumber, description, title, date, filen;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public FileInputStream fileinput;
    @FXML
    private Button txthome;
    @FXML
    private Button txtviewuser;
    @FXML
    private Button signout;
    private String URL = "jdbc:mysql://localhost:3306/filemanagement";
    private String USER = "root";
    private String PSD = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String type;
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("select `type` from `type`");
            rs = pst.executeQuery();
            while (rs.next()) {
                type = rs.getString("type");
                txtcombo.getItems().add(type);
            }
        } catch (Exception e) {
        }
        txtimage.setVisible(false);
    }

    @FXML
    private void handleSelect(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(new ExtensionFilter("PDF file", "*.pdf"));
        file = filechooser.showOpenDialog(stage);
        txtimage.setVisible(false);
        if (file != null) {
            txtimage.setVisible(true);
            txtfilename.setText(file.getName());
        } else {
            txtimage.setVisible(false);
            txtfilename.setText("");
        }

    }

    public void clear() {
        txtListNumber.setText("");
        txtDescription.setText("");
        txtcombo.setValue("");
        txttitle.setText("");
        txtimage.setVisible(false);
        txtfilename.setText("");
    }

    @FXML
    private void handleSave(ActionEvent event) throws SQLException, FileNotFoundException {
        type = txtcombo.getValue();
        listnumber = txtListNumber.getText();
        description = txtDescription.getText();
        title = txttitle.getText();
        if (listnumber.isEmpty() || description.isEmpty() || title.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Input the data");
        } else {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                pst = con.prepareStatement("select * from `uploadfile` where listnumber=?");
                pst.setString(1, listnumber);
                rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "The list number is use alert please input the new list number !");
                } else {
                    con = DriverManager.getConnection(URL, USER, PSD);
                    pst = con.prepareStatement("INSERT INTO uploadfile (listnumber, title, description, type, date, file, filename,location) VALUES (?,?,?,?,?,?,?,?)");
                    pst.setString(1, listnumber);
                    pst.setString(2, title);
                    pst.setString(3, description);
                    pst.setString(4, type);
                    pst.setString(5, String.valueOf(txtdate.getValue()));
                    fileinput = new FileInputStream(file);
                    pst.setBlob(6, fileinput);
                    pst.setString(7, file.getName());
                    pst.setString(8, file.getPath());
                    System.out.println("The time is : ");
                    int su = pst.executeUpdate();
                    if (su == 1) {
                        JOptionPane.showMessageDialog(null, "Upload Successfull");
                        clear();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
                        clear();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../HomePageadmin/Homepageadmin.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Home page admin");
        stage.setScene(new Scene(root));
        stage.show();
        txthome.getScene().getWindow().hide();
    }

    @FXML
    private void handleViewUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../viewuser/viewuser.fxml"));
        Stage stage = new Stage();
        stage.setTitle("View User");
        stage.setScene(new Scene(root));
        stage.show();
        txtListNumber.getScene().getWindow().hide();
    }

    @FXML
    private void handleSignot(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
        signout.getScene().getWindow().hide();
    }

    @FXML
    private void handleAddtype(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../addtype/addtype.fxml"));
        Stage stage = new Stage();
        stage.setTitle("AddType");
        stage.setScene(new Scene(root));
        stage.show();
        txtDescription.getScene().getWindow().hide();
    }
}
