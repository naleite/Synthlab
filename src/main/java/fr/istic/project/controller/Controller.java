package fr.istic.project.controller;

import fr.istic.project.model.Board;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.JavaFXController;
import fr.istic.project.view.Theme;
import fr.istic.project.view.module.Component;
import fr.istic.project.view.module.FxModule;
import fr.istic.project.view.module.port.FxPort;
import fr.istic.project.view.module.port.FxWire;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Controller {

    /**
     * The controller's model.
     */
    private Board model;

    /**
     * The controller's view.
     */
    private final JavaFXController view;

    /**
     * List of FxModules.
     */
    private final Map<Module, FxModule> modules;

    /**
     * List of FxPorts.
     */
    private final Map<JSynPort, FxPort> ports;

    /**
     * The mouse position in the window.
     */
    private Point2D relativeMouse;

    /**
     * Custom format to store the type of the module for drag and drop events.
     */
    private final DataFormat componentFormat = new DataFormat("component.type");

    /**
     * The current selected port.
     */
    private FxPort selectedFxPort;

    /**
     * The menu showed when the user right click.
     */
    private ContextMenu rightClickMenu;

    /**
     * The menu item.
     */
    private MenuItem menuDeleteItem;

    /**
     * Constructor.
     *
     * @param model the model
     * @param view the view
     */
    public Controller(Board model, JavaFXController view) {
        if (model == null || view == null) {
            throw new IllegalArgumentException();
        }

        this.model = model;
        this.view = view;

        this.ports = new HashMap<>();
        this.modules = new HashMap<>();
        this.relativeMouse = new Point2D(0, 0);

        // Right click menu initialization
        this.menuDeleteItem = new MenuItem("Delete");
        this.rightClickMenu = new ContextMenu();
        this.rightClickMenu.getItems().addAll(menuDeleteItem);

        // Observers

        // Activate the board
        model.addObserver(PropertyType.BOARD_ACTIVATED, (o, arg) -> {
            view.getBoardBtn().getStyleClass().removeAll("onButton", "offButton");
            if (model.isActivated()) {
                view.getBoardBtn().getStyleClass().add("onButton");
            } else {
                view.getBoardBtn().getStyleClass().add("offButton");
            }
        });

        // Clear the board
        model.addObserver(PropertyType.MODULE_LIST_CHANGED, (o, arg) -> {
            if (model.getModules().size() == 0) {
                view.getModuleContainer().getChildren().clear();
                view.getWireContainer().getChildren().clear();
            }
        });

        // Modules change observer
        model.addObserver(PropertyType.MODULE_LIST_CHANGED, (o, arg) -> {
            // No modules added
            if (modules.keySet().containsAll(model.getModules().values())) {
                return;
            }

            // Module addition
            Set<Module> newModule = new HashSet<>(model.getModules().values());
            newModule.removeAll(modules.keySet());
            for (Module m : newModule) {
                Long id = model.getModuleID(m);
                FxModule fxModule = Component.build(m.getClass(), model, id);
                view.addToContainer(fxModule);
                registerEvents(fxModule);
                modules.put(m, fxModule);
            }
        });

        // Create action handlers
        view.getBoardBtn().setOnAction(event -> model.setActivated(!model.isActivated()));
        view.getColorPicker().setOnAction(event -> model.setCurrentColor(view.getColorPicker().getValue()));

        // Populate the library from the component list
        for (Component item : Component.values()) {
            view.getLibrary().getItems().add(item);
        }

        createMenuBar();
        createDragControl();
    }

    /**
     * Creates the menu bar containing the different actions.
     */
    private void createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // When we press the Alt key, we hide/show the MenuBar
        view.getWindow().setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ALT)) {
                if (menuBar.isVisible()) {
                    menuBar.setVisible(false);
                    view.getWindow().setTop(null);
                } else {
                    menuBar.setVisible(true);
                    view.getWindow().setTop(menuBar);
                }
            }
        });

        // File
        Menu menuFile = new Menu("File");

        // Load
        MenuItem loadItem = new MenuItem("Open");
        loadItem.setOnAction(event -> loadConfig());
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));

        // Save
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(event -> saveConfig());
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        // Quit
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(event -> view.getStage().fireEvent(
            new WindowEvent(
                view.getStage(),
                WindowEvent.WINDOW_CLOSE_REQUEST)
        ));
        quitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));

        menuFile.getItems().addAll(loadItem, saveItem, new SeparatorMenuItem(), quitItem);

        // Edit
        Menu menuEdit = new Menu("Edit");

        // Clear
        MenuItem clearItem = new MenuItem("Clear");
        clearItem.setOnAction(event -> model.reset());
        clearItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));

        menuEdit.getItems().addAll(clearItem);

        // Preferences
        Menu menuPreferences = new Menu("Preferences");

        // Theme
        Menu menuTheme = new Menu("Theme");
        List<RadioMenuItem> themes = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();

        // Create a RadioMenuItem for each theme
        for (Theme theme : Theme.values()) {
            RadioMenuItem themeItem = new RadioMenuItem(theme.toString());
            themeItem.setToggleGroup(toggleGroup);
            // Define what happens when this theme is selected
            themeItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Clear the previous CSS classes
                    for (Theme themeToRemove : Theme.values()) {
                        view.getWindow().getStyleClass().remove(themeToRemove.getCssSelector());
                    }

                    // Add the new theme class
                    view.getWindow().getStyleClass().add(theme.getCssSelector());
                }
            });
            themes.add(themeItem);
        }

        // Preselect the first theme
        themes.get(0).setSelected(true);

        menuTheme.getItems().addAll(themes);
        menuPreferences.getItems().addAll(menuTheme);

        // Add the menus and set the menu bar to the top
        menuBar.getMenus().addAll(menuFile, menuEdit, menuPreferences);
        view.getWindow().setTop(menuBar);
    }

    /**
     * Registers the drag and drop event from the library.
     */
    private void createDragControl() {
        view.getLibrary().setOnDragDetected(event -> {
            ClipboardContent content = new ClipboardContent();
            // Activate the MOVE transfer mode
            Dragboard dragboard = view.getLibrary().startDragAndDrop(TransferMode.MOVE);

            // Save the selected item in the event
            Component selectedItem = view.getLibrary().getSelectionModel().getSelectedItem();
            content.put(componentFormat, selectedItem);
            dragboard.setContent(content);

            event.consume();
        });

        view.getLibrary().setOnDragDetected(event -> {
            ClipboardContent content = new ClipboardContent();
            // Activate the MOVE transfer mode
            Dragboard dragboard = view.getLibrary().startDragAndDrop(TransferMode.MOVE);

            // Save the selected item in the event
            Component selectedItem = view.getLibrary().getSelectionModel().getSelectedItem();
            content.put(componentFormat, selectedItem);
            dragboard.setContent(content);

            event.consume();
        });

        view.getContainer().setOnDragOver(event -> {
            // Check if the dragged element is valid
            if (event.getGestureSource() != null) {
                // Display the potential module position if it is already placed on the board
                if (event.getGestureSource() != view.getLibrary()) {
                    FxModule module = (FxModule) event.getGestureSource();
                    Module currModel = module.getModel();
                    // The module exists
                    if (view.containsModule(module)) {
                        currModel.setPosition(relativeMouse.multiply(-1).add(event.getX(), event.getY() - view.getTopHeight()));
                    }
                }
                // Accept the drop event
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        view.getContainer().setOnDragDropped(event -> {
            boolean success = false;
            Object source = event.getGestureSource();

            // Module dragged from the library
            if (source == view.getLibrary()) {
                try {
                    Component type = (Component) event.getDragboard().getContent(componentFormat);
                    Long id = model.addModule(type.getKlass());
                    Module module = model.getModule(id);
                    FxModule fxModule = modules.get(module);
                    double posX = Math.max(event.getX() - fxModule.getPrefWidth() / 2, 0);
                    double posY = Math.max(event.getY() - fxModule.getPrefHeight() / 2, 0);
                    module.setPosition(new Point2D(posX, posY));
                    success = true;
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            } else {
                // Something still got dropped on the container, we assume it's a module that has been moved
                if (source instanceof FxModule) {
                    FxModule module = (FxModule) source;
                    if (view.containsModule(module)) {
                        // User moves an existing module on the board
                        double posY = Math.max(event.getY() - relativeMouse.getY() - view.getTopHeight(), 0);
                        double posX = Math.max(event.getX() - relativeMouse.getX(), 0);
                        module.getModel().setPosition(new Point2D(posX, posY));
                        success = true;
                    }
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Visual effects
        view.getContainer().setOnDragEntered(event -> {
            if (event.getGestureSource() == view.getLibrary()) {
                view.getContainer().getStyleClass().add("draggedOn");
            }
            event.consume();
        });

        view.getContainer().setOnDragExited(event -> {
            if (event.getGestureSource() == view.getLibrary()) {
                view.getContainer().getStyleClass().remove("draggedOn");
            }
            event.consume();
        });
    }

    /**
     * Adds the drag and drop events for the component in the container.
     *
     * @param module the component.
     */
    private void registerEvents(FxModule<Module> module) {
        for (FxPort port : module.getPorts()) {
            ports.put(port.getModel(), port);
            port.getModel().addObserver(PropertyType.PORT_CONNECTED, (o, arg) -> {
                if (port.getModel().isConnected() && port.getModel().getPortType().equals(JSynPort.PortType.INPUT)) {
                    FxPort fxPort = ports.get(port.getModel());
                    FxPort other = ports.get(port.getModel().getConnected());
                    FxWire wire = new FxWire(fxPort, other, model.getCurrentColor());

                    // Draw the wire
                    view.addToContainer(wire);
                    registerEvents(wire);

                    port.getModel().addObserver(PropertyType.PORT_CONNECTED, (o1, arg1) -> {
                        if (!port.getModel().isConnected()) {
                            view.removeFromContainer(wire);
                        }
                    });
                }
            });
            // Add the OnClick action for each ports
            port.setOnAction(event -> {
                // The port's model
                JSynPort sourcePort = port.getModel();

                // No previous port has been selected
                if (selectedFxPort == null) {
                    // Save this port to link it with the next port
                    selectedFxPort = port;
                    selectedFxPort.select();
                } else {
                    // Next port has been selected
                    JSynPort targetPort = selectedFxPort.getModel();

                    if (sourcePort.canConnect(targetPort)) {
                        sourcePort.connect(targetPort);
                    } else {
                        // Deselects if the user clicks again on a selected port
                        if (sourcePort != targetPort) {
                            // Or display an alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("An error occurred while connecting");
                            alert.setContentText("You can't connect these ports.");
                            alert.showAndWait();
                        }
                    }
                    // Deselect the port
                    selectedFxPort.deselect();
                    selectedFxPort = null;
                }
            });
        }

        // Drag
        module.setOnDragDetected(event -> {
            // Allow the MOVE transfer mode
            Dragboard db = module.startDragAndDrop(TransferMode.ANY);

            // Save the node identifier on Dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString(module.toString());
            db.setContent(content);

            event.consume();
        });

        module.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rightClickMenu.setOnAction(e -> {
                    if (e.getTarget().equals(menuDeleteItem)) {
                        // Remove the module from model
                        model.removeModule(module.getModel());

                        // Reset the model
                        module.getModel().reset();

                        // Remove it from the view
                        view.removeFromContainer(module);
                        if (selectedFxPort != null) {
                            if (module.getModel().getInputs().contains(selectedFxPort.getModel())
                                    || module.getModel().getOutputs().contains(selectedFxPort.getModel())) {
                                selectedFxPort = null;
                            }
                        }
                    }
                });
                rightClickMenu.show(module, event.getScreenX(), event.getScreenY());
            } else {
                if (rightClickMenu.isShowing()) {
                    rightClickMenu.hide();
                }
            }

            event.consume();
        });

        module.setOnMousePressed(event -> relativeMouse = new Point2D(event.getSceneX() - module.getLayoutX(),
                event.getSceneY() - module.getLayoutY()));
    }

    /**
     * Shows the right click menu for the wire.
     *
     * @param wire the wire to add
     */
    private void registerEvents(FxWire wire) {
        wire.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rightClickMenu.setOnAction(e -> {
                    if (e.getTarget().equals(menuDeleteItem)) {
                        // Disconnect
                        wire.getStart().getModel().disconnect();
                    }
                });
                rightClickMenu.show(wire, event.getScreenX(), event.getScreenY());
            } else {
                if (rightClickMenu.isShowing()) {
                    rightClickMenu.hide();
                }
            }
            event.consume();
        });
    }

    /**
     * Saves the current configuration in a json file.
     */
    private void saveConfig() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("synth files (*.synth)", "*.synth");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(view.getStage());

        // Save the configuration to the file
        if (file != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(model.getMemento().toJson());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads a previous configuration from a json file.
     */
    private void loadConfig() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("synth files (*.synth)", "*.synth");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(view.getStage());

        // Get file content and load it.
        if (file != null) {
            try {
                StringBuilder content = new StringBuilder();
                Files.lines(file.toPath()).forEach(content::append);
                Memento m = new Memento(content.toString());
                try {
                    model.setMemento(m);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
