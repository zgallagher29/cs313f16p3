package edu.luc.etl.cs313.android.shapes.model;
import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {



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

        int minX=g.getShapes().get(0).accept(this).getX();
        int minY=g.getShapes().get(0).accept(this).getY();
        int maxX=0;
        int maxY=0;
        int tempX;
        int tempY;
        for(Shape s: g.getShapes()) {
            Rectangle r=(Rectangle)s.accept(this).getShape();
            tempX=s.accept(this).getX();
            tempY=s.accept(this).getY();
            if(tempX+r.getWidth()>maxX){
                maxX=tempX+r.getWidth();
            }
            if(tempX<minX){
                minX=tempX;
            }
            if(tempY+r.getHeight()>maxY){
                maxY=tempY+r.getHeight();
            }
            if(tempY<minY){
                minY=tempY;
            }
        }
        return new Location(minX,minY,new Rectangle((maxX-minX),(maxY-minY)));
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
        int minX=s.getPoints().get(0).getX();
        int minY=s.getPoints().get(0).getX();
        int maxX=0;
        int maxY=0;
        int tempX;
        int tempY;
        for(Point p: s.getPoints()){
            tempX=p.getX();
            tempY=p.getY();
            if(tempX>maxX){
                maxX=tempX;
            }
            else if(tempX<minX){
                minX=tempX;
            }
            if(tempY>maxY){
                maxY=tempY;
            }
            else if(tempY<minY){
                minY=tempY;
            }

        }

        return  new Location(minX,minY,new Rectangle((maxX-minX),(maxY-minY)));
	}}

