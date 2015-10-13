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
public class AssociateTask extends Task<List<String>> implements Cancelable{

    private String data;
    private String associator;

    public AssociateTask(String data, String associator) {
        this.data = data;
        this.associator = associator;
    }

    @Override
    protected List<String> call() throws Exception {

        updateProgress(-1.0, 100);
        try {

            ElearningWekaWSService service = new ElearningWekaWSService();

            ElearningWekaWS port = service.getElearningWekaWSPort();

            String newData = port.executeAssociate(data, associator);

            updateProgress(-1.0, 100);
            updateMessage(newData);
            updateProgress(0, 100);

            return new ArrayList<>();
        } catch (Exception e) {

            updateMessage("Unsucceful!");
            updateProgress(0, 100);
            cancel(true);
            return new ArrayList<>();
        }
    }

    @Override
    public void cancelProgress() {
        updateMessage("Process cancelled!");
        updateProgress(0, 100);
        cancel(true);
    }

}
