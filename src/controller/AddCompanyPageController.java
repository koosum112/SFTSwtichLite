/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CompanyConfig;
import model.CompanyConfigManager;
import model.MessageDialog;

import net.sf.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AddCompanyPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private TextField password;

    @FXML
    private TextField address;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField companyName;

    @FXML
    private TextField SFTSYS;

    @FXML
    private TextField account;

    @FXML
    private Button saveBtn;

    @FXML
    void saveFN(ActionEvent event) {
        try {
            JSONObject companyJson = new JSONObject();
            companyJson.put("companyName", companyName.getText());
            companyJson.put("address", address.getText());
            companyJson.put("account", account.getText());
            companyJson.put("password", password.getText());
            companyJson.put("SFTSYS", SFTSYS.getText());
            companyJson.put("patchPath", "");
            String msg = beforeSaveCheck();
            if (msg.isEmpty()) {
                CompanyConfigManager ccm = CompanyConfigManager.getInstance();
                ccm.updateCompanyConfig(new CompanyConfig(companyJson));

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
                Scene scene = new Scene(root);
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //取得主畫面的舞台
                appStage.setScene(scene);
                appStage.show();
            } else {
                throw new Exception(msg);
            }
        } catch (Exception ex) {
            MessageDialog.showAlert(ex.getMessage());
        }
    }

    @FXML
    void cancelFN(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
            Scene scene = new Scene(root);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //取得主畫面的舞台
            appStage.setScene(scene);
            appStage.show();
        } catch (Exception ex) {
            MessageDialog.showAlert(ex.getMessage());
        }
    }

    private String beforeSaveCheck() {
        String msg = "";
        if (companyName.getText().isEmpty()) {
            msg = "公司別名稱不可為空";
        } else if (address.getText().isEmpty()) {
            msg = "資料庫IP不可為空";
        } else if (account.getText().isEmpty()) {
            msg = "資料庫帳號不可為空";
        } else if (password.getText().isEmpty()) {
            msg = "資料庫密碼不可為空";
        } else if (SFTSYS.getText().isEmpty()) {
            msg = "SFTSYS不可為空";
        }
        return msg;
    }
}
