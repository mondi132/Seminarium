package calendr.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

import calendr.data.CalendarEvent;
import calendr.logic.MainFrameLogic;
import calendr.logic.MainLogicContract;
import calendr.ui.custom.PlaceholderTextField;

public class MainFrame extends JFrame implements MainViewContract, ActionListener {
	private static final long serialVersionUID = 1L;

	MainLogicContract logicContract = new MainFrameLogic(this);
	ListListener itemListener = new ListListener(logicContract);

	MenuBar menuBar;
	Menu fileMenu;
	Menu navigateMenu;
	Menu helpMenu;
	MenuItem addEventMenuItem;
	MenuItem editEventMenuItem;
	MenuItem deleteEventMenuItem;
	MenuItem exportToXMLMenuItem;
	MenuItem importFromXMLMenuItem;
	MenuItem exportToICalcMenuItem;
	MenuItem settingsMenuItem;
	MenuItem nextMonthMenuItem;
	MenuItem previousMonthMenuItem;
	MenuItem aboutMenuItem;
	
	CalendarView calendarView;
	JTextField searchTextField;
	java.awt.List eventsList;
	DetailsView detailsView;

	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Calendar");
		this.setSize(500, 500);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		JPanel main = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		calendarView = new CalendarView();
		calendarView.setDateChangeListener(new DateChangeListener() {
			@Override
			public void dateChanged(LocalDate date) {
				logicContract.handleDateChanged(date);
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
	    c.gridx = 0;
	    c.gridy = 0;
	    main.add(calendarView, c);

		searchTextField = new PlaceholderTextField("Search...");
		searchTextField.addKeyListener(new SearchKeyAdapter(this.logicContract));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
	    c.gridx = 0;
	    c.gridy = 1;
	    main.add(searchTextField, c);

		eventsList = new java.awt.List();
		eventsList.addItemListener(itemListener);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
	    c.gridx = 0;
	    c.gridy = 2;
	    main.add(eventsList, c);
		
		detailsView = new DetailsView();
		detailsView.setVisible(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
	    c.gridx = 0;
	    c.gridy = 3;
	    main.add(detailsView, c);
	    
	    add(main, BorderLayout.NORTH);
		
		menuBar = new MenuBar();
		fileMenu = new Menu("Files");

		addEventMenuItem = new MenuItem("Add new event");   
		addEventMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
		addEventMenuItem.addActionListener(this);
		fileMenu.add(addEventMenuItem);
		
		editEventMenuItem = new MenuItem("Edit event");
		editEventMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_E, false));
		editEventMenuItem.addActionListener(this);
		fileMenu.add(editEventMenuItem);
		
		deleteEventMenuItem = new MenuItem("Delete event");
		deleteEventMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_DELETE, false));
		deleteEventMenuItem.addActionListener(this);
		fileMenu.add(deleteEventMenuItem);

		exportToXMLMenuItem = new MenuItem("Export to XML");
		exportToXMLMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_E, true));
		exportToXMLMenuItem.addActionListener(this);
		fileMenu.add(exportToXMLMenuItem);

		importFromXMLMenuItem = new MenuItem("Import from XML");
		importFromXMLMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_I, true));
		importFromXMLMenuItem.addActionListener(this);
		fileMenu.add(importFromXMLMenuItem);

		exportToICalcMenuItem = new MenuItem("Export to iCalc");
		exportToICalcMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_C, true));
		exportToICalcMenuItem.addActionListener(this);
		fileMenu.add(exportToICalcMenuItem);

		settingsMenuItem = new MenuItem("Settings");
		settingsMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S, true));
		settingsMenuItem.addActionListener(this);
		fileMenu.add(settingsMenuItem);
		menuBar.add(fileMenu);

		navigateMenu = new Menu("Navigate");
		nextMonthMenuItem = new MenuItem("Go to next month");
		nextMonthMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_RIGHT, false));
		nextMonthMenuItem.addActionListener(this);
		navigateMenu.add(nextMonthMenuItem);

		previousMonthMenuItem = new MenuItem("Go to previous month");
		previousMonthMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_LEFT, false));
		previousMonthMenuItem.addActionListener(this);
		navigateMenu.add(previousMonthMenuItem);
		menuBar.add(navigateMenu);
		
		helpMenu = new Menu("Help");
		
		aboutMenuItem = new MenuItem("About Calendar");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		
		setMenuBar(menuBar);

		this.logicContract.init();
	}

	@Override
	public void showEvents(List<CalendarEvent> events) {
		detailsView.setVisible(false);
		eventsList.removeAll();
		
		for (CalendarEvent event : events) {
			eventsList.add(event.getDescription());
		}
	}

	@Override
	public void showEventDetails(CalendarEvent event) {
		this.detailsView.renderEvent(event);
		this.detailsView.setVisible(true);
	}

	@Override
	public void hideEventDetails() {
		this.detailsView.setVisible(false);
	}

	@Override
	public JFrame getFrame() {
		return this;
	}

	@Override
	public void showReminderDialog(String name) {
		JOptionPane.showMessageDialog(this, "Reminder: " + name);
	}

	@Override
	public void playNotificationSound() {
		Toolkit.getDefaultToolkit().beep();
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == addEventMenuItem) {
			this.logicContract.handleAddEventOpened();
		} else if (e.getSource() == editEventMenuItem) {
			this.logicContract.handleUpdateEventOpened(eventsList.getSelectedIndex());
		} else if (e.getSource() == deleteEventMenuItem) {
			this.logicContract.handleEventDeleted(eventsList.getSelectedIndex());
		} else if (e.getSource() == exportToXMLMenuItem) {
			this.logicContract.handleEventExportedToXML(eventsList.getSelectedIndex());
		} else if (e.getSource() == importFromXMLMenuItem) {
			this.logicContract.handleEventImportedFromXML();
		} else if (e.getSource() == exportToICalcMenuItem) {
			this.logicContract.handleEventExportedToICalc(eventsList.getSelectedIndex());
		} else if (e.getSource() == settingsMenuItem) {
			this.logicContract.handleSettingsOpened();
		} else if (e.getSource() == nextMonthMenuItem) {
			this.calendarView.changeMonth(1);
		} else if (e.getSource() == previousMonthMenuItem) {
			this.calendarView.changeMonth(-1);
		} else if (e.getSource() == aboutMenuItem) {
			this.logicContract.handleAboutOpened();
		}
	}
}
