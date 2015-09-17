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
public class VisualizeTreeTask extends Task<List<String>> {

    
    private String data;
    private String classifier;
    private String evaluation;

    
    public VisualizeTreeTask(String data, String classifier, String ecaluation) {
        this.data = data;
        this.classifier = classifier;
        this.evaluation = ecaluation;
    }

    
    public VisualizeTreeTask(String data, String classifier) {
        this.data = data;
        this.classifier = classifier;
        this.evaluation = null;
    }

    
    @Override
    protected List<String> call() throws Exception {

        updateProgress(-1.0, 100);
        try {

            ElearningWekaWSService service = new ElearningWekaWSService();

            ElearningWekaWS port = service.getElearningWekaWSPort();
            
            String newData = port.visualizeTreeGraph(data, classifier, evaluation);

            updateProgress(-1.0, 100);
            updateMessage(newData);
            updateProgress(0, 100);

            return new ArrayList<>();
        } catch (Exception e) {

            updateMessage("Unsucceful!");
            updateProgress(0, 100);
            cancel(true);
            return null;
        }
    }
    
}
