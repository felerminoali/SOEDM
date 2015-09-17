/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.myabstract;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import jaxb.connections.Connections;
import jaxb.connections.Connections.Datasource;
import mz.com.MainApp;
import mz.com.view.AddConnectionController;
import mz.com.view.FXOptionPane;
import mz.com.view.MainGUIController;
import mz.com.view.ShowConnSettingsController;

/**
 *
 * @author Lenovo
 */
public class MyTreeCell extends TextFieldTreeCell<String> {

    private ContextMenu addConnectionMenu, connectionConfigMenu;
    private MainGUIController mainGUIController;

    public MyTreeCell(MainGUIController mainGUIController) {
//        // instantiate the root context menu
        addConnectionMenu
                = ContextMenuBuilder.create()
                .items(
                        MenuItemBuilder.create()
                        .text("Add Connection")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
//                                        System.out.println("Menu Item Clicked!"); 
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/AddConnection.fxml"));
                                            AnchorPane page = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Add Connection");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);
                                            

                                            Scene scene = new Scene(page);
                                            dialogStage.setScene(scene);

                                             // Set the person into the controller.
                                            AddConnectionController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);
                                            
                                            controller.setMainApp(mainGUIController);
                                            
//                                            getScene().getWindow().
                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build()
                )
                .build();

        connectionConfigMenu
                = ContextMenuBuilder.create()
                .items(
                        MenuItemBuilder.create()
                        .text("Edit..")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
//                                        System.out.println(((MenuItem)arg0.getSource()));

//                                        System.out.println(getItem());

                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/AddConnection.fxml"));
                                            AnchorPane page = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Edit Connection");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            // Set the person into the controller.
                                            AddConnectionController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);

                                            controller.setMainApp(mainGUIController);
                                            
                                            // get the connection object
                                            JAXBContext jaxbContext = JAXBContext
                                            .newInstance(Connections.class);
                                            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                                            Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/datasource.xml"));

                                            Datasource selectDataSrc = null;
                                            for (Datasource dataSrc : datasource.getDatasource()) {
                                                if (dataSrc.getName().equals(getItem())) {
                                                    selectDataSrc = dataSrc;
                                                    break;
                                                }
                                            }

//                                            Connections.Datasource dataSrc = new Connections.Datasource();
//                                            dataSrc.setName(getItem());
//                                            dataSrc.setDomainName("http://192.168.0.104:8282/moodle-latest-27/moodle");
//                                            dataSrc.setToken("9c4991bde66fe656ecc942d9b61e0f04");
                                            controller.setDataSource(selectDataSrc);

                                            Scene scene = new Scene(page);
                                            dialogStage.setScene(scene);

                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        } catch (JAXBException ex) {
                                            Logger.getLogger(MyTreeCell.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                }
                        )
                        .build(),
                        // other menu item

                        MenuItemBuilder.create()
                        .text("Remove")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

//                                       getTreeItem().getChildren().remove(getItem());
//                                       getTreeItem().getChildren().remove(getItem());
//                                       getTreeItem();
//                                       (getScene().getWindow());
//                                        System.out.println((getScene().getWindow()));
//                                        System.out.println(getItem()); 
                                        if (FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), "Are sure that want delete " + getItem() + " connection", "Confirm") == FXOptionPane.Response.YES) {

                                            try {

                                                String file = "src/datasource.xml";
                                                // get the connection object
                                                JAXBContext jaxbContext = JAXBContext
                                                .newInstance(Connections.class);
                                                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                                                Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/datasource.xml"));

                                                for (Datasource dataSrc : datasource.getDatasource()) {
                                                    if (dataSrc.getName().equals(getItem())) {
                                                        datasource.getDatasource().remove(dataSrc);
                                                        break;
                                                    }
                                                }

                                                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
                                                // for getting nice formatted output
                                                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
                                                //specify the location and name of xml file to be created
                                                File XMLfile = new File(file);
//
                                                // Writing to XML file
                                                jaxbMarshaller.marshal(datasource, XMLfile);
                                            } catch (JAXBException ex) {
                                                FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), ex.getMessage(), "Error");
                                            } finally {

                                                // remove item from the treeView
                                                for (TreeItem item : getTreeItem().getParent().getChildren()) {
                                                    if (((String) item.getValue()).equals(getItem())) {
                                                        getTreeItem().getParent().getChildren().remove(item);
                                                        break;
                                                    }
                                                }

                                            }

                                        }
                                    }

                                }
                        )
                        .build(),
                        // Menu Show settings

                        MenuItemBuilder.create()
                        .text("Show settings..")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
//                                        System.out.println(((MenuItem)arg0.getSource()));

                                        System.out.println(getItem());

                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/ShowConnSettings.fxml"));
                                            AnchorPane page = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Show Connections Settings");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            // Set the person into the controller.
                                            ShowConnSettingsController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);

                                            // get the connection object
                                            JAXBContext jaxbContext = JAXBContext
                                            .newInstance(Connections.class);
                                            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                                            Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/datasource.xml"));

                                            Datasource selectDataSrc = null;
                                            for (Datasource dataSrc : datasource.getDatasource()) {
                                                if (dataSrc.getName().equals(getItem())) {
                                                    selectDataSrc = dataSrc;
                                                    break;
                                                }
                                            }

                                            controller.setDataSource(selectDataSrc);

                                            Scene scene = new Scene(page);
                                            dialogStage.setScene(scene);

                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        } catch (JAXBException ex) {
                                            Logger.getLogger(MyTreeCell.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                }
                        )
                        .build()
                )
                .build();

    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        // if the item is not empty and is a root...
//         if (!empty && getTreeItem().getParent()== null) {
//            setContextMenu(connectionConfigMenu);
//        }
        if (!empty && !getTreeItem().isLeaf()) {
            setContextMenu(connectionConfigMenu);
            return;
        }
//        

        setContextMenu(addConnectionMenu);
    }
}
