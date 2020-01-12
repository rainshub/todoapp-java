/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolist.customframe;

import java.awt.AWTEventMulticaster;
import java.awt.AWTException;
import todolist.about.AboutClass;
import todolist.icon.MyIconPack;
import todolist.listdata.Data;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TablePositionBase;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.StyleConstants;
import static jdk.nashorn.internal.parser.TokenType.EOF;

/**
 * *
 *
 * This class handles every set up and functionality.
 *
 * @author Moshiur
 */
public final class MyFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JPanel buttonpanel;

    private JLabel timeLabel;

    private final JButton addbutton;
    private final JButton deletebutton;
    private final JButton deleteallbutton;
    private final JButton savebutton;
    private final JButton exitbutton;
    private final JButton aboutbutton;

    private String dateout;
    private JTextArea textField;
    JTextField inputtexfield;

    private JTable mytable;
    DefaultTableModel mymodel;
    Object[][] ob;
    String[] colnames;
    Vector<Data> myvec = new Vector<>();
    private int prev = -1;

    /**
     * *
     * every set up is made in this constructor.
     */
    public MyFrame() {

        //panel 1 details
        super("ToDoList  1.7");
        Image icon = new ImageIcon(getClass().getResource("appicon.png")).getImage();
        setIconImage(icon);
        // window title
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //   UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

//        addWindowListener(new WindowAdapter() {       // savaing list for window defalut closing button
//            @Override
//            public void windowClosing(WindowEvent e) {
//
//                int indx = mytable.getSelectedRow();
//                if (indx != -1) {
//                    data temp = myvec.elementAt(indx);
//                    temp.setDetails(textField.getText());
//                }
//                saveList();
//                myvec.clear();
//                System.exit(0);
//            }
//
//        });
        addWindowListener(new WindowAdapter() {             // minimize to task bar
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int indx = mytable.getSelectedRow();
                if (indx != -1) {
                    Data temp = myvec.elementAt(indx);
                    temp.setDetails(textField.getText());
                }
                saveList();
                setExtendedState(JFrame.ICONIFIED);
            }
        });

        // my frame lay out
        setLayout(new BorderLayout());
        //setting bottom panel

        timeLabel = new JLabel("Md. Moshiur Rahman Siyam");
        JLabel info = new JLabel(" ToDoList-2015");
        info.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        timeLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(timeLabel, BorderLayout.WEST);
        panel2.add(info, BorderLayout.EAST);

        textField = new JTextArea("Select any task from the list & add details here.\nHappy ToDoListing...");
        textField.setCaretColor(Color.CYAN);
        textField.setRows(4);
        textField.setBackground(new Color(0, 0, 0));
        textField.setForeground(new Color(254, 253, 2));
        textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));

        JScrollPane myscroll2 = new JScrollPane(textField);
        JLabel detailslabel = new JLabel("Task Details :  ");
        detailslabel.setFont(new Font("Consolas", Font.BOLD, 13));

        //  myscroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        myscroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //initiating table panel
        //creating table
        colnames = new String[]{"STATUS", "TASK  LIST", "ADDED ON"};
        mytable = new JTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {

                // determines the types of the columns.
                if (column == 0) {
                    return Boolean.class;
                } else {
                    return String.class;
                }

            }
        };

        mymodel = new DefaultTableModel(colnames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int col) {
                // setting col 1 & col 2 editable, could be also set be by returning false  
                return col != 2;
            }
        };

        mytable.setModel(mymodel);
        mytable.setBackground(new Color(0, 0, 38));
        mytable.setForeground(new Color(255, 255, 255));
        mytable.setFont(new Font("Consolas", Font.BOLD, 14));
        mytable.setFillsViewportHeight(true);
        mytable.setRowHeight(21);
        mytable.setIntercellSpacing(new Dimension(2, 2));
        mytable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        mytable.setGridColor(Color.gray);
        mytable.setShowGrid(true);
        mytable.setSelectionBackground(new Color(128, 0, 70));
        ((DefaultTableCellRenderer) mytable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        mytable.getColumnModel().getColumn(0).setMinWidth(65);
        mytable.getColumnModel().getColumn(0).setMaxWidth(70);
        mytable.getColumnModel().getColumn(1).setMinWidth(420);
        mytable.getColumnModel().getColumn(2).setMinWidth(300);
        mytable.getColumnModel().getColumn(0).setResizable(false);
        mytable.getTableHeader().setReorderingAllowed(false);

        mytable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ((boolean) mymodel.getValueAt(row, 0)) {
                    c.setBackground(new Color(75, 84, 89));
                } else if (isSelected) {
                    c.setBackground(mytable.getSelectionBackground());
                } else {
                    c.setBackground(mytable.getBackground());
                }
                return c;
            }
        });
        mytable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {

                try {
                    if (prev != -1) {
                        Data temp = myvec.elementAt(prev);
                        temp.setDetails(textField.getText());
                    }
                    Data temp = myvec.get(mytable.getSelectedRow());
                    textField.setText(temp.getDetails());
                    prev = mytable.getSelectedRow();

                } catch (Exception e) {

                }

            }
        }
        );
        inputtexfield = new JTextField("Add new Task..");
        inputtexfield.setForeground(Color.WHITE.brighter());
        inputtexfield.setBackground(Color.BLACK.brighter());
        inputtexfield.setCaretColor(Color.CYAN.brighter());
        inputtexfield.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        inputtexfield.setFocusable(false);
        inputtexfield.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {

            }

            @Override
            public void mousePressed(MouseEvent me) {
                inputtexfield.setFocusable(true);
            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                inputtexfield.setFocusable(true);
            }

            @Override
            public void mouseExited(MouseEvent me) {

            }
        });
        inputtexfield.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent fe) {
                inputtexfield.setText("Add new Task..");
            }

            @Override
            public void focusGained(FocusEvent fe) {

                if (inputtexfield.getText().equals("Add new Task..")) {
                    inputtexfield.setText("");
                }
            }
        });
        inputtexfield.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String s1 = ae.getActionCommand();
                s1 = s1.trim();
                inputtexfield.setText("");
                if (!s1.isEmpty()) {
                    String s3 = "";
                    Data temp = new Data(s1, new Date(), s3);
                    myvec.add(temp);
                    mymodel.addRow(temp.getData());
                }
            }
        });
        JScrollPane myscroll = new JScrollPane(mytable);
        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(myscroll, BorderLayout.CENTER);
        mainpanel.add(inputtexfield, BorderLayout.NORTH);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(detailslabel, BorderLayout.NORTH);
        panel1.add(myscroll2, BorderLayout.CENTER);
        panel1.add(panel2, BorderLayout.SOUTH);
        mainpanel.add(panel1, BorderLayout.SOUTH);
        add(mainpanel);
        // calls the method to load the tasks into table while starting the application
        loadList();

        //creating button panel
        //
        buttonpanel = new JPanel();
        buttonpanel.setLayout(new GridLayout(5, 1, 0, 0));
        //creating buttons
        MyIconPack myicon = new MyIconPack();
        addbutton = new JButton("", myicon.addicon);
        addbutton.setToolTipText("Add New Task");
        deletebutton = new JButton("", myicon.deleteicon);
        deletebutton.setToolTipText("Delete Selected Task");
        deleteallbutton = new JButton("", myicon.deleteallicon);
        deleteallbutton.setToolTipText("Delete ALL Task");
        savebutton = new JButton("", myicon.saveicon);
        savebutton.setToolTipText("Save Current List");
        exitbutton = new JButton("", myicon.exiticon);
        exitbutton.setToolTipText("Click to Exit");
        aboutbutton = new JButton("", myicon.abouticon);
        aboutbutton.setToolTipText("About Me");

