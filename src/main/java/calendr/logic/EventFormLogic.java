package calendr.logic;

import calendr.data.CalendarEvent;
import calendr.exceptions.InvalidCalendarEventException;
import calendr.data.SQLiteJDBCDriverConnection;

import java.time.LocalDateTime;

public class EventFormLogic implements EventFormLogicContract{
    @Override
    public void handleAddEventFormSubmitted(String description, String location, LocalDateTime date, int reminderTime) throws InvalidCalendarEventException  {
        CalendarEvent event = new CalendarEvent(date, description, location, reminderTime);
        SQLiteJDBCDriverConnection.getInstance().addEvent(event);
    }

    @Override
    public void handleUpdateEventFormSubmitted(Integer id, String description, String location, LocalDateTime date, int reminderTime) throws InvalidCalendarEventException {
        CalendarEvent event = new CalendarEvent(date, description, location, reminderTime);
        event.setId(id);
        SQLiteJDBCDriverConnection.getInstance().updateEventById(event);
    }
}
