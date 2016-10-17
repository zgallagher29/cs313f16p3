package edu.luc.etl.cs313.android.shapes.model;

/**
 * A decorator for specifying the stroke (foreground) color for drawing the
 * shape.
 */
public class Stroke implements Shape {

	final Shape shape;
	final int color;
	public Stroke(final int color, final Shape shape) {
		this.shape =shape;
		this.color=color;
	}

	public int getColor() {
		return color;
	}

	public Shape getShape() {
		return shape;
	}


	public <Result> Result accept(Visitor<Result> v) {
		return v.onStroke(this);
	}
}
