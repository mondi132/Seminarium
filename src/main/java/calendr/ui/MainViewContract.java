package calendr.ui;

import calendr.data.CalendarEvent;

import javax.swing.*;
import java.util.List;

public interface MainViewContract {
    void showEvents(List<CalendarEvent> events);

    void showEventDetails(CalendarEvent event);

    void hideEventDetails();

    void showReminderDialog(String name);

    void playNotificationSound();

    JFrame getFrame();
}
