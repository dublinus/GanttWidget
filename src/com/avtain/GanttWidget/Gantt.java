package com.avtain.GanttWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.joda.time.DateTime;

import com.avtain.GanttWidget.GanttItem.ItemType;

public class Gantt extends Composite {
	
	Composite titleComposite;
	Composite canvasComposite;
	
	ScrolledComposite titleScroll;
	ScrolledComposite canvasScroll;
	
	GanttAxis axis;
	
	Image pointImage;
	
	DateTime start, finish;
	
	int canvasWidth = 2000;
	int axisHeight = 25;
	
	Color background = new Color(null, 255, 255, 255);
	
	public Gantt(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		SashForm sash = new SashForm(this, SWT.NONE);
		
		Composite leftSash = new Composite(sash, SWT.NONE);
		Composite rightSash = new Composite(sash, SWT.NONE);
		
		final ScrolledComposite axisComposite = new ScrolledComposite(rightSash, SWT.H_SCROLL);
		axis = new GanttAxis(axisComposite, SWT.NONE);
		axis.setBounds(0, 0, canvasWidth, axisHeight);
		axisComposite.setContent(axis);
		FormData fd_axisComposite = new FormData();
		fd_axisComposite.top = new FormAttachment(0);
		fd_axisComposite.left = new FormAttachment(0);
		fd_axisComposite.right = new FormAttachment(100);
		fd_axisComposite.bottom = new FormAttachment(100, 15);
		axisComposite.setLayoutData(fd_axisComposite);
		axisComposite.getHorizontalBar().setVisible(false);
		axisComposite.setBackground(background);
		
		leftSash.setLayout(new FormLayout());
		rightSash.setLayout(new FormLayout());
		
		leftSash.setBackground(background);
		
		titleScroll = new ScrolledComposite(leftSash, SWT.NONE | SWT.V_SCROLL);
		FormData fd_titleScroll = new FormData();
		fd_titleScroll.bottom = new FormAttachment(100);
		fd_titleScroll.left = new FormAttachment(0);
		fd_titleScroll.right = new FormAttachment(100);
		fd_titleScroll.top = new FormAttachment(0, axisHeight);
		titleScroll.setLayoutData(fd_titleScroll);
		titleScroll.setExpandHorizontal(true);
		titleScroll.setBackground(background);
		
		canvasScroll = new ScrolledComposite(rightSash, SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fd_canvasScroll = new FormData();
		fd_canvasScroll.bottom = new FormAttachment(100);
		fd_canvasScroll.left = new FormAttachment(0);
		fd_canvasScroll.right = new FormAttachment(100);
		fd_canvasScroll.top = new FormAttachment(0, axisHeight);
		canvasScroll.setLayoutData(fd_canvasScroll);
		canvasScroll.setBackground(background);
		sash.setWeights(new int[]{10, 90});
		
		titleComposite = new Composite(titleScroll, SWT.NONE);
		titleComposite.setLayout(new GanttLayout(SWT.DEFAULT, 40));
		titleScroll.setContent(titleComposite);
		
		canvasComposite = new Composite(canvasScroll, SWT.NONE);
		canvasComposite.setLayout(new GanttLayout(canvasWidth, 40));
		canvasScroll.setContent(canvasComposite);
		
		axisComposite.moveBelow(canvasScroll);
		
		final ScrollBar vScroll1 = canvasScroll.getVerticalBar();
		final ScrollBar hScroll1 = canvasScroll.getHorizontalBar();
		final ScrollBar vScroll2 = titleScroll.getVerticalBar();
		
		vScroll2.setVisible(false);
		
		vScroll1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				titleScroll.setOrigin(titleScroll.getOrigin().x, canvasScroll.getOrigin().y);
			}
		});
		
		vScroll2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				canvasScroll.setOrigin(canvasScroll.getOrigin().x, titleScroll.getOrigin().y);
			}
		});
		
		hScroll1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				axisComposite.setOrigin(canvasScroll.getOrigin().x, axisComposite.getOrigin().y);
			}
		});
		
	}
	
	public void clear() {
		for(Control control : titleComposite.getChildren())
			control.dispose();
		for(Control control : canvasComposite.getChildren())
			control.dispose();
		start = null;
		finish = null;
		canvasComposite.redraw();
	}
	
	public void setDates(DateTime start, DateTime finish) {
		this.start = start;
		this.finish = finish;
		axis.setDates(start, finish);
	}
	
	public void addItem(ItemType type, String name, DateTime d1, DateTime d2) {
		if(start == null) start = d1;
		if(finish == null) finish = d2;
		if(d1.isBefore(start)) start = d1;
		if(d2.isAfter(finish)) finish = d2;
		new GanttItem(this, titleComposite, canvasComposite, type, name, start, finish, d1, d2);
		axis.setDates(start, finish);
	}
	
	public void finalize() {
		pointImage.dispose();
		background.dispose();
	}
	
	
	
}
