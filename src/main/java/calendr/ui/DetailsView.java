package calendr.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;

import calendr.data.CalendarEvent;

public class DetailsView extends JPanel {
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
	
	JLabel descriptionTitle;
	JLabel descriptionValue;
	
	JLabel dateTitle;
	JLabel dateValue;
	
	JLabel locationTitle;
	JLabel locationValue;
	
	public DetailsView() {
		GridLayout layout = new GridLayout(6, 1);
		layout.setVgap(4);
		this.setLayout(layout);
		
		descriptionTitle = new JLabel("Description");
		add(descriptionTitle);
		
		descriptionValue = new JLabel();
		descriptionValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		add(descriptionValue);
		
		dateTitle = new JLabel("Date");
		add(dateTitle);
		
		dateValue = new JLabel();
		dateValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		add(dateValue);
		
		locationTitle = new JLabel("Location");
		add(locationTitle);
		
		locationValue = new JLabel();
		locationValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		add(locationValue);
	}
	
	public void renderEvent(CalendarEvent event) {
		descriptionValue.setText(event.getDescription());
		
		dateValue.setText(event.getDate().format(dateTimeFormatter));
		
		locationValue.setText(event.getLocation());
	}
}
