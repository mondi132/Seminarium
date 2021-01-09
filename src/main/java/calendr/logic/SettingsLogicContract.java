package calendr.logic;

import java.time.LocalDateTime;

public interface SettingsLogicContract {
    enum SoundSetting {
        ON, OFF
    }

    void init();

    void handleDeleteOlderThan(LocalDateTime date);

    void handleSave(SoundSetting soundSetting);
}
