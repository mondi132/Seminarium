package calendr.data;

import calendr.logic.SettingsLogicContract;

public class MemoryDatabase {
    private static final MemoryDatabase instance = new MemoryDatabase();

    SettingsLogicContract.SoundSetting soundSetting = SettingsLogicContract.SoundSetting.ON;

    private MemoryDatabase() {}

    public static MemoryDatabase getInstance() {
        return instance;
    }

    public SettingsLogicContract.SoundSetting getSoundSetting() {
        return soundSetting;
    }

    public void setSoundSetting(SettingsLogicContract.SoundSetting soundSetting) {
        this.soundSetting = soundSetting;
    }

}
