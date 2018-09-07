/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class SystemPropertiesManager{
    private static Properties props = new Properties();
    private static String propFileName = "system.properties";
    static {
        File propertiesFile = null;
        FileInputStream fis = null;
        try {
            propertiesFile = new File(propFileName);
            if(!propertiesFile.exists()){
                propertiesFile.createNewFile();
                initProperties();
                saveProperties();
            }
            fis = new FileInputStream(propFileName);
            props.load(fis);
        } catch (Exception ex) {
            MessageDialog.showAlert("SystemProperties error:"+ ex.getMessage());
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }

    private static void initProperties() {
        props.setProperty("VERSION", SystemProperties.VERSION);
        props.setProperty("JBOSS_HOME", SystemProperties.JBOSS_HOME);
    }
    private static void saveProperties(){
         FileOutputStream fos = null;
         try{
            fos = new FileOutputStream(propFileName);
            props.store(fos, "Copyright (c) Wen Cheng");
        }catch(Exception ex){
            MessageDialog.showAlert(ex.getMessage());
        }finally{
             try {
                fos.close();
             } catch (IOException ex) {
                MessageDialog.showAlert(ex.getMessage());
             }
        }
    }
}
