/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.myabstract;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mz.com.MainApp;
import mz.com.expert.AttributeType;
import mz.com.view.AddRecomendationController;
import mz.com.view.ESformController;
import mz.com.view.EditESConfigController;
import mz.com.view.FXOptionPane;

/**
 *
 * @author Lenovo
 */
public class MyTreeCellES extends TextFieldTreeCell<Object> {

    private final Node rootIcon;
    private final Node attributeIcon;
    private final Node valueIcon;
    private final Node conclusionIcon;
    private ESformController mainGUIController;

    private ContextMenu settingsMenu;
//    private MainController mainGUIController;
    private TextField textField;

    public MyTreeCellES(ESformController mainGUIController) {
//        // instantiate the root context menu

        this.mainGUIController = mainGUIController;

        rootIcon = mainGUIController.rootIcon;
        attributeIcon = mainGUIController.attributeIcon;
        valueIcon = mainGUIController.valueIcon;
        conclusionIcon = mainGUIController.conclusionIcon;

        settingsMenu
                = ContextMenuBuilder.create()
                .items(
                        MenuItemBuilder.create()
                        .text("Add New Attribute")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        addNewNode("A", attributeIcon);

                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Add New Value")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        addNewNode("V", valueIcon);
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Add New Conclusion")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        addNewNode("C", conclusionIcon);
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Delete Node")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        TreeItem<Object> c = getTreeView().getSelectionModel().getSelectedItem();

                                        String file = "src/main/resources/expert.xml";
//                                         String file = "src/expert.xml";
                                        JAXBContext jaxbContext;
                                        try {
                                            jaxbContext = JAXBContext
                                            .newInstance(AttributeType.class);

                                            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                                            AttributeType root = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

                                            removeRec(root, (AttributeType) c.getValue());

                                            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                                            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                            File XMLfile = new File(file);
                                            jaxbMarshaller.marshal(root, XMLfile);
                                            jaxbMarshaller.marshal(root, System.out);

                                        } catch (JAXBException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");
                                        }

                                        boolean removed = getTreeItem().getParent().getChildren().remove(c);

                                        System.out.println(c);
                                        if (removed) {
                                            System.out.println("\n\nRemoved");
                                        } else {
                                            System.out.println("\n\nNot removed");
                                        }

                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Add Recommendation")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/AddRecomendation.fxml"));
                                            AnchorPane pane = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Add Recommendation");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            Scene scene = new Scene(pane);
                                            dialogStage.setScene(scene);

