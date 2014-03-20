package com.avtain.GanttWidget;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class GanttAxis extends Canvas {
	
	Color background = new Color(null, 255, 255, 255);
	
	DateTime start, finish;

	public GanttAxis(Composite parent, int style) {
		super(parent, style);
		
		this.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setBackground(background);
				int width = GanttAxis.this.getSize().x;
				int height = GanttAxis.this.getSize().y;
				e.gc.fillRectangle(0, 0, width, height);
				
				for(int i = 0; i < width; i += 200) {
					if(i==0) continue;
					String dateString = stringFromPosition(i);
					e.gc.drawText(dateString, i - e.gc.stringExtent(dateString).x / 2, height / 2 - e.gc.getFontMetrics().getHeight() / 2);
					drawTick(e.gc, i);
				}
			}
		});
	}
	
	public void setDates(DateTime start, DateTime finish) {
		this.start = start;
		this.finish = finish;
	}
	
	private String stringFromPosition(int position) {
		if(start == null || finish == null) return "";
		//int size = this.getSize().x;
		double percent = (double)position / this.getSize().x;
		DateTime now = start.plus((long) (new Interval(start, finish).toDurationMillis() * percent));
		return now.toString("M/d/yyyy");
	}
	
	private void drawTick(GC gc, int position) {
		int tickLength = 4;
		gc.drawLine(position, 0, position, tickLength);
		gc.drawLine(position, this.getSize().y, position, this.getSize().y - tickLength);
	}
	
	public void finalize() {
		background.dispose();
	}

}
