package calendr.logic;

import calendr.exceptions.InvalidCalendarEventException;

import java.time.LocalDateTime;

public interface EventFormLogicContract {
    void handleAddEventFormSubmitted(String description, String location, LocalDateTime date, int reminderTime) throws InvalidCalendarEventException;

    void handleUpdateEventFormSubmitted(Integer id, String description, String location, LocalDateTime date, int reminderTime) throws InvalidCalendarEventException;
}
