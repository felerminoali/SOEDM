/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.tasks.remote;

import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import mz.com.wsweka.ElearningWekaWS;
import mz.com.wsweka.ElearningWekaWSService;

/**
 *
 * @author Lenovo
 */
public class ClusterTask extends Task<List<String>> implements Cancelable {

    private String data;
    private String cluster;
    private String evaluation;

    private long timeMillis = 0;

    public ClusterTask(String data, String cluster, String ecaluation) {
        this.data = data;
        this.cluster = cluster;
        this.evaluation = ecaluation;
    }

    public ClusterTask(String data, String cluster) {
        this.data = data;
        this.cluster = cluster;
        this.evaluation = null;
    }

    @Override
    protected List<String> call() throws Exception {
        long starttime = System.currentTimeMillis();
        updateProgress(-1.0, 100);
        try {

            ElearningWekaWSService service = new ElearningWekaWSService();

            ElearningWekaWS port = service.getElearningWekaWSPort();

            String newData = port.executeCluster(data, cluster, evaluation);

            updateProgress(-1.0, 100);
//            List<String> result = new ArrayList<>();
//            result.add(newData);
            updateMessage(newData);
            updateProgress(0, 100);

            long stoptime = System.currentTimeMillis();
            long elapsedtime = stoptime - starttime;
            this.timeMillis = elapsedtime;

            return new ArrayList<>();
        } catch (Exception e) {

            updateMessage("Unsucceful!");
            updateProgress(0, 100);
            cancel(true);
            return null;
        }
    }

    @Override
    public void cancelProgress() {
        updateMessage("Process cancelled!");
        updateProgress(0, 100);
        cancel(true);
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}
