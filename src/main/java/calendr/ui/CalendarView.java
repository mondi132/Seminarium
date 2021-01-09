package calendr.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class CalendarView extends Container implements MouseListener, KeyListener {
    private static final long serialVersionUID = 1L;

    JTable table;
    DefaultTableModel model;
    Calendar cal = new GregorianCalendar();
    JLabel label;

    DateChangeListener dateChangeListener;

    CalendarView() {
        this.setLayout(new GridBagLayout());
        this.setVisible(true);

        GridBagConstraints c = new GridBagConstraints();

        JButton b1 = new JButton("◄");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                changeMonth(-1);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        add(b1, c);

        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.8;
        c.gridx = 1;
        c.gridy = 0;
        add(label, c);

        JButton b2 = new JButton("►");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                changeMonth(1);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 0;
        add(b2, c);

        String[] columns = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        model = new DefaultTableModel(null, columns);
        model.setRowCount(6);
        table = new JTable(model);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(this);
        table.addKeyListener(this);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        add(table.getTableHeader(), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        add(table, c);

        this.updateMonth();
    }

    public void changeMonth(int diff) {
        cal.add(Calendar.MONTH, diff);
        this.updateMonth();
    }

    void setDateChangeListener(DateChangeListener dateChangeListener) {
        this.dateChangeListener = dateChangeListener;
    }

    void updateMonth() {
        boolean setDate = cal.get(Calendar.MONTH) == LocalDate.now().getMonthValue() - 1;

        cal.set(Calendar.DAY_OF_MONTH, 1);

        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = cal.get(Calendar.YEAR);
        label.setText(month + " " + year);

        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = (int) Math.ceil((numberOfDays + startDay - 1) / 7.0);

        model.setRowCount(0);
        model.setRowCount(weeks);

        int i = startDay - 2;
        for (int day = 1; day <= numberOfDays; day++) {
            if (LocalDate.now().getDayOfMonth() == day && setDate) table.changeSelection(i / 7, i % 7, false, false);
            model.setValueAt(day, i / 7, i % 7);
            i = i + 1;
        }

    }

    void handleDateChange() {
        if (this.dateChangeListener == null) return;

        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();

        if (row == -1 || column == -1) return;

        Object value = model.getValueAt(row, column);

        if (value == null) return;

        int day = (int) value;

        LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, day);

        this.dateChangeListener.dateChanged(date);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        handleDateChange();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleDateChange();
    }
}
