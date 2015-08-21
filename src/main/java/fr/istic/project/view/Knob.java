package fr.istic.project.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Knob for JavaFX.
 */
public class Knob extends VBox {

    private static final double OFFSET = 90;
    private static final double IMAGE_WIDTH = 20;
    private static final double IMAGE_HEIGHT = 20;
    private static final double RELATIVE_MIN_ANGLE = -45;
    private static final double RELATIVE_MAX_ANGLE = 225;

    /**
     * The main interaction button.
     */
    @FXML
    private Button button;

    /**
     * The value indicator.
     */
    @FXML
    private Button indicator;

    @FXML
    private StackPane scale;

    /**
     * The knob's name.
     */
    @FXML
    private Label name;

    /**
     * Helper slider.
     */
    private Slider slider;

    /**
     * The min value of the knob.
     */
    private final double min;

    /**
     * The max value of the knob.
     */
    private final double max;

    /**
     * The precision, defines the knob's scale.
     */
    private final double precision;

    /**
     * The knob's default value at start.
     */
    private final double defaultValue;

    /**
     * The knob's current value.
     */
    private final DoubleProperty value;

    /**
     * The knob's size.
     */
    private double size;

    /**
     * List of images to be displayed.
     */
    private List<String> images;

    public Knob(String name, double min, double max, double precision, double defaultValue, List<String> images) {
        // Check for illegal arguments
        if (min >= max) {
            throw new IllegalArgumentException("max value must be greater than min value");
        }
        if (!(min <= defaultValue && defaultValue <= max)) {
            throw new IllegalArgumentException("default value must be between " + min + " and " + max);
        }
        if (images == null) {
            throw new IllegalArgumentException("images list must not be null");
        }

        this.min = min;
        this.max = max;
        this.precision = precision;
        this.defaultValue = defaultValue;
        this.images = images;
        this.value = new SimpleDoubleProperty();

        initialize(name);
    }

    /**
     * Constructs a knob with values from range min to max.
     *
     * @param name the name of the knob
     * @param min the min value
     * @param max the max value
     * @param precision the precision between each value
     * @param defaultValue the defaultValue
     */
    public Knob(String name, double min, double max, double precision, double defaultValue) {
        this(name, min, max, precision, defaultValue, new ArrayList<>());
    }

    /**
     * Constructs a knob with values from range min to max.
     *
     * The default value is set to min.
     * @param name the name of the knob
     * @param min the min value
     * @param max the max value
     * @param precision the precision between each value
     */
    public Knob(String name, double min, double max, double precision) {
        this(name, min, max, precision, min);
    }

    private void initialize(String name) {
        final FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource("fxml/knob.fxml");
        loader.setLocation(url);
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException ignore) {
            //nothing
        }

        initializeSlider();

        this.name.setText(name);

        // Initial rotations
        this.button.setRotate(RELATIVE_MIN_ANGLE);
        this.indicator.setRotate(valueToAngle(defaultValue) - OFFSET);

        // Register listener
        this.button.setOnAction(ActionEvent::consume);
        this.indicator.setOnAction(ActionEvent::consume);

