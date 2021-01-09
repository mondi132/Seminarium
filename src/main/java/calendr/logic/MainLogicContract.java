package calendr.logic;


import java.time.LocalDate;

public interface MainLogicContract {
    void init();

    void handleDateChanged(LocalDate date);

    void handleSearchChanged(String filter);

    void handleEventSelected(int id);

    void handleEventDeleted(int id);

    void handleEventExportedToXML(int id);

    void handleEventImportedFromXML();

    void handleEventExportedToICalc(int id);

    void handleAddEventOpened();

    void handleUpdateEventOpened(int id);

    void handleSettingsOpened();

    void handleAboutOpened();
}
