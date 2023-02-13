/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package EditItem;

import HomePageadmin.HomepageadminController;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class EdititemController implements Initializable {

    @FXML
    private TextField txtlistnumber;
    @FXML
    private Label txtfilename;
    @FXML
    private DatePicker txtdate;
    @FXML
    private TextField txtdescription;
    @FXML
    private TextField txttitle;

    boolean update;
    String listnumber;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    File file;
    Blob blob;
    FileInputStream fileinput;
    @FXML
    private ComboBox<String> txtselecttype;

    ObservableList<String> list = FXCollections.observableArrayList("test1", "test2", "test3");
    @FXML
    private Button txtfile;
    @FXML
    private CheckBox txtbox;
    @FXML
    private ImageView txtimage;
    @FXML
    private Button back;
    private String URL = "jdbc:mysql://localhost:3306/filemanagement";
    private String USER = "root";
    private String PSD = "";

    /**
     *
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtselecttype.setItems(list);
        txtimage.setVisible(false);
    }

    public void setTextFiled(String No, String title, String description, String date, String filename) {
        LocalDate datee = LocalDate.parse(date);
        listnumber = No;
        txtlistnumber.setText(No);
        txttitle.setText(title);
        txtdescription.setText(description);
        txtdate.setValue(datee);
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("select `file`,`type` from `uploadfile` where listnumber='" + listnumber + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtselecttype.setValue(rs.getString("type"));
            }
        } catch (Exception e) {
        }

    }

    public void getUpdate(boolean b) {
        this.update = b;
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        setData();
    }

    private void setData() {
        if (update == true) {
            try {
                String listnumbers = txtlistnumber.getText();
                String title = txttitle.getText();
                String description = txtdescription.getText();
                String date = String.valueOf(txtdate.getValue());
                String type = txtselecttype.getValue();
                if (listnumbers.isEmpty() || title.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please input the data in the form");
                } else {
                    try {
                        con = DriverManager.getConnection(URL, USER, PSD);
                        pst = con.prepareStatement("select * from uploadfile where listnumber=?");
                        pst.setString(1, listnumbers);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "The listnumber is has in the database please input the new listnumber thank !");
                        } else {
                            con = DriverManager.getConnection(URL, USER, PSD);
                            pst = con.prepareStatement("UPDATE `uploadfile` SET `listnumber`=?,`title`=?,`description`=?,`type`=?,`date`=? WHERE `listnumber` = '" + listnumber + "'");
                            pst.setString(1, listnumbers);
                            pst.setString(2, title);
                            pst.setString(3, description);
                            pst.setString(4, type);
                            pst.setString(5, date);

                            int rs = pst.executeUpdate();
                            if (rs == 1) {
                                JOptionPane.showMessageDialog(null, "Edit Successfull !");
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("../HomePageadmin/Homepageadmin.fxml"));
                                try {
                                    loader.load();
                                } catch (Exception e) {
                                }
                                HomepageadminController test = loader.getController();
                                Parent root = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setTitle("The Home Page");
                                stage.setScene(new Scene(root));
                                stage.show();
                                txtlistnumber.getScene().getWindow().hide();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (txtbox.isSelected()) {
                            con = DriverManager.getConnection(URL, USER, PSD);
                            pst = con.prepareStatement("UPDATE `uploadfile` SET `file`=?,`filename`=?,`location`=? WHERE `listnumber` = '" + listnumber + "'");
                            fileinput = new FileInputStream(file);
                            pst.setBlob(1, fileinput);
                            pst.setString(2, file.getName());
                            pst.setString(3, file.getPath());
                            int rs = pst.executeUpdate();
                            if (rs == 1) {
                                System.out.println("Successfull");
                            } else {
                                System.out.println("Error");
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }

        }
    }

    @FXML
    private void handleSelectfile(ActionEvent event) {
        if (txtbox.isSelected()) {

            Stage stage = new Stage();
            FileChooser filechooser = new FileChooser();
            filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
            file = filechooser.showOpenDialog(stage);
            if (file != null) {
                txtfilename.setText(file.getName());
                txtimage.setVisible(true);
            } else {
                txtfilename.setText(file.getName());
                txtimage.setVisible(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please Tick the box .");
            txtimage.setVisible(false);
            txtfilename.setText("");
        }

    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
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
        txtlistnumber.getScene().getWindow().hide();
    }
}
