package calendr.data;

import calendr.exceptions.InvalidCalendarEventException;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SQLiteJDBCDriverConnection {

    private static final SQLiteJDBCDriverConnection instance = new SQLiteJDBCDriverConnection();

    private static Connection connection = null;

    private SQLiteJDBCDriverConnection() {
    }


    public static SQLiteJDBCDriverConnection getInstance() {
        return instance;
    }

    public void connect() {
        final String databaseName = "database";
        final String url = "jdbc:sqlite::resource:".concat(databaseName);

        try {
            // db parameters
            // create a connection to the database
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            System.out.println("Connection closed");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(CalendarEvent event) {
        connect();
        String sql = "INSERT INTO events(date_of_the_event, event_description, place_of_event, notification_reminder_time) VALUES(?,?,?,?)";

        try (Connection conn = connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(event.getDate()));
            preparedStatement.setString(2, event.getDescription());
            preparedStatement.setString(3, event.getLocation());
            preparedStatement.setString(4, Integer.toString(event.getReminderTime()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public List<CalendarEvent> getAllEvents() {
        connect();
        String sql = "SELECT id, date_of_the_event, event_description, place_of_event, notification_reminder_time FROM events";

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        try (Connection connection = SQLiteJDBCDriverConnection.connection;
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                try {
                    CalendarEvent calendarEvent = new CalendarEvent(LocalDateTime.parse(resultSet.getString("date_of_the_event")),
                            resultSet.getString("event_description"), resultSet.getString("place_of_event"), Integer.parseInt(resultSet.getString("notification_reminder_time")));
                    calendarEvent.setId(resultSet.getInt("id"));
                    calendarEvents.add(calendarEvent);
                } catch (InvalidCalendarEventException e) {
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return calendarEvents;
    }

    public void deleteOlderThan(LocalDateTime date) {
        List<CalendarEvent> calendarEvents = getAllEvents();

        String sql = "DELETE FROM events WHERE id = ?";
        connect();

        try (Connection conn = connection; PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            calendarEvents.forEach(e -> {
                if (e.getDate().isBefore(date)) {
                    try {
                        preparedStatement.setInt(1, e.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM events WHERE id = ?";

        connect();

        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
    }

    public List<CalendarEvent> getEventsOnDate(LocalDateTime date) {
        List<CalendarEvent> matchedEvents = new ArrayList<>();
        String sql = "SELECT * FROM events where date_of_the_event LIKE ?";
        String parsedDate = "%" + date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")) + "%";
        connect();

        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parsedDate);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                try {
                    CalendarEvent calendarEvent = new CalendarEvent(LocalDateTime.parse(resultSet.getString("date_of_the_event")),
                            resultSet.getString("event_description"), resultSet.getString("place_of_event"), Integer.parseInt(resultSet.getString("notification_reminder_time")));
                    calendarEvent.setId(resultSet.getInt("id"));
                    matchedEvents.add(calendarEvent);
                } catch (InvalidCalendarEventException e) {
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return matchedEvents;
    }

    public CalendarEvent getEventById(Integer id) {
        String sql = "SELECT * FROM events where id = ?";
        CalendarEvent calendarEvent = null;
        connect();

        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                try {
                    calendarEvent = new CalendarEvent(LocalDateTime.parse(resultSet.getString("date_of_the_event")),
                            resultSet.getString("event_description"), resultSet.getString("place_of_event"));
                    calendarEvent.setId(resultSet.getInt("id"));
                } catch (InvalidCalendarEventException e) {
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();

        return calendarEvent;
    }

    public void updateEventById(CalendarEvent calendarEvent) {
        connect();
        String sql = "UPDATE events SET date_of_the_event = ? , event_description = ? , place_of_event = ? , notification_reminder_time = ? " +
                "WHERE id = ?";

        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, String.valueOf(calendarEvent.getDate()));
            pstmt.setString(2, calendarEvent.getDescription());
            pstmt.setString(3, calendarEvent.getLocation());
            pstmt.setString(4, Integer.toString(calendarEvent.getReminderTime()));
            pstmt.setString(5, String.valueOf(calendarEvent.getId()));

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
    }
}