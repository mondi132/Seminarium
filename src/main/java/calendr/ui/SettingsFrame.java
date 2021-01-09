package calendr.ui;

import calendr.logic.SettingsLogic;
import calendr.logic.SettingsLogicContract;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SettingsFrame extends JFrame implements SettingsViewContract {
    SettingsFrame self = this;
    SettingsLogicContract logicContract = new SettingsLogic(this);

    JLabel deleteOldEventsLabel;
    JSpinner deleteOldEventsSpinner;
    JButton deleteOldEventsButton;

    JLabel reminderLabel;
    CheckboxGroup reminderCheckboxGroup;
    Checkbox reminderSoundOn;
    Checkbox reminderSoundOff;

    JButton saveButton;

    public SettingsFrame() {
        ActionListener deleteEventsListener = new DeleteEventsListener();

        ActionListener saveActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsLogicContract.SoundSetting setting;

                if (reminderCheckboxGroup.getSelectedCheckbox() == reminderSoundOn) {
                    setting = SettingsLogicContract.SoundSetting.ON;
                } else {
                    setting = SettingsLogicContract.SoundSetting.OFF;
                }

                logicContract.handleSave(setting);
                dispatchEvent(new WindowEvent(self, WindowEvent.WINDOW_CLOSING));
            }
        };

        this.setTitle("Settings");
        this.setSize(400, 260);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        deleteOldEventsLabel = new JLabel("Delete events older than");
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(deleteOldEventsLabel, c);

        deleteOldEventsSpinner = new JSpinner(new SpinnerDateModel());
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        add(deleteOldEventsSpinner, c);

        deleteOldEventsButton = new JButton("Delete");
        deleteOldEventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime dateTime = ((Date) deleteOldEventsSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                logicContract.handleDeleteOlderThan(dateTime);
                dispatchEvent(new WindowEvent(self, WindowEvent.WINDOW_CLOSING));
            }
        });
        c.insets = new Insets(0, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        add(deleteOldEventsButton, c);

        reminderLabel = new JLabel("Play sound on reminders");
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        add(reminderLabel, c);

        reminderCheckboxGroup = new CheckboxGroup();

        reminderSoundOn = new Checkbox("On", true, reminderCheckboxGroup);
        c.insets = new Insets(0, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        add(reminderSoundOn, c);

        reminderSoundOff = new Checkbox("Off", false, reminderCheckboxGroup);
        c.insets = new Insets(0, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        add(reminderSoundOff, c);

        saveButton = new JButton("Save");
        saveButton.addActionListener(saveActionListener);
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 5;
        add(saveButton, c);

        logicContract.init();
    }

    @Override
    public void setReminderSetting(SettingsLogicContract.SoundSetting soundSetting) {
        if (soundSetting == SettingsLogicContract.SoundSetting.ON) {
            reminderCheckboxGroup.setSelectedCheckbox(reminderSoundOn);
        } else {
            reminderCheckboxGroup.setSelectedCheckbox(reminderSoundOff);
        }
    }

    class DeleteEventsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalDateTime dateTime = ((Date) deleteOldEventsSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            logicContract.handleDeleteOlderThan(dateTime);
            dispatchEvent(new WindowEvent(self, WindowEvent.WINDOW_CLOSING));
        }
    }
}
