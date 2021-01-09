package calendr.data;

import calendr.exceptions.InvalidCalendarEventException;
import calendr.util.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CalendarEvent {
	Integer id;
	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	LocalDateTime date;
	String description;
	String location;
	int reminderTime;

	public CalendarEvent(LocalDateTime date, String description, String location) throws InvalidCalendarEventException {
		this(date, description, location, 0);
	}

	public CalendarEvent(LocalDateTime date, String description, String location, int reminderTime) throws InvalidCalendarEventException {
		if (description.equals("")) throw new InvalidCalendarEventException();

		this.date = date;
		this.description = description;
		this.location = location;
		this.reminderTime = reminderTime;
	}

	public CalendarEvent(){}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(int reminderTime) {
		this.reminderTime = reminderTime;
	}
}
