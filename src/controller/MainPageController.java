/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CmdProxy;
import model.CompanyConfig;
import model.CompanyConfigManager;
import model.MessageDialog;
import model.SwitchProcessor;
import model.SystemPropertiesManager;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setListener();
    }
    @FXML
    private Button switchBtn;

    @FXML
    private Button runbatBtn;

    @FXML
    private TextField patchPath;

    @FXML
    private ComboBox<String> companyIdCombo;

    @FXML
    private Button addBtn;

    @FXML
    void addFN(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCompanyPage.fxml"));
        Scene scene = new Scene(root);

        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //取得主畫面的舞台
        appStage.setScene(scene);
        appStage.show();
//        Stage addStage = new Stage(); //新建一個舞台
//        addStage.setScene(scene);
//        addStage.show();
    }

    @FXML
    void openCompanyListFN(MouseEvent event) {
        loadComboBoxData();
    }

    @FXML
    void openFileChooserFN(MouseEvent event) {
        try {
            CompanyConfigManager ccm = CompanyConfigManager.getInstance();
            CompanyConfig config = ccm.getCompanyConfig(companyIdCombo.getValue());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //取得主畫面的舞台

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("請選擇patch包");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("EXE", "*.exe")
            );
            if (!config.getPatchPath().isEmpty()) {
                String path = config.getPatchPath();
                File sourceFilePath = new File(path.substring(0, path.lastIndexOf('\\')));
                if (sourceFilePath.exists()) {
                    fileChooser.setInitialDirectory(sourceFilePath);
                }
            }

            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                config.setPatchPath(file.getPath());
                ccm.updateCompanyConfig(config);
                patchPath.setText(file.getPath());
            }
        } catch (Exception ex) {
            MessageDialog.showAlert(ex.getMessage());
        }
    }

    @FXML
    void swtichFN(ActionEvent event) {
        try {
            Alert alert = MessageDialog.showAlert("切換中...請稍後", false);
            SwitchProcessor sp = new SwitchProcessor();
            CompanyConfigManager ccm = CompanyConfigManager.getInstance();
            CompanyConfig config = ccm.getCompanyConfig(companyIdCombo.getValue());
            String result = sp.doSwitch(config);
            alert.close();
            if (result.equals("")) {
                MessageDialog.showAlert("切換完成");
                if (MessageDialog.showConfirm("", "是否要啟動SFT環境?")) {
                    doRunbat();
                }
            } else {
                MessageDialog.showAlert("切換失敗：" + result);
            }
        } catch (Exception ex) {
            MessageDialog.showAlert(ex.getMessage());
        }
    }

    @FXML
    void runbatFN(ActionEvent event) {
        doRunbat();
    }

    private void doRunbat() {
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
//        CmdProxy.callCmd("start \"" + companyIdCombo.getValue() + "_自動切換環境\" " + jboss_home + "\\bin\\run.bat");
        try {
            CmdProxy.callCmd("cmd.exe /c start " + jboss_home + "\\bin\\run.bat");
        } catch (Exception ex) {
            MessageDialog.showAlert("SFT啟動失敗：" + ex.getMessage());
        }
    }

    private void loadComboBoxData() {
        companyIdCombo.getItems().clear();
        CompanyConfigManager ccm = CompanyConfigManager.getInstance();
        HashMap<String, CompanyConfig> companyList = ccm.getCompanyConfig();
        companyList.forEach((key, value) -> {
            companyIdCombo.getItems().add(key);
        });
    }

    private void setListener() {
        // 公司別
        companyIdCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    CompanyConfigManager ccm = CompanyConfigManager.getInstance();
                    CompanyConfig config = ccm.getCompanyConfig(newValue);
                    patchPath.setText(config.getPatchPath());
                }
            } catch (Exception ex) {
                MessageDialog.showAlert(ex.getMessage());
            }
        });
    }

}
