package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

	// TODO entirely your job (except onCircle)

	private final Canvas canvas;

	private final Paint paint;

	public Draw(final Canvas canvas, final Paint paint) {
		this.canvas = canvas;
		this.paint = paint;
		paint.setStyle(Style.STROKE);
	}

	@Override
	public Void onCircle(final Circle c) {
		canvas.drawCircle(0, 0, c.getRadius(), paint);
		return null;
	}

	@Override
	public Void onStroke(final Stroke c) {
		Style style=paint.getStyle();
		int color=paint.getColor();
		paint.setColor(c.getColor());
		paint.setStyle(Style.STROKE);
		c.getShape().accept(this);
		paint.setStyle(style);
		paint.setColor(color);
		return null;
	}

	@Override
	public Void onFill(final Fill f) {


		Style style=paint.getStyle();
		int color=paint.getColor();
		paint.setStyle(Style.FILL_AND_STROKE);
		f.getShape().accept(this);
		paint.setStyle(style);
		paint.setColor(color);
		return null;
	}

	@Override
	public Void onGroup(final Group g) {
		for (Shape shape: g.getShapes()) {
			shape.accept(this);
		}
		return null;

	}

	@Override
	public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);

        canvas.translate(-l.getX(), -l.getY());

		return null;
	}

	@Override
	public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0,0,r.getWidth(), r.getHeight(), paint);

		return null;
	}

	@Override
	public Void onOutline(Outline o) {
		Style tmp = paint.getStyle();
		int color=paint.getColor();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(tmp);
		paint.setColor(color);
		return null;
	}

	@Override
	public Void onPolygon(final Polygon s) {

		final float[] pts=new float[s.getPoints().size()*4];
		int i=0;
		boolean flag=true;
		for(Point p:s.getPoints()){
			if(flag) {
				pts[i] = p.getX();
				pts[i + 1] = p.getY();
				i+=2;
				flag=false;
			}
			else{
				pts[i] = p.getX();
				pts[i + 1] = p.getY();
				pts[i+2]=p.getX();
				pts[i+3]=p.getY();
				i+=4;
			}
		}
		pts[i]=s.getPoints().get(0).getX();
		pts[i+1]=s.getPoints().get(0).getY();
		canvas.drawLines(pts, paint);
		return null;
	}
}
