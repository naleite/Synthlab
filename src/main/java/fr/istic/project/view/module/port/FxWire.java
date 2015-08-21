package fr.istic.project.view.module.port;

import fr.istic.project.model.observable.PropertyType;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

import java.util.Observer;

public class FxWire extends CubicCurve {

    private static final int SPEED = 70;
    private static final double GRAVITY = 0.3;

    private static final double SHADOW_OFFSET_X = 15;
    private static final double SHADOW_OFFSET_Y = 15;
    private static final double SHADOW_RADIUS = 10;
    private static final Color SHADOW_COLOR = Color.color(0.1, 0.1, 0.1);

    private final FxPort start;
    private final FxPort end;

    public FxWire(FxPort start, FxPort end, Color color) {
		if (start == null || end == null || color == null) {
			throw new IllegalArgumentException();
		}

		this.start = start;
		this.end = end;

        getStyleClass().add("wire");

        createController();
        createEffect(color);
        setFill(null);
    }

	public FxPort getStart() {
		return start;
	}

	public FxPort getEnd() {
		return end;
	}

    public Paint getColor() {
        return getStroke();
    }

    protected void updatePosition() {

        Point2D first;
        Point2D last;

        if (start.getX() < end.getX()) {
            first = new Point2D(start.getX(), start.getY());
            last = new Point2D(end.getX(), end.getY());
        } else {
            first = new Point2D(end.getX(), end.getY());
            last = new Point2D(start.getX(), start.getY());
        }

        // Mid
        Point2D mid1 = new Point2D(
            first.midpoint(last).midpoint(first).getX(),
            first.midpoint(last).getY() + GRAVITY * first.distance(last)
        );

        Point2D mid2 = new Point2D(
            first.midpoint(last).midpoint(last).getX(),
            first.midpoint(last).getY() + GRAVITY * first.distance(last)
        );

        setStartX(first.getX());
        setStartY(first.getY());

        setControlX1(mid1.getX());
        setControlY1(mid1.getY());

        setControlX2(mid2.getX());
        setControlY2(mid2.getY());

        setEndX(last.getX());
        setEndY(last.getY());
    }

    private void createEffect(Color color) {
        setStrokeLineCap(StrokeLineCap.ROUND);
        toFront();
        setStroke(color);
        setStrokeWidth(8);
        setFill(Color.TRANSPARENT);

        DropShadow dropShadow = new DropShadow();
        InnerShadow innerShadow = new InnerShadow();

        innerShadow.setColor(Color.BLACK);
        innerShadow.setBlurType(BlurType.GAUSSIAN);

        dropShadow.setOffsetY(SHADOW_OFFSET_X);
        dropShadow.setOffsetX(SHADOW_OFFSET_Y);
        dropShadow.setColor(SHADOW_COLOR);
        dropShadow.setRadius(SHADOW_RADIUS);
        dropShadow.setInput(innerShadow);

        setEffect(dropShadow);
    }

    private void createController() {
        Observer o = (o1, arg) -> updatePosition();

        start.getModel().getModule().addObserver(PropertyType.MODULE_POSITION_CHANGED, o);
        end.getModel().getModule().addObserver(PropertyType.MODULE_POSITION_CHANGED, o);

        ChangeListener<Number> cl = (observable, oldValue, newValue) -> updatePosition();

        start.setOnMove(cl);
        end.setOnMove(cl);

        setOnMouseEntered(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(SPEED), this);
            ft.setFromValue(1.0);
            ft.setToValue(0.5);
            ft.play();
        });

        setOnMouseExited(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(SPEED), this);
            ft.setFromValue(0.5);
            ft.setToValue(1);
            ft.play();
        });
    }
}
