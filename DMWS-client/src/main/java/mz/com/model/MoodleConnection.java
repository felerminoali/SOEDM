/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Lenovo
 */
public class MoodleConnection {

    private String token;
    private String domainName;
    private String restformat = "xml";
    private String functionName;
    private final String urlParameters = "Default, ";

//    private String response;
    public MoodleConnection(String token, String domainName, String functionName) {
        this.token = token;
        this.domainName = domainName;
        this.functionName = functionName;
    }

     public MoodleConnection(String token, String domainName) {
        this.token = token;
        this.domainName = domainName;
    }

    public String getResonse() throws Exception {

        // / REST RETURNED VALUES FORMAT
        // 'json'
        // Setting it to 'json' will fail all calls
        // on earlier Moodle version
        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        // / PARAMETERS - NEED TO BE CHANGED IF YOU CALL A DIFFERENT FUNCTION

                        // REST CALL
        // Send request
        String serverurl = domainName + "/webservice/rest/server.php"
                + "?wstoken=" + token + "&wsfunction=" + functionName;

        HttpURLConnection con = (HttpURLConnection) new URL(serverurl)
                .openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        // Get Response
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }

     public String testConnection() throws Exception {

        // / REST RETURNED VALUES FORMAT
        // 'json'
        // Setting it to 'json' will fail all calls
        // on earlier Moodle version
         
          this.functionName = "local_miningdata_get_students_data";
          
        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        // / PARAMETERS - NEED TO BE CHANGED IF YOU CALL A DIFFERENT FUNCTION

                        // REST CALL
        // Send request
        String serverurl = domainName + "/webservice/rest/server.php"
                + "?wstoken=" + token + "&wsfunction=" + functionName;

        HttpURLConnection con = (HttpURLConnection) new URL(serverurl)
                .openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        // Get Response
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getUrlParameters() {
        return urlParameters;
    }

}
