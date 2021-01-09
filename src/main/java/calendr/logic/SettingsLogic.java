package calendr.logic;

import calendr.data.MemoryDatabase;
import calendr.ui.SettingsViewContract;
import calendr.data.SQLiteJDBCDriverConnection;

import java.time.LocalDateTime;

public class SettingsLogic implements SettingsLogicContract {
    SettingsViewContract viewContract;

    public SettingsLogic(SettingsViewContract viewContract) {
        this.viewContract = viewContract;
    }

    @Override
    public void init() {
        MemoryDatabase database = MemoryDatabase.getInstance();

        this.viewContract.setReminderSetting(database.getSoundSetting());
    }

    @Override
    public void handleDeleteOlderThan(LocalDateTime date) {
        SQLiteJDBCDriverConnection.getInstance().deleteOlderThan(date);
    }

    @Override
    public void handleSave(SoundSetting soundSetting) {
        MemoryDatabase database = MemoryDatabase.getInstance();

        database.setSoundSetting(soundSetting);
    }
}
