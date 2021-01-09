package calendr.ui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	JLabel title;
	JLabel about;
	
	public AboutFrame() {
		this.setLayout(new GridLayout(2, 1));
		this.setTitle("About Calendar");
		this.setSize(400, 100);
	
		title = new JLabel("About Calendar");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		add(title);
		
		about = new JLabel("Created by Bartłomiej Jasiński");
		add(about);
	}
}
