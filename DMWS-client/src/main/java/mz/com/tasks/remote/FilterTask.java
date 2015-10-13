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
public class FilterTask extends Task<List<String>> implements Cancelable {

    private String data;
    private String filter;

    public FilterTask(String data, String filter) {
        this.data = data;
        this.filter = filter;
    }

    @Override
    protected List<String> call() throws Exception {

        updateProgress(-1.0, 100);
        try {

            ElearningWekaWSService service = new ElearningWekaWSService();
//
            ElearningWekaWS port = service.getElearningWekaWSPort();

            String newData = port.executeFilter(data, filter);

            updateProgress(-1.0, 100);
//            List<String> result = new ArrayList<>();
//            result.add(newData);
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

    @Override
    public void cancelProgress() {
        updateMessage("Process cancelled!");
        updateProgress(0, 100);
        cancel(true);
    }

}
