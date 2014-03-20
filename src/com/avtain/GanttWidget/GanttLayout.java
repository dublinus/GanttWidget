package com.avtain.GanttWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class GanttLayout extends Layout {
	
	int defaultWidth = 200;
	int width;
	int itemHeight;
	
	public GanttLayout(int x, int y) {
		width = x;
		itemHeight = y;
		
	}
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean changed) {
	     int height = itemHeight * composite.getChildren().length;
	     Point size = new Point(width == SWT.DEFAULT? defaultWidth: width, height + 2);
	     return size;
	}

	@Override
	protected void layout(Composite composite, boolean changed) {
		int i = 0;
		for(Control control : composite.getChildren()) {
			control.setBounds(0, i * itemHeight, width == SWT.DEFAULT? composite.getSize().x: width, itemHeight);
			i++;
		}
	}

}
