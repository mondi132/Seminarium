package calendr.logic;

import calendr.data.CalendarEvent;
import calendr.data.MemoryDatabase;
import calendr.ui.AboutFrame;
import calendr.ui.EventFormFrame;
import calendr.ui.MainViewContract;
import calendr.ui.SettingsFrame;
import calendr.util.FileUtils;
import calendr.util.ICalendarFormatConverter;
import calendr.data.SQLiteJDBCDriverConnection;
import calendr.util.XMLConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrameLogic implements MainLogicContract {
    MainViewContract viewContract;

    LocalDateTime currentDate = LocalDateTime.now();
    String filter = "";
    List<Integer> blacklistedIds = new ArrayList<>();
    List<CalendarEvent> events;
    EventFormFrameWindowListener formFrameWindowListener = new EventFormFrameWindowListener();

    public MainFrameLogic(MainViewContract viewContract) {
        this.viewContract = viewContract;
    }

    @Override
    public void init() {
        this.refresh();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RemindersTask(), 0, 5 * 1000);
    }

    void refresh() {
        this.events = SQLiteJDBCDriverConnection.getInstance().getEventsOnDate(this.currentDate);
        this.renderList();
    }

    List<CalendarEvent> getFilteredEvents() {
        List<CalendarEvent> filteredEvents = new ArrayList<>();

        for (calendr.data.CalendarEvent event : this.events) {
            if (event.getDescription().contains(this.filter)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    void renderList() {
        List<CalendarEvent> filteredEvents = this.getFilteredEvents();

        viewContract.showEvents(filteredEvents);
    }

    CalendarEvent getEventFromId(int id) {
        return this.getFilteredEvents().get(id);
    }

    @Override
    public void handleDateChanged(LocalDate date) {
        this.currentDate = date.atStartOfDay();

        this.refresh();

        this.viewContract.hideEventDetails();
    }

    @Override
    public void handleSearchChanged(String filter) {
        this.filter = filter;
        this.renderList();
    }

    @Override
    public void handleEventSelected(int id) {
        this.viewContract.showEventDetails(this.getEventFromId(id));
    }

    @Override
    public void handleEventDeleted(int id) {
        if (id == -1) return;

        int realId = getEventFromId(id).getId();

        SQLiteJDBCDriverConnection.getInstance().deleteById(realId);
        this.refresh();
    }

    @Override
    public void handleEventExportedToXML(int id) {
        if (id == -1) return;

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showSaveDialog(viewContract.getFrame()) == JFileChooser.APPROVE_OPTION) {
            XMLConverter xmlConverter = new XMLConverter();

            String xml = xmlConverter.serializeToXML(this.getEventFromId(id));

            FileUtils.writeToFile(fileChooser.getSelectedFile(), xml);
        }
    }

    @Override
    public void handleEventImportedFromXML() {
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(viewContract.getFrame()) == JFileChooser.APPROVE_OPTION) {
            XMLConverter xmlConverter = new XMLConverter();

            CalendarEvent event = xmlConverter.parseToObject(fileChooser.getSelectedFile().getPath());

            SQLiteJDBCDriverConnection.getInstance().addEvent(event);

            this.refresh();
        }
    }

    @Override
    public void handleEventExportedToICalc(int id) {
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showSaveDialog(viewContract.getFrame()) == JFileChooser.APPROVE_OPTION) {
            ICalendarFormatConverter iCalendarFormatConverter = new ICalendarFormatConverter();

            String iCalc = iCalendarFormatConverter.exportToICal(this.getEventFromId(id));

            FileUtils.writeToFile(fileChooser.getSelectedFile(), iCalc);
        }
    }

    @Override
    public void handleAddEventOpened() {
        EventFormFrame eventFormFrame = new EventFormFrame();
        eventFormFrame.setVisible(true);
        eventFormFrame.addWindowListener(formFrameWindowListener);
    }

    @Override
    public void handleUpdateEventOpened(int id) {
        if (id == -1) return;

        List<CalendarEvent> filteredEvents = this.getFilteredEvents();

        EventFormFrame eventFormFrame = new EventFormFrame();
        eventFormFrame.setVisible(true);
        eventFormFrame.addWindowListener(formFrameWindowListener);
        eventFormFrame.startEdit(filteredEvents.get(id));
    }

    @Override
    public void handleSettingsOpened() {
        SettingsFrame settingsFrame = new SettingsFrame();
        settingsFrame.setVisible(true);
        settingsFrame.addWindowListener(formFrameWindowListener);
    }

    @Override
    public void handleAboutOpened() {
        AboutFrame aboutFrame = new AboutFrame();
        aboutFrame.setVisible(true);
    }

    class EventFormFrameWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            refresh();
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }

    class RemindersTask extends TimerTask {
        @Override
        public void run() {
            LocalDateTime now = LocalDateTime.now();
            List<CalendarEvent> events = SQLiteJDBCDriverConnection.getInstance().getEventsOnDate(currentDate);

            for (CalendarEvent event : events) {
                LocalDateTime eventDate = event.getDate();
                int reminderTime = event.getReminderTime();

                if (reminderTime == 0 || blacklistedIds.contains(event.getId())) continue;

                LocalDateTime reminderDate = eventDate.minusMinutes(reminderTime);

                if (now.isBefore(eventDate) && now.isAfter(reminderDate)) {
                    if (MemoryDatabase.getInstance().getSoundSetting() == SettingsLogicContract.SoundSetting.ON) {
                        viewContract.playNotificationSound();
                    }
                    viewContract.showReminderDialog(event.getDescription());

                    blacklistedIds.add(event.getId());
                }
            }
        }
    }
}
