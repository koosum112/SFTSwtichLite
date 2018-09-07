/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author user
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdProxy {

    public static void callCmd(String commandLine) throws Exception {
        String msg = "";
        System.out.println("commandLine=" + commandLine);
        Runtime rt = null;
        Process p = null;
        BufferedReader input = null;
        String line = null;
        try {
            rt = Runtime.getRuntime();
//          p = rt.exec("cmd.exe /c " + commandLine.toString());
            p = rt.exec(commandLine.toString());
            input = new BufferedReader(new InputStreamReader(p.getInputStream(), "BIG5")); //CMD編碼預設為BIG5
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                msg = line;
            }
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (input != null) {
                input.close();
            }
            throw ex;
        } 
    }
}
