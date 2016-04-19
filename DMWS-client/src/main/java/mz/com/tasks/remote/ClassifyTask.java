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
public class ClassifyTask extends Task<List<String>> implements Cancelable{

    private String data;
    private String classifier;
    private String evaluation;
    
    private long timeMillis = 0;

    
    public ClassifyTask(String data, String classifier, String ecaluation) {
        this.data = data;
        this.classifier = classifier;
        this.evaluation = ecaluation;
    }
    
     public ClassifyTask(String data, String classifier) {
        this.data = data;
        this.classifier = classifier;
        this.evaluation = null;
    }

    @Override
    protected List<String> call() throws Exception {

        long starttime = System.currentTimeMillis();
        updateProgress(-1.0, 100);
        try {

            ElearningWekaWSService service = new ElearningWekaWSService();

            ElearningWekaWS port = service.getElearningWekaWSPort();
            
            String newData = port.executeClassifier(data, classifier, evaluation);

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
