package calendr.ui;

import calendr.logic.MainLogicContract;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchKeyAdapter extends KeyAdapter {
    MainLogicContract logicContract;

    public SearchKeyAdapter(MainLogicContract logicContract) {
        this.logicContract = logicContract;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);

        JTextField textField = (JTextField) e.getSource();

        this.logicContract.handleSearchChanged(textField.getText());
    }
}
