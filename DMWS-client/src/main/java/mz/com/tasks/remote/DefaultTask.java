/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.tasks.remote;

import java.util.List;
import javafx.concurrent.Task;

/**
 *
 * @author feler
 */
public class DefaultTask extends Task<List<String>> implements Cancelable{
    
    public DefaultTask(){
    }

    @Override
    protected List<String> call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelProgress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
