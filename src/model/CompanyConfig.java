/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import net.sf.json.JSONObject;

/**
 *
 * @author user
 */
public class CompanyConfig {
    private String companyName = "";
    private String address = "";
    private String account = "";
    private String password = "";
    private String SFTSYS = "";
    private String patchPath = "";

    public CompanyConfig(String companyName, String address, String account, String password, String SFTSYS, String patchPath) {
        this.companyName = companyName;
        this.address = address;
        this.account = account;
        this.password = password;
        this.SFTSYS = SFTSYS;
        this.patchPath = patchPath;
    }
    
    public CompanyConfig(JSONObject json) {
        this.companyName = json.getString("companyName");
        this.address = json.getString("address");
        this.account = json.getString("account");
        this.password = json.getString("password");
        this.SFTSYS = json.getString("SFTSYS");
        this.patchPath = json.getString("patchPath");
    }

    public CompanyConfig() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSFTSYS() {
        return SFTSYS;
    }

    public void setSFTSYS(String SFTSYS) {
        this.SFTSYS = SFTSYS;
    }
    
    public String getPatchPath() {
        return patchPath;
    }

    public void setPatchPath(String patchPath) {
        this.patchPath = patchPath;
    }
}
