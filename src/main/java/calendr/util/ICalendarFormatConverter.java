package calendr.util;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Summary;
import calendr.data.CalendarEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ICalendarFormatConverter {
    public ICalendarFormatConverter(){};

    public String exportToICal(CalendarEvent calendarEvent) {
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();
        Summary summary = event.setSummary(calendarEvent.getDescription());

        Date in = new Date();
        LocalDateTime ldt = calendarEvent.getDate().ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date eventTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        event.setDateStart(eventTime);
        event.setLocation(calendarEvent.getLocation());

        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }
}
