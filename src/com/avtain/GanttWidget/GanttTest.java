package com.avtain.GanttWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.joda.time.DateTime;

import com.avtain.GanttWidget.GanttItem.ItemType;

public class GanttTest {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GanttTest window = new GanttTest();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display.setAppName("Gantt Test");
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1500, 600);
		shell.setText("Gantt Test");
		shell.setLayout(new FillLayout());
		
		Gantt gantt = new Gantt(shell, SWT.NONE);
		
		DateTime start = new DateTime().minusMonths(1), finish = new DateTime().plusMonths(1);
		
		gantt.setDates(start, finish);
		int i = 1;
		for(DateTime it = new DateTime(start); it.isBefore(finish); it = it.plusDays(1), i++) {
			gantt.addItem(ItemType.SPAN, "Day " + i, it, it.plusDays(3));
		}
	}

}
