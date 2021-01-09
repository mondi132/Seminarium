package calendr.ui;

import calendr.logic.MainLogicContract;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ListListener implements ItemListener {
    MainLogicContract logicContract;

    public ListListener(MainLogicContract logicContract) {
        this.logicContract = logicContract;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.logicContract.handleEventSelected((int) e.getItem());
    }
}
