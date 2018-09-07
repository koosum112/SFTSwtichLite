/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author user
 */
public class CompanyConfigManager {

    private static HashMap<String, CompanyConfig> companyConfig = new HashMap<String, CompanyConfig>();
    private static CompanyConfigManager instance;
    private static final String FILE_PATH = "company.conf.xml";

    private CompanyConfigManager() {
        File configFile = new File(FILE_PATH);
        if (!configFile.exists()) {
            buildDomXml();
        }
    }

    // 多執行緒時，當物件需要被建立時才使用synchronized保證Singleton一定是單一的 ，增加程式效能
    public static CompanyConfigManager getInstance() {
        if (instance == null) {
            synchronized (CompanyConfigManager.class) {
                if (instance == null) {
                    instance = new CompanyConfigManager();
                    loadDomXml();
                }
            }
        } else {
            loadDomXml();
        }
        return instance;
    }

    public static void loadDomXml() {
        XmlManager xmlManager = new XmlManager();
        xmlManager.loadXml(FILE_PATH);
        parse(xmlManager.getDocument());
    }

    private void buildDomXml() {
        XmlManager xmlManager = new XmlManager();
        xmlManager.setDocument(buildDocument());
        xmlManager.saveXml(FILE_PATH);
    }

    public static void parse(Document doc) {
        List<Element> companyList = doc.selectNodes("//config/company");
        if (companyList != null) {
            companyList.forEach((companyEl) -> {
                CompanyConfig config = new CompanyConfig();
                String key = companyEl.selectSingleNode("companyName").getText().trim();
                String address = companyEl.selectSingleNode("address").getText().trim();
                String account = companyEl.selectSingleNode("account").getText().trim();
                String password = companyEl.selectSingleNode("password").getText().trim();
                String SFTSYS = companyEl.selectSingleNode("SFTSYS").getText().trim();
                String patchPath = companyEl.selectSingleNode("patchPath").getText().trim();

                config.setCompanyName(key);
                config.setAddress(address);
                config.setAccount(account);
                config.setPassword(password);
                config.setSFTSYS(SFTSYS);
                config.setPatchPath(patchPath);
                companyConfig.put(key, config);
            });
        }
    }

    private Document buildDocument() {
        Document document = DocumentHelper.createDocument();
        Element configEl = document.addElement("config");

        companyConfig.forEach((key, value) -> {
            Element companyEl = configEl.addElement("company");
            companyEl.addElement("companyName").setText(value.getCompanyName());
            companyEl.addElement("address").setText(value.getAddress());
            companyEl.addElement("account").setText(value.getAccount());
            companyEl.addElement("password").setText(value.getPassword());
            companyEl.addElement("SFTSYS").setText(value.getSFTSYS());
            companyEl.addElement("patchPath").setText(value.getPatchPath());
        });
        return document;
    }

    public Boolean isExsitCompany(String companyName) {
        if (companyConfig.containsKey(companyName)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateCompanyConfig(CompanyConfig config) throws Exception {
        String companyName = config.getCompanyName();
        if (!isExsitCompany(companyName)) {
            addCompanyConfig(config);
        } else {
            replaceCompanyConfig(config);
        }
        buildDomXml();
    }

    public void addCompanyConfig(CompanyConfig config) throws Exception {
        String companyName = config.getCompanyName();
        if (!companyConfig.containsKey(companyName)) {
            companyConfig.put(companyName, config);
        } else {
            throw new Exception("已存在的公司別");
        }
    }

    public void removeCompanyConfig(String companyName) throws Exception {
        if (companyConfig.containsKey(companyName)) {
            companyConfig.remove(companyName);
        } else {
            throw new Exception("不存在的公司別");
        }
    }

    public void replaceCompanyConfig(CompanyConfig config) throws Exception {
        String companyName = config.getCompanyName();
        if (companyConfig.containsKey(companyName)) {
            companyConfig.replace(companyName, config);
        } else {
            throw new Exception("不存在的公司別");
        }
    }

    public HashMap<String, CompanyConfig> getCompanyConfig() {
        return companyConfig;
    }

    public CompanyConfig getCompanyConfig(String companyName) throws Exception {
        if (companyConfig.containsKey(companyName)) {
            return companyConfig.get(companyName);
        } else {
            throw new Exception("不存在的公司別");
        }
    }

    public Integer getCompanyConfigCount() {
        return companyConfig.size();
    }
}
