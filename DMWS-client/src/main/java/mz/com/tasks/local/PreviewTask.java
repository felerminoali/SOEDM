/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.tasks.local;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import mz.com.view.FXOptionPane;

/**
 *
 * @author Lenovo
 */
public class PreviewTask extends Task<List<String>> {

    @Override
    protected List<String> call() throws Exception {
        updateProgress(-1.0, 100);
        updateMessage("Preview Start");
        try {
            try {
                // TODO add your handling code here:
                String HS_FILE = "ES/HelpSet.hs";
                URL hsURL = new URL((new File(".")).toURL(), HS_FILE);
                HelpSet helpSet;
                HelpBroker helpBroker = null;
                ClassLoader cl = getClass().getClassLoader();

                try {
                    helpSet = new HelpSet(cl, hsURL);

                    helpBroker = helpSet.createHelpBroker();
                    helpBroker.setDisplayed(true);

                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            } catch (MalformedURLException ex) {
                FXOptionPane.showConfirmDialog(null, ex.getMessage(), "Error");
            }

            updateMessage("Finish");
            updateMessage("");
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