        setSize(60);
    }

    private void drawScale() {

        scale.getChildren().clear();
        scale.getStyleClass().add("scale");

        final double distanceToCenter = size / 2 + 5;
        final double lineSize = 4;

        // TODO: Better solution for this?
        int step = precision != 1 ? BigInteger.valueOf((long)min).gcd(BigInteger.valueOf((long)max)).intValue() : 1;

        int nbV = (int)(max - min) / step; // Number of the values which will be shown.
        int temp = nbV; // A copy of nbV
        int sp = 5; // The step of values.

        for (int i = (int) min; i <= max; i = i + step) {
            double angle = valueToAngle(i);

            double adj = Math.cos(Math.toRadians(angle)) * distanceToCenter;
            double op = Math.sin(Math.toRadians(angle)) * distanceToCenter;

            Line line = new Line();
            line.setStartX(0);
            line.setStartY(0);
            line.setEndX(lineSize);
            line.setEndY(0);

            line.setRotate(-angle);
            line.setTranslateX(adj);
            line.setTranslateY(-op);

            if (i == min) {
                draw((int) min, adj, op);
            }
            else if (i == max) {
                draw((int) max, adj, op);
            }
            else if (nbV <= 10) {
                draw(i, adj, op);
            }
            else if (nbV <= 30) {
                if (temp % sp == 0) {
                    draw(i, adj, op);
                }
                temp--;
            }

            scale.getChildren().addAll(line);
        }
    }

    private void initializeSlider() {
        slider = new Slider();

        // Keyboard increment number
        slider.setBlockIncrement(1.0);

        // Min
        slider.setMin(min);

        // Max
        slider.setMax(max);

        // Step
        slider.setMajorTickUnit(precision);

        // Number of intermediate values between each ticks
        slider.setMinorTickCount(0);

        // To adjust the values automatically
        slider.setSnapToTicks(true);

        // Set the defaultValue
        value.setValue(defaultValue);
    }

    /**
     * Gets the value of the knob.
     *
     * @return the current knob's value
     */
    public final double getValue() {
        return round(value.get(), precision);
    }

    /**
     * Sets the value of the knob.
     *
     * @param value the new value.
     */
    public final void setValue(double value) {
        this.value.set(value);
    }

    /**
     * Returns the value property.
     *
     * @return value
     */
    public DoubleProperty valueProperty() {
        return value;
    }

    /**
     * OnMousePressed handler method.
     *
     * @param event the javafx mouse event
     */
    @FXML
    public void onMousePressed(MouseEvent event) {
        onMouseDragged(event);
    }

    /**
     * OnMouseDragged handler method.
     *
     * @param event the javafx mouse event
     */
    @FXML
    public void onMouseDragged(MouseEvent event) {
        final Parent p = button.getParent();
        final Bounds b = button.getLayoutBounds();
        final Double centerX = b.getMinX() + (b.getWidth() / 2);
        final Double centerY = b.getMinY() + (b.getHeight() / 2);
        final Point2D center = p.localToParent(centerX, centerY);
        final Point2D mouse = p.localToParent(event.getX(), event.getY());
        final Double deltaX = mouse.getX() - center.getX();
        final Double deltaY = mouse.getY() - center.getY();
        final Double radians = Math.atan2(deltaY, deltaX);
        computeValue(Math.toDegrees(radians));
    }

    /**
     * Computes the new value using the angle.
     *
     * @param angle the angle to rotate.
     */
    private void computeValue(Double angle) {
        double rounded = round(angle + 180, 1);

        // Check if it is inbounds.
        // TODO: Check this
        if (0 <= rounded && rounded <= (RELATIVE_MAX_ANGLE - RELATIVE_MIN_ANGLE)) {

            // Compute the value using the rounded angle.
            double newValue = angleToValue(rounded);

            // Compute the value with the slider
            slider.adjustValue(round(newValue, precision));

            // Save the value
            setValue(slider.getValue());
        }
    }

    /**
     * Computes the value of the corresponding angle.
     *
     * @param angle angle in degree.
     * @return the value corresponding to the angle.
     */
    private double angleToValue(double angle) {
        return min + (angle * (max - min) / (RELATIVE_MAX_ANGLE - RELATIVE_MIN_ANGLE));
    }

    /**
     * Computes the angle of the corresponding value.
     *
     * @param value to knob's value.
     * @return the angle corresponding to the value.
     */
    private double valueToAngle(double value) {
        return RELATIVE_MIN_ANGLE + ((value - min) * (RELATIVE_MAX_ANGLE - RELATIVE_MIN_ANGLE) / (max - min));
    }

    /**
     * Rounding value helper.
     *
     * @param value the value to round.
     * @param step the rounding factor.
     * @return the rounded value.
     */
    private double round(double value, double step) {
        return Math.round(value / step) * step;
    }

    /**
     * Sets the knob's rotation.
     *
     * @param value the knob's value.
     */
    public void setRotation(double value) {
        // Set the new value
        setValue(value);

        // Place the indicator at the right angle
        indicator.setRotate(valueToAngle(value) - OFFSET);
    }

    /**
     * Sets the knob's size.
     *
     * @param size the new knob's size
     */
    public void setSize(double size) {
        this.size = size;

        // TODO: Find a better way to set the size in the CSS
        button.setStyle("-fx-min-width: " + size + "; -fx-min-height: " + size + "; -fx-max-width: " + size + ";" + "-fx-max-height: " + size + ";");
        indicator.setStyle("-fx-min-width: " + size + "; -fx-min-height: " + size + "; -fx-max-width: " + size + ";" + "-fx-max-height: " + size + ";");

        drawScale();
    }

    /**
     * Draws the knob's value at the coordinates
     *
     * @param value the value to draw
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void draw(int value, double x, double y) {
        Node node;
        double offset = 1.25;
        double imOffsetX = 0;
        double imOffsetY = 0;

        // If there are images, display them
        if (!images.isEmpty()) {
            node = new ImageView(new Image(images.get(value), IMAGE_WIDTH, IMAGE_HEIGHT, true, true));

            // Values offsetting the image according to its dimensions
            imOffsetX = sign(x) * IMAGE_WIDTH / 2;
            imOffsetY = sign(y) * IMAGE_HEIGHT / 2;
        }
        // If not, display the numerical values instead
        else {
            node = new Text(String.valueOf(value));
            ((Text) node).setFont(new Font(10));
        }

        // Move the node and add it to the view
        node.setTranslateX(-x * offset - imOffsetX);
        node.setTranslateY(-y * offset - imOffsetY);
        scale.getChildren().add(node);
    }

    /**
     * Returns the sign of a value.
     *
     * @param value the value
     * @return -1 if the value is negative, 1 if it is positive, or 0.
     */
    private double sign(double value) {
        if (value == 0) {
            return value;
        }

        return round(value, 1) / Math.abs(value);
    }
}
