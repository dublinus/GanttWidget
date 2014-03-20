package com.avtain.GanttWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class GanttItem {
	public static enum ItemType {POINT, SHIFT, SPAN}
	
	Color main = new Color(null, 255, 255, 255);
	Color alt = new Color(null, 249, 249, 252);
	Color spanColor = new Color(null, 69, 95, 150);
	
	Color background;
	
	Gantt parent;
	
	//private String name;
	CLabel titleLabel;
	Canvas canvas;
	private ItemType type;
	private DateTime start;
	private DateTime finish;
	private DateTime d1;
	private DateTime d2;
	
	public GanttItem(Gantt parent, Composite titleParent, Composite canvasParent, ItemType type, String name, DateTime start, DateTime finish, DateTime d1, DateTime d2) {
		
		titleLabel = new CLabel(titleParent, SWT.NONE);
		canvas = new Canvas(canvasParent, SWT.NONE);
		
		if(titleParent.getChildren().length % 2 != 0) background = main;
		else background = alt;
		titleLabel.setBackground(background);
		
		this.parent = parent;
		this.setName(name);
		this.start = start;
		this.finish = finish;
		this.d1 = d1;
		this.d2 = d2;
		this.type = type;
		
		FormData titleFormData = new FormData();
		titleFormData.top = new FormAttachment(0);
		titleFormData.left = new FormAttachment(0);
		titleFormData.right = new FormAttachment(0, 100);
		titleFormData.bottom = new FormAttachment(100);
		titleLabel.setLayoutData(titleFormData);
		
		FormData canvasFormData = new FormData();
		canvasFormData.top = new FormAttachment(0);
		canvasFormData.left = new FormAttachment(titleLabel, 0);
		canvasFormData.right = new FormAttachment(100);
		canvasFormData.bottom = new FormAttachment(100);
		canvas.setLayoutData(canvasFormData);
		
		titleParent.setSize(titleParent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		canvasParent.setSize(canvasParent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		setListeners();
	}
	
	public void setListeners() {
		switch(this.type) {
		case POINT:
			canvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if(background!= null)e.gc.setBackground(background);
					e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
					
					double currentSeconds =new Interval(start, d1).toDuration().getStandardSeconds();
					double allSeconds =new Interval(start, finish).toDuration().getStandardSeconds(); 
					double percent = currentSeconds / allSeconds;
					
					drawPoint(e, percent);
				}
			});
			break;
		case SHIFT:
			break;
		case SPAN:
			canvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if(background!= null)e.gc.setBackground(background);
					e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
					
					double d1Seconds = new Interval(start, d1).toDuration().getStandardSeconds();
					double d2Seconds = new Interval(start, d2).toDuration().getStandardSeconds();
					double allSeconds = new Interval(start, finish).toDuration().getStandardSeconds(); 
					
					double d1Percent = d1Seconds / allSeconds;
					double d2Percent = d2Seconds / allSeconds;
					
					drawSpan(e, d1Percent, d2Percent);
				}
			});
			
		}
	}
	
	private void drawSpan(PaintEvent e, double percentStart, double percentFinish) {
		
		int barHeight = 20;
		int pad = (canvas.getSize().y - barHeight) / 2;
		
		GC gc = e.gc;
		int start = (int) (canvas.getSize().x * percentStart);
		int finish = (int) (canvas.getSize().x * percentFinish);
		
		gc.setBackground(spanColor);
		
		gc.fillRectangle(start, pad, finish - start, barHeight);
	}
	
	private void drawPoint(PaintEvent e, double percent) {
		if(percent < 0 || percent > 1) return;
		GC gc = e.gc;
		int position = (int) (canvas.getSize().x * percent);
		//gc.drawLine(position, 0, position, canvas.getSize().y);
		//Image point = parent.pointImage;
		//gc.drawImage(point, 0, 0, point.getBounds().x, point.getBounds().y, position, 0, position + 10, canvas.getSize().y);
		gc.drawRectangle(position - 10, 0, 20, canvas.getSize().y - 1);
	}
	
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public DateTime getStart() {
		return start;
	}
	public void setStart(DateTime start) {
		this.start = start;
	}
	public DateTime getFinish() {
		return finish;
	}
	public void setFinish(DateTime finish) {
		this.finish = finish;
	}
	public String getName() {
		return titleLabel.getText();
	}
	public void setName(String name) {
		titleLabel.setText(name);
	}
	
	public Control getLabel() {
		return this.titleLabel;
	}
	
	public Control getCanvas() {
		return this.canvas;
	}
	
	@Override
	public void finalize() {
		alt.dispose();
		main.dispose();
		spanColor.dispose();
	}
	
}
