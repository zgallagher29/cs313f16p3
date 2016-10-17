package edu.luc.etl.cs313.android.shapes.model;
import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

	// TODO entirely your job (except onCircle)

	@Override
	public Location onCircle(final Circle c) {
		final int radius = c.getRadius();
		return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
	}

	@Override
	public Location onFill(final Fill f) {
        return f.getShape().accept(this);
	}

	@Override
	public Location onGroup(final Group g) {

		int xleft = Integer.MAX_VALUE;
		int xright = Integer.MIN_VALUE;
		int ydown = Integer.MAX_VALUE;
		int yup = Integer.MIN_VALUE;
		final List<? extends Shape> l = g.getShapes();
		for (Shape shape : l) {
			final Location bb = shape.accept(this);
			final Rectangle rect = (Rectangle) bb.getShape();

			xleft = Math.min(xleft, bb.getX());
			xright = Math.max(xright, bb.getX() + rect.getWidth());
			ydown = Math.min(ydown, bb.getY());
			yup = Math.max(yup, bb.getY() + rect.getHeight());
		}

		return new Location(xleft, ydown, new Rectangle(xright - xleft, yup - ydown));
	}

	@Override
	public Location onLocation(final Location l) {
		final int x = l.getX();
		final int y = l.getY();

		final Location newLocation = l.getShape().accept(this);
		final int newX = newLocation.getX();
		final int newY = newLocation.getY();
		return new Location((x + newX), (y + newY), newLocation.getShape());

	}

	@Override
	public Location onRectangle(final Rectangle r) {
		final int x = r.getWidth();
		final int y = r.getHeight();

		final int startX = (1/2) * x;
		final int startY = (1/2) * y;
		return new Location(-startX, -startY, r);
	}

	@Override
	public Location onStroke(final Stroke c) {
		return c.getShape().accept(this);
	}

	@Override
	public Location onOutline(final Outline o) {

		return o.getShape().accept(this);
	}

	@Override
	public Location onPolygon(final Polygon s) {
        int xleft = Integer.MAX_VALUE;
        int xright = Integer.MIN_VALUE;
        int ydown = Integer.MAX_VALUE;
        int yup = Integer.MIN_VALUE;
        final List<? extends Point> l = s.getPoints();
        for (Point point : l) {
            xleft = Math.min(xleft, point.accept(this).getX());
            xright = Math.max(xright, point.accept(this).getX());
            ydown = Math.min(ydown, point.accept(this).getY());
            yup = Math.max(yup, point.accept(this).getY());
        }

        return new Location(xleft, ydown, new Rectangle((xright - xleft), (yup - ydown)));
    }
	}

