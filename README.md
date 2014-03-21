This is a simple SWT widget to show durations. You can initialize the widget like this:

    Gantt gantt = new Gantt(shell, SWT.NONE);

Then you can add durations like this:

    gantt.addItem(ItemType.SPAN, "A duration", new DateTime().minusDays(1), new DateTime.plusDays(3));

This widget uses the Joda time library.

This repo has an Eclipse project, and it includes a main class, GanttTest.java. This class is only there for the example code, and can be deleted.

![Gantt Screenshot](https://raw.github.com/dublinus/GanttWidget/master/GanttWidgetSWTScreenshot.png)
