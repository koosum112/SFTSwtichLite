/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileOutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author user
 */
public class XmlManager {

    private Document document;

    public XmlManager() {
        document = DocumentHelper.createDocument();
    }

    public XmlManager(Document document) {
        this.document = document;
    }

    public void loadXml(String sourcePath) {
        loadXml(new File(sourcePath));
    }

    public void loadXml(File file) {
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveXml(String distPath) {
        File distFile = new File(distPath);
        saveXml(distFile);
    }

    public void saveXml(File distFile) {
        FileOutputStream fout = null;
        OutputFormat of = getDefaultOutFormat();
        XMLWriter xw = null;
        try {
            //輸出xml檔案
            fout = new FileOutputStream(distFile);
            xw = new XMLWriter(fout, of);
            xw.write(document);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fout.close();
                xw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private OutputFormat getDefaultOutFormat() {
        OutputFormat of = OutputFormat.createCompactFormat();
        of.setNewLineAfterDeclaration(false);
        of.setIndent(" ");
        of.setIndentSize(2); 			//t=用Tab區分層級
        of.setLineSeparator("\n");           //每一行用\r\n 隔開
        of.setPadText(false); 			//每一行尾端不補上空格
        of.setNewlines(true);			//自動換行
        return of;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
