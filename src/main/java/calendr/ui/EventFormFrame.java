package calendr.ui;

import calendr.data.CalendarEvent;
import calendr.exceptions.InvalidCalendarEventException;
import calendr.logic.EventFormLogic;
import calendr.logic.EventFormLogicContract;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class EventFormFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    EventFormLogicContract logicContract = new EventFormLogic();

    boolean editing = false;
    int id = -1;

    JLabel descriptionLabel;
    JTextField descriptionTextField;

    JLabel locationLabel;
    JTextField locationTextField;

    JLabel dateLabel;
    JSpinner dateSpinner;

    JLabel reminderTimeLabel;
    JFormattedTextField reminderTimeTextField;

    JButton submitButton;

    public EventFormFrame() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        this.setLayout(layout);
        this.setTitle("Add event");
        this.setSize(300, 300);
        c.insets = new Insets(24, 12, 12, 12);

        descriptionLabel = new JLabel("Description");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(descriptionLabel, c);


        descriptionTextField = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        add(descriptionTextField, c);

        c.insets = new Insets(12, 12, 12, 12);

        locationLabel = new JLabel("Location");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        add(locationLabel, c);

        locationTextField = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        add(locationTextField, c);

        dateLabel = new JLabel("Date");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        add(dateLabel, c);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 2;
        add(dateSpinner, c);

        reminderTimeLabel = new JLabel("Reminder time");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        add(reminderTimeLabel, c);

        reminderTimeTextField = new JFormattedTextField(new NumberFormatter(NumberFormat.getNumberInstance()));
        reminderTimeTextField.setText("0");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 3;
        add(reminderTimeTextField, c);

        submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        add(submitButton, c);
    }

    public void startEdit(CalendarEvent event) {
        this.editing = true;
        this.id = event.getId();

        this.descriptionTextField.setText(event.getDescription());
        this.locationTextField.setText(event.getLocation());

        Date date = Date.from(event.getDate().atZone(ZoneId.systemDefault()).toInstant());

        this.dateSpinner.setValue(date);
        this.reminderTimeTextField.setValue(event.getReminderTime());
    }

    void submitForm() {
        LocalDateTime dateTime = ((Date) dateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        try {
            if (this.editing) {
                if (reminderTimeTextField.getText().equals("")) throw new InvalidCalendarEventException();

                logicContract.handleUpdateEventFormSubmitted(id, descriptionTextField.getText(), locationTextField.getText(), dateTime, Integer.parseInt(reminderTimeTextField.getText()));
            } else {
                if (reminderTimeTextField.getText().equals("")) throw new InvalidCalendarEventException();

                logicContract.handleAddEventFormSubmitted(descriptionTextField.getText(), locationTextField.getText(), dateTime, Integer.parseInt(reminderTimeTextField.getText()));
            }
        } catch (InvalidCalendarEventException e) {
            JOptionPane.showMessageDialog(this, "Calendar event is invalid.");
        }

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
