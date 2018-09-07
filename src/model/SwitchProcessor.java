/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.Iterator;
import javafx.scene.control.Alert;
import org.dom4j.Document;
import org.dom4j.Element;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author user
 */
public class SwitchProcessor {

    public SwitchProcessor() {

    }

    public String doSwitch(CompanyConfig config) {
        String msg = "";
        try {
            unZip(config.getPatchPath());
            delWAR_JAR();
            replaceWAR_JAR();
            cleanTmpWork();
            cleanTmpFile();
            replaceDatabaseConfig(config);
            replaceMssql(config);
        } catch (Exception ex) {
            msg = ex.getMessage();
        }
        return msg;
    }

    private void unZip(String filePath) throws Exception {
        System.out.println("unZip...Start");
      
        CmdProxy.callCmd("../winRAR/unRAR.exe x " + filePath + " tmpPatch\\");
        System.out.println("unZip...OK");
    }

    private void delWAR_JAR() throws Exception {
        System.out.println("delWAR_JAR...Start");
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
        File deployFolder = new File(jboss_home + "\\server\\default\\deploy");
        File[] list = deployFolder.listFiles();
        for (File file : list) {
            String fileName = file.getName();

            if (file.isFile()) {
                if (fileName.lastIndexOf(".war") == fileName.length() - 4 || file.getName().indexOf(".jar") == fileName.length() - 4) {
                    file.delete();
                    System.out.println("DEL " + file.getName());
                }
            }
        }
        System.out.println("delWAR_JAR...OK");
    }

    private void replaceWAR_JAR() throws Exception {
        System.out.println("replaceWAR_JAR...Start");
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
        File srcDeployFolder = new File("tmpPatch\\SFT_Patch\\server\\default");
        File targetDeployFolder = new File(jboss_home + "\\server\\default");
        FileUtils.copyDirectory(srcDeployFolder, targetDeployFolder);
        System.out.println("replaceWAR_JAR...OK");
    }

    private void cleanTmpWork() throws Exception {
        System.out.println("cleanTmpWork...Start");
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
        File tmp = new File(jboss_home + "\\server\\default\\tmp");
        if (tmp.exists()) {
            FileUtils.deleteDirectory(tmp);
        }
        File work = new File(jboss_home + "\\server\\default\\work");
        if (work.exists()) {
            FileUtils.deleteDirectory(work);
        }
        System.out.println("cleanTmpWork...OK");
    }

    private void cleanTmpFile() throws Exception {
        System.out.println("cleanTmpFile...Start");
        File tmp = new File("tmpPatch");
        if (tmp.exists()) {
            FileUtils.deleteDirectory(tmp);
        }
        System.out.println("cleanTmpFile...OK");
    }

    private void replaceDatabaseConfig(CompanyConfig config) throws Exception {
        System.out.println("replaceProperties...Start");
        String sourcePath = "fileSource\\database.conf.xml";
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
        String distPath = jboss_home + "\\..\\SFT\\database.conf.xml";

        XmlManager xmlManager = new XmlManager();
        xmlManager.loadXml(sourcePath);
        Document mssqlDoc = xmlManager.getDocument();
        Element rootEl = mssqlDoc.getRootElement();
        doReplaceConfig(rootEl, config);
        xmlManager.setDocument(mssqlDoc);
        xmlManager.saveXml(distPath);
        System.out.println("replaceProperties...OK");
    }

    private void replaceMssql(CompanyConfig config) throws Exception {
        System.out.println("replaceMssql...Start");
        String sourcePath = "fileSource\\mssql-ds.xml";
        String jboss_home = SystemPropertiesManager.getProperty("JBOSS_HOME");
        String distPath = jboss_home + "\\server\\default\\deploy\\mssql-ds.xml";

        XmlManager xmlManager = new XmlManager();
        xmlManager.loadXml(sourcePath);
        Document mssqlDoc = xmlManager.getDocument();
        Element rootEl = mssqlDoc.getRootElement();
        doReplaceConfig(rootEl, config);
        xmlManager.setDocument(mssqlDoc);
        xmlManager.saveXml(distPath);
        System.out.println("replaceMssql...OK");
    }

    private void doReplaceConfig(Element element, CompanyConfig config) throws Exception {
        Iterator<Element> it = element.elementIterator();
        if (it.hasNext()) {
            do {
                Element el = it.next();
                doReplaceConfig(el, config);
            } while (it.hasNext());
        } else {
            updateElementText(element, config);
        }
    }

    private void updateElementText(Element element, CompanyConfig config) throws Exception {
        String newText = element.getText();
        newText = newText.replace("{address}", config.getAddress());
        newText = newText.replace("{account}", config.getAccount());
        newText = newText.replace("{password}", config.getPassword());
        newText = newText.replace("{SFTSYS}", config.getSFTSYS());
        element.setText(newText);
    }
}
