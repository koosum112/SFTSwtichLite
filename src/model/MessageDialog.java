/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author user
 */
public class MessageDialog {

    public static Alert showAlert(String text, Boolean wait) {
        if (wait) {
            return showAlert(text);
        } else {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
            alert.setTitle("提示"); //設定對話框視窗的標題列文字
            alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
            alert.setContentText(text); //設定對話框的訊息文字
            alert.show();
            return alert;
        }
    }

    public static Alert showAlert(String text) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle("提示"); //設定對話框視窗的標題列文字
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(text); //設定對話框的訊息文字
        alert.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
        return alert;
    }

    public static Boolean showConfirm(String title, String text) {
        final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(title.equals("") ? "確認視窗" : title); //設定對話框視窗的標題列文字
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(text); //設定對話框的訊息文字
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
        System.out.println(rtn);
        if (rtn == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideDialog(Alert alert) {
        alert.close();
    }
}
