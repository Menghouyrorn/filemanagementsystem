/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package addtype;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import helper.TypeDocument;
import java.io.IOException;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class AddtypeController implements Initializable {

    @FXML
    private TableView<TypeDocument> tabletype;
    @FXML
    private TableColumn<TypeDocument, String> txttypetable;
    @FXML
    private TextField txttype;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private String URL = "jdbc:mysql://localhost:3306/filemanagement";
    private String PSD = "";
    private String USER = "root";
    ObservableList<TypeDocument> listProduct = FXCollections.observableArrayList();
    TypeDocument itemdata;
    boolean update;
    String typeid;
    @FXML
    private TableColumn<TypeDocument, String> txtid;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        view();
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        itemdata = tabletype.getSelectionModel().getSelectedItem();
        setText(itemdata.getType(), itemdata.getId());
        setUpdate(true);
    }

    public void setUpdate(boolean b) {
        this.update = b;
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            itemdata = tabletype.getSelectionModel().getSelectedItem();
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("DELETE FROM `type` WHERE id = '" + itemdata.getId() + "'");
            pst.execute();
            if (pst != null) {
                System.out.println("Delete Successfull !");
                refress();
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }

        } catch (Exception e) {
        }

    }

    public void setText(String type, String id) {
        typeid = id;
        txttype.setText(type);
    }

    public void refress() {
        listProduct.clear();
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `type`, `id` FROM `type`");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new TypeDocument(rs.getString("id"), rs.getString("type")));
                tabletype.setItems(listProduct);
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        String typeinput = txttype.getText();
        if (update == true) {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                pst = con.prepareStatement("select * from `type` where `type`=?");
                pst.setString(1, typeinput);
                rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "The data is has Please input the new type !");
                } else {
                    pst = con.prepareStatement("UPDATE `type` SET `type`=? WHERE `id`='" + typeid + "'");
                    pst.setString(1, typeinput);
                    int test = pst.executeUpdate();
                    if (test == 1) {
                        System.out.println("Update is Successfull !");
                        refress();
                        txttype.setText("");
                    } else {
                        System.out.println("Error");
                    }
                }

            } catch (Exception e) {
            }
        } else {
            try {
                con = DriverManager.getConnection(URL, USER, PSD);
                if (typeinput.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Input the data !");
                } else {
                    pst = con.prepareStatement("select * from `type` where `type`=?");
                    pst.setString(1, typeinput);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "The data is has Please input the new type !");
                    } else {
                        pst = con.prepareStatement("INSERT INTO `type`(`type`) VALUES (?)");
                        pst.setString(1, typeinput);
                        int test = pst.executeUpdate();
                        if (test == 1) {
                            System.out.println("Successfull");
                            txttype.setText("");
                            refress();
                        } else {
                            System.out.println("Error");
                        }
                    }
                }

            } catch (Exception e) {
            }
        }
    }

    public void view() {
        try {
            con = DriverManager.getConnection(URL, USER, PSD);
            pst = con.prepareStatement("SELECT `type`, `id` FROM `type`");
            rs = pst.executeQuery();
            while (rs.next()) {
                listProduct.add(new TypeDocument(
                        rs.getString("id"),
                        rs.getString("type")
                ));
            }
            tabletype.setItems(listProduct);
            data();
        } catch (Exception e) {
        }
    }

    public void data() {
        txtid.setCellValueFactory(new PropertyValueFactory<>("id"));
        txttypetable.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../addnew/addnew.fxml"));
        Stage stage = new Stage();
        stage.setTitle("AddNew");
        stage.setScene(new Scene(root));
        stage.show();
        txttype.getScene().getWindow().hide();
    }

}