// registering buttons to button panel
//  buttonpanel.add(addbutton);
        buttonpanel.add(deletebutton);
        buttonpanel.add(savebutton);
        buttonpanel.add(deleteallbutton);
        buttonpanel.add(aboutbutton);
        buttonpanel.add(exitbutton);

//registering button handler to the 6 buttons
        buttonhnadler mybuttonhandler = new buttonhnadler();
        addbutton.addActionListener(mybuttonhandler);
        deletebutton.addActionListener(mybuttonhandler);
        deleteallbutton.addActionListener(mybuttonhandler);
        savebutton.addActionListener(mybuttonhandler);
        exitbutton.addActionListener(mybuttonhandler);
        aboutbutton.addActionListener(mybuttonhandler);
//registering  button panel to the main window frame
        add(buttonpanel, BorderLayout.EAST);
        buttonpanel.requestFocusInWindow();

    }
//creating button listener to make the button functioning

    class buttonhnadler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == savebutton) {
                try {

                    int indx = mytable.getSelectedRow();
                    if (indx != -1) {
                        Data temp = myvec.elementAt(indx);
                        temp.setDetails(textField.getText());
                    }
                    saveList();
                } catch (Exception e) {
                    System.out.println("Save button" + e);
                }

            }
            if (ae.getSource() == aboutbutton) {
                AboutClass about = new AboutClass();
                about.setSize(220, 330);
                about.setAlwaysOnTop(true);
                about.setBounds(220, 50, 250, 270);
                about.setVisible(true);
                about.setResizable(false);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dim.getWidth() - about.getWidth()) / 2);
                int y = 160;
                about.setLocation(x, y);
                Image icon = new ImageIcon(getClass().getResource("about.png")).getImage();
                about.setIconImage(icon);
            }
            if (ae.getSource() == exitbutton) {

                int indx = mytable.getSelectedRow();
                if (indx != -1) {
                    Data temp = myvec.elementAt(indx);
                    temp.setDetails(textField.getText());
                }
                saveList();
                myvec.clear();
                System.exit(0);
            }

            if (ae.getSource() == deletebutton) {
                if (mytable.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(rootPane, "No Task Selected. Select One Task.", "Warning!", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int showConfirmDialog = JOptionPane.showConfirmDialog(rootPane, "Are You Sure?");
                        if (showConfirmDialog == 0) {

                            int indx = mytable.getSelectedRows().length;
                            for (int i = 0; i < indx; i++) {
                                int selected_row = mytable.getSelectedRow();
                                mymodel.removeRow(selected_row);
                                myvec.remove(selected_row);
                            }
                            prev = -1;
                            //   System.out.println("present row in delte button" + indx);
                            textField.setText("Happy ToDoListing....");
                        }
                    } catch (Exception e) {
                        System.out.println("delete button: " + e);

                    }

                }
            }
            if (ae.getSource() == deleteallbutton) {
                try {
                    String ps = JOptionPane.showInputDialog(rootPane, "Type  \"BANGLADESH\"  to Confirm.");
                    if (ps.equals("BANGLADESH")) {
                        while (mytable.getRowCount() > 0) {
                            mymodel.removeRow(0);
                        }
                        myvec.clear();
                        prev = -1;
                        textField.setText("Happy ToDoListing....");

                    }

                } catch (Exception ee) {

                }
            }
        }
    }

    /**
     * *
     * this method encapsulating timelabel.. returns timelabel reference..
     * currently it's being used in TimeThread() class.
     *
     * @return Jlabel timelabel reference to the caller.
     */
    public JLabel getTimeLabel() {
        return timeLabel;
    }

    /**
     * *
     *
     * this method saves the current table into a file while exiting the app or
     * save button is pressed.
     */
    private void saveList() {

        try {
            String pathtosave = ("C:\\ToDoList\\Data\\");
            File myfile = new File(pathtosave);
            myfile.mkdirs();
            myfile = new File("C:\\ToDoList\\Data\\DontDelete.ms");
            try (ObjectOutputStream myout = new ObjectOutputStream(new FileOutputStream(myfile))) {
                for (int i = 0; i < mymodel.getRowCount(); i++) {
                    myvec.get(i).setFlag((boolean) mymodel.getValueAt(i, 0));
                    myvec.get(i).setTitle((String) mymodel.getValueAt(i, 1));
                }
                for (Data temp : myvec) {
                    myout.writeObject(temp);

                }
                myout.close();
            }

        } catch (Exception e) {

            //   System.out.println("saveList: " + e);
        }
    }

    /**
     * *
     *
     * this method loads the task list when the application starts up.
     */
    private void loadList() {

        String tasklist = null;
        Data inputdata;
        String pathtoload = "C:\\ToDoList\\Data\\DontDelete.ms";
        File myfile = new File(pathtoload);
        ObjectInputStream myin = null;
        try {
            myin = new ObjectInputStream(new FileInputStream(myfile));
        } catch (IOException ex) {
            //  Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while ((inputdata = (Data) myin.readObject()) != null) {
                if (!inputdata.isPast()) {
                    myvec.add(inputdata);
                    mymodel.addRow(inputdata.getData());
                }
            }
        } catch (Exception e) {

        }
    }
}
