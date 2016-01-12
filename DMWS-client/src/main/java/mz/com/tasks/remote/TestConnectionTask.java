/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.tasks.remote;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;

/**
 *
 * @author Lenovo
 */
public class TestConnectionTask extends Task<List<String>> implements Cancelable {

    private String token;
    private String domainName;
    private String restformat = "xml";
    private String functionName;
    private final String urlParameters = "Default, ";

    public TestConnectionTask(String token, String domainName, String functionName) {
        this.token = token;
        this.domainName = domainName;
        this.functionName = functionName;
    }

    public TestConnectionTask(String token, String domainName) {
        this.token = token;
        this.domainName = domainName;
    }

    @Override
    protected List<String> call() throws Exception {

        StringBuilder response = new StringBuilder();
        try {

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
            
            System.out.println(serverurl);

            HttpURLConnection con = (HttpURLConnection) new URL(serverurl)
                    .openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Language", "en-US");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDoInput(true);
//            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

//            
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            response.append("Connection successful");
            updateProgress(0, 100);

        } catch (Exception ex) {

            updateMessage("Connection Failed! \n" + ex.getMessage());
            updateProgress(0, 100);
            cancel(true);
            return null;
        }

        updateMessage(response.toString());
        return new ArrayList<>();

    }

    public long getTotalBytes() throws Exception {

        try {
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
            long totalBytesRead = 0;

            while ((line = rd.readLine()) != null) {

                StringBuilder sb1 = new StringBuilder(line);
                sb1.reverse();
                String lineReversed = sb1.toString();

                byte[] bytesRead = lineReversed.getBytes();

                for (int i = 0; i < bytesRead.length; i++) {
                    totalBytesRead = totalBytesRead
                            + bytesRead[i];
                }
            }
            rd.close();
            return totalBytesRead;
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public void cancelProgress() {
        updateMessage("Process cancelled!");
        updateProgress(0, 100);
        cancel(true);
    }

}