                                            // Set the person into the controller.
                                            AddRecomendationController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);
                                            TreeItem<Object> c = getTreeView().getSelectionModel().getSelectedItem();

                                            controller.setConclusion((AttributeType) c.getValue());
                                            controller.setMainApp(mainGUIController);

                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }

                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Edit ES settings")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/EditESConfig.fxml"));
                                            AnchorPane pane = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Edit settings");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            Scene scene = new Scene(pane);
                                            dialogStage.setScene(scene);

                                            // Set the person into the controller.
                                            EditESConfigController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);
                                            TreeItem<Object> r = getTreeView().getSelectionModel().getSelectedItem();

                                            controller.setRoot((AttributeType) r.getValue());
                                            controller.setMainApp(mainGUIController);

                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");
                                        } catch (Exception ex) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), ex.getMessage(), "Error");
                                        }

                                    }
                                }
                        )
                        .build()
                )
                .build();
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    private void addNewNode(String attrType, Node icon) {

        AttributeType attr = new AttributeType();
        attr.setType(attrType);

        TreeItem<Object> newAttributeType = new TreeItem<>(attr, icon);

        String file = "src/main/resources/expert.xml";
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext
                    .newInstance(AttributeType.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AttributeType root = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

            addRec(root, (AttributeType) getTreeItem().getValue(), attr);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File XMLfile = new File(file);
            jaxbMarshaller.marshal(root, XMLfile);
            jaxbMarshaller.marshal(root, System.out);

        } catch (JAXBException e) {
            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");
        }

        getTreeItem().getChildren().add(newAttributeType);

    }

    private void removeRec(AttributeType root, AttributeType item) {

        for (AttributeType child : root.getAttribute()) {

            if (child.getId().equals(item.getId())) {
                root.getAttribute().remove(child);
                break;
            }
            removeRec(child, item);
        }
    }

    private void addRec(AttributeType root, AttributeType parent, AttributeType child) {

        if (root.getId().equals(parent.getId())) {
            root.getAttribute().add(child);
            return;
        }

        for (AttributeType node : root.getAttribute()) {

            if (node.getId().equals(parent.getId())) {
                node.getAttribute().add(child);
                break;
            }
            addRec(node, parent, child);
        }
    }

    private void editRec(AttributeType root, AttributeType item) {

        for (AttributeType child : root.getAttribute()) {
            if (child.getId().equals(item.getId())) {
                child.setName(item.getName());
                break;
            }
            editRec(child, item);
        }

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        if (((AttributeType) getItem()).getType().equals("R")) {
            setText((((AttributeType) getItem()).getName()));

            setGraphic(rootIcon);
        } else {
            setText((String) ((AttributeType) getItem()).getName());

            // eu
            if (((AttributeType) getItem()).getType().equals("A")) {
                setGraphic(attributeIcon);
            } else if (((AttributeType) getItem()).getType().equals("C")) {
                setGraphic(conclusionIcon);
            } else if (((AttributeType) getItem()).getType().equals("V")) {
                setGraphic(valueIcon);
            }
        }

    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setPromptText("Set New Value");

        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {

                    TreeItem<Object> c = getTreeView().getSelectionModel().getSelectedItem();

                    AttributeType attr = (AttributeType) c.getValue();
                    attr.setName(textField.getText());

                    String file = "src/main/resources/expert.xml";
                    JAXBContext jaxbContext;
                    try {
                        jaxbContext = JAXBContext
                                .newInstance(AttributeType.class);

                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        AttributeType root = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

                        editRec(root, attr);

                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        File XMLfile = new File(file);
                        jaxbMarshaller.marshal(root, XMLfile);
                        jaxbMarshaller.marshal(root, System.out);

                    } catch (JAXBException e) {
                        FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");
                    }

                    commitEdit(attr);

                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {

            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());

                if (((AttributeType) item).getType().equals("R")) {
                    setGraphic(rootIcon);

                    boolean bol = false;

                    if (getTreeItem().getChildren().isEmpty()) {
                        settingsMenu.getItems().get(0).setDisable(bol);
                    } else {
                        settingsMenu.getItems().get(0).setDisable(!bol);
                    }

                    settingsMenu.getItems().get(1).setDisable(!bol);
                    settingsMenu.getItems().get(2).setDisable(!bol);
                    settingsMenu.getItems().get(3).setDisable(!bol);

                    settingsMenu.getItems().get(4).setDisable(!bol);
                    settingsMenu.getItems().get(5).setDisable(bol);

                } else {

                    if (((AttributeType) item).getType().equals("A")) {
                        setGraphic(attributeIcon);

                        boolean bol = false;
                        settingsMenu.getItems().get(0).setDisable(!bol);
                        settingsMenu.getItems().get(1).setDisable(bol);
                        settingsMenu.getItems().get(2).setDisable(!bol);
                        settingsMenu.getItems().get(3).setDisable(bol);

                        settingsMenu.getItems().get(4).setDisable(!bol);
                        settingsMenu.getItems().get(5).setDisable(!bol);

                    } else if (((AttributeType) item).getType().equals("C")) {
                        setGraphic(conclusionIcon);

                        boolean bol = false;
                        settingsMenu.getItems().get(0).setDisable(!bol);
                        settingsMenu.getItems().get(1).setDisable(!bol);
                        settingsMenu.getItems().get(2).setDisable(!bol);
                        settingsMenu.getItems().get(3).setDisable(bol);

                        settingsMenu.getItems().get(4).setDisable(bol);
                        settingsMenu.getItems().get(5).setDisable(!bol);

                    } else if (((AttributeType) item).getType().equals("V")) {
                        setGraphic(valueIcon);

                        boolean bol = false;

                        if (getTreeItem().getChildren().isEmpty()) {
                            settingsMenu.getItems().get(0).setDisable(bol);
                        } else {
                            settingsMenu.getItems().get(0).setDisable(!bol);
                        }

                        settingsMenu.getItems().get(1).setDisable(!bol);
                        settingsMenu.getItems().get(2).setDisable(bol);
                        settingsMenu.getItems().get(3).setDisable(bol);

                        settingsMenu.getItems().get(4).setDisable(!bol);
                        settingsMenu.getItems().get(5).setDisable(!bol);
                    }
                }

            }

            setContextMenu(settingsMenu);
            return;
        } else {
            setText(null);
            setGraphic(null);
        }

    }
}
