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
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class TestConnectionTask extends Task<List<String>> {

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
            String linha;

            long totalBytesRead = getTotalBytes();
            long totalBytesReadNow = 0;
            int progress = 0;

            if (totalBytesRead > 0) {

                while ((linha = rd.readLine()) != null) {

                    response.append(linha);
                    response.append('\r');
                    response.append('\n');

                    StringBuilder sb1n = new StringBuilder(linha);

                    sb1n.reverse();

                    String lineReversed = sb1n.toString();

                    byte[] bytesReadNow = lineReversed.getBytes();

                    for (int i = 0; i < bytesReadNow.length; i++) {
                        totalBytesReadNow = totalBytesReadNow
                                + bytesReadNow[i];

                        if (totalBytesReadNow >= totalBytesRead * 0.01) {
                            progress = progress + 1;
                            totalBytesReadNow = 0;

                            if (isCancelled()) {
                                break;
                            }

                            updateProgress(progress, 100);
                        }

                    }
                    Thread.sleep(0, 1);
                }
            }

            rd.close();
        } catch (Exception ex) {
            
            updateProgress(0, 100);
            cancel(true);
        }
//        List<String> result = new ArrayList<>();
//        result.add(response.toString());
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

}
