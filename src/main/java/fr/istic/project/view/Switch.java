package fr.istic.project.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Switch extends StackPane {

    private Label label;
    private Button switchBtn;
    private String onLabel;
    private String offLabel;

    private final SimpleBooleanProperty switchedOn;

    public Switch() {
        this("On", "Off");
    }

    public Switch(String onLabel, String offLabel) {
        switchedOn = new SimpleBooleanProperty(true);

        // Define the interactive button
        switchBtn = new Button();
        switchBtn.getStyleClass().add("switch-button");
        switchBtn.setOnAction(t -> switchedOn.set(!switchedOn.get()));
        setOnMouseClicked(t -> switchedOn.set(!switchedOn.get()));

        // Define the label
        label = new Label();
        label.getStyleClass().add("switch-label");

        setOnLabel(onLabel);
        setOffLabel(offLabel);
        
        getChildren().addAll(label, switchBtn);

        switchedOn.set(false);
    }

    /**
     * Get the boolean property.
     * @return the boolean property.
     */
    public SimpleBooleanProperty valueProperty() {
        return switchedOn;
    }
    
    /**
     * Gets the button's state.
     *
     * @return true if the button is switched on.
     */
    public boolean isOn() {
        return switchedOn.get();
    }

    /**
     * Sets the text for the switched on value.
     *
     * @param onLabel the text for the switched on value.
     */
    public void setOnLabel(String onLabel) {
        this.onLabel = onLabel;
    }

    /**
     * Sets the text for the switched off value.
     *
     * @param offLabel the label of the off value.
     */
    public void setOffLabel(String offLabel) {
        this.offLabel = offLabel;
    }

    /**
     * Updates the component when switched on.
     */
    public void switchOn() {
        switchedOn.set(true);
        label.setText(this.onLabel);
        label.getStyleClass().removeAll("switch-on", "switch-off");
        label.getStyleClass().add("switch-on");
        label.setAlignment(Pos.CENTER_LEFT);
        setAlignment(switchBtn, Pos.CENTER_RIGHT);
    }

    /**
     * Updates the component when switched off.
     */
    public void switchOff() {
        switchedOn.set(false);
        label.setText(this.offLabel);
        label.getStyleClass().removeAll("switch-on", "switch-off");
        label.getStyleClass().add("switch-off");
        label.setAlignment(Pos.CENTER_RIGHT);
        setAlignment(switchBtn, Pos.CENTER_LEFT);
    }
}
