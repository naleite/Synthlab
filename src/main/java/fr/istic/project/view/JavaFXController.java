package fr.istic.project.view;

import fr.istic.project.view.module.Component;
import fr.istic.project.view.module.FxModule;
import fr.istic.project.view.module.port.FxWire;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class JavaFXController implements Initializable {

    @FXML
    private BorderPane window;

    @FXML
    private Pane container;

    @FXML
    private ListView<Component> library;

    @FXML
    private Button boardBtn;

    @FXML
    private ColorPicker colorPicker;

    private Group wires;

    private Group modules;

    public ListView<Component> getLibrary() {
        return library;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public Group getWireContainer() {
        return wires;
    }

    public Group getModuleContainer() {
        return modules;
    }

    public BorderPane getWindow() {
        return window;
    }

    public Pane getContainer() {
        return container;
    }

    public Button getBoardBtn() {
        return boardBtn;
    }

    public Boolean hasInContainer(FxWire wire) {
        return wires.getChildren().contains(wire);
    }

    public Boolean hasInContainer(FxModule module) {
        return modules.getChildren().contains(module);
    }

    public Stage getStage() {
        return (Stage) window.getScene().getWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wires = new Group();
        modules = new Group();

        container.getChildren().add(wires);
        container.getChildren().add(modules);

        wires.toFront();
        modules.toBack();
    }

    public void addToContainer(FxWire wire) {
        if (!hasInContainer(wire)) {
            wires.getChildren().add(wire);
        }
    }

    public double getTopHeight() {
        return window.getLayoutY() - container.getLayoutY();
    }

    public void addToContainer(FxModule module) {
        modules.getChildren().add(module);
    }

    public void removeFromContainer(FxWire wire) {
        wires.getChildren().remove(wire);
    }

    public void removeFromContainer(FxModule module) {
        modules.getChildren().remove(module);
    }

    public boolean containsModule(FxModule module) {
        return modules.getChildren().contains(module);
    }
}
