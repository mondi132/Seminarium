package calendr;

import calendr.ui.MainFrame;

import java.awt.*;

public class CalendarApp {

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);

		if (args.length == 1 && args[0].equals("minimize")) {
			mainFrame.setState(Frame.ICONIFIED);
		}
	}
}
