/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jaxb.weka.associations.Associations;
import jaxb.weka.classifers.Classifers;
import jaxb.weka.cluster.Clusters;
import jaxb.weka.filters.Filters;
import mz.com.view.FXOptionPane;
import mz.com.view.ChooserController;
import mz.com.view.MainGUIController;

/**
 *
 * @author Lenovo
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainGUIController controllerMain;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SOEDM");

        initRootLayout();

        showPreprocessTasks();
//        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void initRootLayout() {

        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            primaryStage.show();

        } catch (IOException e) {
            FXOptionPane.showConfirmDialog(primaryStage, "Error", e.getMessage());

        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPreprocessTasks() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainGUI.fxml"));
            TabPane tabPreprocess = (TabPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(tabPreprocess);

            // Give the controller access to the main app.
            controllerMain = loader.getController();
            controllerMain.setMainApp(this);

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(primaryStage, "Error", e.getMessage());

        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
//    public boolean showFilterChooser(Person person) {
    public boolean showFilterChooser(Filters weka) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Chooser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Filter");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ChooserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWekaFilter(weka);

            // FOR TEST 
            controller.setMainController(controllerMain);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
//            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showFileChooser(String content) {

        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");

            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Text Files", "*.arff"),
                    new ExtensionFilter("All Files", "*.*"));

            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                FileWriter fileWriter = null;

                fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
            }
            return true;
        } catch (IOException e) {
//            Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
            return false;
        }
    }

    public boolean showClassifierChooser(Classifers weka) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Chooser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Classifier");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ChooserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWekaClassifier(weka);

            // FOR TEST 
            controller.setMainController(controllerMain);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
//            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showClusterChooser(Clusters weka) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Chooser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Cluster");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ChooserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWekaCluster(weka);

            // FOR TEST 
            controller.setMainController(controllerMain);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
//            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showAssociateChooser(Associations weka) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Chooser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Associator");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ChooserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWekaAssociator(weka);

            // FOR TEST 
            controller.setMainController(controllerMain);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
//            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public MainGUIController getMainGUIController() {
        return this.controllerMain;
    }
}
