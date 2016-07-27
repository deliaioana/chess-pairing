/*
 * EnterFrame.java
 *
 * JAVAPAIRING rev. 2.9 - september 2015
 * Last revised on 19 august 2015 
 * 
 * This program allows to handle a chess individual or team tournament, i.e. team & player
 * registration, pairing, result and output generation.
 *
 * main author Eugenio Cervesato (eucerve@tin.it  mobile +39 338 5960366)
 * "Bobby Fischer" chess club - Cordenons - Italy

---------------------------------------------------------------------------
    Copyright (C) 2009-2014  Eugenio Cervesato from Cordenons (PN) - Italy.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/> or
    write to the Free Software Foundation, Inc., 59 Temple Place - 
    Suite 330, Boston, MA  02111-1307, USA.
---------------------------------------------------------------------------

 * The program has been developed with 
 *     - NetBeans IDE 7.0.1 (http://www.netbeans.org/downloads/)
 *     - Java Runtime Environment (JRE) 1.6 from Sun Microsystems Inc. 
 *       (http://java.sun.com/javase/downloads/index.jsp)
 * Also, the two tools are available in bundle (http://java.sun.com/javase/downloads/netbeans.html)
 * 
 * The distribution can run on any platform, including Windows, Linux
 * and Macintosh, only needed Java support (JRE 1.6 or more).
 *
 * For technical information about the array usage see the note at the bottom
 */

package eu.chessdata.paring.originaljavaparing;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.net.MalformedURLException;
import java.util.Random;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.View;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 *
 * @author  eugenio.cervesato
 */
public class EnterFrame extends javax.swing.JFrame {
    
    /** Creates new form EnterFrame */
    public EnterFrame() {
        initComponents();
        jDialog1p = jDialog1;   // export some controls to be used in Main
        jDialog2p = jDialog2;
        jDialog3p = jDialog3;
        jDialog4p = jDialog4;
        jDialog5p = jDialog5;
        jDialog6p = jDialog6;
        jDialog7p = jDialog7;
        jPopupMenu1p = jPopupMenu1;
        jPopupMenu2p = jPopupMenu2;
        jPopupMenu3p = jPopupMenu3;
        jTabbedPane1p = jTabbedPane1;
        jPanel1p = jPanel1;
        String s = Main.localizedText("WhiteColor");
        if (s.length()==1) WhiteColor = s;
        s = Main.localizedText("BlackColor");
        if (s.length()==1) BlackColor = s;
        // populate tie-break criteria comboboxes
        for (int k=0; k<tieBreaks.length; k++)
            tieBreaks[k] = Main.localizedText(tieBreaks[k]);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(tieBreaks));
        // translate verbosity levels
        for (int k=0; k<verbosities.length; k++)
            verbosities[k] = Main.localizedText(verbosities[k]);
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(verbosities));
        // reset round counter
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 0, 1);
        jSpinner1.setModel(model);
        // set no reordering of column tables
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable5.getTableHeader().setReorderingAllowed(false);
        jTable6.getTableHeader().setReorderingAllowed(false);
        jTable7.getTableHeader().setReorderingAllowed(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable7.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // set a special cell renderer for table4
        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        try
        {
            jTable4.setDefaultRenderer( Class.forName ( "java.lang.Object" ), renderer );
        }
        catch( ClassNotFoundException ex )
        {
            System.exit( 0 );
        }
        // allocate memory for fixed-length tables
        adaptRows(MAXPLAYERSPERTEAM);
        myTableModel myTM1 = new myTableModel(MAXTEAMS,MAXPLAYERSPERTEAM+2);
        myTM1.setColumnName(0, Main.localizedText("Teams & Players"));
        myTM1.setColumnName(1, Main.localizedText("Elo"));
        for (int j=1; j<=MAXPLAYERSPERTEAM; j++) 
            myTM1.setColumnName(j+1, Main.localizedText("Board"+j));
        for (int i=0; i<MAXTEAMS; i++) {
            sortIndex[i]=(short)(i+1);
            teamScores[i][2] = 0;       // reset acceleration
            for (int j=0; j<MAXPLAYERSPERTEAM+2; j++) 
            myTM1.setValueAt("", i, j);
        }
        jTable1.setModel(myTM1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(140);  // adapt to text
        jTable1.getColumnModel().getColumn(1).setMaxWidth(50);  // adapt to text
        jTable1.getTableHeader().addMouseListener(new ColumnHeaderListener());  // add a listener to the header
        int i,j,k;
        for (i=0; i<MAXPAIRS; i++)  // reset pairings array
        for (j=0; j<=MAXBOARDS; j++) 
        for (k=0; k<MAXROUNDS; k++) roundsDetail[i][j][k]="0-0-0-0";

        for (k=0; k<MAXROUNDS; k++) upfloaters[k]="upfloaters of round "+(k+1)+" ;";
        for (k=0; k<MAXROUNDS; k++) downfloaters[k]="downfloaters of round "+(k+1)+" ;";
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jButton27 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton42 = new javax.swing.JButton();
        jTextField55 = new javax.swing.JTextField();
        jButton26 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jCheckBox9 = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jDialog3 = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jButton34 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        jTextField39 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jTextField34 = new javax.swing.JTextField();
        jTextField37 = new javax.swing.JTextField();
        jTextField40 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField44 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jTextField47 = new javax.swing.JTextField();
        jTextField48 = new javax.swing.JTextField();
        jTextField49 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jTextField51 = new javax.swing.JTextField();
        jTextField52 = new javax.swing.JTextField();
        jTextField53 = new javax.swing.JTextField();
        jCheckBox7 = new javax.swing.JCheckBox();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jDialog4 = new javax.swing.JDialog();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        jButton38 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jDialog5 = new javax.swing.JDialog();
        jPanel19 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jButton46 = new javax.swing.JButton();
        jDialog6 = new javax.swing.JDialog();
        jPanel20 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton47 = new javax.swing.JButton();
        jTextField57 = new javax.swing.JTextField();
        jTextField58 = new javax.swing.JTextField();
        jTextField59 = new javax.swing.JTextField();
        jTextField60 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jTextField61 = new javax.swing.JTextField();
        jTextField62 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jDialog7 = new javax.swing.JDialog();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel48 = new javax.swing.JLabel();
        jButton48 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jComboBox8 = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jTextField54 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton22 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel32 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton12 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();

        jPopupMenu1.setInvoker(jTable1);
        jPopupMenu1.setLabel("select an action ...");

        jMenuItem1.setText("edit selected");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("remove selected");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem6.setText("switch retired");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem6);

        jMenuItem9.setText("sort ascending");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem9);

        jMenuItem10.setText("sort descending");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem10);

        jMenuItem11.setText("move UP selected row");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem11);

        jMenuItem3.setText("move UP selected row");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        jMenuItem4.setText("blank selected row");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem4);

        jMenuItem7.setText("switch retired");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem7);

        jDialog1.setTitle("Setup schema");
        jDialog1.setLocationByPlatform(true);
        jDialog1.setMinimumSize(new java.awt.Dimension(730, 640));

        jButton27.setText("Open database file to preview");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton24.setText("Load schema");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel7.setText("or");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(jButton27)
                .addGap(103, 103, 103))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jButton27)
                    .addComponent(jButton24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jList2.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setSelectedIndex(0);
        jScrollPane10.setViewportView(jList2);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "FED", "Birthday", "Sex", "Title", "ID FIDE", "Elo FIDE", "ID NAT", "Elo NAT", "K"
            }
        ));
        jScrollPane11.setViewportView(jTable8);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("LOAD A PREBUILT SCHEMA OR OPEN A TEXT DATABASE WITH PLAYERS DATA");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("then press 'Test schema' to see the import results");

        jButton29.setText("Test schema");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Fixed column format ... or ... Field delimiter                                            :");
        jCheckBox2.setActionCommand("Fixed column format ... or ... Field delimiter:");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/reload.GIF"))); // NOI18N
        jButton42.setToolTipText("Reset");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap(107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton29))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap(139, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jButton42)))))
                .addGap(58, 58, 58))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox2)
                            .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton29)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jButton26.setText("Save schema");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jLabel8.setText("or");

        jButton30.setText("close the window");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton26)
                    .addComponent(jButton30))
                .addContainerGap())
        );

        jDialog2.setTitle("Enter tournament data");
        jDialog2.setBackground(java.awt.Color.white);
        jDialog2.setLocationByPlatform(true);
        jDialog2.setMinimumSize(new java.awt.Dimension(650, 640));
        jDialog2.setModal(true);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 0));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("SETUP TOURNAMENT DATA");
        jLabel17.setPreferredSize(new java.awt.Dimension(120, 16));

        jLabel10.setText("Tournament Name");

        jTextField8.setText("jTextField8");

        jLabel11.setText("Play System");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Swiss Dutch", "Swiss Dubov", "Swiss Simple", "Swiss Perfect Colours", "Amalfi Rating", "Round Robin", "by hand" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jLabel12.setText("Place");

        jTextField9.setText("jTextField9");

        jTextField10.setText("jTextField10");

        jLabel14.setText("Arbiter");

        jTextField11.setText("jTextField11");

        jTextField12.setText("jTextField12");

        jLabel18.setText("to");

        jLabel20.setText("FED");

        jLabel13.setText("Date from");

        jTextField5.setText("jTextField5");

        jLabel21.setText("Rounds to play");

        jTextField6.setText("jTextField6");

        jLabel22.setText("do pairings ...");

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setSelected(true);
        jRadioButton5.setText("regular");
        jRadioButton5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup2.add(jRadioButton6);
        jRadioButton6.setText("allow return !");
        jRadioButton6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton6.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setText("absolutely free ?");
        jRadioButton7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton7.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel4.setText("Boards to play");

        jTextField7.setText("jTextField7");

        jLabel23.setText("max Players per Team");

        jTextField13.setText("jTextField13");
        jTextField13.setPreferredSize(new java.awt.Dimension(59, 20));

        jLabel50.setText("Allow acceleration");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                            .addComponent(jScrollPane2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel21)
                                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel50)
                                                    .addComponent(jLabel22))))
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel23))
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jRadioButton7)
                                            .addComponent(jRadioButton6)
                                            .addComponent(jRadioButton5)
                                            .addComponent(jCheckBox9)
                                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, 0, 43, Short.MAX_VALUE)
                                                .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(jRadioButton5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50)
                                    .addComponent(jCheckBox9))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))))
                .addContainerGap())
        );

        jLabel15.setText("order criteria:   (ranking +)");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Elo");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("random");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("alphabetical");
        jRadioButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("as entered");
        jRadioButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(22, 22, 22)
                .addComponent(jRadioButton1)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton2)
                .addGap(16, 16, 16)
                .addComponent(jRadioButton3)
                .addGap(19, 19, 19)
                .addComponent(jRadioButton4)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton16.setText("Save Changes");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Discard Changes");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel34.setText("tie-break criteria:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox1.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox1.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(130, 20));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox2.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox2.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox2.setPreferredSize(new java.awt.Dimension(130, 20));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox3.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox3.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox3.setPreferredSize(new java.awt.Dimension(130, 20));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox4.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox4.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox4.setPreferredSize(new java.awt.Dimension(130, 20));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox5.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox5.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox5.setPreferredSize(new java.awt.Dimension(130, 20));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Not Played Games" }));
        jComboBox6.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox6.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox6.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, 145, Short.MAX_VALUE)
                    .addComponent(jComboBox4, 0, 145, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, 0, 145, Short.MAX_VALUE)
                    .addComponent(jComboBox5, 0, 145, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3, 0, 145, Short.MAX_VALUE)
                    .addComponent(jComboBox6, 0, 145, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(165, Short.MAX_VALUE)
                .addComponent(jButton16)
                .addGap(79, 79, 79)
                .addComponent(jButton17)
                .addGap(138, 138, 138))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPopupMenu3.setInvoker(jTable2);

        jMenuItem5.setText("Remove selected pair");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem5);

        jMenuItem8.setText("exchange colours");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem8);

        jDialog3.setLocationByPlatform(true);
        jDialog3.setMinimumSize(new java.awt.Dimension(700, 400));
        jDialog3.setModal(true);

        buttonGroup3.add(jCheckBox3);
        jCheckBox3.setText("request ranking by Age");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel24.setText("group");

        jLabel25.setText("yy From");

        jLabel26.setText("yy To");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jCheckBox3)
                .addGap(21, 21, 21)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25))
                .addGap(16, 16, 16)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jButton34.setText("Continue");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("You may also request ranking by AGE, CATEGORY or Elo");

        buttonGroup3.add(jCheckBox4);
        jCheckBox4.setText("request ranking by Category");
        jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel27.setText("group");

        jLabel28.setText("Categories (A;B;C...)");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox4)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField30)
                            .addComponent(jTextField33)
                            .addComponent(jTextField36)
                            .addComponent(jTextField39)
                            .addComponent(jTextField42))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jCheckBox4)
                .addGap(21, 21, 21)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(16, 16, 16)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        buttonGroup3.add(jCheckBox5);
        jCheckBox5.setText("request ranking by Elo");
        jCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel29.setText("group");

        jLabel30.setText("Elo From");

        jLabel31.setText("Elo To");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox5)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField51, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField52, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField53, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jCheckBox5)
                .addGap(21, 21, 21)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30))
                .addGap(16, 16, 16)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        buttonGroup3.add(jCheckBox7);
        jCheckBox7.setText("Reset");
        jCheckBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox7.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(jCheckBox7)
                        .addGap(103, 103, 103)
                        .addComponent(jButton34))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox7)
                            .addComponent(jButton34))
                        .addGap(30, 30, 30))))
        );

        jDialog4.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialog4.setTitle("explain pairing");
        jDialog4.setLocationByPlatform(true);
        jDialog4.setMinimumSize(new java.awt.Dimension(920, 640));
        jDialog4.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                jDialog4WindowClosing(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane13.setViewportView(jTextArea1);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("explain pairing procedure");
        jLabel33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton38.setText("close the window");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/hourglass.GIF"))); // NOI18N
        jButton43.setText("user action!");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Verbosity Level:");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Beginner", "Intermediate", "Expert" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog4Layout.createSequentialGroup()
                .addContainerGap(223, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addGap(526, 526, 526))
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77))
                    .addGroup(jDialog4Layout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 873, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(21, 21, 21)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton43)
                    .addComponent(jLabel49)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog5.setBackground(java.awt.SystemColor.desktop);
        jDialog5.setLocationByPlatform(true);
        jDialog5.setMinimumSize(new java.awt.Dimension(540, 280));
        jDialog5.setModal(true);
        jDialog5.setName("dialog5"); // NOI18N

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("you can optimize page breaks by specifying ");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("the maximum number of rows per sheet");

        jTextField56.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTextField56.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Remember to use the 'send to browser' button and do 'print preview' on that!");

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("And, if available, use the zoom factor to reduce ink and paper waste ");

        jButton46.setText("Continue");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(234, Short.MAX_VALUE))
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(jButton46)
                .addContainerGap(230, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addComponent(jButton46)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog5Layout = new javax.swing.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog5Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog6.setLocationByPlatform(true);
        jDialog6.setMinimumSize(new java.awt.Dimension(550, 440));
        jDialog6.setModal(true);
        jDialog6.setName("dialog6"); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel39.setText("Set simulation parameters:");

        jLabel40.setText("Participant Players:");

        jLabel41.setText("Percentage of draw games:");

        jLabel42.setText("Forfeit one game every");

        jLabel43.setText("Rounds played");

        jLabel44.setText("Players to be retired at the end of the tournament:");

        jButton47.setText("Continue");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jTextField57.setText("31");
        jTextField57.setMinimumSize(new java.awt.Dimension(30, 22));

        jTextField58.setText("40");
        jTextField58.setMinimumSize(new java.awt.Dimension(30, 22));

        jTextField59.setText("37");
        jTextField59.setMinimumSize(new java.awt.Dimension(30, 22));

        jTextField60.setText("45");
        jTextField60.setMinimumSize(new java.awt.Dimension(30, 22));

        jLabel45.setText("Percentage of Win White:");

        jLabel46.setText("Percentage of Win Stronger:");

        jTextField61.setText("3");

        jTextField62.setText("2");

        jLabel47.setText("Allow acceleration:");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField60, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                            .addComponent(jTextField61, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox8)))
                .addGap(303, 303, 303))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jButton47)
                .addContainerGap(424, Short.MAX_VALUE))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(237, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel39)
                .addGap(43, 43, 43)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(jTextField60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jTextField61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jCheckBox8)
                        .addGap(30, 30, 30)
                        .addComponent(jButton47))
                    .addComponent(jLabel47))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog6Layout = new javax.swing.GroupLayout(jDialog6.getContentPane());
        jDialog6.getContentPane().setLayout(jDialog6Layout);
        jDialog6Layout.setHorizontalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog6Layout.setVerticalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog7.setMinimumSize(new java.awt.Dimension(490, 590));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane14.setViewportView(jTextArea2);

        jLabel48.setText("Log of the simulation procedure. Please let it open and copy/paste the statistics");

        jButton48.setText("copy as plain text");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog7Layout = new javax.swing.GroupLayout(jDialog7.getContentPane());
        jDialog7.getContentPane().setLayout(jDialog7Layout);
        jDialog7Layout.setHorizontalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog7Layout.createSequentialGroup()
                .addGroup(jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel48))
                    .addGroup(jDialog7Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog7Layout.setVerticalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton48)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("JavaPairing");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(832, 640));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setBackground(java.awt.Color.yellow);
        jTabbedPane1.setForeground(java.awt.Color.blue);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(854, 545));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setEnabled(false);

        jTable1.setComponentPopupMenu(jPopupMenu1);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teams & Players", "mean Elo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Add Team");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Save Changes");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane9.setBorder(null);

        jTable6.setComponentPopupMenu(jPopupMenu2);
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Player Name", "FED", "Birthday", "Sex", "Title", "ID FIDE", "Elo FIDE", "ID NAT", "Elo NAT", "K"
            }
        ));
        jScrollPane9.setViewportView(jTable6);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jButton2)
                        .addGap(40, 40, 40)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Team Name");

        jButton28.setBackground(new java.awt.Color(255, 255, 255));
        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/options.GIF"))); // NOI18N
        jButton28.setToolTipText("set import players options");
        jButton28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton28.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton28.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton28.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/cut.GIF"))); // NOI18N
        jButton41.setToolTipText("Reset");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jLabel2.setText("Load player from database");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/search_player.GIF"))); // NOI18N
        jButton4.setToolTipText("Enter players from DB");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton4.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton4.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList3.setVisibleRowCount(10);
        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList3MouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(jList3);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DB 1", "DB 2", "DB 3", "DB 4", "DB 5" }));
        jComboBox8.setToolTipText("select DB");
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel2))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox8, 0, 56, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104))
            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jTextField54.setEditable(false);
        jTextField54.setForeground(new java.awt.Color(0, 0, 255));
        jTextField54.setBorder(null);

        jButton5.setText("edit selected");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("remove selected");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton37.setText("switch retired");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField54, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jButton5)
                .addGap(70, 70, 70)
                .addComponent(jButton6)
                .addGap(66, 66, 66)
                .addComponent(jButton37)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton37))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel22, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.getAccessibleContext().setAccessibleParent(this);

        jTabbedPane1.addTab("---Registration---", jPanel2);

        jPanel8.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel8ComponentShown(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton21.setText("done");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jScrollPane3.setBorder(null);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jScrollPane4.setBorder(null);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jButton22.setText("remove last pair");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton20.setText("add pair");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("optimize colours");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setEnabled(false);
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                    .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jCheckBox1)
                        .addGap(15, 15, 15)
                        .addComponent(jButton20)
                        .addGap(29, 29, 29)
                        .addComponent(jButton22)
                        .addGap(70, 70, 70)
                        .addComponent(jButton21))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jScrollPane5.setBorder(null);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Team A", "Team B"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setComponentPopupMenu(jPopupMenu3);
        jScrollPane5.setViewportView(jTable2);

        jButton7.setText("do next round");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jSpinner1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jSpinner1.setMinimumSize(new java.awt.Dimension(42, 30));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Current round");

        jButton18.setText("remove last round");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel32.setText("explain");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                    .addComponent(jButton18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7)
                            .addComponent(jCheckBox6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel32)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("--------Rounds--------", jPanel8);

        jPanel5.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel5ComponentShown(evt);
            }
        });

        jScrollPane6.setBorder(null);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Board", "Team A", "Team B", "score"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setShowVerticalLines(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable5MouseReleased(evt);
            }
        });
        jTable5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable5KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable5KeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(jTable5);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Insert or Modify current round results");

        jButton23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton23.setText("done");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jScrollPane8.setBorder(null);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "player A", "player B", "result"
            }
        ));
        jTable7.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable7.setName(""); // NOI18N
        jTable7.setShowVerticalLines(false);
        jTable7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable7KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable7KeyTyped(evt);
            }
        });
        jScrollPane8.setViewportView(jTable7);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jLabel16))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(jButton23)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("-------Results-------", jPanel5);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/ranking_small.GIF"))); // NOI18N
        jButton9.setText("team ranking");
        jButton9.setToolTipText("team ranking");
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setIconTextGap(0);
        jButton9.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton9.setMaximumSize(new java.awt.Dimension(105, 35));
        jButton9.setMinimumSize(new java.awt.Dimension(105, 35));
        jButton9.setPreferredSize(new java.awt.Dimension(105, 35));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/ranking_small.GIF"))); // NOI18N
        jButton19.setText("player ranking");
        jButton19.setToolTipText("player ranking");
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setIconTextGap(0);
        jButton19.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/print.GIF"))); // NOI18N
        jButton25.setText("send to printer");
        jButton25.setToolTipText("send to printer");
        jButton25.setMaximumSize(new java.awt.Dimension(220, 61));
        jButton25.setMinimumSize(new java.awt.Dimension(220, 61));
        jButton25.setPreferredSize(new java.awt.Dimension(220, 61));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jScrollPane7.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane7.setPreferredSize(new java.awt.Dimension(0, 0));

        jTextPane1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        jTextPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jTextPane1.setPreferredSize(new java.awt.Dimension(0, 0));
        jScrollPane7.setViewportView(jTextPane1);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/team_small.GIF"))); // NOI18N
        jButton12.setText("Teams & Players");
        jButton12.setToolTipText("Teams & Players");
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setIconTextGap(0);
        jButton12.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton12.setMaximumSize(new java.awt.Dimension(105, 35));
        jButton12.setMinimumSize(new java.awt.Dimension(105, 35));
        jButton12.setPreferredSize(new java.awt.Dimension(105, 35));
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/round_small.GIF"))); // NOI18N
        jButton31.setText("Current round");
        jButton31.setToolTipText("Current round");
        jButton31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton31.setIconTextGap(0);
        jButton31.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton31.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/cross_small.GIF"))); // NOI18N
        jButton32.setText("Cross Table");
        jButton32.setToolTipText("Cross Table");
        jButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton32.setIconTextGap(0);
        jButton32.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton32.setMaximumSize(new java.awt.Dimension(105, 35));
        jButton32.setMinimumSize(new java.awt.Dimension(105, 35));
        jButton32.setPreferredSize(new java.awt.Dimension(105, 35));
        jButton32.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/elovar_small.GIF"))); // NOI18N
        jButton33.setText("Elo var");
        jButton33.setToolTipText("Elo var");
        jButton33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton33.setIconTextGap(0);
        jButton33.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton33.setMaximumSize(new java.awt.Dimension(105, 35));
        jButton33.setMinimumSize(new java.awt.Dimension(105, 35));
        jButton33.setPreferredSize(new java.awt.Dimension(105, 35));
        jButton33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/web.GIF"))); // NOI18N
        jButton39.setText("send to browser");
        jButton39.setToolTipText("send to browser");
        jButton39.setMaximumSize(new java.awt.Dimension(220, 61));
        jButton39.setMinimumSize(new java.awt.Dimension(220, 61));
        jButton39.setPreferredSize(new java.awt.Dimension(220, 61));
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/report.GIF"))); // NOI18N
        jButton40.setText("copy as plain text");
        jButton40.setToolTipText("copy as plain text");
        jButton40.setMaximumSize(new java.awt.Dimension(220, 61));
        jButton40.setMinimumSize(new java.awt.Dimension(220, 61));
        jButton40.setPreferredSize(new java.awt.Dimension(220, 61));
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/card_small.GIF"))); // NOI18N
        jButton44.setText("player cards");
        jButton44.setToolTipText("player cards");
        jButton44.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton44.setIconTextGap(0);
        jButton44.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton44.setMaximumSize(new java.awt.Dimension(105, 35));
        jButton44.setMinimumSize(new java.awt.Dimension(105, 35));
        jButton44.setPreferredSize(new java.awt.Dimension(105, 35));
        jButton44.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/round_small.GIF"))); // NOI18N
        jButton45.setText("Board card");
        jButton45.setToolTipText("Board card");
        jButton45.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton45.setIconTextGap(0);
        jButton45.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton45.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("-------Output--------", jPanel6);

        jPanel1.setPreferredSize(new java.awt.Dimension(853, 67));

        jButton10.setBackground(new java.awt.Color(255, 255, 255));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/open.GIF"))); // NOI18N
        jButton10.setToolTipText("open tournament");
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton10.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton10.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(255, 255, 255));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/save.GIF"))); // NOI18N
        jButton11.setToolTipText("save tournament data");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton11.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(255, 255, 255));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/help.GIF"))); // NOI18N
        jButton13.setToolTipText("help");
        jButton13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton13.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton13.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(255, 255, 255));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/quit.GIF"))); // NOI18N
        jButton14.setToolTipText("quit");
        jButton14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton14.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton14.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(255, 255, 255));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/options.GIF"))); // NOI18N
        jButton15.setToolTipText("set tournament options");
        jButton15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton15.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton15.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton15.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setForeground(java.awt.Color.blue);
        jLabel19.setText("Current round");

        jTextField4.setEditable(false);
        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField4.setForeground(java.awt.Color.blue);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setMinimumSize(new java.awt.Dimension(42, 30));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/gplv3.GIF"))); // NOI18N
        jButton8.setToolTipText("License info");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(255, 255, 255));
        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/web.GIF"))); // NOI18N
        jButton35.setToolTipText("create WEB site");
        jButton35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton35.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton35.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton35.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(255, 255, 255));
        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javapairing/report.GIF"))); // NOI18N
        jButton36.setToolTipText("create report to Federation");
        jButton36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton36.setMaximumSize(new java.awt.Dimension(52, 52));
        jButton36.setMinimumSize(new java.awt.Dimension(52, 52));
        jButton36.setPreferredSize(new java.awt.Dimension(52, 52));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
// read data and close Dialog5
        try {
            maxRowsPerPage = Integer.valueOf(jTextField56.getText()).shortValue();
        } catch (NullPointerException ex) {}
          catch (NumberFormatException ex) {}
        jDialog5.dispose();
       
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
 // Board card
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        jTextField56.setText(""+maxRowsPerPage);
        jDialog5.setVisible(true);      // open options dialog (modal)
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int board, i1, i2, j, p1, p2;
        boolean results;
        String s, s1, s2, team1, team2, player1, player2, color1, color2, f, conditionalPageBreak; String [] S;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        int counter=0, toBeAdded;
        try {
            fOut = new FileWriter(tempFile);  // create a temp file
            String lineSeparator = System.getProperty( "line.separator" );
            fOut.write("<html>"+lineSeparator);
            fOut.write("<head></head><body>"+lineSeparator);
            for (j=0; j<addedPairs; j++) {
                s = roundsDetail[j][0][currRound-1];
                S = s.split("-");
                if (S.length != 4) continue;
                i1 = Integer.valueOf(S[0]);
                i2 = Integer.valueOf(S[1]);
                if (i1==0) continue;    // empty row?
                s1 = " | "+teamScores[i1-1][0];
                if (maxPlayersPerTeam==1) s1="";    // individual tournament!
                s2 = " | "+ 0.5f*teamScores[i1-1][0];
                team1 = ""+myTM.getValueAt(sortIndex[i1-1]-1,0)+s1+s2;
                team2 = "BYE"; f="f";
                if (i2>0) {
                    s1 = " | "+teamScores[i2-1][0];
                    if (maxPlayersPerTeam==1) s1="";    // individual tournament!
                    s2 = " | "+ 0.5f*teamScores[i2-1][0];
                    team2 = ""+myTM.getValueAt(sortIndex[i2-1]-1,0)+s1+s2; f="";
                }
                if (S[3].indexOf('f')>0) f="f";
                results=true; toBeAdded=(maxBoards+4);
                if (S[2].equals("0") && S[3].equals("0")) results=false;
                if (!results)
                  for (board=maxBoards+1; board<=maxPlayersPerTeam; board++) {
                    player1 = ""+myTM.getValueAt(sortIndex[i1-1]-1, board+1);
                    player2 = "";
                    if (i2>0) player2 = ""+myTM.getValueAt(sortIndex[i2-1]-1, board+1);
                    player1 = minimalPlayerData(player1);
                    player2 = minimalPlayerData(player2);
                    S = player1.split(";");
                    if (S[0].equals("")) player1 = "";
                    S = player2.split(";");
                    if (S[0].equals("")) player2 = "";
                    if (player1.equals(player2)) continue;  // both empty ?
                    toBeAdded++;
                  }
                counter+= toBeAdded;
                conditionalPageBreak="";
                if (counter>maxRowsPerPage) {conditionalPageBreak=" style=\"page-break-before: always;\""; counter=toBeAdded;}
                fOut.write("<table "+conditionalPageBreak+">"+lineSeparator);
                fOut.write("<tr><td></td><td colspan=5>"+tournamentName+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Main.localizedText("Current round")+": "+currRound+"</td></tr>"+lineSeparator);
                fOut.write("<tr><td><font Size=\"+1\"><strong>"+(j+1)+"</strong></td><td></td>");
                fOut.write("<td><font Size=\"+1\">"+WhiteColor+" - "+team1+"</td>");
                fOut.write("<td> - </td>");
                fOut.write("<td><font Size=\"+1\">"+BlackColor+" - "+team2+"</td>");
                if (results) fOut.write("<td><font Size=\"+1\"> "+HTMLscore(integerScore(S[2])*10)+" - "+HTMLscore(integerScore(S[3])*10)+f+"</td>");
                else fOut.write("<td><font Size=\"+1\"> ...... - ......</td>");
                fOut.write("</tr>"+lineSeparator);
                for (board=1; board<=maxBoards; board++) {
                    p1=board; p2=board;
                    if (results) {
                        s = roundsDetail[j][board][currRound-1];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]);
                        p2 = Integer.valueOf(S[1]);
                        if (p1==0) continue;    // empty row?
                    }
                    player1 = ""+myTM.getValueAt(sortIndex[i1-1]-1, p1+1);
                    player2 = ""; f="f";
                    if (i2>0 && p2>0) {player2 = ""+myTM.getValueAt(sortIndex[i2-1]-1, p2+1); f="";}
                    player1 = minimalPlayerData(player1);
                    player2 = minimalPlayerData(player2);
                    if (player1.split(";")[0].equals("")) player1 = "";
                    if (player2.split(";")[0].equals("")) player2 = "";
                    fOut.write("<tr><td></td><td align=\"right\">"+board+"</td>");
                    color1=WhiteColor;
                    color2=BlackColor;
                    if (board%2==0) { color2=WhiteColor; color1=BlackColor; } // revert colors
                    fOut.write("<td>"+color1+" - "+player1+"</td>");
                    fOut.write("<td> - </td>");
                    fOut.write("<td>"+color2+" - "+player2+"</td>");
                    if (results) {
                        if (S[3].indexOf('f')>0) f="f";
                        fOut.write("<td> "+HTMLscore(integerScore(S[2])*10)+" - "+HTMLscore(integerScore(S[3])*10)+f+"</td>");
                    }
                    else fOut.write("<td> ...... - ......</td>");
                    fOut.write("</tr>"+lineSeparator);
                }
                if (!results)
                  for (board=maxBoards+1; board<=maxPlayersPerTeam; board++) {
                    player1 = ""+myTM.getValueAt(sortIndex[i1-1]-1, board+1);
                    player2 = "";
                    if (i2>0) player2 = ""+myTM.getValueAt(sortIndex[i2-1]-1, board+1);
                    player1 = minimalPlayerData(player1);
                    player2 = minimalPlayerData(player2);
                    S = player1.split(";");
                    if (S[0].equals("")) player1 = "";
                    S = player2.split(";");
                    if (S[0].equals("")) player2 = "";
                    if (player1.equals(player2)) continue;  // both empty ?
                    fOut.write("<tr><td></td><td></td>");
                    fOut.write("<td>"+player1+"</td>");
                    fOut.write("<td> - </td>");
                    fOut.write("<td>"+player2+"</td>");
                    fOut.write("</tr>"+lineSeparator);
                  }
                fOut.write("<tr><td colspan=6><hr /></td></tr>"+lineSeparator);
                fOut.write("</table><br>"+lineSeparator);
            }
            fOut.write("</body>"+lineSeparator);
            fOut.write("</html>"+lineSeparator);
            fOut.close();
                       
            java.net.URL url=null;
            try {
                url = new java.net.URL("file:///"+tempFile);
            } catch (MalformedURLException ex) {
//                
            }
            if (url != null) 
            try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
            } catch (IOException ex) {}
              catch (NullPointerException ex) {}
        } catch (IOException ex) {
        }        
        
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
// player cards
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        jTextField56.setText(""+maxRowsPerPage);
        jDialog5.setVisible(true);      // open options dialog (modal)
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, score, score1, score2;
        int diffElo, diffElo2, diffElo3, cumScore, expScore, result, IDOpponent, cumResult, opponentScore, validScore, opponentElo, validElo, sumElo, ARO;
        int eloPlayer, kPlayer, kOpponent, pa, paTot, nFIDE, totScoreFIDE, totEloOpp;
        int performance, meanElo, percentScore;
        double dp;
        String s, team, color, exp, pE, oE, conditionalPageBreak; String [] S;
        boolean played, bye;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        int counter=0, tablesPerPage = maxRowsPerPage / (currRound+8);
        try {
          fOut = new FileWriter(tempFile);  // create a temp file
          String lineSeparator = System.getProperty( "line.separator" );
          fOut.write("<html>"+lineSeparator);
          fOut.write("<head></head><body>"+lineSeparator);
          for (i0=1; i0<=addedRows; i0++) {
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                s = minimalPlayerData(s);
                S=s.split(";");
                pE=S[3];
                eloPlayer=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                kPlayer=Integer.parseInt(S[4].trim());
                conditionalPageBreak="";
                if (counter>0 && counter%tablesPerPage==0) conditionalPageBreak=" style=\"page-break-before: always;\"";
                counter++;
                fOut.write("<table "+conditionalPageBreak+">"+lineSeparator);
                fOut.write("<tr><td colspan=\"10\">"+Main.localizedText("Tournament")+": "+tournamentName);
                fOut.write(".   "+tournamentPlace+",  "+tournamentDate1+" - "+tournamentDate2+"</td></tr>"+lineSeparator);
                fOut.write("<tr><td colspan=\"10\">"+Main.localizedText("ID")+"="+p0+"  "+S[0].trim()+"  "+S[2].trim()+"  "+"   "+
                        Main.localizedText("Elo")+"="+pE);
                if (kPlayer==0 && eloPlayer>0) fOut.write("   "+Main.localizedText("K")+"=40/20/10");
                else fOut.write("   "+Main.localizedText("K")+"="+kPlayer);
                fOut.write("</td></tr>"+lineSeparator);
                fOut.write("<tr><td colspan=\"10\"><hr /></td></tr>"+lineSeparator);
                fOut.write("<tr><td>R</td><td>"+Main.localizedText("colour")+"</td><td>"+Main.localizedText("ID")+"</td><td>"+
                        Main.localizedText("Player")+"</td><td align=\"center\">"+Main.localizedText("Title")+
                        "</td><td align=\"center\">"+Main.localizedText("Elo")+"</td><td>"+
                        Main.localizedText("difference")+"</td><td>"+Main.localizedText("expected")+"</td><td align=\"center\">"+
                        Main.localizedText("result")+"</td><td align=\"center\">"+Main.localizedText("points")+"</td></tr>"+lineSeparator);
                fOut.write("<tr><td colspan=\"10\"><hr /></td></tr>"+lineSeparator);
                score=cumScore=cumResult=validScore=validElo=sumElo=ARO=paTot=nFIDE=totScoreFIDE=totEloOpp=0;
                for (round=0; round<currRound; round++) {
                    fOut.write("<tr><td>"+(round+1)+"</td>");
                    played=bye=false; 
                    diffElo=diffElo2=diffElo3=expScore=result=IDOpponent=opponentScore=kOpponent=0;
                    color="";
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            p2 = Integer.valueOf(S[1]);
                            if (p1==0 && p2==0) { p1=board; p2=board; }  // not yet initialized i.e. not yet played
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            if (p1==0 || p2==0 || S[3].indexOf('f')>0) {
                                S[3]=S[3].substring(0,1); // forfeit ?
                                if (i0==i1) p2 = 0;
                                else p1 = 0;
                            }
                            if (p1>0) p1 += (i1-1)*maxPlayersPerTeam;
                            if (p2>0 && i2>0) p2 += (i2-1)*maxPlayersPerTeam;
                            if (i0==i1 && p0==p1) {
                                if (i2==0 || p2==0) bye=true;
                                else if (board%2==1) color = WhiteColor;
                                else  color = BlackColor;
                                result=score1;
                                IDOpponent=p2;
                                played=true;
                            } else if (i0==i2 && p0==p2) {
                                if (i1==0 || p1==0) bye=true;
                                else if (board%2==0) color = WhiteColor;
                                else  color = BlackColor;
                                result=score2;
                                IDOpponent=p1;
                                played=true;
                            }
                            if (played) break;
                        }
                        if (played) break;
                    }
                    cumResult+=result;
                    if (IDOpponent>0 && !bye) {
                        i2=sortIndex[(IDOpponent-1)/maxPlayersPerTeam]-1;
                        p2=(IDOpponent-1)%maxPlayersPerTeam+2;
                        s=""+myTM.getValueAt(i2,p2);
                        s = minimalPlayerData(s);
                        S=s.split(";");
                        oE=S[3];
                        opponentElo=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                        kOpponent=Integer.parseInt(S[4].trim());
                        diffElo=eloPlayer-opponentElo;
                        fOut.write("<td>"+color+"</td>");
                        fOut.write("<td>"+IDOpponent+"</td>");
                        fOut.write("<td>"+S[0]+"</td>");
                        fOut.write("<td align=\"center\">"+S[2]+"</td>");
                        fOut.write("<td align=\"center\">"+oE+"</td>");
                        exp="";
                        if (opponentElo>0) {
                            if (kPlayer>0) {
                                validScore+=result; validElo++; sumElo+=opponentElo;
                                pa=calculatePA(eloPlayer, opponentElo);    // for National Elo var
                                paTot+=pa; exp=String.format("%4.2f",0.01f*pa);
                                if (kOpponent==0) {nFIDE++; totScoreFIDE+=result; totEloOpp+=opponentElo;}   // for tranche FIDE
                            }
                            else if (kOpponent==0) {
                                validScore+=result; validElo++; sumElo+=opponentElo;
                                pa=calculatePA(eloPlayer, opponentElo);    // for FIDE Elo var
                                paTot+=pa; exp=String.format("%4.2f",0.01f*pa);
                            }
                        }
                        s=String.format("%+d",diffElo);      // convert to string with sign
                        if (s.equalsIgnoreCase("+0")) s="0";  // don't like '+0' 
                        fOut.write("<td align=\"center\">"+s+"</td>");
                        fOut.write("<td align=\"center\">"+exp+"</td>");
                        fOut.write("<td align=\"center\">"+HTMLscore(result*10)+"</td>");
                        fOut.write("<td align=\"center\">"+HTMLscore(cumResult*10)+"</td>");
                    } else {
                        fOut.write("<td></td><td></td><td></td><td></td><td></td><td></td><td></td>");
                        fOut.write("<td align=\"center\">"+HTMLscore(result*10)+"f</td>");
                        fOut.write("<td align=\"center\">"+HTMLscore(cumResult*10)+"</td>");
                    }
                    fOut.write("</tr>"+lineSeparator);
                }
                fOut.write("<tr><td colspan=\"10\"><hr /></td></tr>"+lineSeparator);
                fOut.write("<tr><td colspan=\"10\">");
                if (validElo>0) {
                    ARO=sumElo/validElo; 
                    fOut.write(Main.localizedText("ARO")+"="+ARO+"  ");
                    pa=roundPA(paTot);
                    dp=(validScore*10-pa)/100.0;
                    if (kPlayer>0) {
                        diffElo=(int)Math.round(dp*kPlayer);
                        s=String.format("%+d",diffElo);      // convert to string with sign
                        if (s.equalsIgnoreCase("+0")) s="0";  // don't like '+0' 
                        fOut.write(Main.localizedText("Elo var")+"="+s+"  ");
                        fOut.write(Main.localizedText("new Elo")+"="+(eloPlayer+diffElo)+"  ");
                    } else {
                        diffElo=(int)Math.round(dp*40);
                        s=String.format("%+d",diffElo);      // convert to string with sign
                        if (s.equalsIgnoreCase("+0")) s="0";  // don't like '+0' 
                        fOut.write(Main.localizedText("Elo var")+"="+s+"/");
                        diffElo2=(int)Math.round(dp*20);
                        s=String.format("%+d",diffElo2);      // convert to string with sign
                        if (s.equalsIgnoreCase("+0")) s="0";  // don't like '+0' 
                        fOut.write(s+"/");
                        diffElo3=(int)Math.round(dp*10);
                        s=String.format("%+d",diffElo3);      // convert to string with sign
                        if (s.equalsIgnoreCase("+0")) s="0";  // don't like '+0' 
                        fOut.write(s+"  ");
                        fOut.write(Main.localizedText("new Elo")+"="+(eloPlayer+diffElo)+"/"+(eloPlayer+diffElo2)+"/"+(eloPlayer+diffElo3)+"  ");
                    }
                    performance=0;
                    if (nFIDE>0) {
                        meanElo=(int)Math.round(1.0*totEloOpp/nFIDE);
                        percentScore=Math.round(totScoreFIDE*10f/nFIDE);
                        if (percentScore<50) performance=deltaPerformance[percentScore];
                        else if (percentScore>50) performance=Math.round(KELOFIDEBASE*(totScoreFIDE*20-nFIDE*100)/200);
                        performance+=meanElo;
                    }
                    s="";
                    if (nFIDE<MINGAMESFIDE || totScoreFIDE<MINSCOREFIDE*10 || performance<MINELOFIDE) s=Main.localizedText("not valid if 1st");
                    if (nFIDE>0) fOut.write(Main.localizedText("tranche FIDE(games)")+"="+performance+"("+nFIDE+") "+s);
                }
                fOut.write("</td></tr></table><br>"+lineSeparator);
              }
          }

          fOut.write("</body>"+lineSeparator);
          fOut.write("</html>"+lineSeparator);
          fOut.close();
                       
          java.net.URL url=null;
          try {
                url = new java.net.URL("file:///"+tempFile);
          } catch (MalformedURLException ex) {
//                
          }
          if (url != null) 
          try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
          } catch (IOException ex) {}
            catch (NullPointerException ex) {}
            catch (OutOfMemoryError ex) {url=null; jTextPane1.setPage(url);}
        } catch (IOException ex) {
            
        }
        
    }//GEN-LAST:event_jButton44ActionPerformed

    public void initializeTournament(EnterFrame EF, String fileName) {
        // this is called only once at the start of the program to open and check a FIDE report
        MainForm=EF;
        FileToBeChecked=fileName;
        safeExitAllowed=checkerRunning=immediatelyExitAfterCalculation=true;
        Timer t=new Timer(0, new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent evt) {
                MainForm.jButton10ActionPerformed(null);
            }
        });
        t.setRepeats(false);
        t.setInitialDelay(1000);
        t.start();
    }
    
    public boolean initializeTournament() {
        // this is called only once at the start of the program to help selecting the kind of tournament
        Dimension d1, d2, d3;
        returnValueFromshowOptionDialog=-1;
        ImageIcon individual = new javax.swing.ImageIcon(getClass().getResource("/javapairing/individual.GIF"));
        final JButton INDIVIDUAL = new javax.swing.JButton(Main.localizedText("start a new individual tournament"));
        INDIVIDUAL.setIcon(individual);
        INDIVIDUAL.setVerticalTextPosition(SwingConstants.BOTTOM);
        INDIVIDUAL.setHorizontalTextPosition(SwingConstants.CENTER);
        d1=INDIVIDUAL.getPreferredSize();
        INDIVIDUAL.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(INDIVIDUAL);
                returnValueFromshowOptionDialog=0;
                if (w!=null) w.setVisible(false);
            }
        });
        ImageIcon team = new javax.swing.ImageIcon(getClass().getResource("/javapairing/team.GIF"));
        final JButton TEAM = new javax.swing.JButton(Main.localizedText("start a new team tournament"));
        TEAM.setIcon(team);
        TEAM.setVerticalTextPosition(SwingConstants.BOTTOM);
        TEAM.setHorizontalTextPosition(SwingConstants.CENTER);
        d2=TEAM.getPreferredSize();
        TEAM.addActionListener(new ActionListener() {
           // @Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(TEAM);
                returnValueFromshowOptionDialog=1;
                if (w!=null) w.setVisible(false);
            }
        });
        ImageIcon open = new javax.swing.ImageIcon(getClass().getResource("/javapairing/open.GIF"));
        final JButton OPEN = new javax.swing.JButton(Main.localizedText("open tournament"));
        OPEN.setIcon(open);
        OPEN.setVerticalTextPosition(SwingConstants.BOTTOM);
        OPEN.setHorizontalTextPosition(SwingConstants.CENTER);
        d3=OPEN.getPreferredSize();
        OPEN.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(OPEN);
                returnValueFromshowOptionDialog=2;
                if (w!=null) w.setVisible(false);
            }
        });
        double maxWidth=d1.getWidth();
        if (d2.getWidth()>maxWidth) maxWidth=d2.getWidth();
        if (d3.getWidth()>maxWidth) maxWidth=d3.getWidth();
        d1.setSize(maxWidth, d1.getHeight());
        d2.setSize(maxWidth, d2.getHeight());
        d3.setSize(maxWidth, d3.getHeight());
        INDIVIDUAL.setPreferredSize(d1);
        TEAM.setPreferredSize(d2);
        OPEN.setPreferredSize(d3);
        Object[] options = {INDIVIDUAL, TEAM, OPEN};
        JOptionPane.setRootFrame(new javax.swing.JFrame());
        JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, select one of:"),
                "JavaPairing. "+Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (returnValueFromshowOptionDialog==-1) return false;
        if (returnValueFromshowOptionDialog==2) {jButton10ActionPerformed(null); if (abort) return false;}
        else {
            if (returnValueFromshowOptionDialog==0) {maxBoards=1; maxPlayersPerTeam=1;}
            jButton15ActionPerformed(null);
        }
        return true;
    }
    
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
// move UP selected row on Table1
        TableModel myTM=jTable1.getModel();
        int row=jTable1.getSelectedRow();
        if (row<1 || row>=addedRows) return;
        String temp;
        for (int k=0; k<maxPlayersPerTeam+2; k++) {
            temp = ""+myTM.getValueAt(row-1,k); // save data previous row
            myTM.setValueAt(""+myTM.getValueAt(row,k),row-1,k);               // exchange data
            myTM.setValueAt(temp,row,k); 
            jTable1.getColumnModel().getColumn(k).setHeaderRenderer(null);    // remove any sort order
        }
        jTable1.setRowSelectionInterval(row-1,row-1);
        repaint();
        sortAscending = false;
        lastSortedCol = -1;        
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
// sort Table1 descending
        int col=jTable1.getSelectedColumn();
        if (col<0) col=0;
        int i,j,k,Elox,Eloy;
        String x,y;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        for (i=0; i<addedRows-1; i++) 
          for (j=i+1; j<addedRows; j++) {
            x=(""+jTable1.getValueAt(i, col)).toUpperCase();
            y=(""+jTable1.getValueAt(j, col)).toUpperCase();
            if (col==1) { // sort numeric on Elo
                Elox=0; Eloy=0;
                try {
                    Eloy=Integer.parseInt(y);
                    Elox=Integer.parseInt(x);
                } catch (NullPointerException ex) {}
                  catch (NumberFormatException ex) {}
                if (Elox<Eloy) for (k=0; k<maxPlayersPerTeam+2; k++) {
                    x=""+jTable1.getValueAt(i, k);
                    y=""+jTable1.getValueAt(j, k);
                    jTable1.setValueAt(y, i, k);
                    jTable1.setValueAt(x, j, k);
                }
            } else    // sort ASCII
                if (x.compareTo(y)<0) for (k=0; k<maxPlayersPerTeam+2; k++) {
                    x=""+jTable1.getValueAt(i, k);
                    y=""+jTable1.getValueAt(j, k);
                    jTable1.setValueAt(y, i, k);
                    jTable1.setValueAt(x, j, k);
                }
          }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        sortIcon = UIManager.getIcon("Table.descendingSortIcon");
        colHeader = ""+jTable1.getColumnModel().getColumn(col).getHeaderValue();
        for (k=0; k<maxPlayersPerTeam+2; k++) jTable1.getColumnModel().getColumn(k).setHeaderRenderer(null);
        jTable1.getColumnModel().getColumn(col).setHeaderRenderer(new iconRenderer());
        Rectangle rect = jTable1.getCellRect(0, 0, true);    // make the row visible (scroll)
        jTable1.scrollRectToVisible(rect);
        jTable1.setRowSelectionInterval(0,0);
        repaint();
        sortAscending = false;
        lastSortedCol = col; 
        for (i=0; i<MAXTEAMS; i++) {
            sortIndex[i]=(short)(i+1);  // reset sort order
            teamScores[i][2] = 0;       // reset acceleration
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
// sort Table1 ascending
        int col=jTable1.getSelectedColumn();
        if (col<0) col=0;
        int i,j,k,Elox,Eloy;
        String x,y;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        for (i=0; i<addedRows-1; i++) 
          for (j=i+1; j<addedRows; j++) {
            x=(""+jTable1.getValueAt(i, col)).toUpperCase();
            y=(""+jTable1.getValueAt(j, col)).toUpperCase();
            if (col==1) { // sort numeric on Elo
                Elox=0; Eloy=0;
                try {
                    Elox=Integer.parseInt(x);
                    Eloy=Integer.parseInt(y);
                } catch (NullPointerException ex) {}
                  catch (NumberFormatException ex) {}
                if (Elox>Eloy) for (k=0; k<maxPlayersPerTeam+2; k++) {
                    x=""+jTable1.getValueAt(i, k);
                    y=""+jTable1.getValueAt(j, k);
                    jTable1.setValueAt(y, i, k);
                    jTable1.setValueAt(x, j, k);
                }
            } else    // sort ASCII
                if (x.compareTo(y)>0) for (k=0; k<maxPlayersPerTeam+2; k++) {
                    x=""+jTable1.getValueAt(i, k);
                    y=""+jTable1.getValueAt(j, k);
                    jTable1.setValueAt(y, i, k);
                    jTable1.setValueAt(x, j, k);
                }
          }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        sortIcon = UIManager.getIcon("Table.ascendingSortIcon");
        colHeader = ""+jTable1.getColumnModel().getColumn(col).getHeaderValue();
        for (k=0; k<maxPlayersPerTeam+2; k++) jTable1.getColumnModel().getColumn(k).setHeaderRenderer(null);
        jTable1.getColumnModel().getColumn(col).setHeaderRenderer(new iconRenderer());
        Rectangle rect = jTable1.getCellRect(0, 0, true);    // make the row visible (scroll)
        jTable1.scrollRectToVisible(rect);
        jTable1.setRowSelectionInterval(0,0);
        repaint();
        sortAscending = true;
        lastSortedCol = col;
        for (i=0; i<MAXTEAMS; i++) {
            sortIndex[i]=(short)(i+1);  // reset sort order
            teamScores[i][2] = 0;       // reset acceleration
        }

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    public class iconRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, 
                Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(colHeader);
            setIcon(sortIcon);
//            setHorizontalAlignment(CENTER);
            return this;
        }
    }

    public class ColumnHeaderListener extends MouseAdapter {
        @Override
      public void mouseClicked(MouseEvent evt) {
        if (maxRound>0) return;     // not allowed to sort after first round!
        JTable table = ((JTableHeader)evt.getSource()).getTable();
        TableColumnModel colModel = table.getColumnModel();

        // The index of the column whose header was clicked
        int vColIndex = colModel.getColumnIndexAtX(evt.getX());

        // Return if not clicked on any column header
        if (vColIndex == -1) {
            return;
        }

        // Determine if mouse was clicked between column heads
        Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
        if (vColIndex == 0) {
            headerRect.width -= 3;    // Hard-coded constant
        } else {
            headerRect.grow(-3, 0);   // Hard-coded constant
        }
        if (!headerRect.contains(evt.getX(), evt.getY())) {
            // Mouse was clicked between column heads
            return;
        }
        table.setColumnSelectionInterval(vColIndex,vColIndex);
        if (vColIndex!=lastSortedCol || !sortAscending) jMenuItem9ActionPerformed(null);
        else jMenuItem10ActionPerformed(null);
      }
}

    private void jDialog4WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog4WindowClosing
// the user is trying to close the window, see if possible to allow
        jButton38ActionPerformed(null);
    }//GEN-LAST:event_jDialog4WindowClosing

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
// signal user action!
        engineRunning = false;
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
// reload database applying the new format fixed/delimited
        processDB();        
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
// when changing tabbedpane clear the output pane
        jTextPane1.setText("");
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
// key typed. I'm interested in <CR> and <DELETE>
        char key=evt.getKeyChar();
        if (key=='\n') {
            evt.consume();
            jButton4ActionPerformed(null);   // CR -> do search
        } else if (key=='\u007f') {
            evt.consume();
            jButton41ActionPerformed(null);  // DELETE -> Reset
        }    
        
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
// Reset player search field
        jTextField3.setText("");
        String[] S=new String[1];
        S[0]="";
        jList3.setListData(S);
        jTextField3.requestFocusInWindow();
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
    // create a plain text from HTML and copy it to clipboard
        jTextPane1.requestFocusInWindow();
        allText=""; tableOpened=false; colTable=0; maxColTable=0; rowTable=0; colSpan=0;
        for (int i=0; i<MAXPLAYERS*2; i++) for (int j=0; j<MAXROUNDS+4; j++) arrTable[i][j]="";
        HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback () {
            @Override
            public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
                if (t==HTML.Tag.TABLE) {tableOpened=true; rowTable=0; colTable=0; maxColTable=0;}
                if (t==HTML.Tag.TR) colSpan=0;
                if (t==HTML.Tag.TD) {
                    try {
                        colSpan=Integer.parseInt(a.getAttribute(HTML.Attribute.COLSPAN).toString());
                    } catch (NullPointerException ex) {colSpan=1;}
                      catch (NumberFormatException ex) {colSpan=1;}
                }
            }
            @Override
            public void handleEndTag(HTML.Tag t, int pos) {
                if (t==HTML.Tag.TABLE) {
                    if (!tableOpened) return;  // in case of nested tables
                    tableOpened=false;
                    int[] lenCol= new int[maxColTable];
                    for (int j=0; j<maxColTable; j++) lenCol[j]=0;
                    // calculate size of cols but skip the header lines
                    for (int i=2; i<rowTable; i++) for (int j=0; j<maxColTable; j++) 
                        if ((arrTable[i][j]).length()>lenCol[j]) lenCol[j]=(arrTable[i][j]).length();
                    for (int j=0; j<maxColTable; j++) 
                        if (lenCol[j]>0 && lenCol[j]<3) lenCol[j]=3;    // set a minimum size for non empty cols
                    for (int i=0; i<rowTable; i++) {                    // scan table line by line
                        String line="";
                        for (int j=0; j<maxColTable; j++) {
                            if (lenCol[j]==0) continue;
                            String s=arrTable[i][j];
                            for (int k=s.length(); k<=lenCol[j]; k++) s=s+" ";
                            line+=s.substring(0,lenCol[j]); 
                            line+=" ";
                        }
                        char c160=(char)160;
                        line=line.replace(""+c160," ");  // &nbsp -> space
                        if (line.trim().length()>0 ) { allText+=line; allText+="\n"; }
                    } 
                    for (int i=0; i<rowTable; i++) for (int j=0; j<maxColTable; j++) arrTable[i][j]=""; // reset
                }
                if (t==HTML.Tag.TR) {rowTable++; colTable=0; colSpan=0; }
                if (t==HTML.Tag.TD) {colTable+=colSpan; if (maxColTable<colTable) maxColTable=colTable;}
            }
            @Override
            public void handleText(char[] data, int pos) {
                if (!tableOpened) { allText+=new String(data); allText+="\n"; }
                else if (rowTable==0 || colSpan<=3) arrTable[rowTable][colTable] = new String(data);
//                else if (colSpan==1 || (rowTable>0 && colSpan<=3)) arrTable[rowTable][colTable] = new String(data);
            }
        };
        Reader reader = new StringReader(jTextPane1.getText());
        try {
            new ParserDelegator().parse(reader, callback, false);
        } catch (IOException ex) {}
        StringSelection contents = new StringSelection(allText);
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(contents, null);             // send text to clipboard
       
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
// exchange colours on table2
        int editingRow = jTable2.getSelectedRow();
        if (editingRow<0) return;   // no row selected?
        if (editingRow>=addedPairs) return;   // do nothing beyond added pairs!
        TableModel myTM2 = jTable2.getModel();
        String team1 = ""+myTM2.getValueAt(editingRow,0);
        String team2 = ""+myTM2.getValueAt(editingRow,1);
        if (team2.equalsIgnoreCase("0| BYE")) return;   // do not exchange BYE!
        myTM2.setValueAt(team2,editingRow,0);
        myTM2.setValueAt(team1,editingRow,1);
        safeExitAllowed = false;
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jTable7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable7KeyPressed
// keystrokes handling for Table7
        int key=evt.getKeyCode();
        int row = jTable7.getSelectedRow();
        if (row==-1) return;
        if (key==java.awt.event.KeyEvent.VK_DOWN || key==java.awt.event.KeyEvent.VK_KP_DOWN) row++;
        else if (key==java.awt.event.KeyEvent.VK_UP || key==java.awt.event.KeyEvent.VK_KP_UP) row--;
        else return;
        evt.consume();
        TableCellEditor t = jTable7.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        try {
            jTable7.setRowSelectionInterval(row,row);
        } catch (IllegalArgumentException ex) { readResults(); jTable5.requestFocusInWindow();  }
    }//GEN-LAST:event_jTable7KeyPressed

    private void jTable5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable5KeyPressed
// keystrokes handling for Table5
        int key=evt.getKeyCode();
        int row = jTable5.getSelectedRow();
        if (row==-1) return;
        if (key==java.awt.event.KeyEvent.VK_DOWN || key==java.awt.event.KeyEvent.VK_KP_DOWN) row++;
        else if (key==java.awt.event.KeyEvent.VK_UP || key==java.awt.event.KeyEvent.VK_KP_UP) row--;
        else return;
        evt.consume();
        prevRow=(short)row;
        try {
            Rectangle rect = jTable5.getCellRect(row, 0, true);    // make the row visible (scroll)
            jTable5.scrollRectToVisible(rect);
            jTable5.setRowSelectionInterval(row,row);
        } catch (IllegalArgumentException ex) { 
            Rectangle rect = jTable5.getCellRect(0, 0, true);    // make the row visible (scroll)
            jTable5.scrollRectToVisible(rect);
            jTable5.setRowSelectionInterval(0,0); 
        }
        java.awt.event.MouseEvent evt2=null;
        jTable5MouseReleased(evt2);
    }//GEN-LAST:event_jTable5KeyPressed

    private void jTable7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable7KeyTyped
// enter result by keyboard for a player of a team (for white player: 0=loss, 1=win, 5=draw)
        char key=evt.getKeyChar();
        evt.consume();
        if (key=='0' || key=='1' || key=='5' || key=='x' || key=='X') ; else return;  // only 0, 1, 5, x, X keys
        TableCellEditor t = jTable7.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        int row = jTable7.getSelectedRow();
        if (row==-1) row=0;
        TableModel myTM7 = jTable7.getModel();
        String player1 = ""+myTM7.getValueAt(row,0);
        String playerScore="";
        String [] s1 = player1.split("\\|");    // get first player 
        if (s1.length==0) return;               // avoid processing blank rows
        if (s1[0].compareTo("")==0) return;     
        if (s1[0].compareTo("null")==0) return;     
        if (key=='0') {
            // loss
            playerScore = "0-1";
        }
        else if (key=='1') {
            // win
            playerScore = "1-0";
        }
        else if (key=='5' || key=='x' || key=='X') {
            // draw
            playerScore = "\u00BD-\u00BD";
        }
        myTM7.setValueAt(playerScore,row,2);
        row++;
        try {
            jTable7.setRowSelectionInterval(row,row);
        } catch (IllegalArgumentException ex) { readResults(); jTable5.requestFocusInWindow(); }
    }//GEN-LAST:event_jTable7KeyTyped

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // send output to the preferred browser
        String os = System.getProperty("os.name").toLowerCase();
        String url="file://"+currentDirectory.getAbsolutePath()+"/temp.html";
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.indexOf( "win" ) >= 0) {
              String[] cmd = new String[4];
              cmd[0] = "cmd.exe";
              cmd[1] = "/C";
              cmd[2] = "start";
              cmd[3] = url;
              rt.exec(cmd);
            } else if (os.indexOf( "mac" ) >= 0) {
                rt.exec( "open " + url);
            } else {
              String[] browsers = {"mozilla", "firefox", "epiphany", "konqueror",
                  "netscape","opera","links","lynx"};
              StringBuffer cmd = new StringBuffer();
              for (int i=0; i<browsers.length; i++)
                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
              rt.exec(new String[] { "sh", "-c", cmd.toString() });
           }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                Main.localizedText("The system failed to invoke your preferred web browser"),
                Main.localizedText("Browser Error"),
                JOptionPane.WARNING_MESSAGE);
        }        
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
// close jDialog4
        if (!engineRunning) jDialog4.dispose();
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
// switch retired player on/off
        int editingRow = jTable6.getSelectedRow();
        if (editingRow<0) return;   // no row selected?
        if (editingRow>=maxPlayersPerTeam) return;   // do nothing beyond last board!
        TableModel myTM6 = jTable6.getModel();
        String player = ""+myTM6.getValueAt(editingRow,0);
        if (player.equals("null")) return;  // empty row?
        if (player.equals("")) return;
        if (player.contains("(W)")) 
            player=player.replace("(W)", "").trim();
        else player=player.trim()+" (W)";
        myTM6.setValueAt(player, editingRow,0);
        
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        jButton37ActionPerformed(evt);        
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
// switch retired team on/off
        editingRow = (short)jTable1.getSelectedRow();
        if (editingRow<0) return;   // no row selected?
        if (editingRow>=addedRows) return;   // do nothing beyond added rows!
        TableModel myTM = jTable1.getModel();
        String team = ""+myTM.getValueAt(editingRow,0);
        if (team.contains("(W)")) 
            team=team.replace("(W)", "").trim();
        else team=team.trim()+" (W)";
        myTM.setValueAt(team, editingRow,0);
        safeExitAllowed = false;
        String xx=Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams();
        jTextField54.setText(xx);
        if (maxPlayersPerTeam>1) jTextField1.requestFocus(); else jTextField3.requestFocus();     
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jTable5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable5KeyTyped
// enter result by keyboard for individual tournaments (for white player: 0=loss, 1=win, 5=draw)
        char key=evt.getKeyChar();
        evt.consume();
        if (maxPlayersPerTeam>1) return;      // only for individual tournaments !
        if (key=='0' || key=='1' || key=='5' || key=='x' || key=='X') ; else return;  // only 0, 1, 5, x, X keys
        jTable7.setVisible(false);
        int row = jTable5.getSelectedRow(), i0, i1;
        if (row==-1) row=0;
        TableModel myTM5 = jTable5.getModel();
        String team1 = ""+myTM5.getValueAt(row,1);
        String team2 = ""+myTM5.getValueAt(row,2);
        String teamScore="";
        String [] s1 = team1.split("\\|");    // get first Team 
        if (s1.length==0) return;             // avoid processing blank rows
        if (s1[0].compareTo("")==0) return;     
        if (s1[0].compareTo("null")==0) return;     
        String [] s2 = team2.split("\\|");    // get second Team 
        if (s2.length==0) return;             // avoid processing blank rows
        if (s2[0].compareTo("")==0) return;     
        if (s2[0].compareTo("null")==0) return;     
        i0 = Integer.valueOf(s1[0]).intValue();  // index to first Team
        i1 = Integer.valueOf(s2[0]).intValue();  // index to second Team
        if (key=='0') {
            // loss
            teamScore = "0-1";
        }
        else if (key=='1') {
            // win
            teamScore = "1-0";
        }
        else if (key=='5' || key=='x' || key=='X') {
            // draw
            teamScore = "\u00BD-\u00BD";
        }
        myTM5.setValueAt(teamScore,row,3);
        roundsDetail[row][0][currRound-1] = ""+i0+"-"+i1+"-"+teamScore;
        roundsDetail[row][1][currRound-1] = "1-1-"+teamScore;
        row++;
        try {
            Rectangle rect = jTable5.getCellRect(row, 0, true);    // make the row visible (scroll)
            jTable5.scrollRectToVisible(rect);
            jTable5.setRowSelectionInterval(row,row);
        } catch (IllegalArgumentException ex) {
            Rectangle rect = jTable5.getCellRect(0, 0, true);    // make the row visible (scroll)
            jTable5.scrollRectToVisible(rect);
            jTable5.setRowSelectionInterval(0,0);
        }
        safeExitAllowed = false;        
    }//GEN-LAST:event_jTable5KeyTyped

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
// create report to Federation
        if (engineRunning) return;
        boolean done=false;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        if (currRound==0) return;
        returnValueFromshowOptionDialog=-1;
        ImageIcon fide = new javax.swing.ImageIcon(getClass().getResource("/javapairing/FIDE.GIF"));
        final JButton FIDE = new javax.swing.JButton();
        FIDE.setToolTipText("FIDE");
        FIDE.setIcon(fide);
        FIDE.addActionListener(new ActionListener() {
           // @Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(FIDE);
                returnValueFromshowOptionDialog=0;
                if (w!=null) w.setVisible(false);
            }
        });
        javax.swing.ImageIcon it = new javax.swing.ImageIcon(getClass().getResource("/javapairing/it.GIF"));
        final JButton IT = new javax.swing.JButton("FSI-Italia");
        IT.setIcon(it);
        IT.setVerticalTextPosition(SwingConstants.BOTTOM);
        IT.setHorizontalTextPosition(SwingConstants.CENTER);
        IT.addActionListener(new ActionListener() {
           // @Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(IT);
                returnValueFromshowOptionDialog=1;
                if (w!=null) w.setVisible(false);
            }
        });
        ImageIcon de = new javax.swing.ImageIcon(getClass().getResource("/javapairing/de.GIF"));
        final JButton DE = new javax.swing.JButton("DSB-Deutschland");
        DE.setIcon(de);
        DE.setVerticalTextPosition(SwingConstants.BOTTOM);
        DE.setHorizontalTextPosition(SwingConstants.CENTER);
        DE.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent evt) {
                Window w = SwingUtilities.getWindowAncestor(DE);
                returnValueFromshowOptionDialog=2;
                if (w!=null) w.setVisible(false);
            }
        });
        Object[] options = {FIDE, IT, DE, Main.localizedText("other")};
//        Object[] options = {"FIDE", "FSI-Italia", "Deutscher Schachbund", Main.localizedText("other")};
        int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, select one report format"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (n==-1) n=returnValueFromshowOptionDialog;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        calculatePlayerScore((short)0);
        switch (n) {
            case 0:
                    reportToFIDE();
                    done=true;
                    break;
            case 1:
                    reportToFSI();
                    done=true;
                    break;
            case 2:
                    reportToDWZ();
                    done=true;
                    break;
            case 3:
                    /* ... Other Federation ... to do ... */
                    Object[] options3 = {"OK"};
                    int n3 = JOptionPane.showOptionDialog(this,
                        "<html>... Other Federations in progress ... Hope you may help!!<br>Send specifications</html>",
                        "Info",
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options3,
                        options3[0]);
                    break;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (done) {
            Object[] options2 = {Main.localizedText("OK")};
            int n2 = JOptionPane.showOptionDialog(this,
                Main.localizedText("Done. You'll find the Report to FED in the same folder and with the same name of the tournament file."),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options2,
                options2[0]);
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void reportToFIDE() {
        /* create standard report to FIDE */
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, rank, nRated, score1, score2;
        String s, team, sex, roundsString, roundString=""; String [] S;
        String title, Elo, birth,name, FED, ID;
        boolean paired, forfeit, bye;
        String ReportFile = selectedFile.getPath();     // get tournament path and file name 
        if (!ReportFile.endsWith(".txt"))  ReportFile+=".txt";   // check if file extension exists
        ReportFile = ReportFile.replace(".txt"," - "+Main.localizedText("Report to FIDE")+".txt");  // use the same name of the tournament file
        try {
          fOut = new FileWriter(ReportFile);  // create the destination file
          String lineSeparator = System.getProperty( "line.separator" );
          // tournament data section
          fOut.write("012 "+tournamentName+lineSeparator);
          fOut.write("022 "+tournamentPlace+lineSeparator);
          fOut.write("032 "+fed+lineSeparator);
          fOut.write("042 "+tournamentDate1+lineSeparator);
          fOut.write("052 "+tournamentDate2+lineSeparator);
          fOut.write("062 "+String.format("%4d",addedRows*maxPlayersPerTeam)+lineSeparator);
          nRated=0;
          for (i0=1; i0<=addedRows; i0++) {
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) {
                    continue;
                }
                S = s.split(";");
                try {
                    if (Integer.valueOf(S[6])>0) nRated++;
                } catch (NumberFormatException ex) {}
                  catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
              }
          }
          fOut.write("072 "+String.format("%4d",nRated)+lineSeparator);
          if (maxPlayersPerTeam==1) fOut.write("082 "+"   0"+lineSeparator);
              else fOut.write("082 "+String.format("%4d",addedRows)+lineSeparator);
          fOut.write("092 "+pairingSystems[tournamentType]+lineSeparator);
          fOut.write("102 "+tournamentArbiter+lineSeparator);
          fOut.write("112 "+lineSeparator);
          fOut.write("122 moves/time, increment"+lineSeparator);
          // players data section
          for (i0=1; i0<=addedRows; i0++) {
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                S = s.split(";");
                fOut.write("001 "+String.format("%4d", p0));
                sex="";
                try {
                    sex = (""+S[3]).toLowerCase();
                    if (sex.equalsIgnoreCase("F")) sex="w";
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (sex.equals("")) sex=" ";
                fOut.write(" ");
                fOut.write(sex);
                title="";
                try {
                    title = ""+S[4];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (title.equals("")) title="   ";
                fOut.write(String.format("%-3.3s",title));    // title
                fOut.write(" ");
                name="";
                try {
                    name = ""+S[0];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (name.equals("")) name="   ";
                name=name.replace("(W)", "");
                fOut.write(String.format("%-33.33s",name)); // name
                fOut.write(" ");
                Elo="";
                try {
                    Elo = ""+S[6];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (Elo.equals("")) Elo="   0";
                fOut.write(String.format("%4.4s",Elo));    // Elo
                fOut.write(" ");
                FED="";
                try {
                    FED = ""+S[1];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (FED.equals("")) FED="   ";
                fOut.write(String.format("%-3.3s",FED));    // FED
                fOut.write(" ");
                ID="";
                try {
                    ID = ""+S[5];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="   ";
                fOut.write(String.format("%-11.11s",ID));  // ID
                fOut.write(" ");
                birth="";
                try {
                    birth = ""+S[2];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (birth.equals("")) birth="   ";
                fOut.write(String.format("%-10.10s",birth));  // birth ... to be formatted? Is it required?
                fOut.write(" ");
                rank=indexAt(p0-1, tempIndex)+1;
                score1=playerScore[p0-1];
                if (score1<0) score1=0;
                fOut.write(String.format(new Locale("en"),"%4.1f",0.1f*score1));    // points
                fOut.write(" ");
                fOut.write(String.format("%4d",rank));    // rank
                roundsString="";
                for (round=0; round<maxRound; round++) {
                    paired=forfeit=bye=false;
                    roundString="";
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            p2 = Integer.valueOf(S[1]);
                            score1=integerScore(S[2]);
                            if (S[3].indexOf('f')>0) {
                                S[3]=S[3].substring(0,1); // forfeit ?
                                if (i0==i1) p2 = 0;
                                else p1 = 0;
                            }
                            score2=integerScore(S[3]);
                            if (p1>0) p1 += (i1-1)*maxPlayersPerTeam;
                            if (p2>0) p2 += (i2-1)*maxPlayersPerTeam;
                            if (i0==i1 && p0==p1) {
                                paired=true;
                                if (p2==0) {roundString = "0000 - ";
                                    if (score1==10) roundString += "+"; 
                                    if (score1==5) roundString  += "=";
                                    if (score1==0) roundString  += "-"; 
                                    bye=true; break;
                                }
                                if (S[3].indexOf('f')>0) forfeit=true; 
                                roundString = String.format("%4d",p2);
                                if (board%2==1) roundString += " w ";
                                else roundString += " b ";
                                if (score1==10) {if (forfeit) roundString += "+"; else roundString += "1";}
                                if (score1==5) roundString += "=";
                                if (score1==0) {if (forfeit) roundString += "-"; else roundString += "0";}
                                break;
                            } else if (i0==i2 && p0==p2) {
                                paired=true;
                                if (p1==0) {roundString = "0000 - ";
                                    if (score2==10) roundString += "+"; 
                                    if (score2==5) roundString  += "=";
                                    if (score2==0) roundString  += "-"; 
                                    bye=true; break;
                                }
                                if (S[3].indexOf('f')>0) forfeit=true; 
                                roundString = String.format("%4d",p1);
                                if (board%2==0) roundString += " w ";
                                else roundString += " b ";
                                if (score2==10) {if (forfeit) roundString += "+"; else roundString += "1";}
                                if (score2==5) roundString += "=";
                                if (score2==0) {if (forfeit) roundString += "-"; else roundString += "0";}
                                break;
                            }
                        }
                        if (paired) break;
                    }
                    if (!paired) roundString="     -  ";
                    roundsString = roundsString+"  "+roundString;
                }
                fOut.write(roundsString); 
                fOut.write(lineSeparator);
              }
          }
          // teams data section
          if (maxPlayersPerTeam>1)
          for (i0=1; i0<=addedRows; i0++) {
              fOut.write("013 ");
              team=""+myTM.getValueAt(sortIndex[i0-1]-1,0);
              team=team.replace("(W)", "");
              fOut.write(String.format("%-32.32s",team)); // name
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) {
                    continue;
                }
                fOut.write(" ");
                fOut.write(String.format("%-4d", p0));
              }
              fOut.write(lineSeparator);
          }
          fOut.close();
        } catch (IOException ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
        }       
        
    }
    
    private void reportToFSI() {
        /* create standard report to FSI (Italian Chess Federation) */
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, score, score1, score2, rank, scoreTeam1, scoreTeam2;
        String s, team, sex, roundsString, roundString=""; String [] S;
        String title, Elo, birth,name, FED, ID, K;
        boolean played;
        int eloIni, eloOpp, kCoeff, totScore, pa, difElo, pFIDE, totScoreFIDE, totEloFIDE;
        double patt, dp;
        String ReportFile = selectedFile.getPath();    // get tournament path and file name
        if (!ReportFile.endsWith(".txt"))  ReportFile+=".txt";   // check if file extension exists
        ReportFile = ReportFile.replace(".txt"," - "+Main.localizedText("Report to FED")+".elo");  // use the same name of the tournament file
        try {
          fOut = new FileWriter(ReportFile);  // create the destination file
          String lineSeparator = System.getProperty( "line.separator" );
          // tournament data section
          fOut.write(tournamentName+lineSeparator);
          fOut.write(tournamentPlace+lineSeparator);
          if (tournamentDate2.equals("")) tournamentDate2=tournamentDate1;
          fOut.write(String.format("%-8.8s",tournamentDate1.replaceAll("\\D",""))+","+
                     String.format("%-8.8s",tournamentDate2.replaceAll("\\D",""))+lineSeparator);
          fOut.write(tournamentArbiter+lineSeparator);
          fOut.write(""+maxRound+lineSeparator);
          fOut.write(""+(addedRows*maxPlayersPerTeam)+lineSeparator);
          // players data section
          for (i0=1; i0<=addedRows; i0++) {
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) {
                    fOut.write("0000000,       0,"+lineSeparator);
                    continue;
                }
                S = s.split(";");
                ID="";
                try {
                    ID = ""+S[7];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="0000000";
                fOut.write(String.format("%7.7s",ID));  // ID NAT
                fOut.write(",");
                ID="";
                try {
                    ID = ""+S[5];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="0";
                fOut.write(String.format("%8.8s",ID));  // ID FIDE
                fOut.write(",");
                name="";
                try {
                    name = (""+S[0]).replaceAll(",","");
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (name.equals("")) name="   ";
                name=name.replace("(W)", "");
                fOut.write(String.format("%-25.25s",name)); // name
                fOut.write(",");
                title="";
                try {
                    title = ""+S[4];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (title.equals("")) title="   ";
                fOut.write(String.format("%-3.3s",title));    // title
                fOut.write(",");
                FED="";
                try {
                    FED = ""+S[1];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (FED.equals("")) FED="   ";
                fOut.write(String.format("%-3.3s",FED));    // FED
                fOut.write(",");
                birth="";
                try {
                    birth = ""+S[2];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (birth.equals("")) birth="   ";
                fOut.write(String.format("%-8.8s",birth.replaceAll("\\D","")));  // birth
                fOut.write(",");
                Elo="";
                try {
                    Elo = ""+S[8];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (Elo.equals("")) Elo="0";
                fOut.write(String.format("%4.4s",Elo));    // Elo Italia
                fOut.write(",");
                K="";
                try {
                    K = ""+S[9];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (K.equals("")) K="  ";
                fOut.write(String.format("%2.2s",K));    // K
                fOut.write(",");
                Elo="";
                try {
                    Elo = ""+S[6];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (Elo.equals("")) Elo="0";
                fOut.write(String.format("%4.4s",Elo));    // Elo FIDE
                fOut.write(",");
                s = minimalPlayerData(s);
                S=s.split(";");
                eloIni=0; kCoeff=0;
                try {
                    eloIni=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                    kCoeff=Integer.parseInt(S[4].trim());
                } catch (NumberFormatException ex) {}
                  catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                totScore = 0; pa = 0;
                pFIDE = 0; totEloFIDE = 0; totScoreFIDE = 0;
                roundsString="";
                for (round=0; round<maxRound; round++) {
                    played=false;
                    roundString="";
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            if (p1==0) continue;        // empty row?
                            p2 = Integer.valueOf(S[1]);
                            score1=integerScore(S[2]);
                            if (S[3].indexOf('f')>0) {
                                S[3]=S[3].substring(0,1); // forfeit ?
                                if (i0==i1) p2 = 0;
                                else p1 = 0;
                            }
                            score2=integerScore(S[3]);
                            if (p1>0) p1 += (i1-1)*maxPlayersPerTeam;
                            if (p2>0 && i2>0) p2 += (i2-1)*maxPlayersPerTeam;
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=10;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=10;}
                            else if (score1!=0) {scoreTeam1=5;scoreTeam2=5;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>maxBoards*5) scoreTeam1=10;
                            if (score2>maxBoards*5) scoreTeam2=10;
                            if (i0==i1 && p0==p1) {
                                score=scoreTeam1;
                                if (i2==0 || p2==0) roundString = "0000-";
                                else {
                                    roundString = String.format("1%03d",p2);
                                    if (board%2==1) roundString+="b";
                                    else roundString+="n";
                                    p2 = Integer.valueOf(S[1]);
                                    s=""+myTM.getValueAt(sortIndex[i2-1]-1,p2+1);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[6].trim());
                                        if (eloOpp>0) {
                                            pFIDE++;
                                            totEloFIDE += eloOpp;
                                            totScoreFIDE += score;
                                        }
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                    s = minimalPlayerData(s);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                                        pa+=calculatePA(eloIni, eloOpp);
                                        totScore += score;
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                }
                                if (score==10) roundString = roundString+"1";
                                if (score==5) roundString = roundString+"3";
                                if (score==0) roundString = roundString+"2";
                                played=true;
                            } else if (i0==i2 && p0==p2) {
                                score=scoreTeam2;
                                if (i1==0 || p1==0) roundString = "0000-";
                                else {
                                    roundString = String.format("1%03d",p1);
                                    if (board%2==0) roundString+="b";
                                    else roundString+="n";
                                    p1 = Integer.valueOf(S[0]);
                                    s=""+myTM.getValueAt(sortIndex[i1-1]-1,p1+1);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[6].trim());
                                        if (eloOpp>0) {
                                            pFIDE++;
                                            totEloFIDE += eloOpp;
                                            totScoreFIDE += score;
                                        }
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                    s = minimalPlayerData(s);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                                        pa+=calculatePA(eloIni, eloOpp);
                                        totScore += score;
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                }
                                if (score==10) roundString = roundString+"1";
                                if (score==5) roundString = roundString+"3";
                                if (score==0) roundString = roundString+"2";
                                played=true;
                            }
                        }
                    }
                    if (!played) roundString="0000-2";
                    roundsString = roundsString+roundString;
                }
                patt=roundPA(pa);
                dp=(totScore*10-patt)/100.0;
                difElo=(int)Math.round(dp*kCoeff);
                fOut.write(String.format("%4d",difElo));    // Elo Italia variation
                fOut.write(",");
                fOut.write(String.format("%2d",pFIDE));    // games valid FIDE
                fOut.write(",");
                if (pFIDE>0) {
                    fOut.write(String.format("%4d",(int)Math.floor(1.0*totEloFIDE/pFIDE))); // mean Elo FIDE
                    fOut.write(",");
                    fOut.write(String.format(new Locale("en"),"%4.1f",0.1f*totScoreFIDE));   // score FIDE
                    fOut.write(",");
                } else fOut.write("   0, 0.0,");
                rank=indexAt(p0-1, tempIndex)+1;
                fOut.write(String.format("%4d",rank));    // rank
                fOut.write(",");
                fOut.write(roundsString); 
                fOut.write(lineSeparator);
              }
          }
          fOut.close();
        } catch (IOException ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
        }       
        
    }

        private void reportToDWZ() {
        /* create standard report to German Chess Federation */
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, score, score1, score2, rank, nRated, scoreTeam1, scoreTeam2;
        String s, team, sex, roundsString, roundString=""; String [] S;
        String title, Elo, birth, name, FED, ID;
        boolean played;
        String ReportFile = selectedFile.getPath();     // get tournament path and file name 
        if (!ReportFile.endsWith(".txt"))  ReportFile+=".txt";   // check if file extension exists
        ReportFile = ReportFile.replace(".txt"," - "+Main.localizedText("Report to FED")+".txt");  // use the same name of the tournament file
        try {
          fOut = new FileWriter(ReportFile);  // create the destination file
          String lineSeparator = System.getProperty( "line.separator" );
          // tournament data section
          fOut.write(tournamentName+lineSeparator);
          fOut.write(tournamentPlace+" "+fed+"  "+tournamentDate1+" / "+tournamentDate2+lineSeparator);
          if (maxPlayersPerTeam==1) fOut.write(" ");
          else fOut.write("M");                         // team
          if (tournamentType==0 || tournamentType==1 || tournamentType==2 || tournamentType==3) fOut.write("S");   // swiss
          else fOut.write(" ");
          fOut.write(String.format("%4d", addedRows*maxPlayersPerTeam));    // # of players
          fOut.write(String.format("%3d", maxRound));                       // # of rounds played
          fOut.write(String.format("%3d", tournamentPairing));              // # regular or return
          fOut.write(String.format("%6.6s","#1.6#")+lineSeparator);         // rev. # of the program
          fOut.write(" ttt. rrr nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv lll ffffffffff pppppppppp gggggggg eeee dddd  zzzzz mmmm  "+lineSeparator);

          // players data section
          for (i0=1; i0<=addedRows; i0++) {
              team = ""+myTM.getValueAt(sortIndex[i0-1]-1,0);    // the team
              if (maxPlayersPerTeam==1) team = "   ";
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s = ""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);    // the player
                fOut.write(String.format("%4d", p0)+"."+String.format("%4d", p0)+" ");
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) {
                    fOut.write(lineSeparator);
                    continue;
                }
                S = s.split(";");
                name="";
                try {
                    name = ""+S[0];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (name.equals("")) name="   ";
                name=name.replace("(W)", "");
                fOut.write(String.format("%-32.32s",name)); // player name
                fOut.write(" ");
                fOut.write(String.format("%-32.32s",team)); // team 
                fOut.write(" ");
                FED="";
                try {
                    FED = ""+S[1];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (FED.equals("")) FED="   ";
                fOut.write(String.format("%-3.3s",FED));    // FED
                fOut.write(" ");
                ID="";
                try {
                    ID = ""+S[5];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="   ";
                fOut.write(String.format("%10.10s",ID));  // ID FIDE
                fOut.write("            ");
                birth="";
                try {
                    birth = ""+S[2];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (birth.equals("")) birth="   ";
                fOut.write(String.format("%8.8s",birth));  // birth ... to be formatted? Is it required?
                fOut.write(" ");
                Elo="";
                try {
                    Elo = ""+S[6];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (Elo.equals("")) Elo="    ";
                fOut.write(String.format("%4.4s",Elo));    // Elo
                fOut.write(" ");
                Elo="";
                try {
                    Elo = ""+S[8];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (Elo.equals("")) Elo="0";
                fOut.write(String.format("%4.4s",Elo));    // Elo NAT.
                fOut.write("  ");
                ID="";
                try {
                    ID = ""+S[7];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="    0|   0";
                S = ID.split("\\|");
                ID = S[0];
                fOut.write(String.format("%5.5s",ID));  // club ID 
                fOut.write(" ");
                ID="";
                try {
                    ID = ""+S[1];
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (ID.equals("")) ID="   0";
                fOut.write(String.format("%4.4s",ID));  // club serial # 
                fOut.write("  ");

                roundsString="";
                for (round=0; round<maxRound; round++) {
                    played=false;
                    roundString="";
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            if (p1==0) continue;        // empty row?
                            p2 = Integer.valueOf(S[1]);
                            score1=integerScore(S[2]);
                            if (S[3].indexOf('f')>0) {
                                S[3]=S[3].substring(0,1); // forfeit ?
                                if (i0==i1) p2 = 0;
                                else p1 = 0;
                            }
                            score2=integerScore(S[3]);
                            if (p1>0) p1 += (i1-1)*maxPlayersPerTeam;
                            if (p2>0 && i2>0) p2 += (i2-1)*maxPlayersPerTeam;
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=10;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=10;}
                            else if (score1!=0) {scoreTeam1=5;scoreTeam2=5;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>maxBoards*5) scoreTeam1=10;
                            if (score2>maxBoards*5) scoreTeam2=10;
                            if (i0==i1 && p0==p1) {
                                if (scoreTeam1==10) roundString = "1";
                                if (scoreTeam1==5) roundString  = "R";
                                if (scoreTeam1==0) roundString  = "0";
                                if (i2==0 || p2==0) {
                                    if (scoreTeam1==10) roundString = "+:  0";
                                    if (scoreTeam1==5) roundString  = "R:  0";
                                    if (scoreTeam1==0) roundString  = "-:  0";
                                } else if (board%2==1) roundString += "W"+String.format("%3d",p2);
                                else roundString += "B"+String.format("%3d",p2);
                                played=true;
                            } else if (i0==i2 && p0==p2) {
                                if (scoreTeam2==10) roundString = "1";
                                if (scoreTeam2==5) roundString  = "R";
                                if (scoreTeam2==0) roundString  = "0";
                                if (i1==0 || p1==0) {
                                    if (scoreTeam2==10) roundString = "+:  0";
                                    if (scoreTeam2==5) roundString  = "R:  0";
                                    if (scoreTeam2==0) roundString  = "-:  0";
                                } else if (board%2==0) roundString += "W"+String.format("%3d",p1);
                                else roundString += "B"+String.format("%3d",p1);
                                played=true;
                            }
                        }
                    }
                    if (!played) roundString=" :  0";
                    roundsString += roundString+" ";
                }
                fOut.write(roundsString); 
                fOut.write(lineSeparator);
              }
          }
          fOut.write("###"+lineSeparator);
          
          fOut.write("Name:       ");
          fOut.write(String.format("%-60.60s",tournamentName)+lineSeparator);
          fOut.write("Ort:        ");
          fOut.write(String.format("%-60.60s",tournamentPlace)+lineSeparator);
          fOut.write("FIDE-Land:  ");
          fOut.write(String.format("%-40.40s", fed)+lineSeparator);   // Federation
          fOut.write("Datum(S):   ");
          fOut.write(String.format("%-20.20s",tournamentDate1));
          fOut.write("Datum(E):   ");
          fOut.write(String.format("%-20.20s",tournamentDate2)+lineSeparator);
          fOut.write("Züge(1):     ");
          fOut.write("                    ");
          fOut.write("Züge(2):     ");
          fOut.write("                    ");
          fOut.write("Züge(3):     ");
          fOut.write("                    "+lineSeparator);
          fOut.write("Hauptschiedsrichter:");
          fOut.write(String.format("%-60.60s",tournamentArbiter)+lineSeparator);
          fOut.write("Weitere Schiedsrichter:");
          fOut.write(String.format("%-60.60s"," ")+lineSeparator);
          fOut.write("Anwender:        ");
          fOut.write(String.format("%-60.60s"," ")+lineSeparator);
          fOut.write("Ser.Nummer:      ");
          fOut.write(String.format("%-60.60s"," ")+lineSeparator);
          fOut.close();
        } catch (IOException ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
        }       
        
    }
    
    private void copyHTML(FileWriter fOut) {
        String s; boolean copy=false;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        try {
            // open input file 
            BufferedReader in = new BufferedReader(new FileReader(tempFile));
            String lineSeparator = System.getProperty( "line.separator" );
            // copy data
            for (;;) {
                if (!in.ready()) throw new IOException();
                s = in.readLine();
                if (s.contains("<body>")) {copy = true; continue;}  // point to start copy
                if (s.contains("</body>")) {break;}                 // point to stop copy
                if (copy) {fOut.write(s); fOut.write(lineSeparator);}
            }
            fOut.write("<br>"); fOut.write(lineSeparator);
            in.close();
        } catch (IOException ex) {
            
        }        
    }
    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
// create web site for the tournament
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        FileWriter fOut;
        String HTMLFile = selectedFile.getPath();
        if (!HTMLFile.endsWith(".txt"))  HTMLFile+=".txt";   // check if file extension exists
        HTMLFile = HTMLFile.replace(".txt",".html");  // use the same name of the tournament file
        try {
          this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          fOut = new FileWriter(HTMLFile);  // create the destination file
          String lineSeparator = System.getProperty( "line.separator" );
          fOut.write("<html>"+lineSeparator);
          fOut.write("<head>"+lineSeparator);
          fOut.write("<font Size=\"+1\">"+Main.localizedText("Tournament")+": "+tournamentName+"<br>"+lineSeparator);
          fOut.write(tournamentPlace+", "+fed+"; "+tournamentDate1+" - "+tournamentDate2+lineSeparator);
          fOut.write("</head><body><br>"+lineSeparator);
          jButton12ActionPerformed(evt);  // Teams & Players
          copyHTML(fOut);
          fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          jButton32ActionPerformed(evt);  // Cross tables
          copyHTML(fOut);
          fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          if (maxPlayersPerTeam>1) {
                jButton9ActionPerformed(evt);   // Team ranking
                copyHTML(fOut);
                fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          }
          jButton19ActionPerformed(evt);  // Player ranking
          copyHTML(fOut);
          fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          jButton33ActionPerformed(evt);  // Elo variation
          copyHTML(fOut);
          fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          jButton44ActionPerformed(evt);  // Player Cards
          copyHTML(fOut);
          fOut.write("<font Size=\"+1\">Powered by JavaPairing</font>"+lineSeparator);
          fOut.write("</body></html>"+lineSeparator);
          fOut.close();
          
          java.net.URL url=null;
          try {
                url = new java.net.URL("file:///"+HTMLFile);
          } catch (MalformedURLException ex) {
//                
          }
          if (url != null) 
          try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
          } catch (IOException ex) {}
            catch (NullPointerException ex) {}
          this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          Object[] options = {Main.localizedText("OK")};
          int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Done. You'll find the web page in the same folder and with the same name of the tournament file."),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        } catch (IOException ex) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
        }       
        
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
// read optional ranking parameters and close jDialog3
        if (jCheckBox3.isSelected()) rankingByAge=true; else rankingByAge=false;
        if (jCheckBox4.isSelected()) rankingByCategory=true; else rankingByCategory=false;
        if (jCheckBox5.isSelected()) rankingByELO=true; else rankingByELO=false;
        if (rankingByAge) {
            group[0]=jTextField14.getText().trim();
            groupLimit[0]=jTextField15.getText().trim()+";"+jTextField16.getText().trim();
            group[1]=jTextField17.getText().trim();
            groupLimit[1]=jTextField18.getText().trim()+";"+jTextField19.getText().trim();
            group[2]=jTextField20.getText().trim();
            groupLimit[2]=jTextField21.getText().trim()+";"+jTextField22.getText().trim();
            group[3]=jTextField23.getText().trim();
            groupLimit[3]=jTextField24.getText().trim()+";"+jTextField25.getText().trim();
            group[4]=jTextField26.getText().trim();
            groupLimit[4]=jTextField27.getText().trim()+";"+jTextField28.getText().trim();
        }
        else if (rankingByCategory) {
            group[0]=jTextField29.getText().trim();
            groupLimit[0]=jTextField30.getText().trim().replace(" ",";");
            group[1]=jTextField32.getText().trim();
            groupLimit[1]=jTextField33.getText().trim().replace(" ",";");
            group[2]=jTextField35.getText().trim();
            groupLimit[2]=jTextField36.getText().trim().replace(" ",";");
            group[3]=jTextField38.getText().trim();
            groupLimit[3]=jTextField39.getText().trim().replace(" ",";");
            group[4]=jTextField41.getText().trim();
            groupLimit[4]=jTextField42.getText().trim().replace(" ",";");
        }
        else if (rankingByELO) {
            group[0]=jTextField31.getText().trim();
            groupLimit[0]=jTextField34.getText().trim()+";"+jTextField37.getText().trim();
            group[1]=jTextField40.getText().trim();
            groupLimit[1]=jTextField43.getText().trim()+";"+jTextField44.getText().trim();
            group[2]=jTextField45.getText().trim();
            groupLimit[2]=jTextField46.getText().trim()+";"+jTextField47.getText().trim();
            group[3]=jTextField48.getText().trim();
            groupLimit[3]=jTextField49.getText().trim()+";"+jTextField50.getText().trim();
            group[4]=jTextField51.getText().trim();
            groupLimit[4]=jTextField52.getText().trim()+";"+jTextField53.getText().trim();
        }
        jDialog3.dispose();
    }//GEN-LAST:event_jButton34ActionPerformed

    private int calculatePA(int eloIni, int eloOpp) {
        int pa=0, difelo=0;
        if(eloOpp>=eloIni)
            difelo=eloOpp-eloIni;
        else
            difelo=eloIni-eloOpp;
        if (difelo<=3) {pa=50;}
        if (difelo>=4 && difelo<=10) {pa=49;}
        if (difelo>=11 && difelo<=17) {pa=48;}
        if (difelo>=18 && difelo<=25) {pa=47;}
        if (difelo>=26 && difelo<=32) {pa=46;}
        if (difelo>=33 && difelo<=39) {pa=45;}
        if (difelo>=40 && difelo<=46) {pa=44;}
        if (difelo>=47 && difelo<=53) {pa=43;}
        if (difelo>=54 && difelo<=61) {pa=42;}
        if (difelo>=62 && difelo<=68) {pa=41;}
        if (difelo>=69 && difelo<=76) {pa=40;}
        if (difelo>=77 && difelo<=83) {pa=39;}
        if (difelo>=84 && difelo<=91) {pa=38;}
        if (difelo>=92 && difelo<=98) {pa=37;}
        if (difelo>=99 && difelo<=106) {pa=36;}
        if (difelo>=107 && difelo<=113) {pa=35;}
        if (difelo>=114 && difelo<=121) {pa=34;}
        if (difelo>=122 && difelo<=129) {pa=33;}
        if (difelo>=130 && difelo<=137) {pa=32;}
        if (difelo>=138 && difelo<=145) {pa=31;}
        if (difelo>=146 && difelo<=153) {pa=30;}
        if (difelo>=154 && difelo<=162) {pa=29;}
        if (difelo>=163 && difelo<=170) {pa=28;}
        if (difelo>=171 && difelo<=179) {pa=27;}
        if (difelo>=180 && difelo<=188) {pa=26;}
        if (difelo>=189 && difelo<=197) {pa=25;}
        if (difelo>=198 && difelo<=206) {pa=24;}
        if (difelo>=207 && difelo<=215) {pa=23;}
        if (difelo>=216 && difelo<=225) {pa=22;}
        if (difelo>=226 && difelo<=235) {pa=21;}
        if (difelo>=236 && difelo<=245) {pa=20;}
        if (difelo>=246 && difelo<=256) {pa=19;}
        if (difelo>=257 && difelo<=267) {pa=18;}
        if (difelo>=268 && difelo<=278) {pa=17;}
        if (difelo>=279 && difelo<=290) {pa=16;}
        if (difelo>=291 && difelo<=302) {pa=15;}
        if (difelo>=303 && difelo<=315) {pa=14;}
        if (difelo>=316 && difelo<=328) {pa=13;}
        if (difelo>=329 && difelo<=344) {pa=12;}
        if (difelo>=345 && difelo<=357) {pa=11;}
        if (difelo>=358 && difelo<=374) {pa=10;}
        if (difelo>=375 && difelo<=391) {pa=9;}
        if (difelo>=392) {pa=8;} 
        if(eloOpp<eloIni) {pa=100-pa;}
        return pa;
}
        
    private int roundPA(int number) {
//        int lastDigit = number%10;
//        number-=lastDigit;
//        if (lastDigit>5) number+=10; // last digit 6...9 round up
        return number;
}
    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
// output Elo variation
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, score1, score2, score;
        String s, s1, s2, s3, team; String [] S;
        boolean played, isDoubtTrancheFIDE=false;
        int eloIni, eloOpp, kCoeff, kOpp, difElo, pa, totScore, totScoreFIDE, nFIDE, totEloOpp;
        int meanElo, percentScore, performance, difElo1, difElo2, difElo3;
        double patt, dp;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        try {
          fOut = new FileWriter(tempFile);  // create a temp file
          String lineSeparator = System.getProperty( "line.separator" );
          fOut.write("<html>"+lineSeparator);
          fOut.write("<head>"+lineSeparator);
          fOut.write("<font Size=\"+2\">"+Main.localizedText("Tournament")+": "+tournamentName+lineSeparator);
          fOut.write("</head><body>"+lineSeparator);
          fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Elo variation for Players after Round")+": "+currRound+lineSeparator);
          fOut.write("<br>("+Main.localizedText("not official!")+")"+lineSeparator);
          if (currRound==maxRound && missingResults) fOut.write("<br>("+Main.localizedText("Some results missing!")+")"+lineSeparator);
          fOut.write("<br><table>"+lineSeparator);
          fOut.write("<tr><td bgColor=\"yellow\"><font Size=\"+1\">ID</td><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Player")+"</td>");
          fOut.write("<td align=\"center\" bgColor=\"yellow\"><font Size=\"+1\">&nbsp;&nbsp;"+Main.localizedText("Elo")+"&nbsp;&nbsp;</td><td align=\"center\" bgColor=\"yellow\"><font Size=\"+1\">&nbsp;&nbsp;"+Main.localizedText("K")+"&nbsp;&nbsp;</td>");
          fOut.write("<td align=\"center\" bgColor=\"yellow\"><font Size=\"+1\">&nbsp;&nbsp;"+Main.localizedText("Elo var")+"&nbsp;&nbsp;</td>"+"<td align=\"center\" bgColor=\"yellow\"><font Size=\"+1\">&nbsp;&nbsp;"+Main.localizedText("new Elo")+"&nbsp;&nbsp;</td>");
          fOut.write("<td align=\"center\" bgColor=\"yellow\"><font Size=\"+1\">&nbsp;&nbsp;"+Main.localizedText("tranche FIDE(games)")+"&nbsp;&nbsp;</td></tr>"+lineSeparator);
          for (i0=1; i0<=addedRows; i0++) {
              fOut.write("<tr><td colspan=\"7\"><hr /></td></tr>"+lineSeparator);
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                s = minimalPlayerData(s);
                S=s.split(";");
                fOut.write("<tr><td bgColor=\"silver\" align=\"center\">"+p0+"</td>");
                eloIni=kCoeff=0;
                try {
                    fOut.write("<td>"+S[0]+"</td>");
                    fOut.write("<td align=\"center\">"+S[3]+"</td>");
                    eloIni=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                    kCoeff=Integer.parseInt(S[4].trim());
                } catch (NumberFormatException ex) {}
                  catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                if (eloIni==0) {fOut.write("</tr>"+lineSeparator); continue;}
                totScore=totScoreFIDE=nFIDE=totEloOpp=pa=0;
                for (round=0; round<currRound; round++) {
                    played=false; eloOpp = 0; score = 0; kOpp=0;
                    for (j=0; j<addedPairs; j++) {
                        if (played) break;
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            if (p1==0) continue;        // empty row?
                            p2 = Integer.valueOf(S[1]);
                            score1=integerScore(S[2]);
                            if (S[3].indexOf('f')>0) continue; // forfeit ?
                            score2=integerScore(S[3]);
                            if (i0==i1 && k==p1) {
                                if (i2==0 || p2==0) continue; //"BYE"
                                else {
                                    score=score1;
                                    s=""+myTM.getValueAt(sortIndex[i2-1]-1,p2+1);
                                    if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                                    s = minimalPlayerData(s);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                                        kOpp=Integer.parseInt(S[4].trim());
                                        played=true;
                                        break;
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                }
                            } else if (i0==i2 && k==p2) {
                                if (i1==0 || p1==0) continue; //"BYE"
                                else {
                                    score=score2;
                                    s=""+myTM.getValueAt(sortIndex[i1-1]-1,p1+1);
                                    if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                                    s = minimalPlayerData(s);
                                    try {
                                        S=s.split(";");
                                        eloOpp=Integer.parseInt(S[3].replaceAll("\\D",""));  // strip non digit and convert to integer
                                        kOpp=Integer.parseInt(S[4].trim());
                                        played=true;
                                        break;
                                    } catch (NumberFormatException ex) {}
                                      catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                }
                            }
                        }
                    }
                    if (played && eloOpp>0) {
                        if (kCoeff>0) {
                            pa+=calculatePA(eloIni, eloOpp);    // for National Elo var
                            totScore += score;
                            if (kOpp==0) {nFIDE++; totScoreFIDE+=score; totEloOpp+=eloOpp;}   // for tranche FIDE
                        }
                        else if (kOpp==0) {
                            pa+=calculatePA(eloIni, eloOpp);    // for FIDE Elo var
                            totScore += score;
                        }
                    }
                }
                patt=roundPA(pa);
                dp=(totScore*10-patt)/100.0;
                if (kCoeff>0) {
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;"+kCoeff+"&nbsp;&nbsp;</td>");
                    difElo=(int)Math.round(dp*kCoeff);
                    s=String.format("%+d",difElo);      // convert to string with sign
                    if (s.equalsIgnoreCase("+0")) s="&nbsp;0";  // don't like '+0' 
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;"+s+"&nbsp;&nbsp;</td>");
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;"+(eloIni+difElo)+"&nbsp;&nbsp;</td>");
                } else {
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;40/20/10&nbsp;&nbsp;</td>");
                    difElo1=(int)Math.round(dp*40);
                    difElo2=(int)Math.round(dp*20);
                    difElo3=(int)Math.round(dp*10);
                    s1=String.format("%+d",difElo1);      // convert to string with sign
                    if (s1.equalsIgnoreCase("+0")) s1="&nbsp;0";  // don't like '+0' 
                    s2=String.format("%+d",difElo2);      // convert to string with sign
                    if (s2.equalsIgnoreCase("+0")) s2="&nbsp;0";  // don't like '+0' 
                    s3=String.format("%+d",difElo3);      // convert to string with sign
                    if (s3.equalsIgnoreCase("+0")) s3="&nbsp;0";  // don't like '+0' 
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;"+s1+"/"+s2+"/"+s3+"&nbsp;&nbsp;</td>");
                    fOut.write("<td align=\"center\">&nbsp;&nbsp;"+(eloIni+difElo1)+"/"+(eloIni+difElo2)+"/"+(eloIni+difElo3)+"&nbsp;&nbsp;</td>");
                }
                performance=0;
                if (nFIDE>0) {
                    meanElo=(int)Math.round(1.0*totEloOpp/nFIDE);
                    percentScore=Math.round(totScoreFIDE*10f/nFIDE);
                    if (percentScore<50) performance=deltaPerformance[percentScore];
                    else if (percentScore>50) performance=Math.round(KELOFIDEBASE*(totScoreFIDE*20-nFIDE*100)/200);
                    performance+=meanElo;
                }
                s="";
                if (nFIDE<MINGAMESFIDE || totScoreFIDE<MINSCOREFIDE*10 || performance<MINELOFIDE) {
                    s="(*)"; if (nFIDE>0) isDoubtTrancheFIDE=true;
                }
                if (nFIDE>0) fOut.write("<td align=\"center\">"+""+performance+"("+nFIDE+") "+s+"</td>");
                else fOut.write("<td align=\"center\">&nbsp;</td>");
                fOut.write("</tr>"+lineSeparator);
              }
          }
          fOut.write("<tr><td colspan=\"7\"><hr /></td></tr>"+lineSeparator);
          fOut.write("</table>"+lineSeparator);
          if (isDoubtTrancheFIDE) fOut.write("(*) "+Main.localizedText("not valid if 1st"));
          fOut.write("</body>"+lineSeparator);
          fOut.write("</html>"+lineSeparator);
          fOut.close();
                     
          java.net.URL url=null;
          try {
                url = new java.net.URL("file:///"+tempFile);
          } catch (MalformedURLException ex) {
//                
          }
          if (url != null) 
          try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
          } catch (IOException ex) {}
            catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }       
        
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
//  output Cross Table
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int i, i0, i1, i2, j, k, p0, p1, p2, board, round, score, score1, score2, scoreTeam, scoreTeam1, scoreTeam2;
        int games, r1, r2, nRetired;
        String s, team, EloString; String [] S;
        boolean played, bye, forfeit, retired;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        try {
          fOut = new FileWriter(tempFile);  // create a temp file
          String lineSeparator = System.getProperty( "line.separator" );
          fOut.write("<html>"+lineSeparator);
          fOut.write("<head>"+lineSeparator);
          fOut.write("<font Size=\"+2\">"+Main.localizedText("Tournament")+": "+tournamentName+lineSeparator);
          fOut.write("</head><body>"+lineSeparator);
          if (maxPlayersPerTeam>1) {
            fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Cross Table for Teams")+lineSeparator);
            fOut.write("<br><table>"+lineSeparator);
            fOut.write("<tr><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("ID")+"</td><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Team")+"</td><td bgColor=\"yellow\"></td>" +
                    "<td bgColor=\"yellow\" colspan=\""+currRound);
            fOut.write("\"><font Size=\"+1\">"+Main.localizedText("Rounds played")+"</td><td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("score")+"</td></tr>"+lineSeparator);
            for (i0=1; i0<=addedRows; i0++) {
                team = ""+myTM.getValueAt(sortIndex[i0-1]-1,0);
                EloString = ""+myTM.getValueAt(sortIndex[i0-1]-1,1);
                fOut.write("<tr><td colspan=\""+(currRound+4)+"\"><hr /></td></tr>"+lineSeparator);
                fOut.write("<tr><td bgColor=\"silver\" align=\"center\">"+i0+"</td>");
                fOut.write("<td>"+team+"</td>");
                fOut.write("<td>"+EloString+"</td>");
                scoreTeam = 0;
                for (round=0; round<currRound; round++) {
                    played=false;
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;    // empty row?
                        if (!(i0==i1 || i0==i2)) continue; // not the right pair
                        played=true;
                        bye=forfeit=false;
                        if (i2==0) bye=true;
                        else if (S[3].indexOf('f')>0) forfeit=true;
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        games=scoreTeam1=scoreTeam2=0;
                        for (i=1; i<=maxBoards;i++) {
                            S = roundsDetail[j][i][round].split("-");  // get individual match
                            if (S.length != 4) continue;
                            r1 = integerScore(S[2]);  // result 1
                            r2 = integerScore(S[3]);  // result 2
                            if (r1==0 && r2==0) continue;     // not played at all
                            games++;
                        }
                        if (games>0) {
                            if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                            else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>games*5) scoreTeam1=2;
                            if (score2>games*5) scoreTeam2=2;
                            if (score1==games*5) scoreTeam1=1;
                            if (score2==games*5) scoreTeam2=1;
                            if (score1<games*5) scoreTeam1=0;
                            if (score2<games*5) scoreTeam2=0;
                        }
                        s = "BYE";
                        if (i0==i1) {
                            if (!bye) {s = WhiteColor+i2; if (forfeit) s+="f";}
                            if (scoreTeam1==2) fOut.write("<td>+"+s+"</td>");
                            if (scoreTeam1==1) fOut.write("<td>="+s+"</td>");
                            if (scoreTeam1==0) fOut.write("<td>-"+s+"</td>");
                            scoreTeam+=scoreTeam1;
                        } else if (i0==i2) {
                            if (!bye) {s = BlackColor+i1; if (forfeit) s+="f";}
                            if (scoreTeam2==2) fOut.write("<td>+"+s+"</td>");
                            if (scoreTeam2==1) fOut.write("<td>="+s+"</td>");
                            if (scoreTeam2==0) fOut.write("<td>-"+s+"</td>");
                            scoreTeam+=scoreTeam2;
                        }
                    }
                    if (!played) fOut.write("<td>--</td>");
                }
                fOut.write("<td align=\"center\">"+scoreTeam+"</td>");
                fOut.write("</tr>"+lineSeparator);
            }
            fOut.write("<tr><td colspan=\""+(currRound+4)+"\"><hr /></td></tr>"+lineSeparator);
            fOut.write("</table><br>"+lineSeparator);
            fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
          }
          fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Cross Table for Players")+lineSeparator);
          if (currRound==maxRound && missingResults) fOut.write("<br>("+Main.localizedText("Some results missing!")+")"+lineSeparator);
          fOut.write("<br><table>"+lineSeparator);
          fOut.write("<tr><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("ID")+"</td>");
          fOut.write("<td  colspan=\"3\" bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Player")+"</td>" +
                    "<td bgColor=\"yellow\" colspan=\""+currRound);
          fOut.write("\"><font Size=\"+1\">"+Main.localizedText("Rounds played")+"</td><td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("score")+"</td></tr>"+lineSeparator);
          nRetired=statsPlayers= statsGames= statsGamesPlayed= statsBye= statsForfeit= statsDraw= statsWinWhite= statsWinStronger=0; // statistics counters
          for (i0=1; i0<=addedRows; i0++) {
              fOut.write("<tr><td colspan=\""+(currRound+5)+"\"><hr /></td></tr>"+lineSeparator);
              s=""+myTM.getValueAt(sortIndex[i0-1]-1,0);
              retired=false;
              if (s.contains("(W)")) retired=true;
              for (k=1; k<=maxPlayersPerTeam; k++) {
                p0 = (i0-1)*maxPlayersPerTeam+k; 
                s=""+myTM.getValueAt(sortIndex[i0-1]-1,k+1);
                if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                s = minimalPlayerData(s);
                fOut.write("<tr><td bgColor=\"silver\" align=\"center\">"+p0+"</td>");
                S=s.split(";");
                fOut.write("<td>"+S[0]+"</td>");
                fOut.write("<td>"+S[2]+"</td>");
                fOut.write("<td>"+S[3]+"</td>");
                score = 0; statsPlayers++;
                if (retired || S[0].contains("(W)")) nRetired++;
                for (round=0; round<currRound; round++) {
                    played=false;
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0) continue;            // empty row?
                        if (i0!=i1 && i0!=i2) continue;     // not right pair
                        for (board=1; board<=maxBoards; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            p2 = Integer.valueOf(S[1]);
                            if (p1==0 && p2==0) continue;  // not yet initialized i.e. not yet played
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            if (p1>0) p1 += (i1-1)*maxPlayersPerTeam;
                            if (p2>0 && i2>0) p2 += (i2-1)*maxPlayersPerTeam;
                            if (i0==i1 && p0==p1) {
                                forfeit = S[3].indexOf('f')>0; played=true;
                                if (i2==0 || p2==0 || possiblePlayerResults[6].equals(S[2]+"-"+S[3])
                                        || possiblePlayerResults[13].equals(S[2]+"-"+S[3])) {s = "BYE"; statsBye++;}
                                else {
                                    if (board%2==1) {
                                        s = WhiteColor+p2; if (forfeit) s+="f"; 
                                        if (score1==10 && !forfeit) statsWinWhite++; 
                                    }
                                    else {
                                        s = BlackColor+p2; if (forfeit) s+="f"; 
                                        if (score2==10 && !forfeit) statsWinWhite++; 
                                    }
                                    if (score1==5 && score2==5 && !forfeit) statsDraw++;
                                    if (forfeit) statsForfeit++; else statsGamesPlayed++;
                                    if (p1<p2) {
                                        if (score1==10 && !forfeit) statsWinStronger++;
                                    } else {
                                        if (score2==10 && !forfeit) statsWinStronger++;
                                    }
                                } 
                                if (score1==10) fOut.write("<td>+"+s+"</td>");
                                if (score1==5) fOut.write("<td>="+s+"</td>");
                                if (score1==0) fOut.write("<td>-"+s+"</td>");
                                score+=score1; 
                                break;
                            } else if (i0==i2 && p0==p2) {
                                forfeit = S[3].indexOf('f')>0; played=true;
                                if (i1==0 || p1==0 || possiblePlayerResults[6].equals(S[2]+"-"+S[3])
                                        || possiblePlayerResults[13].equals(S[2]+"-"+S[3])) {s = "BYE"; statsBye++;} 
                                else {
                                    if (board%2==0) {s = WhiteColor+p1; if (forfeit) s+="f";}
                                    else {s = BlackColor+p1; if (forfeit) s+="f";}
                                }
                                if (score2==10) fOut.write("<td>+"+s+"</td>");
                                if (score2==5) fOut.write("<td>="+s+"</td>");
                                if (score2==0) fOut.write("<td>-"+s+"</td>");
                                score+=score2;
                                break;
                            }
                        }
                    }
                    if (!played) fOut.write("<td>--</td>");
                }
                fOut.write("<td align=\"center\">"+HTMLscore(score*10)+"</td>");
                fOut.write("</tr>"+lineSeparator);
              }
            }
            fOut.write("<tr><td colspan=\""+(currRound+5)+"\"><hr /></td></tr>"+lineSeparator);
            fOut.write("</table>"+lineSeparator);
            statsGames=(addedRows+1)/2*currRound*maxBoards;
            fOut.write("<br>"+Main.localizedText("Tournament statistics:"));
            fOut.write("<br>"+Main.localizedText("Players")+" "+statsPlayers);
            fOut.write(", "+Main.localizedText("Retired")+" "+nRetired);
            fOut.write(", "+Main.localizedText("Rounds played")+" "+currRound);
            fOut.write(", "+Main.localizedText("Games")+" "+statsGames+lineSeparator);
            fOut.write("<br>"+Main.localizedText("Bye")+" "+statsBye+" ("+Math.round(statsBye*1000.0/statsGames)/10.0+"%)");
            fOut.write(", "+Main.localizedText("Forfeit")+" "+statsForfeit+" ("+Math.round(statsForfeit*1000.0/statsGames)/10.0+"%)");
            fOut.write(", "+Main.localizedText("Games Played")+" "+statsGamesPlayed+" ("+Math.round(statsGamesPlayed*1000.0/statsGames)/10.0+"%)"+lineSeparator);
            fOut.write("<br>"+Main.localizedText("Draw")+"         "+statsDraw+" ("+Math.round(statsDraw*1000.0/statsGamesPlayed)/10.0+"%)");
            fOut.write(", "+Main.localizedText("Win White")+"    "+statsWinWhite+" ("+Math.round(statsWinWhite*1000.0/statsGamesPlayed)/10.0+"%)");
            fOut.write(", "+Main.localizedText("Win Lower ID")+" "+statsWinStronger+" ("+Math.round(statsWinStronger*1000.0/statsGamesPlayed)/10.0+"%)"+lineSeparator);

          fOut.write("</body>"+lineSeparator);
          fOut.write("</html>"+lineSeparator);
          fOut.close();
                       
          java.net.URL url=null;
          try {
                url = new java.net.URL("file:///"+tempFile);
          } catch (MalformedURLException ex) {
//                
          }
          if (url != null) 
          try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
          } catch (IOException ex) {}
            catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }
        
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
//  output Current Round (simple list)
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int board, i1, i2, j, p1, p2;
        boolean results;
        String s, s1, s2, team1, team2, player1, player2, color1, color2, f; String [] S;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        try {
            fOut = new FileWriter(tempFile);  // create a temp file
            String lineSeparator = System.getProperty( "line.separator" );
            fOut.write("<html>"+lineSeparator);
            fOut.write("<head></head><body>"+lineSeparator);
            fOut.write(tournamentName+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>"+Main.localizedText("Current round")+": "+currRound+"</strong><br><br>"+lineSeparator);
            fOut.write("<table>"+lineSeparator);
            for (j=0; j<addedPairs; j++) {
                s = roundsDetail[j][0][currRound-1];
                S = s.split("-");
                if (S.length != 4) continue;
                i1 = Integer.valueOf(S[0]);
                i2 = Integer.valueOf(S[1]);
                if (i1==0) continue;    // empty row?
                s1 = " | "+teamScores[i1-1][0];
                if (maxPlayersPerTeam==1) s1="";    // individual tournament!
                s2 = " | "+ 0.5f*teamScores[i1-1][0];
                team1 = ""+myTM.getValueAt(sortIndex[i1-1]-1,0)+s1+s2;
                team2 = "BYE"; f="f";
                if (i2>0) {
                    s1 = " | "+teamScores[i2-1][0];
                    if (maxPlayersPerTeam==1) s1="";    // individual tournament!
                    s2 = " | "+ 0.5f*teamScores[i2-1][0];
                    team2 = ""+myTM.getValueAt(sortIndex[i2-1]-1,0)+s1+s2; f="";
                }
                if (S[3].indexOf('f')>0) f="f";
                results=true;
                if (S[2].equals("0") && S[3].equals("0")) results=false;
                fOut.write("<tr><td>"+(j+1)+"</td>");
                fOut.write("<td>"+WhiteColor+" - "+team1+"</td>");
                fOut.write("<td> - </td>");
                fOut.write("<td>"+BlackColor+" - "+team2+"</td>");
                if (results) fOut.write("<td> "+HTMLscore(integerScore(S[2])*10)+" - "+HTMLscore(integerScore(S[3])*10)+f+"</td>");
                else fOut.write("<td> ...... - ......</td>");
                fOut.write("</tr>"+lineSeparator);
                fOut.write("<tr><td colspan=5><hr /></td></tr>"+lineSeparator);
            }
            fOut.write("</table>"+lineSeparator);
            fOut.write("</body>"+lineSeparator);
            fOut.write("</html>"+lineSeparator);
            fOut.close();
                       
            java.net.URL url=null;
            try {
                url = new java.net.URL("file:///"+tempFile);
            } catch (MalformedURLException ex) {
//                
            }
            if (url != null) 
            try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
            } catch (IOException ex) {}
              catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }
        
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
//  output Teams and Players
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        int i,i0,j,k, ELOp;
        String s; String[] S; String[] alphaOrdered = new String[addedRows];
        try {
            fOut = new FileWriter(tempFile);  // create a temp file
            String lineSeparator = System.getProperty( "line.separator" );
            fOut.write("<html>"+lineSeparator);
            fOut.write("<head>"+lineSeparator);
            fOut.write("<font Size=\"+2\">"+Main.localizedText("Tournament")+": "+tournamentName+lineSeparator);
            fOut.write("</head><body>"+lineSeparator);
            if (maxPlayersPerTeam==1)
                fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Players")+"<br><br>"+lineSeparator);
            else fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Teams & Players")+"<br><br>"+lineSeparator);
            fOut.write("<table>"+lineSeparator);
            fOut.write("<tr><td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("ID")+"</td>");
            s=Main.localizedText("Team");
            if (maxPlayersPerTeam==1) s="";
            fOut.write("<td bgColor=\"yellow\"><font Size=\"+1\">"+s+"</td>");
            fOut.write("<td bgColor=\"yellow\">"+Main.localizedText("Player (name; sex; title; Elo; k)")+"</td>");
            if (maxPlayersPerTeam==1)
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("Elo")+"</td></tr>"+lineSeparator);
            else fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("mean Elo")+"</td></tr>"+lineSeparator);
            fOut.write("<tr></tr>"+lineSeparator);
            for (i0=1; i0<=addedRows; i0++) {
                i=sortIndex[i0-1]-1;
                String team = ""+myTM.getValueAt(i,0);
                alphaOrdered[i0-1]=team;    // store the name for later use
                fOut.write("<tr><td colspan=4><hr /></td></tr>"+lineSeparator);
                fOut.write("<tr><td bgColor=\"silver\" align=\"center\"><font Size=\"+1\">"+i0+"</td>");
                fOut.write("<td bgColor=\"silver\" colspan=\"2\"><font Size=\"+1\">"+team+"</td>");
                fOut.write("<td bgColor=\"silver\" align=\"center\"><font Size=\"+1\">"+myTM.getValueAt(i,1)+"</td></tr>");
                for (k=0; k<maxPlayersPerTeam; k++) {  // scan table columns for players
                    s=""+myTM.getValueAt(i,k+2);
                    if (s.isEmpty() || s.equals("null") || s.equals("BYE")) continue;
                    s = minimalPlayerData(s);
                    fOut.write("<tr><td></td><td></td>");
                    fOut.write("<td colspan=\"2\">"+s+"</td></tr>");
                }
                fOut.write("</tr>"+lineSeparator);
            }
            fOut.write("<tr><td colspan=4><hr /></td></tr>"+lineSeparator);
            fOut.write("</table>"+lineSeparator);
            /* order alphabetically */
            for (i=0; i<addedRows-1; i++)
            for (j=i+1; j<addedRows; j++) 
                if (alphaOrdered[i].compareToIgnoreCase(alphaOrdered[j])>0){
                    s=alphaOrdered[j];
                    alphaOrdered[j]=alphaOrdered[i];
                    alphaOrdered[i]=s;
                }
            fOut.write("<br><font Size=\"+1\">"+Main.localizedText("alphabetical")+"</font><br><br>"+lineSeparator);
            for (i=0; i<addedRows; i++) 
                fOut.write(alphaOrdered[i]+"<br>"+lineSeparator);
            fOut.write("</body>"+lineSeparator);
            fOut.write("</html>"+lineSeparator);
            fOut.close();
                       
            java.net.URL url=null;
            try {
                url = new java.net.URL("file:///"+tempFile);
            } catch (MalformedURLException ex) {
//                
            }
            if (url != null) 
            try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
            } catch (IOException ex) {}
              catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }
      
    }//GEN-LAST:event_jButton12ActionPerformed

private String minimalPlayerData(String s) {
// return a minimal string with player data
    String[] S; String name="", sex="", title="", EloFIDE="", EloNAT="", kString="", flagFIDE="";
    int Elo=0, k=0;
    S = s.split(";");
    try {
            name    = S[0];
            sex     = S[3];
            title   = S[4];
            EloFIDE = S[6];
            EloNAT  = S[8];
            kString = S[9];
    } catch (ArrayIndexOutOfBoundsException ex) {}  
      catch (IndexOutOfBoundsException ex) {}
    try {
        Elo=Integer.valueOf(EloFIDE); //FIDE rating
        flagFIDE="F";
    } catch (NumberFormatException ex) {}
    try {
        if (Elo==0) {Elo=Integer.valueOf(EloNAT); flagFIDE="";} //National rating
    } catch (NumberFormatException ex) {}
    try {
        k=Integer.valueOf(kString);
    } catch (NumberFormatException ex) {}
    s = name+"; "+sex+"; "+title+"; "+Elo+flagFIDE+"; "+k;
    return s;
}
    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
//  play system changed
        
    }//GEN-LAST:event_jList1MouseClicked

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
// Remove selected pair in Table2
        if (engineRunning) return;
        short editingRow = (short)jTable2.getSelectedRow();
        if (editingRow<0) return;   // no row selected?
        if (editingRow>=addedPairs) return;   // do nothing beyond added pairs!
        TableModel myTM3 = jTable3.getModel();
        TableModel myTM4 = jTable4.getModel();
        TableModel myTM2 = jTable2.getModel();
        String team1 = ""+myTM2.getValueAt(editingRow,0);
        String team2 = ""+myTM2.getValueAt(editingRow,1);
        if (team2.equalsIgnoreCase("0| BYE")) team2=team1;
        myTM3.setValueAt(team1,pairFrom[editingRow][0],0);
        myTM3.setValueAt(team2,pairFrom[editingRow][1],0);
        myTM4.setValueAt(team1,pairFrom[editingRow][0],0);
        myTM4.setValueAt(team2,pairFrom[editingRow][1],0);
        addedPairs--;
        for (int i=editingRow; i<addedPairs; i++)  {      // shift up all following pairs
            myTM2.setValueAt(myTM2.getValueAt(i+1,0),i,0);
            myTM2.setValueAt(myTM2.getValueAt(i+1,1),i,1);
            pairFrom[i][0]=pairFrom[i+1][0];
            pairFrom[i][1]=pairFrom[i+1][1];
            for (int k=0; k<=maxBoards; k++) roundsDetail[i][k][currRound-1]=roundsDetail[i+1][k][currRound-1];
        }
        for (int k=0; k<=maxBoards; k++) roundsDetail[addedPairs][k][currRound-1]="0-0-0-0";  // reset last pair
        myTM2.setValueAt("",addedPairs,0);
        myTM2.setValueAt("",addedPairs,1);
        pairFrom[addedPairs][0]=0;
        pairFrom[addedPairs][1]=0;
        safeExitAllowed = false;       
        
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
// license information
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(this,
                "<html>JAVAPAIRING.  Copyright &copy; 2009-2014  Eugenio Cervesato<br>(eucerve@tin.it  mobile +39 338 5960366),<br>\"Bobby Fischer\" chess club - Cordenons - Italy.<br><br>"+Main.localizedText("This program allows to handle a chess team tournament, i.e. team & player registration,<br>pairing, result and output generation.")+"<br>"+Main.localizedText("This program comes with ABSOLUTELY NO WARRANTY.<br>This is free software, and you are welcome to redistribute it under GNU GPLv3 conditions;<br>see the license file for details.")+"</html>",
                Main.localizedText("License info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jPanel8ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel8ComponentShown
// shown pairing page
        if (engineRunning) return;
        loadCurrentRound();
    }//GEN-LAST:event_jPanel8ComponentShown

    private void jPanel5ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel5ComponentShown
// shown results page
        if (engineRunning) return;
        if (!batchOrder && tournamentType!=5 && currRound>1) calculateTeamScores(-1); // needed to later recalculate floaters
        insertResults();
    }//GEN-LAST:event_jPanel5ComponentShown

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
//    round changed; set values
        if (engineRunning) return;
        String round= ""+jSpinner1.getValue();        
        jTextField4.setText(round);
        currRound = Integer.valueOf(jSpinner1.getValue().toString()).shortValue();
        loadCurrentRound();
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jList3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MouseClicked
//   check double click on jList3
        if (prevevt==null) {prevevt=evt; return;}
        if ((Math.abs(prevevt.getWhen()-evt.getWhen())<doubleClickTime) &&
            (Math.abs(prevevt.getX()-evt.getX())<doubleClickSpace) &&
            (Math.abs(prevevt.getY()-evt.getY())<doubleClickSpace)) {
            // double click has been recognized
            String str=""+jList3.getSelectedValue();
            importString(str);          // drop text on the table
            prevevt=null;
        }
        else prevevt=evt;
    }//GEN-LAST:event_jList3MouseClicked

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
//  blank row in table6
        TableCellEditor t = jTable6.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        TableModel TM6=jTable6.getModel();
        int row=jTable6.getSelectedRow();
        if (row<0 || row>=maxPlayersPerTeam) return;
        for (int k=0; k<10; k++) 
            TM6.setValueAt("",row,k);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
//   move row UP in table6
        TableCellEditor t = jTable6.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        TableModel TM6=jTable6.getModel();
        int row=jTable6.getSelectedRow();
        if (row<1 || row>=maxPlayersPerTeam) return;
        String temp;
        for (int k=0; k<10; k++) {
            temp = ""+TM6.getValueAt(row-1,k); // save data previous row
            TM6.setValueAt(""+TM6.getValueAt(row,k),row-1,k);               // exchange data
            TM6.setValueAt(temp,row,k); 
        }
        jTable6.setRowSelectionInterval(row-1,row-1);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
// close the dialog
        TableCellEditor t = jTable8.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        TableModel TM8=jTable8.getModel();
        if ((""+TM8.getValueAt(0, 0)).length()>0) {
            delimiterDB[activeDB]=jTextField2.getText();
            quotesDB[activeDB]=jTextField55.getText();
            indexesDB[activeDB]=new String[10];
            for (int j=0; j<10; j++) 
                indexesDB[activeDB][j]=""+TM8.getValueAt(0, j);
        }
        jDialog1.dispose();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
// load schema
        // read data from disc. Open File dialog
        TableModel TM8=jTable8.getModel();
        String [] S = {};
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            Main.localizedText("JavaPairing Schema (.scm)"), "scm");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(currentDirectory);
        chooser.setSelectedFile(selectedSchema);
        int returnVal = chooser.showOpenDialog(jDialog1);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getCurrentDirectory();
            selectedSchema = chooser.getSelectedFile();
            try {
                // read disc file 
                BufferedReader in = new BufferedReader(new FileReader(selectedSchema));
                if (!in.ready()) throw new IOException();
                // read setup data
                String DBName = in.readLine();
                String s = in.readLine();    
                delimiterDB[activeDB] = s.substring(0,1); // blank = fixed format
                quotesDB[activeDB] = "";
                if (s.length()>1) quotesDB[activeDB] = s.substring(1,2);
                jTextField2.setText(delimiterDB[activeDB]);
                jTextField55.setText(quotesDB[activeDB]);
                indexesDB[activeDB] = (in.readLine()+";;;;;;;;;;;;;;;x;").split(";");
                in.close();
                selectedDB[activeDB]=new File(DBName);
                if (delimiterDB[activeDB].equals(" ")) {    // assume fixed
                    jLabel5.setText(Main.localizedText("set absolute range position of each column in the file at the first row of the following table"));
                    jCheckBox2.getModel().setSelected(true);
                }
                else {
                    jLabel5.setText(Main.localizedText("set relative position of each column in the file at the first row of the following table"));
                    jCheckBox2.getModel().setSelected(false);
                }
                jList2.setListData(S);
                jList2.invalidate();
                for (int j=0; j<10; j++) {
                    TM8.setValueAt(indexesDB[activeDB][j], 0,j);
                    for (int k=1; k<11;k++) TM8.setValueAt("", k,j);
                }
                if (selectedDB[activeDB].exists()) processDB();
            } catch (IOException ex) {
                
            }
        }
            
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
// save schema
        TableCellEditor t = jTable8.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        TableModel TM8=jTable8.getModel();
        delimiterDB[activeDB]=jTextField2.getText();
        quotesDB[activeDB] = jTextField55.getText();
        indexesDB[activeDB]=new String[10];
        for (int j=0; j<10; j++) {
            indexesDB[activeDB][j]=""+TM8.getValueAt(0, j);
        }
        String indexes=join(indexesDB[activeDB], ";");
        String DBName=selectedDB[activeDB].getPath();
                // save data to disc. open File dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            Main.localizedText("JavaPairing Schema (.scm)"), "scm");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(currentDirectory);
        chooser.setSelectedFile(selectedSchema);
        int returnVal = chooser.showDialog(jDialog1, Main.localizedText("Save Schema to File"));
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getCurrentDirectory();
            selectedSchema = chooser.getSelectedFile();
            String s=selectedSchema.getPath();    
            if (!s.endsWith(".scm")) s+=".scm"; // check if file extension exists
            try {
                // save to disc file 
                FileWriter fOut = new FileWriter(s);
                String lineSeparator = System.getProperty( "line.separator" );
                // write setup data
                fOut.write(DBName+lineSeparator);
                fOut.write(delimiterDB[activeDB].substring(0,1));
                if (quotesDB[activeDB].length()>0) fOut.write(quotesDB[activeDB].substring(0,1));
                fOut.write(lineSeparator);
                fOut.write(indexes+lineSeparator);
                fOut.close();
            } catch (IOException ex) {
                
            }
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
// test schema
        TableCellEditor t = jTable8.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        TableModel TM8=jTable8.getModel();
        ListModel L2=jList2.getModel();
        delimiterDB[activeDB]=jTextField2.getText();
        quotesDB[activeDB]=jTextField55.getText();
        String[] S; String s; int i,j,field, start,stop;
        if (jCheckBox2.isSelected()) for (i=0; i<10; i++) { // fixed format ...
            s=""+L2.getElementAt(i+2);
            for (j=0; j<10; j++) {
                S=(""+TM8.getValueAt(0, j)).split("-");
                start = stop = 0;
                try {
                    start = Integer.valueOf(S[0])-1;
                    stop = Integer.valueOf(S[1]);
                    TM8.setValueAt(s.substring(start, stop).trim(), i+1, j);
                } catch (ArrayIndexOutOfBoundsException ex) {TM8.setValueAt("", i+1, j);}
                  catch (IndexOutOfBoundsException ex) {TM8.setValueAt("", i+1, j);}
                  catch (NumberFormatException ex) {TM8.setValueAt("", i+1, j);}
            }
        } 
        else for (i=0; i<10; i++) {        // csv format assumed ...
            s=""+L2.getElementAt(i+2);
            S=splitCSV(s);
            for (j=0; j<10; j++) {
                try {
                    if ((""+TM8.getValueAt(0, j)).contains("|")) {  // workaround to join fields
                        String [] S2=(""+TM8.getValueAt(0, j)).split("\\|");
                        int field1=Integer.valueOf(S2[0]);
                        int field2=Integer.valueOf(S2[1]);
                        TM8.setValueAt(S[field1-1].trim()+"|"+S[field2-1].trim(), i+1, j);
                    }
                    else if ((""+TM8.getValueAt(0, j)).contains("-")) {  // workaround to separate fields
                        String [] S2=(""+TM8.getValueAt(0, j)).split("-");
                        field=Integer.valueOf(S2[0]);
                        String [] S3=S[field-1].split("-");
                        TM8.setValueAt(S3[0].trim(), i+1, j);                        
                    }
                    else {
                        field=Integer.valueOf(""+TM8.getValueAt(0, j));
                        TM8.setValueAt(S[field-1].trim(), i+1, j);
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {TM8.setValueAt("", i+1, j);}
                  catch (IndexOutOfBoundsException ex) {TM8.setValueAt("", i+1, j);}
                  catch (NumberFormatException ex) {TM8.setValueAt("", i+1, j);}
            }
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
// open setup import window
        activeDB=jComboBox8.getSelectedIndex();
        jDialog1.setVisible(true);
        jTable8.getColumnModel().getColumn(0).setMinWidth(160);  // adapt to text
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
// Open the .txt/.csv file
        // read data from disc. Open File dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
            Main.localizedText("plain text (.txt)"), "txt");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter(
            Main.localizedText("delimited text file (.csv)"), "csv");
        chooser.setFileFilter(filter2);
        chooser.setFileFilter(filter1);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(currentDirectory);
        chooser.setSelectedFile(selectedDB[activeDB]);
        int returnVal = chooser.showOpenDialog(jDialog1);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getCurrentDirectory();
            selectedDB[activeDB] = chooser.getSelectedFile();
            processDB();
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void processDB() {
        // process database file
            TableModel TM8=jTable8.getModel();
            for (int j=0; j<10; j++) 
                    for (int k=1; k<11;k++) TM8.setValueAt("", k,j);
            if (!selectedDB[activeDB].isFile()) return;
            if (!selectedDB[activeDB].exists()) return;
            if (jTextField2.getText().equals("")) {
                if (selectedDB[activeDB].getName().toLowerCase().endsWith(".csv"))     // assume delimited
                    jCheckBox2.getModel().setSelected(false);
                else 
                    jCheckBox2.getModel().setSelected(true);
                jTextField2.setText(" ");
            }
            if (jCheckBox2.isSelected())     // assume delimited
                    jLabel5.setText(Main.localizedText("set absolute range position of each column in the file at the first row of the following table"));
            else 
                    jLabel5.setText(Main.localizedText("set relative position of each column in the file at the first row of the following table"));
            try {
                // read disc file 
                BufferedReader in = new BufferedReader(new FileReader(selectedDB[activeDB]));
                if (!in.ready()) throw new IOException();
                // read data
                String row; int i, k, prevcount; String[] S=new String[12];
                char[] x; char xx, delimiter, quotes; int[] arrChar=new int[256];  // try lo find the delimiter & quotes
                for (k=0; k<256; k++) arrChar[k]=0;
                for (i=2; i<12; i++) {
                    row = in.readLine();
                    if (row==null) {S[i]=""; continue;}
                    S[i]=row;
                    x = row.toCharArray();
                    for (k=0; k<row.length(); k++) arrChar[x[k]]++;   // increment char counter
                }
                S[0] = S[1] = "";
                if (jCheckBox2.isSelected()) {    // plain text fixed length?
                    S[0] = "         1         2         3         4         5         6         7         8";
                    S[1] = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
                }    
                jList2.setListData(S);                
                jList2.invalidate();
                in.close();
                prevcount=0; delimiter=0; quotes=0;
                if (jCheckBox2.isSelected()) {   // plain text fixed length?
                    jTextField2.setText(" ");
                    jLabel5.setText(Main.localizedText("set absolute range position of each column in the file at the first row of the following table"));
                } else {
                    if (arrChar['"']>10) quotes='"';
                    if (arrChar['\'']>10 && arrChar['\'']>arrChar['"']) quotes='\'';
                    for (k=0; k<256; k++) {
                        xx = (char)k;
                        if (xx>='A' && xx<='Z') continue;
                        if (xx>='a' && xx<='z') continue;
                        if (xx>='0' && xx<='9') continue;
                        if (xx == ' ') continue;
                        if (k!=quotes && arrChar[k]>prevcount) { delimiter=(char)k; prevcount=arrChar[k]; }
                    }
                    jTextField2.setText(""+delimiter);      // set the field delimiter
                    jTextField55.setText("");
                    if (quotes>0) jTextField55.setText(""+quotes);        // set the string quotes
                    jLabel5.setText(Main.localizedText("set relative position of each column in the file at the first row of the following table"));
                }
            } catch (IOException ex) {} 
    }
    
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
// HELP needed
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(this,
                "<html>JAVAPAIRING rev. 2.9 september 2015. Dutch engine version marked 'june 2013' endorsed by FIDE (october 2013).<br>Copyright &copy; 2009-2015  Eugenio Cervesato, \"Bobby Fischer\" chess club - Cordenons - Italy<br>(eucerve@tin.it  mobile +39 338 5960366)<br><br>"+Main.localizedText("This program allows to handle a chess team tournament, i.e. team & player registration,<br>pairing, result and output generation.")+"<br>"+Main.localizedText("For more info see the JavaPairing Manual.")+"<br>"+"<br>"+Main.localizedText("credits ...")+"</html>",
                Main.localizedText("Program info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// add player from file
        String s; int i=0, j, k; String[] S=new String[50];
        String playerToSearchFor = jTextField3.getText().toUpperCase();
        if (playerToSearchFor.equals("SIMULATION") && maxPlayersPerTeam==1) {
            EngineThread = new Thread(new simulateTournament(this));
            EngineThread.start();  // run in a different thread, so it is interruptable 
            return;
        }
        int lengthToSearch=playerToSearchFor.length();
        activeDB=jComboBox8.getSelectedIndex();
        if (!selectedDB[activeDB].isFile()) {jButton28ActionPerformed(evt); return;} 
        if (playerToSearchFor.equals("*") && maxPlayersPerTeam==1) {importAllPlayers(); return;}
        if (playerToSearchFor.equals("*") && maxPlayersPerTeam>1) {importAllTeams(); return;}
        if (lengthToSearch<4) {
            S[0] = Main.localizedText("specify at least 4 chars.");
            jList3.setListData(S);
            return;   // set a minimum string size
        }
        if (selectedDB[activeDB].exists()) 
        try {
                    // read disc file 
                    BufferedReader in = new BufferedReader(new FileReader(selectedDB[activeDB]));
                    if (!in.ready()) throw new IOException();
                    // read data
                    for (;;) {
                        if ((s = in.readLine()) == null) break;   // EOF !
                        if (i==50) break;                           // max size of list 
                        if (s.toUpperCase().indexOf(playerToSearchFor) >=0 ) 
                                S[i++]=s;
                    }
                    if (i==0) S[0]=Main.localizedText("none player found.");
                    for (j=0; j<i-1; j++)       // sort alphabetically
                        for (k=j+1; k<i; k++) 
                            if (S[j].compareToIgnoreCase(S[k])>0) { s = S[j]; S[j] = S[k]; S[k] = s; }
                    jList3.setListData(S);
                    in.close();
                    jList3.setDragEnabled(true);
                    jList3.invalidate();
                    jTable6.setDragEnabled(true);
                    jTable6.setTransferHandler(new TransferHandler2(this));
        } catch (IOException ex) {}
       
    }//GEN-LAST:event_jButton4ActionPerformed

public static class TransferHandler2 extends TransferHandler{
    TransferHandler2(EnterFrame c){
        super();
        caller=c;
    }
        @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return true;
    }
        @Override
    public boolean importData(JComponent c, Transferable t) {
            try {
                String str = (String)t.getTransferData(DataFlavor.stringFlavor);
                caller.importString(str);
                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }

            return false;
    }
    private EnterFrame caller;
}

public void importString(String s) {
    int i, j, start, stop, field;
    String[] S;
    TableCellEditor t = jTable6.getCellEditor();// complete editing if  
    if (t!=null) t.stopCellEditing();           // left in edit mode
    jTable6.clearSelection();
    TableModel TM6=jTable6.getModel();
    for (i=0; i<maxPlayersPerTeam; i++) 
        if ((""+TM6.getValueAt(i, 0)).compareTo("")==0) { // look at first empty row
            if (delimiterDB[activeDB].equals(" ")) { // fixed format ...
                for (j=0; j<10; j++) {
                    S=indexesDB[activeDB][j].split("-");
                    start = stop = 0;
                    try {
                        start = Integer.valueOf(S[0])-1;
                        stop = Integer.valueOf(S[1]);
                        TM6.setValueAt(s.substring(start, stop).trim(), i, j);
                    } catch (NumberFormatException ex) {}
                      catch (ArrayIndexOutOfBoundsException ex) {}
                      catch (IndexOutOfBoundsException ex) {}
                }
            }
            else {        // delimited format assumed ...
                S=splitCSV(s);
                for (j=0; j<10; j++) {
                  try {
                    if (indexesDB[activeDB][j].contains("|")) {  // workaround to join fields
                        String [] S2=indexesDB[activeDB][j].split("\\|");
                        int field1=Integer.valueOf(S2[0]);
                        int field2=Integer.valueOf(S2[1]);
                        TM6.setValueAt(S[field1-1].trim()+"|"+S[field2-1].trim(), i, j);
                    }
                    else if (indexesDB[activeDB][j].contains("-")) {  // workaround to separate fields
                        String [] S2=indexesDB[activeDB][j].split("-");
                        field=Integer.valueOf(S2[0]);
                        String [] S3=S[field-1].split("-");
                        TM6.setValueAt(S3[0].trim(), i, j);                        
                    }
                    else {
                        field=Integer.valueOf(indexesDB[activeDB][j]);
                        TM6.setValueAt(S[field-1].trim(), i, j);
                    }
                  } catch (NumberFormatException ex) {}
                    catch (ArrayIndexOutOfBoundsException ex) {}
                    catch (IndexOutOfBoundsException ex) {}
                }
            }
            jTable6.invalidate();
            break;
        }
}

    private void importAllPlayers() {
        int j, start, stop, field, Elo;
        String s, team, player, element; String[] S;
        TableModel myTM = jTable1.getModel();
        if (selectedDB[activeDB].exists()) 
        try {
            // read disc file 
            BufferedReader in = new BufferedReader(new FileReader(selectedDB[activeDB]));
            if (!in.ready()) throw new IOException();
            // read data
            for (;;) {
                if ((s = in.readLine()) == null) break;   // EOF !
                if (s.trim().equals("")) continue;        // skip empty record
                if (addedRows == MAXTEAMS) break;                           // max size of list 
                player = "";
                if (delimiterDB[activeDB].equals(" ")) { // fixed format ...
                    for (j=0; j<10; j++) {
                        S=indexesDB[activeDB][j].split("-");
                        start = stop = 0; element="";
                        try {
                            start = Integer.valueOf(S[0])-1;
                            stop = Integer.valueOf(S[1]);
                            element = s.substring(start, stop);
                        } catch (NumberFormatException ex) {}
                          catch (ArrayIndexOutOfBoundsException ex) {}
                          catch (IndexOutOfBoundsException ex) {}
                        player += element.trim()+";";
                    }
                } else {        // delimited format assumed ...
                    S=splitCSV(s);
                    for (j=0; j<10; j++) {
                      element="";
                      try {
                        if (indexesDB[activeDB][j].contains("|")) {  // workaround to join fields
                            String [] S2=indexesDB[activeDB][j].split("\\|");
                            int field1=Integer.valueOf(S2[0]);
                            int field2=Integer.valueOf(S2[1]);
                            element = S[field1-1].trim()+"|"+S[field2-1].trim();
                        }
                        else if (indexesDB[activeDB][j].contains("-")) {  // workaround to separate fields
                            String [] S2=indexesDB[activeDB][j].split("-");
                            field=Integer.valueOf(S2[0]);
                            String [] S3=S[field-1].split("-");
                            element = S3[0].trim();                        
                        }
                        else {
                            field=Integer.valueOf(indexesDB[activeDB][j]);
                            element = S[field-1];
                        }
                      } catch (NumberFormatException ex) {}
                        catch (ArrayIndexOutOfBoundsException ex) {}
                        catch (IndexOutOfBoundsException ex) {}
                      player += element.trim()+";";
                    }
                }
                S = player.split(";");
                if (S.length==0) continue;        // skip empty record
                Elo=0;
                try {
                    Elo=Integer.valueOf(S[6]); // first consider FIDE rating
                } catch (NumberFormatException ex) {}
                  catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                try {
                    if (Elo==0) Elo=Integer.valueOf(S[8]); // second National rating
                } catch (NumberFormatException ex) {}
                  catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
                team = S[0];
                myTM.setValueAt(team,addedRows,0);      // player name
                myTM.setValueAt(""+Elo,addedRows,1);    // Elo
                myTM.setValueAt(player,addedRows,2);    // player string
                Rectangle rect = jTable1.getCellRect(addedRows, 0, true);    // make the row visible (scroll)
                jTable1.scrollRectToVisible(rect);
                jTable1.setRowSelectionInterval(addedRows,addedRows);       // enhance the added row
                sortIndex[addedRows]=(short)(addedRows+1);                  // assign temp values
                tempIndex[addedRows]=(short)(addedRows);
                addedRows++;
            }
            in.close();
            safeExitAllowed = false;
            jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
        } catch (IOException ex) {}
    }

    private void importAllTeams() {
        int i, j, start, stop, field, Elo, n=0;
        String s, team, player, element, Elom="", temp=""; String[] S;
        TableModel myTM = jTable1.getModel();
        if (selectedDB[activeDB].exists()) 
        try {
            // read disc file 
            BufferedReader in = new BufferedReader(new FileReader(selectedDB[activeDB]));
            if (!in.ready()) throw new IOException();
            team=""; i=0;
            // read data
            for (;;) {
                if ((s = in.readLine()) == null) break;   // EOF !
                if (s.trim().equals("")) continue;        // skip empty record
                if (addedRows == MAXTEAMS) break;                           // max size of list 
                player = "";
                {        // delimited format assumed ...
                    S=splitCSV(s);
                    for (j=0; j<10; j++) {
                      element="";
                      try {
                        if (indexesDB[activeDB][j].contains("|")) {  // workaround to join fields
                            String [] S2=indexesDB[activeDB][j].split("\\|");
                            int field1=Integer.valueOf(S2[0]);
                            int field2=Integer.valueOf(S2[1]);
                            element = S[field1-1].trim()+"|"+S[field2-1].trim();
                        }
                        else if (indexesDB[activeDB][j].contains("-")) {  // workaround to separate fields
                            String [] S2=indexesDB[activeDB][j].split("-");
                            field=Integer.valueOf(S2[0]);
                            String [] S3=S[field-1].split("-");
                            element = S3[0].trim();                        
                        }
                        else {
                            field=Integer.valueOf(indexesDB[activeDB][j]);
                            element = S[field-1];
                            if (j==0) temp = S[field-2];        // the team name is in the previous field of the player name
                        }
                      } catch (NumberFormatException ex) {}
                        catch (ArrayIndexOutOfBoundsException ex) {}
                        catch (IndexOutOfBoundsException ex) {}
                      player += element.trim()+";";
                    }
                }
                S = player.split(";");
                if (S.length==0) continue;        // skip empty record
                try {
                    if (temp.length()>2) {
                        if (!team.equals("")) {
                            if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),addedRows-1,1);    // mean Elo
                        }
                        n=0; Elom="";
                        team=temp.replaceAll("\"", ""); temp="";
                        myTM.setValueAt(team,addedRows,0);   // team name
                        myTM.setValueAt("0",addedRows,1);    // Elo
                        Rectangle rect = jTable1.getCellRect(addedRows, 0, true);    // make the row visible (scroll)
                        jTable1.scrollRectToVisible(rect);
                        jTable1.setRowSelectionInterval(addedRows,addedRows);       // enhance the added row
                        sortIndex[addedRows]=(short)(addedRows+1);                  // assign temp values
                        tempIndex[addedRows]=(short)(addedRows);
                        addedRows++;
                        i=0;
                    } else { i++; if (i>maxPlayersPerTeam) continue;
                        myTM.setValueAt(player,addedRows-1,i+1);    // player string
                        Elo=0;
                        try {
                            Elo=Integer.valueOf(S[6]); // first consider FIDE rating
                        } catch (NumberFormatException ex) {}
                          catch (ArrayIndexOutOfBoundsException ex) {}
                          catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (Elo==0) Elo=Integer.valueOf(S[8]); // second National rating
                        } catch (NumberFormatException ex) {}
                          catch (ArrayIndexOutOfBoundsException ex) {}
                          catch (IndexOutOfBoundsException ex) {}
                        if (Elo>0) {n++; Elom+=Elo+";";}
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {}
                  catch (IndexOutOfBoundsException ex) {}
            }
            if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),addedRows-1,1);    // mean Elo last team
            in.close();
            safeExitAllowed = false;
            jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
        } catch (IOException ex) {}
    }
    
    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // send output to printer 
            new PrintMe(jTextPane1).print();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void calculateTeamScores(int delta) {
        short i,k,r,i1,i2,score1,score2,scoreTeam1,scoreTeam2;
        short [] nPlayed = new short[addedRows];
        String [] S=null; String s;
        int index, played, r1, r2;
        boolean bye;
        for (i=0; i<addedRows; i++) {     // reset scores
            teamScores[i][0] = teamScores[i][2];  // start from acceleration, if any
            teamScores[i][1] = tempScore[i] = 0; nPlayed[i]=0;
            tempIndex[i] = i;       // initialize arrays
            if (doSortOrder) sortIndex[i]=(short)(i+1);  
            for (k=0; k<6; k++) Z[i][k]=0;
        }
        addedPairs = (short)((addedRows+1)/2);   
        for (r=0; r<(short)(currRound+delta); r++)
        for (k=0; k<addedPairs; k++) {
            S = roundsDetail[k][0][r].split("-");  // get match
            if (S.length != 4) continue;
            i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
            i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
            if (i1==0) continue;                      // not yet paired
            bye=false;
            if (i2==0 || S[3].indexOf('f')>0) bye=true;
            score1=integerScore(S[2]);
            score2=integerScore(S[3]);
            teamScores[i1-1][1] += score1;       // increment player scores 
            if (i2>0) teamScores[i2-1][1] += score2;
            played=scoreTeam1=scoreTeam2=0;
            for (i=1; i<=maxBoards;i++) {
                S = roundsDetail[k][i][r].split("-");  // get individual match
                if (S.length != 4) continue;
                r1 = integerScore(S[2]);  // result 1
                r2 = integerScore(S[3]);  // result 2
                if (r1==0 && r2==0) continue;     // not played at all
                played++;
            }
            if (played>0) {
                if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                if (score1>played*5) scoreTeam1=2;
                if (score2>played*5) scoreTeam2=2;
                if (score1==played*5) scoreTeam1=1;
                if (score2==played*5) scoreTeam2=1;
                if (score1<played*5) scoreTeam1=0;
                if (score2<played*5) scoreTeam2=0;
                teamScores[i1-1][0] += scoreTeam1;       // increment team scores 
                if (i2>0) teamScores[i2-1][0] += scoreTeam2;
                if (!bye) {
                    tempScore[i1-1] += scoreTeam1; nPlayed[i1-1]++;          // increment team scores and game counter
                    if (i2>0) { tempScore[i2-1] += scoreTeam2; nPlayed[i2-1]++; }
                }
            }
        }
        for (i=0; i<addedRows; i++) {     // for tie-break take into account games not played
            if (nPlayed[i]>0)               // but for teams having at least one valid game
                for (r=nPlayed[i]; r<(short)(currRound+delta); r++)
                    tempScore[i] += 1;    // add draw against itself
        }
        orderTeams(delta);
        if (delta==-1) return;      // not final ranking, may teminate
        // tie breaks !! selection from flagsTieBreak 
        for (k=0; k<6; k++) {
            try {
                s=flagsTieBreak.substring(k,k+1); 
                index=Integer.parseInt(s, 16);
                if (index==1) BuchholzCut1Teams(k);
                if (index==2) BuchholzTotalTeams(k);
                if (index==3) BuchholzMedianTeams(k);
                if (index==4) SonnebornTeams(k);
                if (index==5) DirectTeams(k);
                if (index==6) AROTeams(k);
                if (index==7) TPRTeams(k);
                if (index==8) WonGamesTeams(k);
                if (index==9) GamesWithBlackTeams(k);
                if (index==10) ScorePercentTeams(k);
                if (index==11) WeightedBoardsTeams(k);
            }    
            catch (NullPointerException ex) {}
            catch (StringIndexOutOfBoundsException ex) {}
            catch (NumberFormatException ex) {}
        }
        orderTeams(delta);
        
    }

    private void orderTeams(int delta) {    // do order decreasing ranking (and tie break if needed)
        short i,j,k,i0,i1,value;
        int Elo0,Elo1;
        boolean swap, exEquo;
        TableModel myTM=jTable1.getModel();

        if (doSortOrder) {       // at first round, establish order (assign IDs)
            switch (teamOrder) {  // check jRadioButton 1 - 4 ordering
                case 1:           // decreasing Elo 
                    for (i=0; i<addedRows-1; i++)
                    for (j=(short)(i+1); j<addedRows; j++) {
                        i0 = tempIndex[i];
                        i1 = tempIndex[j];
                        Elo0=getElo(i0+1,0);
                        Elo1=getElo(i1+1,0);
                        /* order by decreasing Elo and alphabetically */
                        if (  (Elo0<Elo1) || ((Elo0==Elo1) && 
                           ((""+myTM.getValueAt(sortIndex[i0]-1,0)).compareToIgnoreCase(""+myTM.getValueAt(sortIndex[i1]-1,0))>0)) ) {
                                value=tempIndex[j]; // swap values
                                tempIndex[j] = tempIndex[i];
                                tempIndex[i] = value;
                        }
                    }
                    break;
                case 2:           // random 
                    for (k=0; k<5000; k++) {
                        i = (short)(Math.round(Math.random()*(addedRows-1)));
                        j = (short)(Math.round(Math.random()*(addedRows-1)));
                        i0 = tempIndex[i];
                        i1 = tempIndex[j];
                        value=tempIndex[j]; // swap values
                        tempIndex[j] = tempIndex[i];
                        tempIndex[i] = value;
                    }
                    break;
                case 3:           // alphabetical 
                    for (i=0; i<addedRows-1; i++)
                    for (j=(short)(i+1); j<addedRows; j++) {
                        i0 = tempIndex[i];
                        i1 = tempIndex[j];
                        if ((""+myTM.getValueAt(sortIndex[i0]-1,0)).compareToIgnoreCase(""+myTM.getValueAt(sortIndex[i1]-1,0))>0) {
                            value=tempIndex[j]; // swap values
                            tempIndex[j] = tempIndex[i];
                            tempIndex[i] = value;
                        }
                    }
                    break;
                case 4:           // as entered ... nothing to do! ...
                    break;
            }  
            for (i=0; i<MAXTEAMS; i++) {     // fix sort order forever
                sortIndex[i]=(short)(tempIndex[i]+1); 
                tempIndex[i] = i;
            }
            doSortOrder=false;
        } else {  /* at following rounds do sort decreasing Team score, decreasing Player score, decreasing tie break */
            for (i=0; i<addedRows-1; i++)
            for (j=(short)(i+1); j<addedRows; j++) {
                i0=tempIndex[i];
                i1=tempIndex[j];
                if (teamScores[i0][0]<teamScores[i1][0]) {      /* less team score, swap*/ 
                        k=tempIndex[i]; // swap values
                        tempIndex[i] = tempIndex[j];
                        tempIndex[j] = k;
                } else if (teamScores[i0][0]==teamScores[i1][0] && teamScores[i0][1]<teamScores[i1][1]) {     /* or less player score*/ 
                        k=tempIndex[i]; // swap values
                        tempIndex[i] = tempIndex[j];
                        tempIndex[j] = k;
                } else if (delta==0 && teamScores[i0][0]==teamScores[i1][0] && teamScores[i0][1]==teamScores[i1][1]) {                          
                                                                     /* check tie break criteria */
                        swap = false;
                        for (k=0; k<6; k++) if (Z[i0][k]<Z[i1][k]) { swap = true; break; }
                                            else if ((Z[i0][k]>Z[i1][k])) break;
                        if (swap) {
                            k=tempIndex[i]; // swap values
                            tempIndex[i] = tempIndex[j];
                            tempIndex[j] = k;
                        }
                }
            }
            /* handle ex-equo differently as requested by setup parameter */
            switch (teamOrder) {  // check jRadioButton 1 - 4 ordering 
                case 1:           // decreasing Elo 
                    for (i=0; i<addedRows-1; i++)
                    for (j=(short)(i+1); j<addedRows; j++) {
                        i0 = tempIndex[i];
                        i1 = tempIndex[j];
                        Elo1=Elo0=0;
                        try {
                            Elo0=Integer.valueOf(""+myTM.getValueAt(sortIndex[i0]-1,1));
                        } catch (NumberFormatException ex) {}
                        try {
                            Elo1=Integer.valueOf(""+myTM.getValueAt(sortIndex[i1]-1,1));
                        } catch (NumberFormatException ex) {}
                            if ((teamScores[i0][0]==teamScores[i1][0]) && (teamScores[i0][1]==teamScores[i1][1]) ) { // only if all equal
                                exEquo = true;
                                for (k=0; k<6 ; k++) if (Z[i0][k]!=Z[i1][k]) { exEquo = false; break; }
                                if (exEquo) 
                                    if (  (Elo0<Elo1) || ((Elo0==Elo1) && (i0>i1)) ) {
                                        value=tempIndex[j]; // swap values
                                        tempIndex[j] = tempIndex[i];
                                        tempIndex[i] = value;
                                    }
                            }
                    }
                    break;
                case 2:           // random ... nothing to do! ...
                    break;
                case 3:           // alphabetical 
                case 4:           // as entered
                    for (i=0; i<addedRows-1; i++)
                    for (j=(short)(i+1); j<addedRows; j++) {
                        i0 = tempIndex[i];
                        i1 = tempIndex[j];
                        if ((teamScores[i0][0]==teamScores[i1][0]) && (teamScores[i0][1]==teamScores[i1][1]) ) { // only if all equal
                            exEquo = true;
                            for (k=0; k<6 ; k++) if (Z[i0][k]!=Z[i1][k]) { exEquo = false; break; }
                            if (exEquo) 
                                if (i0>i1) {
                                    value=tempIndex[j]; // swap values
                                    tempIndex[j] = tempIndex[i];
                                    tempIndex[i] = value;
                                }
                        }
                    }
                    break;
            }
        }
}
    
private void SonnebornTeams(int index) {
        short i,k,r,i0,i1,i2,score1,score2,scoreTeam1,scoreTeam2, sonneb;
        int j,played,r1,r2;
        String [] S=null, S2;
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); 
                for (r=0; r<currRound; r++) {
                  sonneb = tempScore[i0-1];
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    played=0;
                    for (j=1; j<=maxBoards;j++) {
                        S2 = roundsDetail[k][j][r].split("-");  // get individual match
                        if (S2.length != 4) continue;
                        r1 = integerScore(S2[2]);  // result 1
                        r2 = integerScore(S2[3]);  // result 2
                        if (r1==0 && r2==0) continue;     // not played at all
                        played++;
                    }
                    if (played>0 && i0==i1) {
                        if (i2>0 && (S[3].indexOf('f')<0)) {
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                            else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>played*5) scoreTeam1=2;
                            if (score2>played*5) scoreTeam2=2;
                            if (score1==played*5) scoreTeam1=1;
                            if (score2==played*5) scoreTeam2=1;
                            if (score1<played*5) scoreTeam1=0;
                            if (score2<played*5) scoreTeam2=0;
                            sonneb = (short)(tempScore[i2-1]*scoreTeam1);
                        }
                        break;
                    }
                    else if (played>0 && i0==i2) {
                        if (i1>0 && (S[3].indexOf('f')<0)) {
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                            else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>played*5) scoreTeam1=2;
                            if (score2>played*5) scoreTeam2=2;
                            if (score1==played*5) scoreTeam1=1;
                            if (score2==played*5) scoreTeam2=1;
                            if (score1<played*5) scoreTeam1=0;
                            if (score2<played*5) scoreTeam2=0;
                            sonneb = (short)(tempScore[i1-1]*scoreTeam2);
                        }
                        break;
                    }
                  }
                  Z[i][index]+=sonneb;
                }
        }
    }

    private void DirectTeams(int index) {
        short k,r,i1,i2,score1,score2,scoreTeam1,scoreTeam2;
        String [] S=null; 
        for (r=0; r<currRound; r++) 
          for (k=0; k<addedPairs; k++) {
                S = roundsDetail[k][0][r].split("-");  // get match
                if (S.length != 4) continue;
                i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                if (i1==0 || i2==0) continue;             // BYE or empty row? skip
                if ((teamScores[i1-1][0]==teamScores[i2-1][0]) && (teamScores[i1-1][1]==teamScores[i2-1][1]) )  // only if equal scores
                    if (S[3].indexOf('f')<0) {
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        scoreTeam1=scoreTeam2=0;
                        if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                        else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                        else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                        // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                        if (score1>maxBoards*5) scoreTeam1=2;
                        if (score2>maxBoards*5) scoreTeam2=2;
                        if (scoreTeam1==2)  Z[i1-1][index]+=1;
                        if (scoreTeam2==2)  Z[i2-1][index]+=1;
                    }
          }        
    }

    private void BuchholzCut1Teams(int index) {
        short i,j,k,r,i0,i1,i2,score1,score2,scoreTeam1,scoreTeam2,prevScore,buchholz,cut1;
        int virtualScore,played,r1,r2;
        String [] S=null; 
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); cut1 = 10000;
                prevScore=0; 
                for (r=0; r<currRound; r++) {
                  buchholz = -1; 
                  i1=i2=score1=score2=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i0==i1) {
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        if (i2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i2-1];
                        break;
                    }
                    else if (i0==i2) {
                        score1=integerScore(S[3]);
                        score2=integerScore(S[2]);
                        if (i1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i1-1];
                        break;
                    }
                  }
                  played=scoreTeam1=scoreTeam2=0;
                  for (j=1; j<=maxBoards;j++) {
                        S = roundsDetail[k][j][r].split("-");  // get individual match
                        if (S.length != 4) continue;
                        r1 = integerScore(S[2]);  // result 1
                        r2 = integerScore(S[3]);  // result 2
                        if (r1==0 && r2==0) continue;     // not played at all
                        played++;
                  }
                  if (played>0) {
                    if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                    else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                    else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                    // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                    if (score1>played*5) scoreTeam1=2;
                    if (score2>played*5) scoreTeam2=2;
                    if (score1==played*5) scoreTeam1=1;
                    if (score2==played*5) scoreTeam2=1;
                    if (score1<played*5) scoreTeam1=0;
                    if (score2<played*5) scoreTeam2=0;
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (scoreTeam1==0) virtualScore=2;     // for bye invert score
                      if (scoreTeam1==1) virtualScore=1;
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=1*(currRound-1-r);      // add 1 point following games vitual player
                  } 
                  Z[i][index]+=buchholz;                // add Buchholz to total
                  if (cut1>buchholz) cut1=buchholz;     // store cut1
                  prevScore+=scoreTeam1;
                }
                if (cut1!=10000) Z[i][index]-=cut1;              // subtract cut 1
        }
   }

    private void BuchholzTotalTeams(int index) {
        short i,j,k,r,i0,i1,i2,score1,score2,scoreTeam1,scoreTeam2,prevScore,buchholz;
        int virtualScore,played,r1,r2;
        String [] S=null; 
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); 
                prevScore=0; 
                for (r=0; r<currRound; r++) {
                  buchholz = -1; 
                  i1=i2=score1=score2=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i0==i1) {
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        if (i2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i2-1];
                        break;
                    }
                    else if (i0==i2) {
                        score1=integerScore(S[3]);
                        score2=integerScore(S[2]);
                        if (i1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i1-1];
                        break;
                    }
                  }
                  played=scoreTeam1=scoreTeam2=0;
                  for (j=1; j<=maxBoards;j++) {
                        S = roundsDetail[k][j][r].split("-");  // get individual match
                        if (S.length != 4) continue;
                        r1 = integerScore(S[2]);  // result 1
                        r2 = integerScore(S[3]);  // result 2
                        if (r1==0 && r2==0) continue;     // not played at all
                        played++;
                  }
                  if (played>0) {
                    if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                    else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                    else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                    // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                    if (score1>played*5) scoreTeam1=2;
                    if (score2>played*5) scoreTeam2=2;
                    if (score1==played*5) scoreTeam1=1;
                    if (score2==played*5) scoreTeam2=1;
                    if (score1<played*5) scoreTeam1=0;
                    if (score2<played*5) scoreTeam2=0;
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (scoreTeam1==0) virtualScore=2;     // for bye invert score
                      if (scoreTeam1==1) virtualScore=1;
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=1*(currRound-1-r);      // add 1 point following games vitual player
                  } 
                  Z[i][index]+=buchholz;                // add Buchholz to total
                  prevScore+=scoreTeam1;
                }
        }        
   }

    private void BuchholzMedianTeams(int index) {
        short i,j,k,r,i0,i1,i2,score1,score2,scoreTeam1,scoreTeam2,prevScore,buchholz,cut1,cut2;
        int virtualScore,played,r1,r2;
        String [] S=null; 
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); cut1 = 10000; cut2=0;
                prevScore=0; 
                for (r=0; r<currRound; r++) {
                  buchholz = -1; 
                  i1=i2=score1=score2=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i0==i1) {
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        if (i2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i2-1];
                        break;
                    }
                    else if (i0==i2) {
                        score1=integerScore(S[3]);
                        score2=integerScore(S[2]);
                        if (i1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[i1-1];
                        break;
                    }
                  }
                  played=scoreTeam1=scoreTeam2=0;
                  for (j=1; j<=maxBoards;j++) {
                        S = roundsDetail[k][j][r].split("-");  // get individual match
                        if (S.length != 4) continue;
                        r1 = integerScore(S[2]);  // result 1
                        r2 = integerScore(S[3]);  // result 2
                        if (r1==0 && r2==0) continue;     // not played at all
                        played++;
                  }
                  if (played>0) {
                    if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                    else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                    else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                    // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                    if (score1>played*5) scoreTeam1=2;
                    if (score2>played*5) scoreTeam2=2;
                    if (score1==played*5) scoreTeam1=1;
                    if (score2==played*5) scoreTeam2=1;
                    if (score1<played*5) scoreTeam1=0;
                    if (score2<played*5) scoreTeam2=0;
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (scoreTeam1==0) virtualScore=2;     // for bye invert score
                      if (scoreTeam1==1) virtualScore=1;
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=1*(currRound-1-r);      // add 1 point following games vitual player
                  } 
                  Z[i][index]+=buchholz;                // add Buchholz to total
                  if (cut1>buchholz) cut1=buchholz;     // store cut1
                  if (cut2<buchholz) cut2=buchholz;     // store cut2
                  prevScore+=scoreTeam1;
                }
                if (cut1!=10000) Z[i][index]-=cut1;              // subtract cut 1
                if (cut2!=0)     Z[i][index]-=cut2;              // subtract cut 2
        }
   }
    
   private void AROTeams(int index) {
        short i,i0;
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); 
                Z[i][index] = calculateAROteam(i0, 0);
        }
    }
    
   private void TPRTeams(int index) {
        int i, percentage;
        AROTeams(index);
        for (i=0; i<addedRows; i++) { 
                percentage = Math.round(tempScore[i]*50f/currRound);
                if (percentage<=50) Z[i][index] += deltaPerformance[percentage]; 
                else                Z[i][index] -= deltaPerformance[100-percentage];
        }
    }
    
    private void WonGamesTeams(int index) {
        short i,k,r,i0,i1,i2,score1,score2,scoreTeam1,scoreTeam2;
        String [] S=null;
        boolean bye;
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); 
                for (r=0; r<currRound; r++) {
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i0==i1) {
                        if (i2>0 && (S[3].indexOf('f')<0)) {
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                            else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>maxBoards*5) scoreTeam1=2;
                            if (score2>maxBoards*5) scoreTeam2=2;
                            if (scoreTeam1==2) Z[i][index]+=1;
                        }
                        break;
                    }
                    else if (i0==i2) {
                        if (i1>0 && (S[3].indexOf('f')<0)) {
                            score1=integerScore(S[2]);
                            score2=integerScore(S[3]);
                            scoreTeam1=scoreTeam2=0;
                            if (score1>score2) {scoreTeam1=2;scoreTeam2=0;}
                            else if (score1<score2) {scoreTeam1=0;scoreTeam2=2;}
                            else if (score1!=0) {scoreTeam1=1;scoreTeam2=1;}
                            // check if some non standard results ... i.e. 1-1, 1/2-0, ...
                            if (score1>maxBoards*5) scoreTeam1=2;
                            if (score2>maxBoards*5) scoreTeam2=2;
                            if (scoreTeam2==2) Z[i][index]+=1;
                        }
                        break;
                    }
                  }
                }
        }
    }
    
    private void GamesWithBlackTeams(int index) {
        short i,k,r,i0,i1,i2;
        String [] S=null;
        for (i=0; i<addedRows; i++) { 
                i0=(short)(i+1); 
                for (r=0; r<currRound; r++) {
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i0==i1) break;      // played with White
                    else if (i0==i2) {
                        if (i1>0 && (S[3].indexOf('f')<0)) Z[i][index]+=1;
                        break;
                    }
                  }
                }
        }
    }
    
    private void ScorePercentTeams(int index) {
        int i, percentage;
        for (i=0; i<addedRows; i++) { 
                percentage = Math.round(tempScore[i]*50f/currRound);
                Z[i][index] = percentage;
        }
    }
    
    private void WeightedBoardsTeams(int index) {
        short i,k,r,i1,i2,score1,score2;
        int i0, board, wb, coeff;
        String [] S=null; String s;
        for (i=0; i<addedRows; i++) { 
            i0=i+1; 
            wb=0;
            for (r=0; r<currRound; r++) {
                for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=1; board<=maxBoards;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        coeff=21-2*board; if (coeff<0) coeff=0;     // if over 10 boards ?!
                        if (i0==i1) {
                            score1 = integerScore(S[2]);
                            if (i2==0 || (S[3].indexOf('f')>0)) score1=5;   // forfeit draw agaist itself
                            wb+=score1*coeff;
                        }
                        else if (i0==i2) {
                            score2 = integerScore(S[3]);
                            if (i1==0 || (S[3].indexOf('f')>0)) score2=5;   // forfeit draw agaist itself
                            wb+=score2*coeff;
                        }
                      }
                      break;
                    }
                }
            }
            Z[i][index] = wb;
        }
    }
    
    private void calculatePlayerScore(short board) {
        short i,k,r,i1,i2,score1,score2,p1,p2;
        String [] S=null; String s;
        int index, maxPlayers=addedRows*maxPlayersPerTeam;
        short [] nPlayed = new short[maxPlayers];
        boolean bye;
        for (i=0; i<maxPlayers; i++) {     // reset scores
            playerScore[i] = -1; nPlayed[i] = 0;
            tempIndex[i] = i; tempScore[i]=0;
            for (k=0; k<6; k++) Z[i][k]=0;
        }
        short lboard=board;
        short uboard=board;
        if (board==0) { lboard=1; uboard=4; }
        addedPairs = (short)((addedRows+1)/2);   
        for (r=0; r<currRound; r++)
        for (k=0; k<addedPairs; k++) 
        for (board=lboard; board<=uboard;board++) {
            S = roundsDetail[k][0][r].split("-");  // get match
            if (S.length != 4) continue;
            i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
            if (i1==0) continue;    // empty row?
            i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
            S = roundsDetail[k][board][r].split("-");  // get match
            if (S.length != 4) continue;
            p1 = Integer.valueOf(S[0]).shortValue();  // index to first Player
            p2 = Integer.valueOf(S[1]).shortValue();  // index to second Player
            bye=false;
            if (p1==0 || p2==0 || S[3].indexOf('f')>0) bye=true;
            score1=integerScore(S[2]);
            score2=integerScore(S[3]);
            if (i1>0 && p1>0) {
                if (playerScore[(i1-1)*maxPlayersPerTeam+(p1-1)]==-1) playerScore[(i1-1)*maxPlayersPerTeam+(p1-1)]=0;
                playerScore[(i1-1)*maxPlayersPerTeam+(p1-1)] += score1;          // increment player score
            }         
            if (i2>0 && p2>0) {
                if (playerScore[(i2-1)*maxPlayersPerTeam+(p2-1)]==-1) playerScore[(i2-1)*maxPlayersPerTeam+(p2-1)]=0;
                playerScore[(i2-1)*maxPlayersPerTeam+(p2-1)] += score2;
            }         
            if (!bye) {
                if (i1>0 && p1>0) {
                    tempScore[(i1-1)*maxPlayersPerTeam+(p1-1)] += score1;   // increment player scores and game counter 
                    nPlayed[(i1-1)*maxPlayersPerTeam+(p1-1)]++;                
                }          
                if (i2>0 && p2>0) {
                    tempScore[(i2-1)*maxPlayersPerTeam+(p2-1)] += score2;
                    nPlayed[(i2-1)*maxPlayersPerTeam+(p2-1)]++;
                }          
            }
        }
        for (i=0; i<maxPlayers; i++) {     // for tie-break take into account games not played
            if (nPlayed[i]>0)                // but for players having at least one valid game
                for (r=nPlayed[i]; r<currRound; r++)
                    tempScore[i] += 5;    // add draw against itself
        }
        // tie breaks !! selection from flagsTieBreak 
        // note: if the opponent of the player changed board during the tournament, tie-breaks are
        //       calculated ONLY for the games played on the same board of the player! 
        for (k=0; k<6; k++) {
            try {
                s=flagsTieBreak.substring(k,k+1); 
                index=Integer.parseInt(s, 16);
                if (index==1) BuchholzCut1Players(k, lboard, uboard);
                if (index==2) BuchholzTotalPlayers(k, lboard, uboard);
                if (index==3) BuchholzMedianPlayers(k, lboard, uboard);
                if (index==4) SonnebornPlayers(k, lboard, uboard);
                if (index==5) DirectPlayers(k, lboard, uboard);
                if (index==6) AROPlayers(k, lboard, uboard);
                if (index==7) TPRPlayers(k, lboard, uboard);
                if (index==8) WonGamesPlayers(k, lboard, uboard);
                if (index==9) GamesWithBlackPlayers(k, lboard, uboard);
                if (index==10) ScorePercentPlayers(k, lboard, uboard);

            }    
            catch (NullPointerException ex) {}
            catch (StringIndexOutOfBoundsException ex) {}
            catch (NumberFormatException ex) {}
        }
        orderPlayers();

   }
    
    private void orderPlayers() {
        short i, j, k, i1, i2, value, t1, p1, t2, p2;
        boolean swap, exEquo;
        int maxPlayers=addedRows*maxPlayersPerTeam, Elo1, Elo2;
        // do order decreasing ranking
        for (i=0; i<maxPlayers-1; i++)
        for (j=(short)(i+1); j<maxPlayers; j++) {
            i1=tempIndex[i];
            i2=tempIndex[j];
            if (playerScore[i1]<playerScore[i2]) {
                value=tempIndex[i]; // swap values
                tempIndex[i] = tempIndex[j];
                tempIndex[j] = value;
            } else if (playerScore[i1]==playerScore[i2]) {
                swap = false;
                for (k=0; k<6; k++) if (Z[i1][k]<Z[i2][k]) { swap = true; break; }
                                    else if ((Z[i1][k]>Z[i2][k])) break;
                if (swap) {
                    value=tempIndex[i]; // swap values
                    tempIndex[i] = tempIndex[j];
                    tempIndex[j] = value;
                }
            }
        }
        /* handle ex-equo differently as requested by setup parameter */
        switch (teamOrder) {  // check jRadioButton 1 - 4 ordering 
            case 1:           // decreasing Elo 
                for (i=0; i<maxPlayers-1; i++) {
                    i1 = tempIndex[i];
                    if (playerScore[i1]==-1) continue;
                    for (j=(short)(i+1); j<maxPlayers; j++) {
                        i1 = tempIndex[i];
                        i2 = tempIndex[j];
                        if (playerScore[i2]==-1) continue;
                        if (playerScore[i1]==playerScore[i2] ) { // only if equal score
                            exEquo = true;
                            for (k=0; k<6 ; k++) if (Z[i1][k]!=Z[i2][k]) { exEquo = false; break; }
                            if (exEquo) {
                                t1=(short)(i1/maxPlayersPerTeam+1); 
                                p1=(short)(i1%maxPlayersPerTeam+1);  
                                t2=(short)(i2/maxPlayersPerTeam+1); 
                                p2=(short)(i2%maxPlayersPerTeam+1); 
                                Elo1=getElo(t1, p1);
                                Elo2=getElo(t2, p2);
                                if (  (Elo1<Elo2) || ((Elo1==Elo2) && (i1>i2)) ) {
                                    value=tempIndex[j]; // swap values
                                    tempIndex[j] = tempIndex[i];
                                    tempIndex[i] = value;
                                }
                            }
                        }
                    }
                } 
                break;
            case 2:           // random ... nothing to do! ...
                break;
            case 3:           // alphabetical 
            case 4:           // as entered
                for (i=0; i<maxPlayers-1; i++) {
                    i1 = tempIndex[i];
                    if (playerScore[i1]==-1) continue;
                    for (j=(short)(i+1); j<maxPlayers; j++) {
                        i1 = tempIndex[i];
                        i2 = tempIndex[j];
                        if (playerScore[i2]==-1) continue;
                        if (playerScore[i1]==playerScore[i2] ) { // only if all equal
                            exEquo = true;
                            for (k=0; k<6 ; k++) if (Z[i1][k]!=Z[i2][k]) { exEquo = false; break; }
                            if (exEquo) 
                                if (i1>i2) {
                                    value=tempIndex[j]; // swap values
                                    tempIndex[j] = tempIndex[i];
                                    tempIndex[i] = value;
                                }
                        }
                    }
                }
                break;
        } 

    }

    private void SonnebornPlayers(int index, short lboard, short uboard) {
        short i,k,r,i0,i1,i2,score1,score2,p0,p1,p2,board,sonneb;
        int maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1); 
                p0=(short)(i%maxPlayersPerTeam+1);  
                for (r=0; r<currRound; r++) {
                  sonneb = tempScore[i];
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (i0==i1 && p0==p1) {
                            if (i2>0 && p2>0 && (S[3].indexOf('f')<0)) {
                                score1 = integerScore(S[2]);
                                if (score1>5) sonneb = (short)(tempScore[(i2-1)*maxPlayersPerTeam+p2-1]*2);
                                if (score1==5) sonneb = tempScore[(i2-1)*maxPlayersPerTeam+p2-1];
                                if (score1<5) sonneb = 0;
                            }
                            break;
                        }
                        else if (i0==i2 && p0==p2) {
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0)) {
                                score2 = integerScore(S[3]);
                                if (score2>5) sonneb = (short)(tempScore[(i1-1)*maxPlayersPerTeam+p1-1]*2);
                                if (score2==5) sonneb = tempScore[(i1-1)*maxPlayersPerTeam+p1-1];
                                if (score2<5) sonneb = 0;
                            }
                            break;
                        }
                      }
                      break;
                    }
                  }
                  Z[i][index]+=sonneb*5;
                }
          }
        
    }

    private void DirectPlayers(int index, short lboard, short uboard) {
        short i,j,k,r,i1,i2,score1,score2,p1,p2,board;
        String [] S=null;
        for (r=0; r<currRound; r++) 
          for (k=0; k<addedPairs; k++) {
               S = roundsDetail[k][0][r].split("-");  // get match
               if (S.length != 4) continue;
               i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
               i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
               if (i1==0 || i2==0) continue;
               for (board=lboard; board<=uboard;board++) {
                    S = roundsDetail[k][board][r].split("-");  // get board
                    if (S.length != 4) continue;
                    p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                    p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                    if (p1==0 || p2==0 || S[3].indexOf('f')>0) continue;  // no bye & forfeit !
                    i=(short)((i1-1)*maxPlayersPerTeam+p1-1); 
                    j=(short)((i2-1)*maxPlayersPerTeam+p2-1); 
                    if ((playerScore[i]==playerScore[j]) )  { // only if equal score
                        score1=integerScore(S[2]);
                        score2=integerScore(S[3]);
                        if (score1==10)  Z[i][index]+=100;
                        if (score2==10)  Z[j][index]+=100;
                    }
                }
          }        
        
    }

    private void BuchholzCut1Players(int index, short lboard, short uboard) {
        short i,k,r,i0,i1,i2,score,prevScore,p0,p1,p2,board,buchholz,cut1;
        int virtualScore, maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1);          // team
                p0=(short)(i%maxPlayersPerTeam+1);          // player
                cut1 = 10000;
                prevScore=0;
                for (r=0; r<currRound; r++) {
                  buchholz = -1; score=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (i0==i1 && p0==p1) {
                            score=integerScore(S[2]);
                            if (i2>0 && p2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i2-1)*maxPlayersPerTeam+p2-1];
                            break;
                        }
                        else if (i0==i2 && p0==p2) {
                            score=integerScore(S[3]);
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i1-1)*maxPlayersPerTeam+p1-1];
                            break;
                        }
                      }
                      break;
                    }
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (score==0) virtualScore=10;    // for bye invert score
                      if (score==5) virtualScore=5;
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=5*(currRound-1-r);      // add 1/2 point following games vitual player
                  } 
                  Z[i][index]+=buchholz*10;             // add Buchholz to total
                  if (cut1>buchholz) cut1=buchholz;     // store cut1
                  prevScore+=score;
                }
                if (cut1!=10000) Z[i][index]-=cut1*10;              // subtract cut 1
        }        
    }

    private void BuchholzTotalPlayers(int index, short lboard, short uboard) {
        short i,k,r,i0,i1,i2,score,prevScore,p0,p1,p2,board,buchholz;
        int virtualScore, maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1);          // team
                p0=(short)(i%maxPlayersPerTeam+1);          // player
                prevScore=0; 
                for (r=0; r<currRound; r++) {
                  buchholz = -1; score=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (i0==i1 && p0==p1) {
                            score=integerScore(S[2]);
                            if (i2>0 && p2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i2-1)*maxPlayersPerTeam+p2-1];
                            break;
                        }
                        else if (i0==i2 && p0==p2) {
                            score=integerScore(S[3]);
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i1-1)*maxPlayersPerTeam+p1-1];
                            break;
                        }
                      }
                      break;
                    }
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (score==0) virtualScore=10;    // for bye invert score
                      if (score==5) virtualScore=5;    
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=5*(currRound-1-r);      // add 1/2 point following games vitual player
                  } 
                  Z[i][index]+=buchholz*10;             // add Buchholz to total
                  prevScore+=score;
                }
        }        
    }
    

    private void BuchholzMedianPlayers(int index, short lboard, short uboard) {
        short i,k,r,i0,i1,i2,score,prevScore,p0,p1,p2,board,buchholz,cut1,cut2;
        int virtualScore, maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1);          // team
                p0=(short)(i%maxPlayersPerTeam+1);          // player
                cut1 = 10000; cut2 = 0;
                prevScore=0;
                for (r=0; r<currRound; r++) {
                  buchholz = -1; score=0;
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (i0==i1 && p0==p1) {
                            score=integerScore(S[2]);
                            if (i2>0 && p2>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i2-1)*maxPlayersPerTeam+p2-1];
                            break;
                        }
                        else if (i0==i2 && p0==p2) {
                            score=integerScore(S[3]);
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0)) buchholz = tempScore[(i1-1)*maxPlayersPerTeam+p1-1];
                            break;
                        }
                      }
                      break;
                    }
                  }
                  if (buchholz==-1) {                   // Kallithea 2009 !!
                      virtualScore=0;
                      if (score==0) virtualScore=10;    // for bye invert score
                      if (score==5) virtualScore=5;
                      buchholz =prevScore;              // add previous points done
                      buchholz+=virtualScore;           // add virtual score    
                      buchholz+=5*(currRound-1-r);      // add 1/2 point following games vitual player
                  } 
                  Z[i][index]+=buchholz*10;             // add Buchholz to total
                  if (cut1>buchholz) cut1=buchholz;     // store cut1
                  if (cut2<buchholz) cut2=buchholz;     // store cut2
                  prevScore+=score;
                }
                if (cut1!=10000) Z[i][index]-=cut1*10;              // subtract cut 1
                if (cut2!=0)     Z[i][index]-=cut2*10;              // subtract cut 2
        }        
    }
        
   private void AROPlayers(int index, short lboard, short uboard) {
        int i, maxPlayers=addedRows*maxPlayersPerTeam;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                Z[i][index] = calculateAROindividual(i, lboard, uboard)*100;              
        }
    }
        
   private void TPRPlayers(int index, short lboard, short uboard) {
        int i, percentage, maxPlayers=addedRows*maxPlayersPerTeam;
        AROPlayers(index, lboard, uboard);
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                percentage = Math.round(tempScore[i]*10f/currRound);
                if (percentage<=50) Z[i][index] += deltaPerformance[percentage]*100; 
                else                Z[i][index] -= deltaPerformance[100-percentage]*100;
        }
    }

    private void WonGamesPlayers(int index, short lboard, short uboard) {
        short i,k,r,i0,i1,i2,score1,score2,p0,p1,p2,board;
        int maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1); 
                p0=(short)(i%maxPlayersPerTeam+1);  
                for (r=0; r<currRound; r++) {
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1 || i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (i0==i1 && p0==p1) {
                            if (i2>0 && p2>0 && (S[3].indexOf('f')<0)) {
                                score1 = integerScore(S[2]);
                                if (score1==10) Z[i][index]+=100;
                            }
                            break;
                        }
                        else if (i0==i2 && p0==p2) {
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0)) {
                                score2 = integerScore(S[3]);
                                if (score2==10) Z[i][index]+=100;
                            }
                            break;
                        }
                      }
                      break;
                    }
                  }
                }
          }
        
    }

    private void GamesWithBlackPlayers(int index, short lboard, short uboard) {
        // I suppose that players of a Team on the boards alternate colors
        short i,k,r,i0,i1,i2,p0,p1,p2,board;
        int maxPlayers=addedRows*maxPlayersPerTeam;
        String [] S=null;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                i0=(short)(i/maxPlayersPerTeam+1); 
                p0=(short)(i%maxPlayersPerTeam+1);  
                for (r=0; r<currRound; r++) {
                  for (k=0; k<addedPairs; k++) {
                    S = roundsDetail[k][0][r].split("-");  // get match
                    if (S.length != 4) continue;
                    i1 = Integer.valueOf(S[0]).shortValue();  // index to first Team
                    i2 = Integer.valueOf(S[1]).shortValue();  // index to second Team
                    if (i1==0) continue;    // empty row?
                    if (i0==i1) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (p0==p2) {
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0) && board%2==0) Z[i][index]+=100;
                            break;
                        }
                      }
                      break;
                    }
                    if (i0==i2) {
                      for (board=lboard; board<=uboard;board++) {
                        S = roundsDetail[k][board][r].split("-");  // get board
                        if (S.length != 4) continue;
                        p1 = Integer.valueOf(S[0]).shortValue();  // index to first player
                        p2 = Integer.valueOf(S[1]).shortValue();  // index to second player
                        if (p0==p2) {
                            if (i1>0 && p1>0 && (S[3].indexOf('f')<0) && board%2==1) Z[i][index]+=100;
                            break;
                        }
                      }
                      break;
                    }
                  }
                }
          }
        
    }

    private void ScorePercentPlayers(int index, short lboard, short uboard) {
        int i, percentage, maxPlayers=addedRows*maxPlayersPerTeam;
        for (i=0; i<maxPlayers; i++) { 
                if (playerScore[i]==-1) continue;
                percentage = Math.round(tempScore[i]*1000f/currRound);
                Z[i][index] = percentage;
        }
        
    }
    
    private void readResults() {
        // reread the table of results
        int i0=0,i1=0,j0=0,j1=0;
        short row = prevRow;
        if (row<0) return;
        if (jTable7.getSelectedRow()==-1) return;   // no changes!
        TableModel myTM = jTable1.getModel();
        TableModel myTM5 = jTable5.getModel();
        TableModel myTM7 = jTable7.getModel();
        TableCellEditor t = jTable7.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        jTable7.clearSelection();
        String team1 = ""+myTM5.getValueAt(row,1);
        String team2 = ""+myTM5.getValueAt(row,2);
        String [] s1 = team1.split("\\|");   // get first Team 
        if (s1[0].compareTo("null")==0) return;     // avoid processing blank rows
        String [] s2 = team2.split("\\|");   // get second Team 
        i0 = Integer.valueOf(s1[0]).intValue();  // index to first Team
        i1 = Integer.valueOf(s2[0]).intValue();  // index to second Team
        j0 = sortIndex[i0-1]-1;              // absolute Team position
        if (i1>0) j1 = sortIndex[i1-1]-1;              // absolute Team position
        short score1=0, score2=0, forfeits=0;
        for (int j=0;j<maxBoards;j++) {                     // loop boards
            String name0 = ""+myTM7.getValueAt(j,0);  // name of first player
            String name1 = ""+myTM7.getValueAt(j,1);  // name of second player
            int p0=0, p1=0;
            for (int k=1;k<=maxPlayersPerTeam;k++) {                    // scan for absolute position
                if (name0.equals(minimalPlayerData(""+myTM.getValueAt(j0,k+1)))) p0=k;
                if (i1>0) if (name1.equals(minimalPlayerData(""+myTM.getValueAt(j1,k+1)))) p1=k;
            }
            String result = ""+myTM7.getValueAt(j,2); 
            String s = ""+p0+"-"+p1+"-"+result;
            if (!s.equals(roundsDetail[row][j+1][currRound-1])) {
                roundsDetail[row][j+1][currRound-1] = s;    // something changed. Store
                safeExitAllowed = false;                    // and set global flag
            }
            String r[] = result.split("-");
            if (r[1].indexOf('f')>0) {r[1]=r[1].substring(0,1); forfeits++;} // forfeit ?
            if (r[0].equals("1")) score1+=10;
            if (r[0].equals("\u00BD")) score1+=5;
            if (r[1].equals("\u00BD")) score2+=5;
            if (r[1].equals("1")) score2+=10;
        }
        String teamScore = ""+stringscore(score1*10)+"-"+stringscore(score2*10);
        if (forfeits==maxBoards) teamScore+="f";    // all forfeits?
        myTM5.setValueAt(teamScore,row,3);
        roundsDetail[row][0][currRound-1] = ""+i0+"-"+i1+"-"+teamScore;
    }
    
    private void insertResults() {
        // insert results
        int i,i0=0,i1=0,j0=0,j1=0;
        boolean noResult = false;
        currRound = Integer.valueOf(jSpinner1.getValue().toString()).shortValue();
        if (currRound==0) return;  
        prevRow = -1;
        jLabel16.setText(Main.localizedText("Insert results for round")+" :" + currRound);
        addedPairs=(short)((addedRows+1)/2);
        TableModel myTM = jTable1.getModel();
        myTableModel myTM5 = new myTableModel(addedPairs,4);
        myTM5.setColumnName(0, Main.localizedText("Board"));
        if (maxPlayersPerTeam == 1) {
            myTM5.setColumnName(1, Main.localizedText("player A"));
            myTM5.setColumnName(2, Main.localizedText("player B"));
            myTM5.setColumnName(3, Main.localizedText("result"));
        } else {
            myTM5.setColumnName(1, Main.localizedText("Team A"));
            myTM5.setColumnName(2, Main.localizedText("Team B"));
            myTM5.setColumnName(3, Main.localizedText("score"));
        }
        jTable5.setModel(myTM5);
        jTable5.getColumnModel().getColumn(0).setMinWidth(50);  // adapt to text
        jTable5.getColumnModel().getColumn(0).setMaxWidth(50);  // adapt to text
        TableColumn col = jTable5.getColumnModel().getColumn(3);
        col.setMinWidth(80);
        col.setMaxWidth(80);
        myTableModel myTM7 = new myTableModel(maxBoards,3, true);
        myTM7.setColumnName(0, Main.localizedText("player A"));
        myTM7.setColumnName(1, Main.localizedText("player B"));
        myTM7.setColumnName(2, Main.localizedText("result"));
        jTable7.setModel(myTM7);
        col = jTable7.getColumnModel().getColumn(2);
        col.setMinWidth(80);
        col.setMaxWidth(80);
        col.setCellEditor(new MyComboBoxEditor(possiblePlayerResults));
        for (i=0; i<addedPairs; i++) {  // for each pair
            myTM5.setValueAt(""+(i+1), i, 0);
            String[] s = roundsDetail[i][0][currRound-1].split("-");
            if (s.length != 4) continue;
            i0 = Integer.valueOf(s[0]).intValue();  // index to first Team
            i1 = Integer.valueOf(s[1]).intValue();  // index to second Team
            if(i0==0) {
                myTM5.setValueAt("", i, 1);
                myTM5.setValueAt("", i, 2);
                continue;
            }
            j0 = sortIndex[i0-1]-1;              // absolute Team position
            myTM5.setValueAt(""+i0+"|"+myTM.getValueAt(j0,0),i,1);
            if (i1>0) { 
                j1 = sortIndex[i1-1]-1;              // absolute Team position
                myTM5.setValueAt(""+i1+"|"+myTM.getValueAt(j1,0),i,2);
            }
            else myTM5.setValueAt("0| BYE",i,2);
            myTM5.setValueAt(s[2]+"-"+s[3],i,3);
            if (!noResult && (s[2]+"-"+s[3]).equals("0-0")) {
                noResult = true;
                Rectangle rect = jTable5.getCellRect(i, 0, true);    // make the row visible (scroll)
                jTable5.scrollRectToVisible(rect);
                jTable5.setRowSelectionInterval(i, i);
            }
        } 
        if (!noResult) {
            Rectangle rect = jTable5.getCellRect(0, 0, true);    // make the row visible (scroll)
            jTable5.scrollRectToVisible(rect);
            jTable5.setRowSelectionInterval(0, 0);
        }
        jTable5.invalidate();
        jTable7.invalidate();
        java.awt.event.MouseEvent evt2=null;
        jTable5.requestFocusInWindow();
        if (maxPlayersPerTeam>1) jTable5MouseReleased(evt2);

    }
    
    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
    // finished inserting results
        int j,k;
        boolean completed=true;
        String s;
        String [] S;
        readResults();      // read previuos results
        // check all results were inserted
        for (j=0; j<addedPairs; j++) {
            s=roundsDetail[j][0][currRound-1];
            if (s.equals("0-0-0-0")) continue;  // empty row?
            S=s.split("-");
            if (S.length!=4) continue;  // some kind of setup error?
            s=S[2]+"-"+S[3];    // compose the team results string
            if (s.equals("0-0")) {completed=false; break;}  // no results at all!
            for (k=1; k<=maxBoards; k++) {
                s=roundsDetail[j][k][currRound-1];
                if (s.equals("0-0-0-0")) continue;  // empty row?
                S=s.split("-");
                if (S.length!=4) continue;  // some kind of setup error?
                s=S[2]+"-"+S[3];    // compose the results string
                if (s.equals("0-0")) {completed=false; break;}  // no results at all!
            }
            if (!completed) break;
        }
        safeExitAllowed = false;
        if (batchOrder) { if (tournamentType!=5) updateFloaters(); return; }
        if (completed) {
            if (tournamentType!=5) updateFloaters();
            JOptionPane.showMessageDialog(this,
                Main.localizedText("Results are complete."),
                Main.localizedText("info"),
                JOptionPane.INFORMATION_MESSAGE);
            if (currRound==maxRound) missingResults=false;
        } else {
            JOptionPane.showMessageDialog(this,
                Main.localizedText("Some results missing!"),
                Main.localizedText("info"),
                JOptionPane.WARNING_MESSAGE);
            if (currRound==maxRound) missingResults=true;
        }
        if (currRound==maxRound) jButton7.setEnabled(true);  // enable next round button
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
    // remove last pair
        if (engineRunning) return;
        TableModel myTM3 = jTable3.getModel();
        TableModel myTM4 = jTable4.getModel();
        TableModel myTM2 = jTable2.getModel();
        for (;;) {
            if (addedPairs==0) break;              // all removed
            addedPairs--;
            String team1 = ""+myTM2.getValueAt(addedPairs,0);
            if (team1.equals("")) continue;
            String team2 = ""+myTM2.getValueAt(addedPairs,1);
            myTM2.setValueAt("",addedPairs,0);
            myTM2.setValueAt("",addedPairs,1);
            myTM3.setValueAt(team1,pairFrom[addedPairs][0],0);
            myTM4.setValueAt(team1,pairFrom[addedPairs][0],0);
            if (team2.equalsIgnoreCase("0| BYE"));
            else {
                myTM3.setValueAt(team2,pairFrom[addedPairs][1],0);
                myTM4.setValueAt(team2,pairFrom[addedPairs][1],0);
            }
            break;
        }
        for (int k=0; k<=maxBoards; k++) roundsDetail[addedPairs][k][currRound-1]="0-0-0-0";  // reset last pair
        safeExitAllowed = false;
    }//GEN-LAST:event_jButton22ActionPerformed

    private String join(String [] s, String delimiter) {
        // Join an array of strings into a String using the specified delimiter. 
        // This is the counterpart of String [] string.split(String delimiter);
        String buffer="";
        int i=1,n=s.length;
        if (n>0) buffer=s[0];
        for (;i<n;i++) {
            buffer=buffer+delimiter+s[i];
        }
        return buffer;
    } 
    
    public class CustomTableCellRenderer extends DefaultTableCellRenderer 
    {
        // This sets the background cell colour to RED for previous pairing
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) 
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if( value instanceof Object )
            {
                if(!isSelected)
                if(isAlreadyPaired(jTable3, value.toString()))
                {
                    cell.setBackground( Color.RED );
                    // cell.setForeground();
                    // cell.setFont();
                }
                else
                {
                    cell.setBackground( Color.WHITE );
                }
            }
            return cell;
        }
    }
    
    public boolean isAlreadyPaired(JTable table, String value){
        // test if a Team can be paired with another one
        if (maxRound==1) return false;          // first round: do not test
        int row = table.getSelectedRow();
        if (row>=0) {                               // if something is selected ...
            TableModel myTM = table.getModel();
            String team1 = ""+myTM.getValueAt(row,0);
            if (team1.equals("")) return false;  // empty row !?
            if (value.equals("")) return false;  // empty row !?
            String [] s1 = team1.split("\\|");   // get first Team index
            String [] s2 = value.split("\\|");   // get second Team index
            if (s1[0].equals(s2[0])) return true;   // not the same, please !
            if (tournamentPairing==3) return false;  // absolutely free ??
            int i1, i2;
            i1 = indexAt(Integer.valueOf(s1[0])-1, tempIndex);
            i2 = indexAt(Integer.valueOf(s2[0])-1, tempIndex);
            if (establishCompatibility(i1, i2, jCheckBox1.isSelected(), "")==0) return true;
            if (jCheckBox1.isSelected() && colourDue(i1)==colourDue(i2)) return true;
        }
        return false;
    }

    public class MyComboBoxEditor extends DefaultCellEditor  {
        public MyComboBoxEditor(String[] items) {
            super(new JComboBox(items));
            JComboBox JC = (JComboBox)super.editorComponent;
            JC.setSize(80,160);
            JC.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTable7KeyPressed(evt);
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    jTable7KeyTyped(evt);
                }
            });
        }
    }
    
    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // end pairing. Store values
        if (engineRunning) return;
        TableModel myTM2 = jTable2.getModel();
        String [] s1,s2,r = null;
        if (addedPairs<(countAvailableTeams()+1)/2) {
            JOptionPane.showMessageDialog(this,
                Main.localizedText("some pair missing!"),
                Main.localizedText("Info"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (int i=0; i<addedPairs; i++) {  // for each pair
            s1 = (""+myTM2.getValueAt(i,0)).split("\\|");   // get first Team index
            s2 = (""+myTM2.getValueAt(i,1)).split("\\|");   // get second Team index
            r = roundsDetail[i][0][currRound-1].split("-");   // get details
            if (s1.length < 4) continue;
            if (s2.length < 2) continue;
            if (r.length != 4) continue;
            if ((!s1[0].equals(r[0]) || (!s2[0].equals(r[1])))) {               // pair changed?
                roundsDetail[i][0][currRound-1]=s1[0]+"-"+s2[0]+"-0-0";  // placeholder for result
                for (short j=1;j<=maxBoards;j++) roundsDetail[i][j][currRound-1]="0-0-0-0";  // placeholder for individual results
                safeExitAllowed = false; if (currRound==maxRound) missingResults=true;
            }
            if (maxPlayersPerTeam==1 && s2[0].equals("0"))       // individual, BYE?
                if (!roundsDetail[i][0][currRound-1].equals(s1[0]+"-0-1-0f")) {
                    roundsDetail[i][0][currRound-1]=s1[0]+"-0-1-0f";  // placeholder for result
                    roundsDetail[i][1][currRound-1]="1-0-1-0f";       // placeholder for individual results
                    safeExitAllowed = false; if (currRound==maxRound) missingResults=true;
                }
        }
        if (currRound>1) orderPairs();
        if (tournamentType!=5) addFloaters();  // not for 'round robin'
        if (!batchOrder) {
            (new File("round.txt")).delete();                 // erase round file
            // save to disc file 
            try {
                FileWriter fOut = new FileWriter("round.txt");
                String lineSeparator = System.getProperty( "line.separator" );
                availableTeams = countAvailableTeams();
                int effectivePairs=(availableTeams+1)/2;
                // write data
                fOut.write(""+effectivePairs+lineSeparator);
                for (int i=0; i<effectivePairs; i++) {  // for each pair
                    r = roundsDetail[i][0][currRound-1].split("-");   // get details
                    if (r.length != 4) continue;
                    fOut.write(""+r[0]+" "+r[1]+lineSeparator);
                }
                fOut.close();
            } catch (IOException ex) {}
            JOptionPane.showMessageDialog(this,
                Main.localizedText("Pairs ordered by ranking and stored."),
                Main.localizedText("Info"),
                JOptionPane.INFORMATION_MESSAGE);
        }
        jTabbedPane1.setSelectedIndex(2);   // go to results page!

    }//GEN-LAST:event_jButton21ActionPerformed

    class myTableModel extends AbstractTableModel {
    public myTableModel(int rows, int cols, boolean editable) {
        super();
        maxRows=rows;
        maxCols=cols;
        isEditable=editable;
        rowData=new String[maxRows][maxCols]; // allocate local memory for the arrays
        colNames=new String[maxCols];
    }
    public myTableModel(int rows, int cols) {
        super();
        maxRows=rows;
        maxCols=cols;
        rowData=new String[maxRows][maxCols]; // allocate local memory for the arrays
        colNames=new String[maxCols];
    }
       // @Override
    public int getColumnCount() { 
       return maxCols; 
    }     
        @Override
    public String getColumnName(int col) {
        return colNames[col];
    }
    public void setColumnName(int col, String s) {
        colNames[col] = s;
    } 
      //  @Override
    public int getRowCount() { 
       return maxRows; 
    }     
        @Override
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = ""+value;
        fireTableCellUpdated(row, col);
    }    
      //  @Override
    public Object getValueAt(int row, int col) { 
        return  rowData[row][col]; 
    }
        @Override
    public boolean isCellEditable(int x, int y) {
        return isEditable;
    }
    private String[][] rowData=null; 
    private String[] colNames=null;
    private int maxRows=0, maxCols=0;
    private boolean isEditable=false;
}; 

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // add pair
        if (engineRunning) return;
        short row3,row4;
        int r,j, i1, i2, compatible, availableTeams;
        row3 = (short)jTable3.getSelectedRow();
        row4 = (short)jTable4.getSelectedRow();
        TableModel myTM3 = jTable3.getModel();
        TableModel myTM4 = jTable4.getModel();
        TableModel myTM2 = jTable2.getModel();
        String team1 = ""+myTM3.getValueAt(row3,0);
        String team2 = ""+myTM4.getValueAt(row4,0);
        if (team1.equals("")) return ;  // empty row !?
        if (team2.equals("")) return ;  // empty row !?
        availableTeams = countAvailableTeams();
        for (;;) {
            if (addedPairs==0) break;              // none added yet
            addedPairs--;
            String team = ""+myTM2.getValueAt(addedPairs,0);
            if (team.equals("")) continue;
            addedPairs++;
            break;
        }
        if (row3 == row4) { // cannot pair with itself except last and odd
            if (availableTeams==(addedPairs*2+1)) {
                               // last one, odd players. give BYE
                myTM3.setValueAt("",row3,0);
                myTM3.setValueAt("",row4,0);
                myTM4.setValueAt("",row3,0);
                myTM4.setValueAt("",row4,0);
                myTM2.setValueAt(team1,addedPairs,0);
                myTM2.setValueAt("0| BYE",addedPairs,1);
                pairFrom[addedPairs][0]=row3;  // store position may be needed later
                pairFrom[addedPairs][1]=row4;
                addedPairs++;
            }
            safeExitAllowed = false;
            return;       
        }
        if (isAlreadyPaired(jTable3, team2)) return;  // yet not allowed, change options !!
        myTM3.setValueAt("",row3,0);
        myTM3.setValueAt("",row4,0);
        myTM4.setValueAt("",row3,0);
        myTM4.setValueAt("",row4,0);
        String [] s1 = team1.split("\\|");   // get first Team index
        String [] s2 = team2.split("\\|");   // get second Team index
        i1 = indexAt(Integer.valueOf(s1[0])-1, tempIndex);
        i2 = indexAt(Integer.valueOf(s2[0])-1, tempIndex);
        compatible = establishCompatibility(i1, i2, jCheckBox1.isSelected(), "");
        if (compatible==0) return;
        if (compatible==2) {
            // swap team1, team2
            String team=team1; team1=team2; team2=team;
            short row=row3; row3=row4; row4=row;
            String s=s1[0]; s1[0]=s2[0]; s2[0]=s;
        }
//        if (addedPairs*2==addedRows) addedPairs--;
        myTM2.setValueAt(team1,addedPairs,0);
        myTM2.setValueAt(team2,addedPairs,1);
        pairFrom[addedPairs][0]=row3;  // store position may be needed later
        pairFrom[addedPairs][1]=row4;
        roundsDetail[addedPairs][0][currRound-1]=s1[0]+"-"+s2[0]+"-0-0";
        for (int k=0; k<maxPlayersPerTeam; k++) roundsDetail[addedPairs][k+1][currRound-1]="0-0-0-0";
        addedPairs++;
        safeExitAllowed = false;
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // player ranking
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        jDialog3.setVisible(true);      // open options dialog (modal)
        TableModel myTM = jTable1.getModel();
        FileWriter fOut;
        int groupInd, i, i0, Elo, EloL = 0, EloU =0, position, ageL=0, ageU=0, age, k, index;
        short pi, prevpi;
        String team, player, playerM, EloString, positionString, ageString, category, s;
        String[] limits, S;
        short board=0, uBoard=maxBoards;
        boolean found;
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        int maxPlayers=addedRows*maxPlayersPerTeam;
        try {
            fOut = new FileWriter(tempFile);  // create a temp file
            String lineSeparator = System.getProperty( "line.separator" );
            fOut.write("<html>"+lineSeparator);
            fOut.write("<head>"+lineSeparator);
            fOut.write("<font Size=\"+2\">"+Main.localizedText("Tournament")+": "+tournamentName+lineSeparator);
            fOut.write("</head><body>"+lineSeparator);
            fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Player ranking after Round")+": "+currRound+lineSeparator);
            if (maxBoards==1) uBoard=0;
            for (; board<=uBoard; board++) {
                calculatePlayerScore(board);
                for (groupInd=-1;groupInd<15;groupInd++){
                    if (board>0 && groupInd>=0) break;       // do only one cycle 
                    if (board==0) {
                        try {
                          if (groupInd==-1) {
                              fOut.write("<br><br><font Size=\"+1\">"+Main.localizedText("All players")+"<br>"+lineSeparator);
                              if (currRound==maxRound && missingResults) fOut.write("<br>("+Main.localizedText("Some results missing!")+")"+lineSeparator);
                          }
                          else if (group[groupInd%5].length()>0) {
                            limits=groupLimit[groupInd%5].split(";");
                            if (rankingByAge && groupInd/5==0) {
                            try {
                                ageL=Integer.valueOf(limits[0]); 
                                ageU=Integer.valueOf(limits[1]); 
                            } catch (NumberFormatException ex) {continue;}
                              catch (ArrayIndexOutOfBoundsException ex) {continue;}
                              catch (IndexOutOfBoundsException ex) {continue;}
                              fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
                              fOut.write("<br><br><font Size=\"+1\">"+group[groupInd%5]+" ("+Main.localizedText("between")+" "+ageL+" "+Main.localizedText("and")+" "+ageU+")<br>"+lineSeparator);
                            }
                            else if (rankingByCategory && groupInd/5==1) {
                              fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
                              fOut.write("<br><br><font Size=\"+1\">"+group[groupInd%5]+" ("+groupLimit[groupInd%5]+")<br>"+lineSeparator);
                            }
                            else if (rankingByELO && groupInd/5==2) {
                            try {
                                EloL=Integer.valueOf(limits[0]); 
                                EloU=Integer.valueOf(limits[1]); 
                            } catch (NumberFormatException ex) {continue;}
                              catch (ArrayIndexOutOfBoundsException ex) {continue;}
                              catch (IndexOutOfBoundsException ex) {continue;}
                              fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
                              fOut.write("<br><br><font Size=\"+1\">"+group[groupInd%5]+" ("+Main.localizedText("between")+" "+EloL+" "+Main.localizedText("and")+" "+EloU+")<br>"+lineSeparator);
                            }
                            else continue;
                          }
                          else continue;
                        } catch (NullPointerException ex) {break;}
                    }
                    else { 
                        fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
                        fOut.write("<br><br><font Size=\"+1\">"+Main.localizedText("Board"+board)+":<br>"+lineSeparator);
                    }
                    fOut.write("<table>"+lineSeparator);
                    fOut.write("<tr><td colspan=\"6\" bgColor=\"yellow\">&nbsp;</td>");
                    fOut.write("<td colspan=\"6\" bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("tie break")+"</td>"+lineSeparator);
                    fOut.write("</tr>"+lineSeparator);
                    fOut.write("<tr><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Rank")+"</td>");
                    fOut.write("<td bgColor=\"yellow\">"+Main.localizedText("ID")+"</td>");
                    fOut.write("<td colspan=\"3\" bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Player")+"</td>");
                    fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("score")+"</td>");
                    for (k=0; k<6; k++) {
                        try {
                            s=flagsTieBreak.substring(k,k+1); 
                            index=Integer.parseInt(s, 16);
                            if (index>0 && index<11) fOut.write("<td bgColor=\"yellow\" align=\"center\">"+tieBreaks[index]+"</td>");
                        }    
                        catch (NullPointerException ex) {}
                        catch (StringIndexOutOfBoundsException ex) {}
                        catch (NumberFormatException ex) {}
                    }
                    fOut.write("</tr>"+lineSeparator);
                    position = 0; prevpi=-1; 
                    for (i=0; i<maxPlayers; i++) {
                            i0 = tempIndex[i];
                            pi = playerScore[i0];
                            if (pi==-1) break;
                            try {
                                team = ""+myTM.getValueAt(sortIndex[i0/maxPlayersPerTeam]-1,0);
                                if (maxPlayersPerTeam==1) team="";
                                player = ""+myTM.getValueAt(sortIndex[i0/maxPlayersPerTeam]-1,i0%maxPlayersPerTeam+2);
                                playerM = minimalPlayerData(player);
                                if (playerM.isEmpty()) continue;
                            } catch (ArrayIndexOutOfBoundsException ex) {continue;}
                              catch (IndexOutOfBoundsException ex) {continue;}
                            if (rankingByAge && groupInd>=0 && groupInd/5==0) {      // ranking by AGE requested
                                try {
                                    S=player.split(";");
                                    ageString = S[2].trim();
                                    if (ageString.length()<2) continue;     // take only last two digits (yy)
                                    ageString = ageString.substring(ageString.length()-2);
                                    age=Integer.valueOf(ageString); 
                                    if (ageL <= ageU) {
                                        if (age<ageL || age>ageU) continue;
                                    } else
                                        if (age<ageL && age>ageU) continue;
                                } catch (NumberFormatException ex) {continue;}
                                  catch (ArrayIndexOutOfBoundsException ex) {continue;}
                                  catch (IndexOutOfBoundsException ex) {continue;}
                            }
                            if (rankingByCategory && groupInd>=0 && groupInd/5==1) {      // ranking by Category requested
                                S=playerM.split(";");
                                category = S[2].trim();
                                S = groupLimit[groupInd%5].split(";");
                                found = false;
                                for (k=0; k<S.length; k++) {
                                    if (S[k].trim().equalsIgnoreCase(category)) found=true;
                                }
                                if (!found) continue;
                            }
                            if (rankingByELO && groupInd>=0 && groupInd/5==2) {      // ranking by Elo requested
                                try {
                                    S=playerM.split(";");
                                    EloString = S[3].replaceAll("\\D","");  // strip non digit and convert to integer
                                    Elo=Integer.valueOf(EloString); 
                                    if (Elo<EloL || Elo>EloU) continue;
                                } catch (NumberFormatException ex) {continue;}
                                  catch (ArrayIndexOutOfBoundsException ex) {continue;}
                                  catch (IndexOutOfBoundsException ex) {continue;}
                            }
                            position++; 
                            if (pi==prevpi) {
                                positionString=""+position; // ex-equo!
                                fOut.write("<tr><td bgColor=\"silver\" align=\"center\">&nbsp;</td><td colspan=\"11\"><hr /></td></tr>"+lineSeparator);
                            } else {
                                prevpi=pi; 
                                positionString="<font Size=\"+1\">"+position;
                                fOut.write("<tr><td colspan=\"12\"><hr /></td></tr>"+lineSeparator);
                            }
                            fOut.write("<tr>"+lineSeparator);
                            fOut.write("<td bgColor=\"silver\" align=\"center\">"+positionString+"</td>");
                            fOut.write("<td>"+(i0+1)+"</td>");
                            S=playerM.split(";");
                            fOut.write("<td>"+S[0]+"</td>");
                            fOut.write("<td>"+S[2]+"</td>");
                            fOut.write("<td>"+S[3]+"</td>");
                            fOut.write("<td align=\"center\"><font Size=\"+1\">"+HTMLscore(pi*10)+"</td>");
                            for (k=0; k<6; k++) {
                                try {
                                    s=flagsTieBreak.substring(k,k+1); 
                                    index=Integer.parseInt(s, 16);
                                    if (index>0 && index<11) fOut.write("<td align=\"center\"><font Size=\"+1\">"+HTMLscore(Z[i0][k])+"</td>");
                                }    
                                catch (NullPointerException ex) {}
                                catch (StringIndexOutOfBoundsException ex) {}
                                catch (NumberFormatException ex) {}
                            }
                            fOut.write("</tr>"+lineSeparator);
                
                    }
                    fOut.write("<tr><td colspan=\"12\"><hr /></td></tr>"+lineSeparator);
                    fOut.write("</table>"+lineSeparator);
                }
            }
            fOut.write("</body>"+lineSeparator);
            fOut.write("</html>"+lineSeparator);
            fOut.close();
                       
            java.net.URL url=null;
            try {
                url = new java.net.URL("file:///"+tempFile);
            } catch (MalformedURLException ex) {
//                
            }
            if (url != null) 
            try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
            } catch (IOException ex) {}
              catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }
        
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // team ranking
        if (engineRunning) return;
        if (selectedFile==null) {
            Object[] options = {Main.localizedText("OK")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Please, open or save the tournament data first and redo"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            return;
        }
        jDialog3.setVisible(true);      // open options dialog (modal)
        TableModel myTM = jTable1.getModel();
        long buchholz, t1, t2, z, prevz;
        int groupInd, i, i0, Elo, EloL = 0, EloU =0, position, k, index;
        short ps, pi, prevps, prevpi;
        String team, positionString, s;
        String[] limits;
        FileWriter fOut;
        calculateTeamScores(0);
        String tempFile = currentDirectory.getAbsolutePath()+"/temp.html";
        try {
            fOut = new FileWriter(tempFile);  // create a temp file
            String lineSeparator = System.getProperty( "line.separator" );
            fOut.write("<html>"+lineSeparator);
            fOut.write("<head>"+lineSeparator);
            fOut.write("<font Size=\"+2\">"+Main.localizedText("Tournament")+": "+tournamentName+lineSeparator);
            fOut.write("</head><body>"+lineSeparator);
            fOut.write("<br><font Size=\"+1\">"+Main.localizedText("Ranking after Round")+": "+currRound+"<br>"+lineSeparator);
            for (groupInd=-1;groupInd<5;groupInd++){
                try {
                  if (groupInd==-1) fOut.write("<br><font Size=\"+1\">"+Main.localizedText("All Teams")+"<br>"+lineSeparator);
                  else if (rankingByELO && group[groupInd].length()>0) {
                    limits=groupLimit[groupInd].split(";");
                    try {
                        EloL=Integer.valueOf(limits[0]); 
                        EloU=Integer.valueOf(limits[1]); 
                    } catch (NumberFormatException ex) {continue;}
                      catch (ArrayIndexOutOfBoundsException ex) {continue;}
                      catch (IndexOutOfBoundsException ex) {continue;}
                    fOut.write("<p style=\"page-break-before: always;\"></p>"+lineSeparator);
                    fOut.write("<br><font Size=\"+1\">"+group[groupInd]+" ("+Main.localizedText("between")+" "+EloL+" "+Main.localizedText("and")+" "+EloU+")<br>"+lineSeparator);
                  }
                  else continue;
                } catch (NullPointerException ex) {break;}
                fOut.write("<table>"+lineSeparator);
                fOut.write("<tr><td colspan=\"4\" bgColor=\"yellow\">&nbsp;</td>");
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("Team")+"</td>");
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("Players")+"</td>");
                fOut.write("<td colspan=\"6\" bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("tie break")+"</td></tr>"+lineSeparator);
                fOut.write("<tr><td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Rank")+"</td>");
                fOut.write("<td bgColor=\"yellow\">"+Main.localizedText("ID")+"</td>");
                fOut.write("<td bgColor=\"yellow\"><font Size=\"+1\">"+Main.localizedText("Team")+"</td>");
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("Elo")+"</td>");
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("score")+"</td>");
                fOut.write("<td bgColor=\"yellow\" align=\"center\"><font Size=\"+1\">"+Main.localizedText("score")+"</td>");
                for (k=0; k<6; k++) {
                    try {
                        s=flagsTieBreak.substring(k,k+1); 
                        index=Integer.parseInt(s, 16);
                        if (index>0) fOut.write("<td bgColor=\"yellow\" align=\"center\">"+tieBreaks[index]+"</td>");
                    }    
                    catch (NullPointerException ex) {}
                    catch (StringIndexOutOfBoundsException ex) {}
                    catch (NumberFormatException ex) {}
                }
                fOut.write("</tr><tr></tr>"+lineSeparator);
                position = 0; prevps=-1; prevpi=-1; prevz=-1;
                for (i=0; i<addedRows; i++) {
                    i0 = tempIndex[i]; i0++;
                    team = ""+myTM.getValueAt(sortIndex[i0-1]-1,0);
                    Elo = getElo(i0, 0);
                    if (rankingByELO && groupInd>=0)       // ranking by Elo requested
                        if (Elo<EloL || Elo>EloU) continue;
                    ps = teamScores[i0-1][0];
                    pi = teamScores[i0-1][1];
                    position++; 
                    if (ps==prevps && pi==prevpi) {
                        positionString=""+position; // ex-equo!
                        fOut.write("<tr><td bgColor=\"silver\" align=\"center\">&nbsp;</td><td colspan=\"11\"><hr /></td></tr>"+lineSeparator);
                    } else {
                        prevps=ps; prevpi=pi; 
                        positionString="<font Size=\"+1\">"+position;
                        fOut.write("<tr><td colspan=\"12\"><hr /></td></tr>"+lineSeparator);
                    }
                    fOut.write("<tr>"+lineSeparator);
                    fOut.write("<td bgColor=\"silver\" align=\"center\">"+positionString+"</td>");
                    fOut.write("<td>"+i0+"</td>");
                    fOut.write("<td>"+team+"</td>");
                    fOut.write("<td>"+Elo+"</td>");
                    fOut.write("<td align=\"center\"><font Size=\"+1\">"+ps+"</td>");
                    fOut.write("<td align=\"center\"><font Size=\"+1\">"+HTMLscore(pi*10)+"</td>");
                    for (k=0; k<6; k++) {
                        try {
                            s=flagsTieBreak.substring(k,k+1); 
                            index=Integer.parseInt(s, 16);
                            if (index>0 && index<11) fOut.write("<td align=\"center\"><font Size=\"+1\">"+Z[i0-1][k]+"</td>");
                            if (index==11) fOut.write("<td align=\"center\"><font Size=\"+1\">"+String.format("%.2f",0.01f*Z[i0-1][k])+"</td>");
                        }    
                        catch (NullPointerException ex) {}
                        catch (StringIndexOutOfBoundsException ex) {}
                        catch (NumberFormatException ex) {}
                    }
                    fOut.write("</tr>"+lineSeparator);
                }
                fOut.write("<tr><td colspan=\"12\"><hr /></td></tr>"+lineSeparator);
                fOut.write("</table>"+lineSeparator);
            }
            fOut.write("</body>"+lineSeparator);
            fOut.write("</html>"+lineSeparator);
            fOut.close();
                       
            java.net.URL url=null;
            try {
                url = new java.net.URL("file:///"+tempFile);
            } catch (MalformedURLException ex) {
//                
            }
            if (url != null) 
            try {
                jTextPane1 = new javax.swing.JTextPane();
                jTextPane1.setMinimumSize(new java.awt.Dimension(610, 570));
                jScrollPane7.setViewportView(jTextPane1);
                jTextPane1.setPage(url);    // load temp file into the Pane
            } catch (IOException ex) {}
              catch (NullPointerException ex) {}
        } catch (IOException ex) {
            
        }
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // remove last round
        if (engineRunning) return;
        if (maxRound>0) {
            upfloaters[maxRound-1]="upfloaters of round "+maxRound+" ;";
            downfloaters[maxRound-1]="downfloaters of round "+maxRound+" ;";
            maxRound--;
        }
        SpinnerNumberModel model=null;
        if (maxRound>0) {
            model = new SpinnerNumberModel(maxRound, 1, maxRound, 1);
            jTextField4.setText(""+maxRound);
            jButton6.setEnabled(false);
            jMenuItem2.setEnabled(false);
            jMenuItem9.setEnabled(false);
            jMenuItem10.setEnabled(false);
            jMenuItem11.setEnabled(false);
        }
        else {
            model = new SpinnerNumberModel(0, 0, 0, 1);
            jTextField4.setText("");
            jButton6.setEnabled(true);
            jMenuItem2.setEnabled(true);
            jMenuItem9.setEnabled(true);
            jMenuItem10.setEnabled(true);
            jMenuItem11.setEnabled(true);
            for (int i=0; i<MAXTEAMS; i++) {
                sortIndex[i]=(short)(i+1);  // reset sort order
                teamScores[i][2] = 0;       // reset acceleration
            }
        }
        jSpinner1.setModel(model);
        jButton7.setEnabled(true);  // enable next round button
        currRound=maxRound;
        safeExitAllowed = false;
        missingResults=false;
        loadCurrentRound();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // do next round
        if (engineRunning) return;
        optimizationRequested=true;      // set global flag for colour and floater optimization (default)
        if ((tournamentType==0 || tournamentType==1 || tournamentType==2 || tournamentType==4) && !jCheckBox1.isSelected()) {
                    Object[] options = {Main.localizedText("Yes"), Main.localizedText("No")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Are you sure you wont pairings withour any colour and floater optimization?"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]);
                    if (n==1) return;
                    optimizationRequested=false;
        }
        jButton43.setEnabled(true);
        EngineThread = new Thread(new doNextRound(this));
        EngineThread.start();  // run in a different thread, so it is interruptable
    }
    
    public class doNextRound implements Runnable {
      private EnterFrame theMainForm=null; 
      public doNextRound(EnterFrame EF) {
        theMainForm = EF;
      }
      //  @Override
      public void run() {  
        int i,j;
        if (maxRound==MAXROUNDS) return; // max capacity reached. avoid array overflow
        if (engineRunning) return;
        engineRunning=true;
        jButton7.setEnabled(false);     // disable the button (will be enabled after results)
        theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        maxRound++;                     // increment round counter
        if (maxRound==1) doSortOrder=true;  // if first round, sort order (i.e. ID assignment) will be done in orderTeams() routine
        currRound = maxRound;           // align the current round to the max
        SpinnerNumberModel model = new SpinnerNumberModel(currRound, 1, maxRound, 1);
        jSpinner1.setModel(model);      // update the spinner control
        jTextField4.setText(""+currRound);  // update the toolbar control
        jButton6.setEnabled(false);
        jMenuItem2.setEnabled(false);
        jMenuItem9.setEnabled(false);
        jMenuItem10.setEnabled(false);
        jMenuItem11.setEnabled(false);
        for (i=0; i<MAXPAIRS; i++)      // reset pairings & results array
        for (j=0; j<=maxBoards; j++) roundsDetail[i][j][currRound-1]="0-0-0-0"; // i.e. "ID1-ID2-result1-result2"
        for (i=0; i<MAXTEAMS; i++) {     // reset team, player scores and acceleration
                teamScores[i][0] = teamScores[i][1] = teamScores[i][2] =0;
        }
        missingResults=true;
        loadCurrentRound();   // this routine ultimately calls calculateTeamScores() and orderTeams() to establish the current ranking
        addedPairs=0;         // so teams are always presented ordered
        nonePlayedAlternate=0; // flag to alternate colour for late entries
        if (allowAcceleration)
            if (tournamentType == 0 || tournamentType==1 || tournamentType == 2 || tournamentType == 3) calculateAcceleration();
        jDialog4.setVisible(false); jTextArea1.setText("");
        if (jCheckBox6.isSelected() && tournamentType<6) jDialog4.setVisible(true); // show explain window (and action button ...)
        addedPairs=0;
        try {
        if      (tournamentType == 0) swissDutch(jCheckBox6.isSelected());
        else if (tournamentType == 1) swissDubov(jCheckBox6.isSelected());
        else if (tournamentType == 2) swissSimple(jCheckBox6.isSelected());
        else if (tournamentType == 3) swissPerfectColours(jCheckBox6.isSelected());
        else if (tournamentType == 4) AmalfiRating(jCheckBox6.isSelected());
        else if (tournamentType == 5) roundRobin(jCheckBox6.isSelected());
        else if (tournamentType == 6) {   // setup pairs by hand ... nothing to calculate !!
            if (currRound==Integer.valueOf(rounds)+1) { // are you going over last round ?? ...
                if (tournamentPairing==1) {         // ... if a regular tournament this is not of course allowed. notify it
                    Object[] options = {Main.localizedText("OK")};
                    int n = JOptionPane.showOptionDialog(theMainForm,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                }
            }
        } else {                    // other engines!
            theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            String tournamentName = ""+jList1.getModel().getElementAt(tournamentType);
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(theMainForm,
                "<html>yet ... TO DO ... tournament type '"+tournamentName+"'. Hope you may help!!<br>Switching to type 'by Hand' ...</html>",
                "Info",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            // reset to previous situation
            maxRound--;
            currRound = maxRound;
            if (maxRound>0) {
                model = new SpinnerNumberModel(maxRound, 1, maxRound, 1);
                jTextField4.setText(""+currRound);
            } else {
                model = new SpinnerNumberModel(0, 0, 0, 1);
                jTextField4.setText("");
                jButton6.setEnabled(true);
                jMenuItem2.setEnabled(true);
                jMenuItem9.setEnabled(true);
                jMenuItem10.setEnabled(true);
                jMenuItem11.setEnabled(true);
            }
            jSpinner1.setModel(model);
            tournamentType = 6;
            jButton7ActionPerformed(null); // redo by hand this time!
        }
        } catch (Exception ex) {ex.printStackTrace();
            if (!jDialog4.isVisible()) {jDialog4.setVisible(true); // show explain window (and action button ...)
                                            jTextArea1.setText("");}
            addExpl("Cannot perform pairing!", "", "");
            addExpl("Cannot perform pairing!", "", "");
            addExpl("Cannot perform pairing!", "", "");
        }
        theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (engineRunning) safeExitAllowed = false; // flag for changes
        else {  // engine interrupted for some reason?
            Object[] options = {Main.localizedText("Ok")};
            JOptionPane.showOptionDialog(theMainForm,
                        Main.localizedText("You may evaluate to do pairings without any colour and floater optimization.")+"\n"+
                            Main.localizedText("Remove the round, uncheck 'optimize colours' and retry."),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
            jDialog4.setVisible(true); // show explain window
        }
        jButton43.setEnabled(false);
        engineRunning=false;
      }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void firstRoundSwiss(boolean explain){
        // first round of any swiss system; take into account it may be accelerated!
        int i, i1, i2, compatible, k, p1, p2, thisGroup, availableTeams=0;
        i=0;
        for (i1=0; i1<addedRows; i1++) {        // populate the array of elements to be paired
            if (!isAlreadyPaired(i1)) { // this also checks isRetired(), bacause may have someone entering 2.nd round
                tI[i++][0]=i1; 
                availableTeams++;
            }
        }
        if (nGroupsAccelerated==1)
            for (i=0; i<availableTeams/2; i++) {    // not accelerated. simply pair 1.st half vs 2.nd half
                i1=tI[i][0];
                i2=tI[i+availableTeams/2][0];
                compatible = establishCompatibility(i1, i2, true, "");
                // remind: establishCompatibility() returns 0 if the pair is not compatible, 1 if optimal 'i1-i2', 2 if optimal 'i2-i1'
                if (compatible==1) roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[i1]+1)+"-"+(tempIndex[i2]+1)+"-0-0";
                else roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[i2]+1)+"-"+(tempIndex[i1]+1)+"-0-0";
                if (explain) addExpl("pair added", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                addedPairs++;   // increment the pairs counter 
            }
        else {
            p1=p2=0;    // accelerated. follow score groups
            for (;;) {
                for(;;) {
                    if (teamScores[p1][0]==teamScores[p2][0]) p2++;
                    else break;
                    if (p2==availableTeams) break;
                }
                thisGroup = p2-p1;
                for (i=0; i<thisGroup/2; i++) {    // pair 1.st half vs 2.nd half
                    i1=tI[i+p1][0];
                    i2=tI[i+p1+thisGroup/2][0];
                    compatible = establishCompatibility(i1, i2, true, "");
                    // remind: establishCompatibility() returns 0 if the pair is not compatible, 1 if optimal 'i1-i2', 2 if optimal 'i2-i1'
                    if (compatible==1) roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[i1]+1)+"-"+(tempIndex[i2]+1)+"-0-0";
                    else roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[i2]+1)+"-"+(tempIndex[i1]+1)+"-0-0";
                    if (explain) addExpl("pair added", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                    addedPairs++;   // increment the pairs counter 
                }
                if (p2==availableTeams) break;
                p1=p2;
            }
        }
    }
    
    private void swissDubov(boolean explain) {
        // 'swiss Dubov FIDE'. Engine implementation
            /* Rules are: 
                - Two players who have played each other shall not be paired again (rule 2.1).
                - White and Black colours tend to be equal and alternated between games. Three colour sequence 
                    or delta colour grater than two are not allowed (rules 2.3, 2.4 and 3). In case of exactly equal 
                    colour history, white is assigned to the player with the higher ARO (rule 3).
                - Forfeit and BYE games are considered played without opponent and colour
                - if odd players, last ranked, but only one time in the tournament, gets the BYE (rules 2.2 and 4)
                - Starting from the top of the ranking, a player is tried to be paired within the same
                    score group (rule 6.1). If odd, a player with balancing colour due is floated from the next score group
                    (rules 7.1 and 7.2), but apart from the last round a player cannot be transferred to a higher 
                    score group two times running and more than three times (if the tournament has less than 10 
                    rounds) or four times (if the tournament has more than 9 rounds) during one tournament (rule 2.5).
                    If needed, colour dues are balanced exchanging some elements of the group (rules 7.3 and 7.4), 
                    but a player shall not be transferred from the subgroup due to a colour to the subgroup due to 
                    the other colour if this would violate the limitations 2.3 or 2.4. (rule 2.6). Then 1.st half 
                    (colour due White) is ordered by increasing ARO (Average Opponent Rating), increasing Rating and
                    alphabetically; 2.nd half (colour due Black) is ordered by decreasing Rating, decreasing ARO and 
                    alphabetically (rule 6.2 first part). Finally 1.st half is paired vs 2.nd half (rule 6.2 second part), 
                    making small permutations of the 2.nd half if needed because match already 
                    played (rule 6.3 first part).
                    If impossible to pair, all elements of the group are allowed to be mixed (rule 6.3 second part).
                    If impossible to pair, the group is then grown with two more floaters; if gone to the end,
                    the last done pair is broken and the procedure is repeated including all left unpaired.
            */
        int i, j, k;
        int i1, i2, ID, compatible, availableTeams, bC, cD, ARO, reserve, ARO1,ARO2, score, deltaScore, prevScore;
        int [] sup, inf;
        boolean ex,takeAll;
        String [] S;
        String flag;
        float fMolt;
        TableModel myTM = jTable1.getModel();
        if (currRound==Integer.valueOf(rounds)+1) { // are you going over last round ?? ...
                if (tournamentPairing==1) {         // ... if a regular tournament this is not of course allowed. notify it
                    Object[] options = {Main.localizedText("OK")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                    return;
                }
        }
        availableTeams = countAvailableTeams();         // obtain dinamically the number of teams available to play
        if (explain) {
            addExpl("pairs to be generated for round", currRound, "swiss Dubov FIDE algorithm");
            addExpl("available",availableTeams,"to be paired");
            addExpl("","","");
        }
        // if odd, set BYE to last ranked not taken before 
        if ((availableTeams % 2) == 1 ) {
            int lnb=lastNotBYE(false);  // rules 2.4 and 4.
            roundsDetail[(availableTeams+1)/2-1][0][currRound-1]=""+lnb+"-0-0-0";
            if (explain) addExpl("odd -> BYE set to ID",lnb,"");
        }
        tI = new int [availableTeams][3];   // stores pointer to ID, colour due and ARO
        if (maxRound==1) firstRoundSwiss(explain);  // first round! rule 5.
        else {
            if (explain) {
                prevScore=9999; i=0;
                for (i1=0; i1<addedRows; i1++) 
                    if (!isAlreadyPaired(i1)) {
                        ID=tempIndex[i1]+1;
                        cD=colourDue(i1);
                        ARO=calculateAROteam(ID, -1);
                        fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                        score=teamScores[ID-1][0];
                        if (score<prevScore) {
                            addExpl("","","");
                            addExpl("score group", ++i, "score", score*fMolt, "rank", i1+1);
                            prevScore=score;
                        }
                        flag="";
                        if(!canChangeColourDue(i1, cD)) flag+="C";   // flag cannot change colour due
                        if (!allowedUpfloater(ID)) flag+="U";       // flag cannot upfloat
                        addExpl("ID", ID, "colour due", colourPreference[10+cD], "flag:", flag);
                    }
                addExpl("","","");
            }
            nTeams=0;       // this will store the size of the score group to be paired
            takeAll=false;  // this will be set true when gone to the end and some pair is broken, so all remaining unpaired are taken
            for (;;) {        // the main loop. try to pair (i.e. starting from the highest score group)
                if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                // 1.st look at the starting team of higher score group not yet paired
                if (addedPairs==availableTeams/2) break;  // finished !!
                i1 = 0; i=0; reserve=-1;
                for (;;) {
                    if (isAlreadyPaired(i1)) i1++;
                    else break;
                    if (i1==addedRows) break;
                }
                if (i1==addedRows) break;   // finished !!
                ID=tempIndex[i1]+1;
                cD=colourDue(i1);
                ARO=calculateAROteam(ID, -1);
                tI[i][0]=i1; tI[i][1]=cD; tI[i++][2]= ARO;     // store the element
                if (explain) {
                    fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                    score=teamScores[ID-1][0];
                    addExpl("member of this group ID", ID, "colour due", colourPreference[10+cD], "", "");
                }
                // 2.nd look at the team ending this score group
                for (;;) {
                    i2 = i1+1;
                    for (;;) {  // n.b. this loop is needed because the candidate selection may require more steps
                        if (i2==addedRows) {
                            if (explain && !takeAll) addExpl("last group. from now include all left unpaired","","");
                            takeAll=true;   // the last remaining unpaired for any reason. force take all
                            break;
                        }
                        ex=false;       // before to add element, check not added yet ...
                        for (j=0; j<i; j++) {   // ... because of nested loops, may come here more times
                            if (tI[j][0]==i2) { ex=true; break; }  
                        }
                        if (!ex && !isAlreadyPaired(i2)) {
                            if (teamScores[tempIndex[i1]][0]==teamScores[tempIndex[i2]][0] || takeAll) {
                                ID=tempIndex[i2]+1;     // same score group ...
                                cD=colourDue(i2);
                                ARO=calculateAROteam(ID, -1);
                                tI[i][0]=i2; tI[i][1]=cD; tI[i++][2]= ARO;     // ... store the element
                                if (explain) {
                                    fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                                    score=teamScores[ID-1][0];
                                    addExpl("member of this group ID", ID, "colour due", colourPreference[10+cD], "", "");
                                }
                            } else {
                                if (i%2==0 && i>=nTeams) break;      // over this group! if even and enough terminate
                                // this immediately follows the score group. odd or need some upfloater(s). rules 7.1 and 7.2
                                ID=tempIndex[i2]+1;
                                if (allowedUpfloater(ID)) {  // rule 2.5
                                    bC=balanceColours(tI, i);    // need to select a balancing colour due one ...
                                    cD=colourDue(i2);
                                    if (reserve>=0 && 
                                        teamScores[tempIndex[reserve]][0]>teamScores[ID-1][0]) {
                                        // ... but a reserve, with opposite colour due, was left unpaired in the middle of a group trying to span more than one score group! 
                                        i2=reserve;  // get back the reserve ...
                                        reserve=-1;
                                        cD=bC;    // ... and force colour due to enter next if for sure
                                        ID=tempIndex[i2]+1;
                                        if (explain) addExpl("get back the reserve ID", ID, "changed colour due to", colourPreference[10+cD], "", "");
                                    }
                                    if (bC==0 || cD==bC) {      // add an upfloater to extend the group or to balance colours ...
                                        ex=false;   // ... but check may have at least one opponent in this group!
                                        for (j=0; j<i; j++) { 
                                            if (establishCompatibility(tI[j][0],i2,optimizationRequested,"")>0) { ex=true; break; }  
                                        }
                                        if (ex) {
                                            cD=colourDue(i2);
                                            ARO=calculateAROteam(ID, -1);
                                            tI[i][0]=i2; tI[i][1]=cD; tI[i++][2]= ARO;     // store the element
                                            if (reserve==i2) reserve=-1;
                                            if (explain) {
                                                fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                                                score=teamScores[ID-1][0];
                                                addExpl("upfloated ID", ID, "colour due", colourPreference[10+cD], "", "");
                                            }
                                            break;   // go to consider next element
                                        } else {
                                            if (explain) addExpl("ID", ID, "not compatible with previous one(s)!");
                                        }
                                    } else {
                                        if (explain) addExpl("ID", ID, "discharged, because of wrong colour due!");
                                        if (reserve==-1) {
                                            cD=colourDue(i2);
                                            if (canChangeColourDue(i2, cD)) {
                                                reserve=i2;  // this element was discharged, because of wrong colour due, but let it be a reserve
                                                ID=tempIndex[i2]+1;
                                                if (explain) addExpl("set as reserve ID", ID, "colour due", colourPreference[10+cD], "", "");
                                            } else
                                                if (explain) addExpl("ID", ID, "discharged, because of colour constrain!");
                                        }
                                    }
                                } else {
                                    if (explain) addExpl("ID", ID, "cannot be moved up!");
                                }
                            }
                        }
                        i2++;
                    }
                    // comes here after a break instruction. may be task finished, added an upfloater or take all requested
                    if (nTeams==0) {    // if there was not a specific size of the score group request ...
                        if (i%2==0) break; // ... and the group is even terminate
                        else continue;
                    }
                    if (i%2==0 && i>=nTeams) break; // if there was a specific size of the score group request, may terminate only if grown enough and even
                }
                nTeams=i;   // size of the score group to be paired
                if (takeAll) {      // when 'take all' a large group may be selected! Check the group not too much heterogeneous (this may  
                                    // happen in an exteme condition, i.e. at 6th round of a tournament with only 10 players or teams).
                                    // In this situation the normal swiss 'S1 vs S2' may alter the tournament and secondary prizes win!
                    if (i1+1<=availableTeams/2) {   // if true, the group extends from half ranking to the bottom, ... too much!!
                    // ask to switch to Amalfi rating !!  this seems to be an optimization at the moment
                        Object[] options = {Main.localizedText("Yes"), Main.localizedText("No")};
                        int n = JOptionPane.showOptionDialog(this,
                            Main.localizedText("Extreme situation found! Score group is too much heterogeneous.")+"\n"+
                            Main.localizedText("I suggest you to switch to Amalfi rating system! Abort?"),
                            Main.localizedText("Info"),
                            JOptionPane.OK_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]);
                        if (n==0) { // if Yes abort and then manually switch to Amalfi rating
                            if (explain) addExpl("pairing impossible! quit", "", ""); 
                            return;    
                        }
                    }
                }
                if (explain) {
                        addExpl("group of",nTeams,"");
                        for (k=0; k<nTeams; k++) addExpl("ID",tempIndex[tI[k][0]]+1,"colour due", colourPreference[10+tI[k][1]], "", "");
                        addExpl("","","");
                }
                // 1.st try to balance colours (rules 7.3 and 7.4)
                String alreadyEvaluated="-";
                for (k=0; k<nTeams; k++) {
                    bC=balanceColours(tI, nTeams);
                    if (bC==0) break;      // finished!
                    i=-1;   // need to balance colours. look at the next candidate
                    cD=0;
                    if (bC==2) {
                        ARO=99999;
                        for (i1=0; i1<nTeams; i1++) {
                            if (tI[i1][1]!=bC) 
                                if (tI[i1][2]<ARO || 
                                   (tI[i1][2]==ARO && (i==-1 || getElo(tempIndex[tI[i1][0]]+1,0)>getElo(tempIndex[tI[i][0]]+1,0)))) 
                                        if (alreadyEvaluated.indexOf("-"+i1+"-")<0) {
                                            ARO=tI[i1][2];  // the lowest ARO have to change colour due
                                            cD=tI[i1][1];
                                            i=i1;
                                        }
                        }
                    } else {
                        ARO=0;
                        for (i1=0; i1<nTeams; i1++) {
                            if (tI[i1][1]!=bC) 
                                if (tI[i1][2]>ARO ||
                                   (tI[i1][2]==ARO && (i==-1 || getElo(tempIndex[tI[i1][0]]+1,0)<getElo(tempIndex[tI[i][0]]+1,0)))) 
                                        if (alreadyEvaluated.indexOf("-"+i1+"-")<0) {
                                            ARO=tI[i1][2];    // the highest ARO have to change colour due
                                            cD=tI[i1][1];
                                            i=i1;
                                        }
                        }
                    }
                    if (i>=0) {
                        if (canChangeColourDue(tI[i][0], cD)) { // evaluate if colour due may really be changed !!!!
                            tI[i][1]=bC;
                            if (explain) addExpl("ID",tempIndex[tI[i][0]]+1,"changed colour due to", colourPreference[10+bC], "", "");
                        } else { 
                            if (explain) addExpl("ID",tempIndex[tI[i][0]]+1,"cannot change colour due!", colourPreference[10+cD], "", "");
                            alreadyEvaluated=alreadyEvaluated+i+"-";
                        }
                    } else break;
                }
                bC=balanceColours(tI, nTeams);
                if (bC>0) { 
                        // cannot balance colours, i.e. cannot do pairings!
                        if (nTeams>40 && !explain) {
                            jTextArea1.setText("");
                            jDialog4.setVisible(true); // show explain window (and action button ...)
                            explain = true;
                        }
                        if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                        if (addedPairs+nTeams/2==availableTeams/2) {
                            if (explain && !takeAll) addExpl("last group. from now include all left unpaired","","");
                            takeAll=true; // at the end include all teams
                            orderPairs();       // ... start to break the low order pair ...
                            addedPairs--;       
                            if (addedPairs<0) {   // ... but if all pairs are broken return with an error
                                            if (explain) addExpl("pairing impossible! quit", "", "");
                                            Object[] options = {Main.localizedText("OK")};
                                            int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                            return;
                            }
                            if (explain) addExpl("pair broken", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                            roundsDetail[addedPairs][0][currRound-1]="0-0-0-0"; // ... reset the pair ...
                        } 
                        if (explain) addExpl("pairing impossible. grow the group and retry", "", "");
                        nTeams++; nTeams++;   // request two more upfloaters. (rules 2.3, 2.4 and 7.1)
                        continue;
                } 
                // 2.nd need 1.st half colour due white, 2.nd half colour due black (rule 6.2)
                for (i1=0; i1<nTeams/2; i1++) 
                    if (tI[i1][1]==2) 
                        for (i2=nTeams/2; i2<nTeams; i2++)
                            if (tI[i2][1]==1) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                                break;
                            }
                // 3.rd order 1.st half increasing ARO, increasing Rating and alphabetically; 
                //            2.nd half decreasing Rating, decreasing ARO and alphabetically (rule 6.2)
                for (i1=0; i1<nTeams/2-1; i1++)
                    for (i2=i1+1; i2<nTeams/2; i2++) {
                        ARO1=tI[i1][2];
                        ARO2=tI[i2][2];
                        if ( ARO1 > ARO2 ||
                         (ARO1 == ARO2 && getElo(tempIndex[tI[i1][0]]+1,0)>getElo(tempIndex[tI[i2][0]]+1,0)) ||
                         (ARO1 == ARO2 && getElo(tempIndex[tI[i1][0]]+1,0)==getElo(tempIndex[tI[i2][0]]+1,0) && 
                            (""+myTM.getValueAt(sortIndex[tempIndex[tI[i1][0]]]-1,0)).compareToIgnoreCase(""+myTM.getValueAt(sortIndex[tempIndex[tI[i2][0]]]-1,0))>0)) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                        }
                    }
                for (i1=nTeams/2; i1<nTeams-1; i1++)
                    for (i2=i1+1; i2<nTeams; i2++) {
                        ARO1=tI[i1][2];
                        ARO2=tI[i2][2];
                        if (getElo(tempIndex[tI[i1][0]]+1,0)<getElo(tempIndex[tI[i2][0]]+1,0) ||
                            (getElo(tempIndex[tI[i1][0]]+1,0)==getElo(tempIndex[tI[i2][0]]+1,0) && ARO1<ARO2) ||
                               (getElo(tempIndex[tI[i1][0]]+1,0)==getElo(tempIndex[tI[i2][0]]+1,0) && ARO1==ARO2 &&
                                (""+myTM.getValueAt(sortIndex[tempIndex[tI[i1][0]]]-1,0)).compareToIgnoreCase(""+myTM.getValueAt(sortIndex[tempIndex[tI[i2][0]]]-1,0))>0)) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                        }
                    }
                // ready to pair the group! set arrays needed to recurse into
                sup=new int [0];
                inf=new int [nTeams];
                solution=new String [nTeams/2];
                k=0;
                for (i=0; i<nTeams/2; i++) {inf[k++]=i+1;inf[k++]=i+nTeams/2+1;}  // populate the array (i.e. for 6 elements: 1-4-2-5-3-6
                                                                                  // so the first solution could be 1-4, 2-5, 3-6)
                visits=0;
                // 1.st try normal swiss way, i.e. 1.st half vs 2.nd half, permuting only 2.nd half
                if (explain) {
                        addExpl("now ordered.","","trying to pair 1.st half vs 2.nd half");
                        for (i1=0; i1<nTeams/2; i1++) addExpl("ID",tempIndex[tI[i1][0]]+1,"ARO",tI[i1][2]);
                        if (nTeams>2) addExpl("----------------","","");
                        for (i2=nTeams/2; i2<nTeams; i2++) addExpl("ID",tempIndex[tI[i2][0]]+1,"rating",getElo(tempIndex[tI[i2][0]]+1,0));
                }
                if (!solveColumnS1vsS2normal(nTeams, sup, inf) || visits>10000) { // look if any solution found (apply rule 6.3) ...
                    // ... if not, try permuting all elements of the group. this is time consuming!
                    if (visits>1000 && !explain) {
                        jTextArea1.setText("");
                        jDialog4.setVisible(true); // show explain window (and action button ...)
                        explain = true;
                    }
                    if (explain) addExpl("visits",visits,"none found. try mixing all");
                    visits=0;
                    if (!solveColumnAllnormal(nTeams, sup, inf) || visits>50000) { // look if any solution found ...
                        if (explain) addExpl("visits",visits,"none found.");
                        if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                        if (addedPairs+nTeams/2==availableTeams/2) {
                            if (explain && !takeAll) addExpl("last group. from now include all left unpaired","","");
                            takeAll=true; // at the end include all teams
                            orderPairs();       // ... start to break the low order pair ...
                            addedPairs--;       
                            if (addedPairs<0 || visits>50000) {   // ... but if all pairs are broken return with an error
                                            if (explain) addExpl("pairing impossible! quit", "", "");
                                            Object[] options = {Main.localizedText("OK")};
                                            int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                            return;
                            }
                            if (explain) addExpl("pair broken", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                            roundsDetail[addedPairs][0][currRound-1]="0-0-0-0"; // ... reset the pair ...
                        } 
                        if (explain) addExpl("pairing impossible. grow the group and retry", "", "");
                        nTeams++; nTeams++;   // request two more upfloaters
                        continue;
                    }
                }
                // successful! fix pairs 
                if (explain) addExpl("visits",visits,"done. pairs generated"); 
                for (i=0; i<nTeams/2; i++) {
                    roundsDetail[addedPairs++][0][currRound-1]=solution[i]+"-0-0";
                    if (explain) {
                        S=solution[i].split("-");
                        i1=Integer.valueOf(S[0]);
                        i2=Integer.valueOf(S[1]);
                        deltaScore=teamScores[i1-1][0]-teamScores[i2-1][0];
                        fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                        addExpl("",solution[i],"delta score=",deltaScore*fMolt);
                    }
                }
                if (explain) addExpl("","","");
                nTeams=0;  // reset to non specific request and go to select next score group
            }
        }
        if (explain) {
            addExpl("done.","","");
            jDialog4.toFront();
        }
        addedPairs = (short)((availableTeams+1)/2);   // take care of odd teams
        loadCurrentRound();
    }

    private void swissDutch(boolean explain) {
        // 'Swiss Dutch FIDE'. Engine implementation
            /* rules: 
                Absolute Criteria
                    (These may not be violated. If necessary players will be moved down to a
                    lower score bracket.)
                B.1
                    a) Two players shall not meet more than once.
                    b) A player who has received a point without playing, either through a
                       bye or due to an opponent not appearing in time, shall not receive a bye.
                B.2
                    a) No player's colour difference will become >+2 or <-2.
                    b) No player will receive the same colour three times in row.
                Relative Criteria
                    (These are in descending priority. They should be fulfilled as much as
                    possible. To comply with these criteria, transpositions or even exchanges
                    may be applied, but no player should be moved down to a lower score bracket).
                B.3 The difference of the scores of two players paired against each other should
                    be as small as possible and ideally zero.
                B.4 As many players as possible receive their colour preference. (Whenever x of
                    a score bracket is unequal to zero this rule will have to be ignored. x is
                    deducted by one each time a colour preference cannot be granted.)
                B.5 No player shall receive an identical float in two consecutive rounds.
                B.6 No player shall have an identical float as two rounds before.
             
                Notes:
                    B2, B5 and B6 may not be applied when pairing players with a score of over 50% in the last
                    round if this is helpful to avoid additional downfloaters.
             
             */
        int i, i1, i2, i1Abs, i2Abs, k, j, group, prevScore, score, ID, ID1, ID2, cD, cD1, cD2, deltaScore, nTeams;
        boolean done;
        float fMolt;
        String flag, c1, c2;
        String[] S;
        
        if (currRound==Integer.valueOf(rounds)+1) { // are you going over last round ?? ...
                if (tournamentPairing==1) {         // ... if a regular tournament this is not of course allowed. notify it
                    Object[] options = {Main.localizedText("OK")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                    return;
                }
        }
        lastRound=(currRound==Integer.valueOf(rounds));
        availableTeams = countAvailableTeams();         // obtain dinamically the number of teams available to play
        (new File("log.txt")).delete();                 // erase log file
        if (explain) {
            jTextArea1.setText("");
            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
            addExpl("available",availableTeams,"to be paired");
            addExpl("","","");
        }
        lnb=9999; group_lnb=9999;
        if (maxRound==1) {
            // if odd, set BYE to last ranked 
            if ((availableTeams % 2) == 1 ) {
                lnb=lastNotBYE(false);  
                roundsDetail[(availableTeams+1)/2-1][0][currRound-1]=""+lnb+"-0-0-0";
                if (explain) addExpl("odd -> BYE set to ID",lnb,"");
            }
            tI = new int [availableTeams][1];
            firstRoundSwiss(explain);  // first round! 
        }
        else {
            short [][] tILoc = new short [availableTeams][4];   // stores pointer to ID, colour due, score group and moved score group
            colours = new String [availableTeams];     // strings of colours
            gamesPlayed = new String [availableTeams]; // strings of games played
            score=9999;
            for (i1=0; i1<addedRows; i1++) 
                if (!isRetired(i1)) {
                    ID=tempIndex[i1]+1;
                    score=teamScores[ID-1][0];
                    break;
                }
            if (score==9999) return;     // ALL retired ??????
            if ((availableTeams % 2) == 1 ) {
                lnb=lastNotBYE(false);  // rules 2.4 and 4.
            }
            prevScore=score; group=1;
            fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
            if (explain) {addExpl("","",""); addExpl("score group", group, "score", score*fMolt, "rank", 1);}
            nTeams=0;
            for (i1=0; i1<addedRows; i1++) 
                if (!isRetired(i1)) {
                    ID=tempIndex[i1]+1;
                    score=teamScores[ID-1][0];
                    if (score<prevScore) {
                        group++;
                        if (explain) {addExpl("","",""); addExpl("score group", group, "score", score*fMolt, "rank", i1+1);}
                        prevScore=score;
                    }
                    colours[nTeams] = getColours(i1);               // prepare the arrays to speed search
                    cD=colourDue(colours[nTeams]);
                    tILoc[nTeams][0] = (short)i1; tILoc[nTeams][1] = (short)cD; tILoc[nTeams][2] = (short)group; tILoc[nTeams][3] = (short)group; // store pointer to ID, colour due, score group and moved score group
                    if (explain) {
                        flag="";
                        if (!allowedUpfloaterB5(ID)) flag+="UB5 ";        // flag cannot upfloat
                        if (!allowedDownfloaterB5(ID)) flag+="DB5 ";      // flag cannot downfloat
                        if (!allowedUpfloaterB6(ID)) flag+="UB6 ";        // flag cannot upfloat
                        if (!allowedDownfloaterB6(ID)) flag+="DB6 ";      // flag cannot downfloat
                        addExpl("ID=", ID, "colour due", colourPreference[cD], "flag:", flag);
                        if (ID==lnb) addExpl("(lowest could take the BYE)","","");
                    }
                    if (ID==lnb) group_lnb=group; 
                    nTeams++;
                }
            score50=currRound-1;      // score of level 50%
            lowestGroup=group; 
/*            if (group_lnb<9999) {
                i=0; k=0;
                for (i1=nTeams-1; i1>=0; i1--) {
                    if (tILoc[i1][2] > group_lnb) k++;
                    if (tILoc[i1][2] >= group_lnb)  { i++; 
                        if (i<=2*MPAFC) group=tILoc[i1][2];
                        else {group++; break;}
                    }
                }
                if (group==group_lnb) group++;
                if (group<lowestGroup) {
                    if (explain) addExpl("","","");
                    if (explain) addExpl("join groups:",""+group,"to",""+lowestGroup);
                    for (i1=nTeams-1; i1>=0; i1--) 
                        if (tILoc[i1][2] > group)  tILoc[i1][3] = (short) group;
                    lowestGroup=group; 
                }
            }
            if (group_lnb>lowestGroup) group_lnb=lowestGroup;
            if (explain) addExpl("","","");  */
            
            /* C section. Pairing Procedures. Starting with the highest score bracket apply the following procedures to all
                  score brackets until an acceptable pairing is obtained. */
            alreadySeen=new String[lowestGroup+2];
            int [] messages=new int [7];
            loadGamesPlayed();
            for (k=0; k<alreadySeen.length; k++) alreadySeen[k]="";     // this array will store the groups as they are visited
            messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[5]=messages[6]=0;
            engineStartTime=System.currentTimeMillis(); visits=0; addedPairs=0; lastCompletedGroup=lastVisitedGroup=floatWeight=0; globalExplainText=""; avoidBlocking=false; 
            verbosity_level=-1; if (jCheckBox6.isSelected()) verbosity_level=jComboBox7.getSelectedIndex(); 
            strong_verbose=(verbosity_level>1); mild_verbose=(verbosity_level>0);
            solveDutch(1, tILoc, NO_REMINDER, messages, false);    // recursively solve the pairing
            if (jDialog4.isVisible()) explain=true;
            // end of C section
            if (addedPairs == (availableTeams+1)/2) {                           // success !!!!
                if (explain) addExpl("","","");
                jTextArea1.append(globalExplainText);
                /* E section. Colour Allocation Rules
                        For each pairing apply (with descending priority): */
                for (k=0; k<availableTeams/2; k++) {
                    S= roundsDetail[k][0][currRound-1].split("-");
                    ID1=Integer.valueOf(S[0]);
                    ID2=Integer.valueOf(S[1]);
                    if (ID2==0) continue;
                    i1Abs=indexAt(ID1-1, tempIndex);
                    i2Abs=indexAt(ID2-1, tempIndex);
                    i1=i2=0;
                    for (j=0; j<availableTeams; j++) {
                        if (i1Abs==tILoc[j][0]) i1=j; 
                        if (i2Abs==tILoc[j][0]) i2=j; 
                    }
                    
                    // E.1 Grant both colour preferences.
                    cD1=tILoc[i1][1]; cD2=tILoc[i2][1];
                    if (cD1%2 != cD2%2) {
                        if (cD1%2==1) 
                            roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                        else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                        continue;
                    }
                
                    // E.2 Grant the stronger colour preference.
                    if (cD1+cD2>50 && cD1+cD2<60) {       // absolute - absolute can change?
                        if (cD1/10 < cD2/10) {
                            if (cD1%2==1) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                            else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                            continue;
                        } else if (cD1/10 > cD2/10) {
                            if (cD2%2==0) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                            else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                            continue;
                        }
                    } else {
                        if (!canChangeColourDue(i1Abs, cD1)) cD1+=10;   // increase strength
                        if (!canChangeColourDue(i2Abs, cD2)) cD2+=10;
                        if (cD1/10 > cD2/10) {
                            if (cD1%2==1) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                            else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                            continue;
                        } else if (cD1/10 < cD2/10) {
                            if (cD2%2==0) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                            else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                            continue;
                        }
                    }
                    
                    // E.3 Alternate the colours to the most recent round in which they played with different colours.
//                    if (currRound>5) {      // F.3 rule
                        colours[i1]=colours[i1].replace(" ","");
                        while (colours[i1].length()<currRound-1) colours[i1]=" "+colours[i1];
                        colours[i2]=colours[i2].replace(" ","");
                        while (colours[i2].length()<currRound-1) colours[i2]=" "+colours[i2];
//                    }
                    done=false;
                    for (j=currRound-2; j>=0; j--) {
                        c1=colours[i1].substring(j,j+1); c2=colours[i2].substring(j,j+1);
                        done=true;
                        if (c1.equalsIgnoreCase("B") && c2.equalsIgnoreCase("W")) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                        else if (c1.equalsIgnoreCase("W") && c2.equalsIgnoreCase("B")) roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                        else done=false; 
                        if (done) break;
                    }
                    if (done) continue;
                    
                    // E.4 Grant the colour preference of the higher ranked player.
                    if (i1Abs<i2Abs) {
                        if (cD1==0 || cD1%2==1) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                        else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                    } else {
                        if (cD2%2==0) roundsDetail[k][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";
                        else roundsDetail[k][0][currRound-1]=""+ID2+"-"+ID1+"-0-0";
                    } 
                }
                if (explain) {
                    addExpl("","","");
                    addExpl("=========================================","","");
                    addExpl("done. pairs generated","","");
                    for (k=0; k<(availableTeams+1)/2; k++) {
                        S= roundsDetail[k][0][currRound-1].split("-");
                        ID1=Integer.valueOf(S[0]);
                        ID2=Integer.valueOf(S[1]);
                        if (ID2==0) addExpl("ID=",""+ID1,"takes the BYE");
                        else {
                            deltaScore=teamScores[ID1-1][0]-teamScores[ID2-1][0];
                            fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                            addExpl("",""+ID1+"-"+ID2,"delta score=",deltaScore*fMolt);
                        }
                    }
                    write_to_log(false);  // complete writing to log
                    read_from_log();      // reread complete log
                }
            } else {
                if (explain) addExpl("pairing impossible! quit", "", "");
                Object[] options = {Main.localizedText("OK")};
                int n = JOptionPane.showOptionDialog(this,
                    Main.localizedText("Cannot perform pairing!"),
                    Main.localizedText("Info"),
                    JOptionPane.OK_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
            }
        }
        if (explain) {addExpl("","",""); addExpl("done!","",""); jDialog4.toFront();}
        loadCurrentRound();

    }
    
    private String getColours(int i) {
        // returns the string of colours played
        int r,j, games; 
        boolean found;
        String[] S;
        String col="";
        // need to evaluate the history
        games=(addedRows+1)/2;
        for (r=0;r<maxRound-1;r++) {  // scan previous rounds
                found=false;
                for (j=0;j<games;j++) {   // scan pairs
                    S = roundsDetail[j][0][r].split("-");
                    if (S.length!=4) continue;     // some kind of unexpected error?
                    // store colour history, but set *** forfeit = no colour *** 
                    if (S[0].equals(""+(tempIndex[i]+1))) {
                        if (S[1].equals("0") || S[3].indexOf('f')>0) col+=" ";
                        else col+="W"; 
                        found=true;
                        break;
                    }
                    if (S[1].equals(""+(tempIndex[i]+1))) {
                        if (S[3].indexOf('f')>0) col+=" ";  
                        else col+="B"; 
                        found=true;
                        break;
                    }
                }
                if (!found) col+=" ";   // not paired at all!
        }
        return col;
    }

    private void loadGamesPlayed() {
        int games, ID1, ID2, i, j, r;
        String [] S;
        boolean played;
        games=(addedRows+1)/2;
        gamesPlayed=new String[addedRows+1];
        for (i=0; i<addedRows+1; i++) gamesPlayed[i]="";
        for (r=0;r<maxRound-1;r++) {  // scan rounds
            for (j=0;j<games;j++) {   // scan pairs
                S = roundsDetail[j][0][r].split("-");
                if (S.length!=4) continue;     // some kind of unexpected error?
                ID1=Integer.valueOf(S[0]);
                ID2=Integer.valueOf(S[1]);
                // test the pair, but avoid *** Forfeit ***
                played=true;
                if (ID1==0) played=false;
                if (ID2==0) played=false;
                if (S[3].indexOf('f')>0) played=false;
                if (played) {
                    gamesPlayed[ID1] += "-"+ID2+"-";
                    gamesPlayed[ID2] += "-"+ID1+"-";
                }
            }
        }
    }
    
    private boolean wasPlayed(int ID1, int ID2) {
        if (ID1<ID2) {
            if (gamesPlayed[ID1].contains("-"+ID2+"-")) return true;
        } else {
            if (gamesPlayed[ID2].contains("-"+ID1+"-")) return true;
        }
        return false;
    }
            
    private int establishCompatibility(int i1, int i2, boolean allowColourChangeA7d, int cD1, int cD2) {
        // establish compatibility of colours
        int nw1, nw2, nb1, nb2, r, n;
        String col1=colours[i1], col2=colours[i2], s1, s2;
        if(cD1==21 && cD2==21) return 0;       // avoid absolute-absolute
        if(cD1==22 && cD2==22) return 0;       // avoid absolute-absolute
        if (!allowColourChangeA7d) {
            if (cD1>30 || cD2>30) {
                if (cD1>30) cD1-=10;
                if (cD2>30) cD2-=10;
                if(cD1==cD2) return 0;       // avoid absolute-absolute
            }
        }
        nw1=nw2=nb1=nb2=0; n=col1.length();
        for (r=0; r<n; r++) {
            s1=col1.substring(r,r+1); s2=col2.substring(r,r+1);
            if (s1.equalsIgnoreCase("W")) nw1++;
            if (s1.equalsIgnoreCase("B")) nb1++;
            if (s2.equalsIgnoreCase("W")) nw2++;
            if (s2.equalsIgnoreCase("B")) nb2++;
        }
        col1=col1.trim(); col2=col2.trim();
        // try to assign W-B
        if (Math.abs(nw1+1-nb1)<=2 && Math.abs(nw2-nb2-1)<=2) 
            if (!(col1.endsWith("WW") || col2.endsWith("BB"))) return 1;
        // try to assign B-W
        if (Math.abs(nw1-nb1-1)<=2 && Math.abs(nw2+1-nb2)<=2) 
            if (!(col1.endsWith("BB") || col2.endsWith("WW"))) return 2;
        return 0;
    }
    
    private int colourDue(String s) {
        int w=0,b=0,n,k,n2;
        String col, s2;
        s=s.toUpperCase();
        s2=s.replaceAll(" ","");    // without BYE
        n=s.length(); 
        for (k=0; k<n; k++) {
            col=s.substring(k,k+1);
            if (col.equals("W")) w++;
            if (col.equals("B")) b++;
        }
        if (w>b+1 || s2.endsWith("WW")) return 22;       // absolute black (rule A.7 a)
        if (b>w+1 || s2.endsWith("BB")) return 21;       // absolute white (rule A.7 a)
        if ((n-w-b)%2==0) {         // all games played or bye even
            if (w>b) return 12;     // strong black (rule A.7 b)
            if (b>w) return 11;     // strong white (rule A.7 b)
            for (k=n; k>0; k--) {
                col=s.substring(k-1,k); // equal w & b, see last valid game
                if (col.equals(" ")) continue;
                if (col.equals("W")) return 12;  // mild black (rule A.7 c) - same as strong
                if (col.equals("B")) return 11;  // mild white (rule A.7 c) - same as strong
            }
        } else {                    // there was a odd BYE or forfeit
            if (w>b) return 32;     // absolute black but may be changed (rule A.7 d)
            if (b>w) return 31;     // absolute white but may be changed (rule A.7 d)
            s=s.replaceAll(" ","");             // equal w & b, see last valid game
            n2=s.length(); 
            if (n2>0) {
                col=s.substring(n2-1,n2);
                if (col.equals("W")) return 2;    // mild black but may be changed to reduce x (rule A.7 e)
                if (col.equals("B")) return 1;    // mild white but may be changed to reduce x (rule A.7 e)
            }
        }
        return 0;       // any colour is good!
    }
    
    private boolean solveDutch(int group, short[][] tIPassed, int reminder, int [] messagesPassed, boolean immediatelyReturn) {
        /*
                The algorithm follows the flow of the official FIDE rules, making use of recursion over score brackets.
         
                main parameter description:
                group =  the group to be paired (either the heterogeneous or homogeneous part of a score bracket)
                tIPassed, tILoc... = the array of elements to be paired. Each element stores: the absolute position in ranking, 
                           the colour preference, the group coming from and the group assigned to
                reminder = an alert flag to fire callback if needed (rules C9/C12)
                messages... = array of bidirectional parameter passing. It stores the command and optionally: the number
                           of pairs to be done in the remainder group, the x and the floats status
                nTeams     = the number of elements in the group to be paired
                S1         = the number of the left side elements in the group to be paired
                S2         = the number of the right side elements in the group to be paired
                addedPairs = the number of pairs done from the beginning
                strict     = flag fired when there are O, O+ or O-. The program tries to optimize colours first, then
                             it commute to the ordinary way 
        */
        int i=0, i1, i2, j, k, ID1, ID2, S1=0, M1, S2=0, score1, score2, index, nTeams, i1Abs, i2Abs, x, w, b, p, q, pDone, xStart, cD1, cD2;
        int [] messages={0,0,0,0,0,0};
        int wishCanReturn=0, wishCanReturn2=0, goodPairsDone=0, originalX, nZero, nZeroPlus, nZeroMinus, w1, w2, b1, b2, z, zStart=0, zMax=0, originalZ;
        int originalW, originalB, nBye, coD1, coD2, valid, p0;
        int originalS1=0, originalS2=0, pureWhite=0, pureBlack=0, prevScore=0, jIndexForUnblock;
        short aP=addedPairs, aPReserve=0, aPTemp, aP2;
        boolean done, homogeneous=false, B2, retry=false, avoidC12=false;
        int upfloatsB5, upfloatsB6, downfloatsB5, downfloatsB6;
        int maxUpfloatsB5, maxUpfloatsB6, maxDownfloatsB5, maxDownfloatsB6, floatsB5, floatsB6, lastID5, lastID6, BYECandidates;
        int originalMaxUpfloatsB5, originalMaxUpfloatsB6, originalMaxDownfloatsB5, originalMaxDownfloatsB6;
        boolean gotoDecreasep=false, avoidB2=false, maybeNeededAvoidB2=false;
        boolean allowColourChangeA7d=false, maybeAllowedColourChangeA7d=false, allowColourChangeA7e=false, maybeAllowedColourChangeA7e=false;
        boolean fcB5, fcB6, B3=false, isC1, firstC12=false;
        int originalnZeroPlus, originalnZeroMinus, originalnZero, n, nA7d, nByeS2;
        String [] S; String hText, str1, str2, str3, seen, flag;
        short [][] tILoc; 
        long time;
                /* start of C1 section. If the score bracket contains a player for whom no opponent can be found
                   within this score bracket without violating B1 or B2 then:
                    · if this player was moved down from a higher score bracket apply C12.
                    · if this score bracket is the lowest one apply C13.
                    · in all other cases: move this player down to the next score bracket. */
        explain=jCheckBox6.isSelected() || verbosity_level>=0;
        if (immediatelyReturn) explain=false;
        if (explain && !jDialog4.isVisible()) {
            jTextArea1.setText("");
            jDialog4.setVisible(true); // show explain window (and action button ...)
            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
            EngineThread.yield();
        }
        
        if (group == lowestGroup) { messagesPassed[0]|=RETURN_C13; // score bracket is the lowest one. Signal rule C.13
            if (strong_verbose && explain) addExpl("group=", ""+group, "is the last group.", "flag for C13."); 
            lastCompletedGroup=99999;
        }
        if (lastVisitedGroup<group) lastVisitedGroup=group;
        if (alreadySeen[group].equals("")) firstC12=true;

        tILoc = duplicate(tIPassed);  // make a local copy of the array
        nTeams=w=b=xStart=nZero=nZeroPlus=nZeroMinus=q=w1=w2=b1=b2=0;
        seen=";";  
        for (j=0; j<availableTeams; j++)   // count heterogeneous score bracket
            if (tILoc[j][3] == group && tILoc[j][2] < group) {
                    nTeams++; S1++; seen+="-"+j;
                    if (tILoc[j][1] > 10) {
                        if (tILoc[j][1]%2 == 1) w1++; if (tILoc[j][1]%2 == 0) b1++; 
                    }
                    else if (tILoc[j][1] == 1) nZeroPlus++;
                    else if (tILoc[j][1] == 2) nZeroMinus++;
                    else if (tILoc[j][1] == 0) nZero++;
            }
        for (j=0; j<availableTeams; j++)   // count homogeneous score bracket
            if (tILoc[j][3] == group && tILoc[j][2] >= group) {
                nTeams++; S2++; seen+="-"+j;
                if (tILoc[j][1] > 10) {
                    if (tILoc[j][1]%2 == 1) w2++; if (tILoc[j][1]%2 == 0) b2++; 
                }
                else if (tILoc[j][1] == 1) nZeroPlus++;
                else if (tILoc[j][1] == 2) nZeroMinus++;
                else if (tILoc[j][1] == 0) nZero++;
            }
        if (!immediatelyReturn && !seen.equalsIgnoreCase(";")) {    // workaround to avoid infinite loops
            seen+="@"+(nZero+nZeroPlus+nZeroMinus)+"#"+messagesPassed[0]+"^"+messagesPassed[1]+"!"+messagesPassed[2]+"§"+messagesPassed[3]+"?"+messagesPassed[4]+"@"+messagesPassed[6]+"."+reminder+";";
            if (alreadySeen[group].contains(seen)) return false;    // this group was already seen without solution; go back
            alreadySeen[group]+=seen;
        }
        if (strong_verbose && explain) write_to_log(true);  // avoid speed problem with strings in the log window ...
        short [] toPermute = new short [nTeams];
        nTeams = 0;
        for (j=0; j<availableTeams; j++)   // load the score bracket
            if (tILoc[j][3] == group) {
                toPermute[nTeams++] = (short)j;
                i1Abs=tILoc[j][0];
                coD1=tILoc[j][1];
                if (coD1>30 && currRound%2==1) maybeAllowedColourChangeA7d=true;
                if (coD1==11) pureWhite++;      // may be needed later to avoid blocking situations
                if (coD1==12) pureBlack++;
            }

        if ((reminder&REMINDER_FOR_AVOIDING_C12)>0) avoidC12=true;

        if (nTeams==0) {    // empty group. go over
            if (group>lowestGroup) return false;                      // never say ... bug?
            if ((reminder&REMINDER_FOR_C9)>0 && messagesPassed[1]>99999) {
                if (strong_verbose && explain) addExpl("group=", ""+group, "empty remainder group", "going back"); return false;}
            messages=new int [7];
            messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[5]=messages[6]=0;
            if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
            if (solveDutch(group+1, tILoc, NO_REMINDER|(reminder&REMINDER_FOR_C12), messages, immediatelyReturn)) return true;  // retry 
                        if ((messages[0]&RETURN_C13)>0) { messagesPassed[0]|=RETURN_C13; // pass back the flag
                                for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                    } 
                        }
            if ((messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0) messagesPassed[0]|=RETURN_CONDITIONAL_DECREASE_P; // pass back the flag
            addedPairs=aP; return false;
        }   
        
        upfloatsB5=upfloatsB6=maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB6=0;   // float violations
        downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;   // passed or 0
        if (!jCheckBox1.isSelected() || immediatelyReturn) {upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;}
        
        if (nTeams==1) { 
            i1=toPermute[0];
            i1Abs=tILoc[i1][0];
            ID1 = tempIndex[i1Abs]+1;
            if (group>=lowestGroup) {    // check if this can take the BYE 
                if (canTakeTheBye(i1Abs) && addedPairs==availableTeams/2) { // good?
                    if (immediatelyReturn) return true;
                } else {
                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"cannot take the BYE!", "going back");
                    return false;    // retry
                }
            }
            if (immediatelyReturn) return true;
            done=true;
            if (downfloatsB5==0 && !allowedDownfloaterB5(ID1)) { // this cannot downfloat. 
                            done = false;
            }
            if (downfloatsB6==0 && !allowedDownfloaterB6(ID1)) { // this cannot downfloat. 
                            done = false;
            }
            if ((reminder&REMINDER_FOR_C9)>0 && !done) {if (strong_verbose && explain) addExpl("ID=",""+ID1,"cannot downfloat!","going back"); return false;}
            if (group>=lowestGroup) {    // odd elements. check if this can take the BYE
                if (canTakeTheBye(i1Abs) && addedPairs==availableTeams/2) { // good?
                    roundsDetail[addedPairs++][0][currRound-1]=""+ID1+"-0-0-0";
                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"takes the BYE");
                    aP2=addedPairs; normal_explain(group, S1, true, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); 
                    return true;    // pairing finished !! terminate
                } else {
                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"cannot take the BYE!", "going back");
                    return false;    // retry
                }
            } else {    //  only one player in the score bracket. force move down ...
                if (strong_verbose && explain) {
                    addExpl("=========================================","","");
                    addExpl("ID=",""+ID1,"moved down to group:",""+(group+1));
                }
                tILoc[i1][3]++; 
                messages=new int [7];
                messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[5]=messages[6]=0;
                if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
                if (avoidC12) reminder=REMINDER_FOR_AVOIDING_C12;
                else if ((reminder&REMINDER_FOR_C12)>0) reminder=REMINDER_FOR_C12;
                else reminder=NO_REMINDER;
                if (solveDutch(group+1, tILoc, reminder, messages, immediatelyReturn)) return true;  // retry 
                        if ((messages[0]&RETURN_C13)>0) { messagesPassed[0]|=RETURN_C13; // pass back the flag
                                for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                    } 
                        }
                if ((messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0) messagesPassed[0]|=RETURN_CONDITIONAL_DECREASE_P; // pass back the flag
                if (strong_verbose && explain) addExpl("unable to pair the group:",""+(group+1),"");
                addedPairs=aP; return false;
            }
        }

        if (group>lowestGroup) return false;                      // never say ... bug?

        // C2.a
        originalS1=S1; originalS2=nTeams-originalS1;     // useful at the end to avoid blocking situations in the lowest group
        p0=nTeams/2;
        if ((reminder&REMINDER_FOR_C9)>0 || (reminder&REMINDER_FORCE_HOMOGENEOUS)>0) {S1=nTeams/2; S2=nTeams-S1; homogeneous=true;}   // reminder group: force homogeneous
        p=S1; M1=S1;      // M1 is a global variable to set combinations for heterogeneous groups
        if (S1>=S2 && !homogeneous) { // heterogeneous score bracket with prevalent downfloaters: treat as homogeneous
            M1=p0=p=S2; S1=p; S2=nTeams-S1; homogeneous=true;
            if(strong_verbose && explain) addExpl("group=",""+group,"heterogeneous score bracket with prevalent downfloaters: treat as homogeneous");
        } 
        if (S1==0 || S2==0) { S1=nTeams/2; S2=nTeams-S1; p=S1; M1=S1; homogeneous=true; }   // homogeneous score bracket: divide half (round downward)
        if ((reminder&REMINDER_FOR_C9)>0) { 
                    if (messagesPassed[1]>0) p0=messagesPassed[1]-99999;
                    if (p>p0) p=p0;
                    if (p>0) {S1=p; S2=nTeams-S1;}
                    if ((reminder&REMINDER_FOR_A7e)>0) allowColourChangeA7e=true; 
                    if ((reminder&REMINDER_FOR_A7d)>0) allowColourChangeA7d=true;
                    if ((reminder&REMINDER_FOR_maybeNeededAvoidB2)>0) {maybeNeededAvoidB2=avoidB2=true;}
        } 
        nBye=0; 
        if (p0>0 && lnb<9999) {             // optimization. check how many can take the bye if odd players
            for (j=0; j<availableTeams; j++) 
              if (tILoc[j][3] >= group) {   
                i1Abs=tILoc[j][0];
                ID1 = tempIndex[i1Abs]+1;
                if (canTakeTheBye(i1Abs)) nBye++;
                if (nBye>1) break;
              }
            if (nBye==0) {if (strong_verbose && explain) addExpl("group=", ""+group, "none can take the BYE!", "going back"); 
                messagesPassed[0]|=RETURN_C13; // Signal rule C.13
                lastCompletedGroup=99999;
                return false;
            }
            if (group==lowestGroup) {
                nByeS2=0; B3=false;
                for (k=0; k<nTeams; k++) {
                    i1=toPermute[k];
                    i1Abs=tILoc[i1][0];
                    ID1 = tempIndex[i1Abs]+1;
                    if (k>=S1) if (tILoc[i1][2]>group) B3=true;
                    if (canTakeTheBye(i1Abs)) if (k>=S1) nByeS2++;
                }
                if (nByeS2==0) {
                    if (strong_verbose && explain) addExpl("homogeneous side","cannot take the BYE!", "");
                    if (mild_verbose && explain) addExpl("group:",""+group,"going to break C12", "");
                    reminder&=~REMINDER_FOR_C12; 
                    if (S1==1) {homogeneous=true; S1=M1=nTeams/2; S2=nTeams-S1; upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;
                                p0=p=S1; if (mild_verbose && explain) addExpl("group=", ""+group, "retry as homogeneous", ""); 
                                if (S1==S2 && p0==S1) {downfloatsB5=downfloatsB6=99999; maxDownfloatsB5=maxDownfloatsB6=0;}
                    } else if (!homogeneous && B3) {
                        if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "");
                        p--; M1--;
                    }
                }
            }
        }
        if (!immediatelyReturn && ((reminder&REMINDER_FOR_C9)==0 || p0==0) && (reminder&REMINDER_KEEP_HETEROGENEOUS)==0) {
          // C.1 rule. determine elements without any legal opponent. Apply B1 and B2 (not for top scorers)
          retry=isC1=false; valid=nTeams; BYECandidates=floatsB5=floatsB6=lastID5=lastID6=0;
          for (j=0; j<nTeams; j++) {
            done=false;
            i1=toPermute[j];
            i1Abs=tILoc[i1][0];
            ID1 = tempIndex[i1Abs]+1;
            coD1=tILoc[i1][1];
            if(!homogeneous && j>=originalS1 && upfloatsB5<90000 && !allowedUpfloaterB5(ID1)) maxUpfloatsB5++;
            if(!homogeneous && j>=originalS1 && upfloatsB6<90000 && !allowedUpfloaterB6(ID1)) maxUpfloatsB6++;
            fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
            if (tILoc[i1][2]<group) {fcB5=false; fcB6=true;} // already floated; set B6
            if(downfloatsB5<90000 && fcB5) maxDownfloatsB5++;
            if(downfloatsB6<90000 && fcB6) maxDownfloatsB6++;
            if (p0>0)
                for (k=0; k<nTeams; k++) {
                        if (k==j) continue;
                        i2=toPermute[k];
                        i2Abs=tILoc[i2][0];
                        ID2 = tempIndex[i2Abs]+1;
                        if (wasPlayed(ID1, ID2)) continue; // check B1 rule
                        B2=true;
                        if (jCheckBox1.isSelected()) {
                            score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                            if (score1 < score2) score1=score2;     // take the highest of the two
                            if (lastRound && score1>score50) B2=false;
                        } else B2=false;
                        coD2=tILoc[i2][1]; 
                        if (B2 && establishCompatibility(i1, i2, true, coD1, coD2)==0) continue;     // check B2 rule
                        done=true; 
                }
            if (!done) {
                    if (group==lowestGroup) {
                        if (nTeams%2==0) {
                            if (strong_verbose && explain) addExpl("ID=",""+ID1,"without any legal opponent.","going back");
                            return false;
                        }
                        if (canTakeTheBye(i1Abs)) BYECandidates++; else {
                            if (strong_verbose && explain) addExpl("ID=",""+ID1,"without any legal opponent.","going back");
                            return false;
                        }
                    }
                    if ((reminder&REMINDER_FOR_C9)>0) {
                        fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                        if (tILoc[i1][2]<group) {fcB5=false; fcB6=true;} // already floated; set B6
                        if (fcB5) { // check can downfloat. 
                            floatsB5++; lastID5=ID1;
                        }
                        if (fcB6) { 
                            floatsB6++; lastID6=ID1;
                        }
                    }
                    if (j<originalS1) isC1=true;
                    valid--;
                    tILoc[i1][3]++;                               // move down and flag
                    if (!retry) if (strong_verbose && explain) addExpl("=========================================","","");
                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"without any legal opponent.","moved down to group:",""+(group+1));
                    retry=true;
            }
          }
          if (retry) {
            if (p0>0 && (reminder&REMINDER_FOR_C12)>0) {
                if (valid/2>=originalS2) {
                }
                else if (!firstC12 && ((valid/2<p0) || isC1)) {
                        if (strong_verbose && explain) addExpl("group=", ""+group, "reminder for C12", "going back"); 
                        return false;
                }
                else if (firstC12 && isC1) {
                        if (strong_verbose && explain) addExpl("group=", ""+group, "reminder for C12", "going back"); 
                        return false;
                } 
            }
            if (BYECandidates>1) { if (strong_verbose && explain) addExpl("group=", ""+group, ">1 can take the BYE!", "going back");
                    return false; }
            if (group==lowestGroup && nTeams-valid>1) { if (strong_verbose && explain) addExpl("group=", ""+group, "not enough elements remained!", "going back");
                    return false; } 
            if (!immediatelyReturn) {
              if ((reminder&REMINDER_FOR_C9)>0) {
                if (p0>0 && (valid/2<p0)) {
                        if (strong_verbose && explain) addExpl("group=", ""+group, "reminder for C9", "going back"); 
                        return false;
                } 
                if (floatsB5>downfloatsB5) {if (strong_verbose && explain) addExpl("ID=",""+lastID5,"cannot downfloat!","going back"); return false;}
                if (floatsB6>downfloatsB6) {if (strong_verbose && explain) addExpl("ID=",""+lastID6,"cannot downfloat!","going back"); return false;}
                downfloatsB5-=floatsB5; downfloatsB6-=floatsB6;
                if (downfloatsB5<0) downfloatsB5=0; if (downfloatsB6<0) downfloatsB6=0;
                messagesPassed[3]=downfloatsB5*CUTOFF+downfloatsB6;
              }
              if (p0==0) {reminder=NO_REMINDER; messages=new int [7];
                messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[5]=messages[6]=0;
                if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
              } else {messages=messagesPassed; if (homogeneous) reminder|=REMINDER_FORCE_HOMOGENEOUS;}
            } else messages=messagesPassed;
            if (solveDutch(group, tILoc, reminder, messages, immediatelyReturn)) return true;
            addedPairs=aP; if (immediatelyReturn) return false;
                        if ((messages[0]&RETURN_C13)>0) { messagesPassed[0]|=RETURN_C13; // pass back the flag
                                for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                    } 
                        }
            if ((messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0) messagesPassed[0]|=RETURN_CONDITIONAL_DECREASE_P; // pass back the flag, if any
            if (strong_verbose && explain) addExpl("group=", ""+group, "cannot retry", "going back");
            return false;
          }
        }
        w=w1+w2; b=b1+b2;
        if (strong_verbose && explain) if (currRound%2==0) addExpl("group=", ""+group, "w="+w+", b="+b+", nZero="+nZero+", nZeroPlus="+nZeroPlus+", nZeroMinus="+nZeroMinus, "");
                                       else addExpl("group=", ""+group, "w="+w+", b="+b+", nZero="+nZero);
        originalnZeroPlus=nZeroPlus; originalnZeroMinus=nZeroMinus; originalnZero=nZero;
        originalW=w; originalB=b;
        
        // C.2 rule. Determine X1 and Z1  (p already done)
        if (b+originalnZeroMinus>w+originalnZeroPlus) xStart=p0-w-originalnZeroPlus-originalnZero;
        else xStart=p0-b-originalnZeroMinus-originalnZero;
        if (xStart<0) xStart=0;
        if (xStart>0 && (reminder&REMINDER_KEEP_HETEROGENEOUS)>0) return false;
        nA7d=0;                                     // # of violations of the A.7d rule
        if (currRound%2==0) {zMax=nZeroPlus+nZeroMinus; if (zMax>0) maybeAllowedColourChangeA7e=true;
            if (b>w) zStart=p0-w-originalnZeroMinus-originalnZeroPlus-originalnZero;
            else zStart=p0-b-originalnZeroMinus-originalnZeroPlus-originalnZero;
            if (zStart<0) zStart=0;
            if (zStart<xStart) {allowColourChangeA7e=true; if (strong_verbose && explain) addExpl("group=", ""+group, "Apply A7.e", "allow colour change", "z="+zStart);}
        }
        
        // check passed values, if any
        originalX=xStart;
        if (messagesPassed[2]>=99999) xStart=messagesPassed[2]-99999;  // x passed if any
        if (xStart>p0) xStart=p0;               // never say ... bug ?

        if ((reminder&REMINDER_FOR_C9)>0) {
            zStart=xStart-messagesPassed[4];   // z passed if any
            allowColourChangeA7e=true;
        } 
        if ((reminder&REMINDER_FOR_A7d)>0) { 
            nA7d=messagesPassed[6];  // A7d violations allowed passed if any
            if (xStart<nA7d) { 
                if (strong_verbose && explain) addExpl("group=", ""+group, "passed a lower x!", "going back");
                return false;     // needed more than passed, go back!
            }
            if (nA7d>0) allowColourChangeA7d=true;
            zStart=0;
        }
        if (zStart>p0) zStart=p0;               // never say ... bug ?

        // C.3 Set requirements P, B2, A7d, X, Z, B5/B6
        // *** already done ***
        
        // C.4 Put the highest players in S1, all other players in S2.
        // *** already done ***
        
        // C.5 Order the players in S1 and S2 according to A2.
        // *** already done *** 
        
        if (!jCheckBox1.isSelected() || immediatelyReturn) {upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999; xStart=zStart=99999;}
        if (originalX>xStart+(nTeams-2*p0)/2) { 
            if (strong_verbose && explain) addExpl("group=", ""+group, "passed a lower x!", "going back");
            return false;     // needed more than passed, go back!
        }
        originalX=xStart;                       // fix the starting x value
        originalZ=zStart;                       // fix the starting z value
        
        if (mild_verbose && explain) {
            hText = Main.localizedText("heterogeneous")+" ";
            if (homogeneous) hText = Main.localizedText("homogeneous")+" ";
            if ((reminder&REMINDER_FOR_C9)>0) hText+=Main.localizedText("reminder for C9")+" ";
            if ((reminder&REMINDER_FOR_C12)>0) hText+=Main.localizedText("reminder for C12")+" ";
            if ((reminder&REMINDER_FOR_A7e)>0) hText+=Main.localizedText("reminder for A7.e")+" ";
            if ((reminder&REMINDER_FOR_A7d)>0) hText+=Main.localizedText("reminder for A7.d");
            addExpl("=========================================","","");        
            addExpl("Now pairing the group:",""+group,hText);
            addExpl("S1=",""+S1,"members");
            if (S1>0)
              for (k=0; k<S1; k++) {
                i1=toPermute[k];
                i1Abs=tILoc[i1][0];
                ID1 = tempIndex[i1Abs]+1;
                cD1=tILoc[i1][1];
                flag="";
                if (!allowedUpfloaterB5(ID1)) flag+="UB5 ";        // flag cannot upfloat
                fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                if (tILoc[i1][2]<group) {fcB5=false; fcB6=true;} // already floated; set B6
                if (fcB5) flag+="DB5 ";      // flag cannot downfloat
                if (!allowedUpfloaterB6(ID1)) flag+="UB6 ";        // flag cannot upfloat
                if (fcB6) flag+="DB6 ";      // flag cannot downfloat
                addExpl("ID=", ID1, "colour due", colourPreference[cD1], "flag:", flag);
              }
            addExpl("------------------------------------------","","");
            addExpl("S2=",""+S2,"members");
            if (S2>0)
              for (k=0; k<S2; k++) {
                i2=toPermute[S1+k];
                i2Abs=tILoc[i2][0];
                ID2 = tempIndex[i2Abs]+1;
                cD2=tILoc[i2][1];
                flag="";
                if (!allowedUpfloaterB5(ID2)) flag+="UB5 ";        // flag cannot upfloat
                if (!allowedDownfloaterB5(ID2)) flag+="DB5 ";      // flag cannot downfloat
                if (!allowedUpfloaterB6(ID2)) flag+="UB6 ";        // flag cannot upfloat
                if (!allowedDownfloaterB6(ID2)) flag+="DB6 ";      // flag cannot downfloat
                addExpl("ID=", ID2, "colour due", colourPreference[cD2], "flag:", flag);
              }
            addExpl("------------------------------------------","","");
            str1=""; // if (zMax>0) str1=" zMax="+zMax;
            if (currRound%2==0) addExpl("p0="+p0, "p="+p,"x="+xStart+str1+" z="+zStart);
            else addExpl("p0="+p0, "p="+p,"x="+xStart);
        }
        
        // some optimizations ...
        if (!jCheckBox1.isSelected() || immediatelyReturn) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
        originalMaxUpfloatsB5=maxUpfloatsB5; originalMaxUpfloatsB6=maxUpfloatsB6;
        originalMaxDownfloatsB5=maxDownfloatsB5; originalMaxDownfloatsB6=maxDownfloatsB6;
        if (maxUpfloatsB5>p) maxUpfloatsB5=p; 
        if (maxUpfloatsB6>p) maxUpfloatsB6=p; 
        if (maxDownfloatsB5>nTeams-p0*2) maxDownfloatsB5=nTeams-p0*2;
        if (maxDownfloatsB6>nTeams-p0*2) maxDownfloatsB6=nTeams-p0*2;

        if ((reminder&REMINDER_KEEP_HETEROGENEOUS)>0) homogeneous=false; // workaround for a blocking situation
        
        if (homogeneous) {maxUpfloatsB5=maxUpfloatsB6=0; upfloatsB5=upfloatsB6=99999;} // some optimization to accelerate computation
        if (S1==S2 && p0==S1) downfloatsB5=downfloatsB6=99999;

        prevScore=messagesPassed[5];
        short [] permutation = new short [nTeams];
        
        // check if B.3 rule to be applied
        int nGroups=0, prevGroup=0;
        for (j=0; j<nTeams; j++) {
            i1=toPermute[j];
            if (prevGroup<tIPassed[i1][2]) {prevGroup=tIPassed[i1][2]; nGroups++;}
        }
        if ((nGroups>2 || (nGroups==2 && homogeneous)) && (reminder&REMINDER_FOR_C9)==0 && nTeams>2 && !immediatelyReturn) B3=true;
        if (p0>MPAFC && group<lowestGroup/2) B3=false;      // avoid computational complexity at top ranking
        
        // C.6 C.7 C.8 are treated together. see nextPermutation() for details on permutations, exchanges and combinations
        
        if (B3) {       // apply B3 rule to minimize the sum of square differences
            // 1.st enumerate a tree of all possible solutions with calculated weight 
          int weight=0, minWeight=Integer.MAX_VALUE, loops;
          for (;;) { index=0; loops=0;
            weight=valid=0; minWeight=Integer.MAX_VALUE;
            Node firstNode=null, prevNode=null, node;
            if (mild_verbose && explain) addExpl("group=", ""+group+".", ""+nTeams+"","Apply the min sq diff algorithm");
            aP=addedPairs;
            nextPermutation(permutation, -1, homogeneous, M1, S1);  // populate array
            do {
                loops++;
                if (loops%10000==0) {
                        time=System.currentTimeMillis();
                        if (time-engineStartTime>MAXWAITTIME && loops<4000000) loops=4000000;
                        if (loops==4000000 && !avoidBlocking) {
                            if (!jDialog4.isVisible() && !immediatelyExitAfterCalculation) {
                                jTextArea1.setText("");
                                jDialog4.setVisible(true); // show explain window (and action button ...)
                            } else addExpl("","","");
                            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                            addExpl("","","");
                            addExpl("WARNING.", "There seems to be a computational complexity in group", ""+group+"!", "Please, don't be impatient. Let JavaPairing to work at least the same amount of time before acting!");
                            addExpl("","","");
                            if (!immediatelyExitAfterCalculation) {explain = true; if (verbosity_level<0) verbosity_level=0;}
                            addExpl("group=", ""+group+".", ""+nTeams+"","Apply the min sq diff algorithm");
                            if (valid>0) addExpl("weight=",""+minWeight,"Not yet optimized, but a solution was found!");
                        }
                        if (time-engineStartTime>2*MAXWAITTIME && loops<8000000) loops=8000000;
                        if (loops>=8000000 && !avoidBlocking) {avoidBlocking=true;
                            jDialog4.setVisible(true); // show explain window (and action button ...)
                            if (valid>0) addExpl("weight=",""+minWeight,"Not yet optimized, but a solution was found!");
                            addExpl("","","");
                            addExpl("WARNING.","wait more or press 'user action!' to break permutations","");
                        }
                }
                EngineThread.yield();
                explain=jCheckBox6.isSelected() || verbosity_level>=0;
                if (!engineRunning) {addExpl("","",""); addExpl("user action!","","");  // ended by the user?
                    if (minWeight<Integer.MAX_VALUE) addExpl("weight=",""+minWeight,"the best solution found!");
                    addExpl("WARNING.","wait more",""); EngineThread.yield();
                    engineRunning=true; avoidBlocking=false; 
                    visits=0; engineStartTime=System.currentTimeMillis(); break; }   
                weight=calculateSolutionB3(group, nTeams, S1, p, p0, homogeneous, permutation, toPermute, tIPassed);       // calculate the weight of this permutation
                if (weight<Integer.MAX_VALUE) {
                    if (valid<50000 || minWeight>weight-400) {
                        node=new Node(permutation, weight);
                        if (firstNode==null) {firstNode=node;
                            if (mild_verbose && explain) addExpl("weight=",""+weight,"Not yet optimized, but a solution was found!"); 
                        }
                        if (prevNode!=null) prevNode.setNextNode(node); // link tree node
                        prevNode=node;
                    }
                    if (minWeight>weight) minWeight=weight; 
                    valid++;
                }
            } while (nextPermutation(permutation, nTeams, homogeneous, M1, S1));
            if (minWeight==Integer.MAX_VALUE) {
                if (mild_verbose && explain) addExpl("min sq diff", "unable to pair the group:",""+group, "Continue");
                addedPairs=aP; tILoc = duplicate(tIPassed);
            } else {
                reminder&=~REMINDER_FOR_C12;   // a solution was found! drop C.12 rule, if set
                for (;;) {                  // navigate the tree of solutions
                    visits++;
                    if (visits%1000==0) {
                        time=System.currentTimeMillis();
                        if (time-engineStartTime>MAXWAITTIME && visits<500000) visits=500000;
                        if (visits==500000 && !avoidBlocking) {
                            if (!jDialog4.isVisible() && !immediatelyExitAfterCalculation) {
                                jTextArea1.setText("");
                                jDialog4.setVisible(true); // show explain window (and action button ...)
                            } else addExpl("","","");
                            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                            addExpl("","","");
                            addExpl("WARNING.", "There seems to be a computational complexity in group", ""+group+"!", "Please, don't be impatient. Let JavaPairing to work at least the same amount of time before acting!");
                            addExpl("","","");
                            if (!immediatelyExitAfterCalculation) {explain = true; if (verbosity_level<0) verbosity_level=0;}
                            addExpl("group=", ""+group+".", ""+nTeams+"","Apply the min sq diff algorithm");
                            addExpl("weight=",""+minWeight,"Not yet optimized, but a solution was found!");
                        }
                        if (time-engineStartTime>2*MAXWAITTIME && visits<1000000) visits=1000000;
                        if (visits>=1000000 && !avoidBlocking) {avoidBlocking=true; addExpl("","","");
                            if (!jDialog4.isVisible()) jDialog4.setVisible(true);
                            if(avoidC12||group==lowestGroup) addExpl("WARNING.","wait more or press 'user action!' to decrease p",""+p0); 
                            else          addExpl("WARNING.","wait more or press 'user action!' to ignore C12","");
                        }
                    }
                    EngineThread.yield();
                    explain=jCheckBox6.isSelected() || verbosity_level>=0;
                    if (!engineRunning) break;    // ended by the user?
                    minWeight=Integer.MAX_VALUE;
                    node=firstNode;
                    Node good=null;
                    for (;;) {              // trasverse nodes to search next best solution
                        w=node.getWeight();
                        if (w<minWeight) {
                            minWeight=w;
                            good=node;
                        }
                        if (node.getNextNode()==null) break;    // end list
                        node=node.getNextNode();
                    }
                    if (good==null) break;      // finished !!
                    permutation=good.getPermutation();
                    good.setWeight(Integer.MAX_VALUE);      // deactivate this node
                    for (j=0;j<p;j++) {
                        i1=permutation[j];
                        i2=permutation[j+S1];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tIPassed[i1][0];
                        i2Abs=tIPassed[i2][0];
                        ID1 = tempIndex[i1Abs]+1;
                        ID2 = tempIndex[i2Abs]+1;
                        roundsDetail[addedPairs++][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";   // fix the pair
                    }
                    if (p<p0) {         // heterogeneous group. pair the reminder side
                        index=p0-p;
                        for (j=S1+p;j<S1+p0;j++) {
                            i1=permutation[j];
                            i2=permutation[j+index];
                            i1=toPermute[i1];
                            i2=toPermute[i2];
                            i1Abs=tIPassed[i1][0];
                            i2Abs=tIPassed[i2][0];
                            ID1 = tempIndex[i1Abs]+1;
                            ID2 = tempIndex[i2Abs]+1;
                            roundsDetail[addedPairs++][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";   // fix the pair
                        }
                    }
                    if (addedPairs==(availableTeams+1)/2) {
                        aP2=addedPairs; normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed);
                        return true;  // all paired. finished!!
                    }
                    for (k=aP; k<addedPairs; k++) {             // flush out paired elements
                        S= roundsDetail[k][0][currRound-1].split("-");
                        ID1=Integer.valueOf(S[0]);
                        ID2=Integer.valueOf(S[1]);
                        if (mild_verbose && explain) addExpl("group=", ""+group, "paired ID=",""+ID1,"and ID=",""+ID2);
                        i1Abs=indexAt(ID1-1, tempIndex);
                        i2Abs=indexAt(ID2-1, tempIndex);
                        for (j=0; j<nTeams; j++)
                            if (i1Abs==tILoc[toPermute[j]][0] || i2Abs==tILoc[toPermute[j]][0]) 
                                tILoc[toPermute[j]][3] = 0;
                    }
                    if (mild_verbose && explain) addExpl("","","");
                            if (lastCompletedGroup<group) lastCompletedGroup=group;
                            wishCanReturn2=NO_REMINDER; score1=0; 
                            for (j=0; j<nTeams; j++)    //  move any residual players down to the next score bracket
                                if (tILoc[toPermute[j]][3] == group) { // apply C6 (second sentence)
                                    i1=toPermute[j];
                                    i1Abs=tILoc[i1][0];
                                    ID1 = tempIndex[i1Abs]+1;
                                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"moved down to group:",""+(group+1));
                                    tILoc[i1][3]++;
                                    wishCanReturn2=REMINDER_FOR_C12;  // put reminder for C12
                                    score1 += teamScores[ID1-1][0];
                                }
                            if (!avoidC12 && (wishCanReturn&REMINDER_FOR_C12)>0 && group<lowestGroup && prevScore>0 && score1>prevScore) {   // Istanbul 2012
                                addedPairs=aP; tILoc = duplicate(tIPassed);
                                prevScore=0;
                                gotoDecreasep=true; break;          // workaround to go to set avoidC12=true and restart
                            } 
                            prevScore=score1;  
                            // prepare recursion into next group
                            messages=new int [7];
                            messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[6]=0;
                            messages[5]=messagesPassed[5];
                            if (wishCanReturn==NO_REMINDER) messages[5]=0;
                            wishCanReturn=wishCanReturn2;
                            if (avoidC12) wishCanReturn=NO_REMINDER; 
                            if (strong_verbose && explain) addExpl("done group:",""+group,"move to the next one");
                            aP2=addedPairs;
                            if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
                            if (solveDutch(group+1, tILoc, wishCanReturn, messages, immediatelyReturn)) {normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); return true;} // try next group
                            if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0) messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12; // pass back the flag
                            if ((messages[0]&RETURN_C13)>0) {messagesPassed[0]|=RETURN_C13; // pass back the flag
                                if (group<lowestGroup-1) {
                                    if (mild_verbose && explain) {
                                        addExpl("=========================================","","");
                                        addExpl("Apply C13. Join groups:",""+(group+1),"to:",""+lowestGroup);
                                    }
                                    lowestGroup = (short)(group+1);
                                    wishCanReturn=NO_REMINDER; avoidC12=true; index=-1; 
                                    for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                        }
                                    avoidBlocking=false; visits=0; engineStartTime=System.currentTimeMillis();
                                    addedPairs=aP; tILoc = duplicate(tIPassed); break;
                                } 
                            }
                            EngineThread.yield();               // (n.b. this is a java function to synchronize threads, not part of the algorithm)
                            explain=jCheckBox6.isSelected() || verbosity_level>=0;
                            if (explain && !jDialog4.isVisible()) {
                                jTextArea1.setText("");
                                jDialog4.setVisible(true); // show explain window (and action button ...)
                                addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                                EngineThread.yield();
                            }
                            addedPairs=aP; tILoc = duplicate(tIPassed);
                            messagesPassed[5]=messages[5];
                            if ((messages[0]&RETURN_DECREASE_P)>0) {
                                reminder&=~REMINDER_FOR_C12; avoidC12=true; gotoDecreasep=true; break;}
                            if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0 && !avoidC12) {
                                gotoDecreasep=true; break;}
                            if (group==lowestGroup-1 && ((p>MPAFC&&(messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0)||visits>2000000)) {
                                visits=0;
                                messages[0]&=~RETURN_CONDITIONAL_DECREASE_P;
                                if ((reminder&REMINDER_FOR_C9)>0) messagesPassed[0]|=RETURN_DECREASE_P;
                                avoidC12=true; reminder&=~REMINDER_FOR_C12; 
                                gotoDecreasep=true; break;
                            }
                            if (strong_verbose && explain) addExpl("unable to pair the group:",""+(group+1),"");
                            if (strong_verbose && explain) addExpl("","try a new pairing in group:",""+group);
                            if ((wishCanReturn2&REMINDER_FOR_C12)==0) {    // there was no reminder. avoid not useful loops and go to decrease p
                                if (group < lowestGroup) {
                                    gotoDecreasep=true;
                                    if (strong_verbose && explain) addExpl("group=",""+group, "avoid not useful loops.", "going to decrease p"); 
                                    if ((reminder&REMINDER_FOR_C9)>0) {messagesPassed[0]=messagesPassed[0]|RETURN_DECREASE_P; return false;}
                                    avoidC12=true;
                                    break;
                                }
                            }
                }
            }
            if (!engineRunning) {
                    if (avoidBlocking) { 
                        engineRunning=true; avoidBlocking=false; visits=0; engineStartTime=System.currentTimeMillis();
                        if(avoidC12||group==lowestGroup) {
                            if (explain) {addExpl("","",""); addExpl("user action!","going to decrease p","");}
                            reminder&=~REMINDER_FOR_C12; 
                        } else if (explain) {addExpl("","",""); addExpl("user action!","going to drop C12","");}
                    } else return true;  
            }
            if (index==-1) continue;
            // no solution found!!
            if ((reminder&REMINDER_FOR_C9)>0) { 
                    if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C9.", "going back");
                    return false;       // C9 rule (first sentence)
            }
            if ((reminder&REMINDER_FOR_C12)>0) {    // Apply C12
                    if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C12.", "going back");
                    return false;       // C12 rule. heterogeneous undo the previous pairings
            }
            if (group==lowestGroup) {       // score bracket is the lowest one. 
      //                  if (originalS1<originalS2-1) {if (strong_verbose && explain) addExpl("group=", ""+group, "a blocking situation detected in the lowest group.", "more floaters may be requested.");
      //                                messagesPassed[0]=messagesPassed[0]|RETURN_CONDITIONAL_DECREASE_P;} // a blocking situation detected in the lowest group!!
                        if (homogeneous) {
                            if (strong_verbose && explain) addExpl("group=", ""+group, "p cannot be decreased.", "going back");
                            return false; 
                        }
            } 
            if (group<lowestGroup && (wishCanReturn&REMINDER_FOR_C12)>0 && !avoidC12) {avoidC12=true; // 2nd try without C12
                    if (mild_verbose && explain) addExpl("group:",""+group,"going to break C12", "");
                    continue;
            }
            if (group==lowestGroup && nTeams%2==0) { if (strong_verbose && explain) addExpl("group=", ""+group, "p cannot be decreased.", "going back");
                return false;
            }
            if (p0>p && group<lowestGroup) {
                    if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "in the remainder group");
                    p0--; 
            } else {
                if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "");
                if (group==lowestGroup && !homogeneous && nTeams%2==1 && p<S1) {
                    if (strong_verbose && explain) addExpl("group=", ""+group, "p cannot be decreased.", "going back"); return false;
                }
                p--;  M1--; if (homogeneous) p0--; else p0=(S2+p)/2;
                if (p==0) {
                    if (homogeneous) break;
                    else {     // Istanbul 2012
                        // this may happen if the group is like [r s][x y z]  (or last group is like [x] [y z] and x goes to take the bye)
                        homogeneous=true; S1=M1=S2/2; S2=nTeams-S1; upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;
                        p0=p=S1; if (mild_verbose && explain) addExpl("group=", ""+group, "retry as homogeneous", ""); 
                        continue; 
                    } 
                }
                if (homogeneous) {S1=p; S2=nTeams-S1;}
            }  
            if (minWeight<Integer.MAX_VALUE) avoidC12=true;
          }
        }
        else {      // normal way ... does not need B.3
            if (!immediatelyReturn && (reminder&REMINDER_FOR_C9)==0 && p0<=MPAFC) {
                // in this situation a quick pairing without any optimization is tested to see if effective, to speed up the process
                if (strong_verbose && explain) addExpl("group=", ""+group, "quickly check if pairable", "");
                done=QuicklyCheckMayBePaired(group, nTeams, M1, S1, p, p0, homogeneous, reminder, permutation, toPermute, tILoc);
                if (done) {
                    if (strong_verbose && explain) addExpl("group=", ""+group, "is pairable.", "", "Continue");
                    reminder&=~REMINDER_FOR_C12;    // drop rule C.12 and continue
                } else {
                    if ((reminder&REMINDER_FOR_C12)>0) {
                        if (group==lowestGroup) {       // score bracket is the lowest one. 
                            if (originalS1<originalS2-1) {if (strong_verbose && explain) addExpl("group=", ""+group, "a blocking situation detected in the lowest group.", "more floaters may be requested.");
                                      messagesPassed[0]=messagesPassed[0]|RETURN_CONDITIONAL_DECREASE_P;} // a blocking situation detected in the lowest group!!
                        }
                        return false;
                    } else if (!homogeneous && group==lowestGroup && M1==1) {
                        homogeneous=true; S1=M1=nTeams/2; S2=nTeams-S1; upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;
                        p0=p=S1; xStart=originalX; if (mild_verbose && explain) addExpl("group=", ""+group, "retry as homogeneous", ""); 
                        if (S1==S2 && p0==S1) {downfloatsB5=downfloatsB6=99999; maxDownfloatsB5=maxDownfloatsB6=0;}
                        done=QuicklyCheckMayBePaired(group, nTeams, M1, S1, p, p0, homogeneous, reminder, permutation, toPermute, tILoc);
                        if (!done) return false;
                    } else {
                        if (p0>p && group<lowestGroup) {
                            if (strong_verbose && explain) addExpl("group=", ""+group,"Cannot perform pairing!","");
                            if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "in the remainder group");
                            p0--; 
                            if (originalX>0) originalX--; xStart=originalX;
                            if (originalZ>0) originalZ--; zStart=originalZ;
                            maxUpfloatsB5=originalMaxUpfloatsB5; maxUpfloatsB6=originalMaxUpfloatsB6;
                            maxDownfloatsB5=originalMaxDownfloatsB5; maxDownfloatsB6=originalMaxDownfloatsB6;
                            downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;   // passed or 0
                            if (maxUpfloatsB5>p) maxUpfloatsB5=p; 
                            if (maxUpfloatsB6>p) maxUpfloatsB6=p; 
                            if (maxDownfloatsB5>nTeams-p0*2) maxDownfloatsB5=nTeams-p0*2;
                            if (maxDownfloatsB6>nTeams-p0*2) maxDownfloatsB6=nTeams-p0*2;
                            if (homogeneous) {maxUpfloatsB5=maxUpfloatsB6=0; upfloatsB5=upfloatsB6=99999;} // some optimization to accelerate computation
//                            avoidC12=true;
                        } else if (p>1) {
                            if (strong_verbose && explain) addExpl("group=", ""+group,"Cannot perform pairing!","");
                            if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "");
                            p--;  M1--; 
                            if (homogeneous) { p0--; S1=p; S2=nTeams-S1;
                                if (originalX>0) originalX--; xStart=originalX;
                                if (originalZ>0) originalZ--; zStart=originalZ;
//                                avoidC12=true;
                            } else p0=(S2+p)/2;
                            maxUpfloatsB5=originalMaxUpfloatsB5; maxUpfloatsB6=originalMaxUpfloatsB6;
                            maxDownfloatsB5=originalMaxDownfloatsB5; maxDownfloatsB6=originalMaxDownfloatsB6;
                            downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;   // passed or 0
                            if (maxUpfloatsB5>p) maxUpfloatsB5=p; 
                            if (maxUpfloatsB6>p) maxUpfloatsB6=p; 
                            if (maxDownfloatsB5>nTeams-p0*2) maxDownfloatsB5=nTeams-p0*2;
                            if (maxDownfloatsB6>nTeams-p0*2) maxDownfloatsB6=nTeams-p0*2;
                            if (homogeneous) {maxUpfloatsB5=maxUpfloatsB6=0; upfloatsB5=upfloatsB6=99999;} // some optimization to accelerate computation
                        }  
                    }
                }
            }
            for (;;) {    
                if (p0>MPAFC && maybeAllowedColourChangeA7d && !allowColourChangeA7d && xStart>0 && xStart<90000)   // check for blocking situations
                    if (nTeams-w+pureWhite<p0 || nTeams-b+pureBlack<p0) { allowColourChangeA7d=true;
                        if (strong_verbose && explain) addExpl("group=",""+group,"avoid not useful loops.", "enable colour change A7.d"); 
                        xStart=zStart=originalX; nA7d=1; 
                        if (strong_verbose && explain) addExpl("p0="+p0, "p="+p,"x="+xStart+" nA7d="+nA7d);} 
                for (i = 0; i < nTeams; i++) permutation[i] = (short)i;  // populate permutation array
                index=nTeams-2; cD1=cD2=0; 
                if (2*p0==nTeams) downfloatsB5=downfloatsB6=99999; // not needed now
                if (strong_verbose && explain) {
                    addExpl("try group:",""+group,"(p="+p+")");
                    str1=str2="";
                    if (upfloatsB5<maxUpfloatsB5) str1+=Main.localizedText("With upfloater B5 constrain.")+"("+upfloatsB5+") ";
                    if (upfloatsB6<maxUpfloatsB6) str1+=Main.localizedText("With upfloater B6 constrain.")+"("+upfloatsB6+") ";
                    if (downfloatsB5<maxDownfloatsB5) str1+=Main.localizedText("With downfloater B5 constrain.")+"("+downfloatsB5+") ";
                    if (downfloatsB6<maxDownfloatsB6) str1+=Main.localizedText("With downfloater B6 constrain.")+"("+downfloatsB6+") ";
                    str3="(x="+xStart+") "; if (maybeAllowedColourChangeA7e) str3+="(z="+zStart+")";
                    if (maybeAllowedColourChangeA7d && !allowColourChangeA7d) str3+=Main.localizedText("strict"); addExpl(str1,str2,str3);
                }
                do {
                    goodPairsDone=0; x=xStart; z=zStart; n=nA7d; pDone=p; addedPairs=aP;    // reset variables 
                    nZeroPlus=originalnZeroPlus; nZeroMinus=originalnZeroMinus; nZero=originalnZero;
                    w=originalW; b=originalB;
                    EngineThread.yield();                       // (n.b. this is a java function to synchronize threads, not part of the algorithm)
                    explain=jCheckBox6.isSelected() || verbosity_level>=0;
                    if (!engineRunning) break;   // ended by the user?
                    visits++; 
                    if (visits%1000==0) {
                        time=System.currentTimeMillis();
                        if (time-engineStartTime>MAXWAITTIME && visits<500000) visits=500000;
                        if (visits==500000 && !avoidBlocking) {
                            if (!jDialog4.isVisible() && !immediatelyExitAfterCalculation) {
                                jTextArea1.setText("");
                                jDialog4.setVisible(true); // show explain window (and action button ...)
                            } else addExpl("","","");
                            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                            addExpl("","","");
                            addExpl("WARNING.", "There seems to be a computational complexity in group", ""+group+"!", "Please, don't be impatient. Let JavaPairing to work at least the same amount of time before acting!");
                            addExpl("","","");
                            EngineThread.yield();
                            if(!immediatelyExitAfterCalculation) {explain = true; if (verbosity_level<0) verbosity_level=0;}
                        }
                        if (time-engineStartTime>2*MAXWAITTIME && visits<1000000) visits=1000000;
                        if (visits>=1000000 && !avoidBlocking) {avoidBlocking=true; 
                            if (lastVisitedGroup>group) {addExpl("","",""); 
                            if (!jDialog4.isVisible()) jDialog4.setVisible(true);
                                if(avoidC12||group==lowestGroup) addExpl("WARNING.","wait more or press 'user action!' to decrease p",""+p0); 
                                else          addExpl("WARNING.","wait more or press 'user action!' to drop C12","");
                            }
                            EngineThread.yield();
                        }
                    }
                    if (explain && !jDialog4.isVisible()) {
                            jTextArea1.setText("");
                            jDialog4.setVisible(true); // show explain window (and action button ...)
                            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                            EngineThread.yield();
                    }
                    jIndexForUnblock=-1;
                    for (j=goodPairsDone; j<M1; j++) {              // C6 main loop
                        if (j>goodPairsDone) break;  // now allowed to skip unpaired
                        if (avoidBlocking) {
                            if (lastVisitedGroup==group && x==0 && (nZeroPlus+nZeroMinus+nZero)==0) {    // workaround for big groups. try w-b and b-w separately
                                if (immediatelyReturn || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0) return false;
                                avoidBlocking = false; visits=0; engineStartTime=System.currentTimeMillis();
                                addExpl("workaround. try to split the group at level", ""+j, "/"+M1);
                                jIndexForUnblock=j;
                                EngineThread.yield();
                                for (k=aP; k<addedPairs; k++) {             // flush out paired elements
                                    S= roundsDetail[k][0][currRound-1].split("-");
                                    ID1=Integer.valueOf(S[0]);
                                    ID2=Integer.valueOf(S[1]);
                                    if (mild_verbose && explain) addExpl("group=", ""+group, "paired ID=",""+ID1,"and ID=",""+ID2);
                                    i1Abs=indexAt(ID1-1, tempIndex);
                                    i2Abs=indexAt(ID2-1, tempIndex);
                                    for (j=0; j<nTeams; j++)
                                        if (i1Abs==tILoc[toPermute[j]][0] || i2Abs==tILoc[toPermute[j]][0]) 
                                            tILoc[toPermute[j]][3] = 0; 
                                }
                                if (mild_verbose && explain) addExpl("","","");
                                w1=w2=b1=b2=0;        // count w and b in S1 & S2 (a subgroup may be odd)
                                for (j=0; j<M1; j++) {
                                    i1=toPermute[permutation[j]];
                                    cD1=tILoc[i1][1];   
                                    if (cD1%2==1) if (tILoc[i1][3]==group) w1++;
                                    if (cD1%2==0) if (tILoc[i1][3]==group) b1++;
                                }
                                for (j=M1; j<nTeams; j++) {
                                    i2=toPermute[permutation[j]];
                                    cD2=tILoc[i2][1];   
                                    if (cD2%2==1) if (tILoc[i2][3]==group) w2++;
                                    if (cD2%2==0) if (tILoc[i2][3]==group) b2++;
                                }
                                if (w1==b2 && w1>0) {       // treat first the even subgroup 
                                    // temporarily remove remaining b-w pairs 
                                    for (j=0; j<M1; j++) {
                                        i1=toPermute[permutation[j]];
                                        i1Abs=tILoc[i1][0];
                                        ID1 = tempIndex[i1Abs]+1;
                                        cD1=tILoc[i1][1];    
                                        if (cD1%2==0) if (tILoc[i1][3]==group) tILoc[i1][3]=-1;
                                    }
                                    for (j=M1; j<nTeams; j++) {
                                        i2=toPermute[permutation[j]];
                                        cD2=tILoc[i2][1];   
                                        if (cD2%2==1) if (tILoc[i2][3]==group) tILoc[i2][3]=-1;
                                    }
                                    if (!solveDutch(group, tILoc, REMINDER_KEEP_HETEROGENEOUS, messagesPassed, true)) {addedPairs=aP; tILoc = duplicate(tIPassed); break;}
                                    // restore remaining b-w pairs 
                                    for (j=0; j<M1; j++) {
                                        i1=toPermute[permutation[j]];
                                        cD1=tILoc[i1][1];    
                                        if (cD1%2==0) if (tILoc[i1][3]==-1) tILoc[i1][3]=(short)group;
                                        if (cD1%2==1) tILoc[i1][3]=0;
                                    }
                                    for (j=M1; j<nTeams; j++) {
                                        i2=toPermute[permutation[j]];
                                        cD2=tILoc[i2][1];   
                                        if (cD2%2==1) if (tILoc[i2][3]==-1) tILoc[i2][3]=(short)group;
                                        if (cD2%2==0) tILoc[i2][3]=0;
                                    }
                                    w1=b2=0;
                                } else if (b1==w2 && b1>0) {
                                    // temporarily remove remaining w-b pairs 
                                    for (j=0; j<M1; j++) {
                                        i1=toPermute[permutation[j]];
                                        cD1=tILoc[i1][1];    
                                        if (cD1%2==1) if (tILoc[i1][3]==group) tILoc[i1][3]=-1;
                                    }
                                    for (j=M1; j<nTeams; j++) {
                                        i2=toPermute[permutation[j]];
                                        cD2=tILoc[i2][1];   
                                        if (cD2%2==0) if (tILoc[i2][3]==group) tILoc[i2][3]=-1;
                                    }
                                    if (!solveDutch(group, tILoc, REMINDER_KEEP_HETEROGENEOUS, messagesPassed, true)) {addedPairs=aP; tILoc = duplicate(tIPassed); break;}
                                    // restore remaining w-b pairs 
                                    for (j=0; j<M1; j++) {
                                        i1=toPermute[permutation[j]];
                                        cD1=tILoc[i1][1];    
                                        if (cD1%2==1) if (tILoc[i1][3]==-1) tILoc[i1][3]=(short)group;
                                        if (cD1%2==0) tILoc[i1][3]=0;
                                    }
                                    for (j=M1; j<nTeams; j++) {
                                        i2=toPermute[permutation[j]];
                                        cD2=tILoc[i2][1];   
                                        if (cD2%2==0) if (tILoc[i2][3]==-1) tILoc[i2][3]=(short)group;
                                        if (cD2%2==1) tILoc[i2][3]=0;
                                    }
                                    b1=w2=0;
                                } else if (nTeams==availableTeams) {            // workaround for all draw sample !!
                                    xStart=zStart=p0;                                  // cannot be paired without this !!!!!
                                    addExpl("group=", ""+group, "", "increase x and restart S2");
                                    addExpl("try group:",""+group,"(p="+p+")","(x="+xStart+") ");
                                    index=-2; break;              // restart permutations
                                } 
                                // try to complete the group and go over
                                aP2=addedPairs;
                                done=solveDutch(group, tILoc, REMINDER_KEEP_HETEROGENEOUS, messagesPassed, false);
                                if(done) {normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); return true; }
                                else {addedPairs=aP; tILoc = duplicate(tIPassed); break;}
                            }
                            if (nTeams==availableTeams && xStart>0 && xStart<p0) {          // workaround for all draw sample !!
                                    avoidBlocking = false; visits=0; engineStartTime=System.currentTimeMillis();
                                    xStart=p0;                                  // cannot be paired without this !!!!!
                                    addExpl("group=", ""+group, "", "increase x and restart S2");
                                    addExpl("try group:",""+group,"(p="+p+")","(x="+xStart+") ");
                                    index=-2; break;              // restart permutations
                            }
                        }
                        index=j+S1; if (index>=nTeams) break;
                        i1=permutation[j];
                        i2=permutation[index];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tILoc[i1][0];
                        i2Abs=tILoc[i2][0];
                        ID1 = tempIndex[i1Abs]+1;   // first element of the pair
                        ID2 = tempIndex[i2Abs]+1;   // second element of the pair
//                        if (nBye==1 && (ID1==lnb || ID2==lnb)) continue;    // workaround. last not BYE cannot be paired if left alone!
                        if (wasPlayed(ID1, ID2)) continue;              // check B1 rule
                        coD1=cD1=tILoc[i1][1]; coD2=cD2=tILoc[i2][1];
                        B2=true;
                        if (jCheckBox1.isSelected()) {
                            score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                            if (score1 < score2) score1=score2;     // take the highest of the two
                            if (lastRound && score1>score50 && (coD1==21 && coD2==21 || coD1==22 && coD2==22)) {maybeNeededAvoidB2=true; if (avoidB2) B2=false;}
                        } else B2=false;
                        if (B2 && establishCompatibility(i1, i2, allowColourChangeA7d, coD1, coD2)==0) continue;     // check B2 rule
                        if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                        if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                        if (cD1>0 && cD2>0 && cD1%2==cD2%2 && x==0) continue;
                        if (currRound%2==0 && cD1>10 && cD2>10 && cD1==cD2 && z==0) continue;
                        if (currRound%2==1 && (coD1>30 || coD2>30) && coD1>20 && coD2>20 && cD1==cD2 && n==0) continue;
                        if (cD1>10 && cD2>10 && cD1%2!=cD2%2) {
                            if (coD1==1) nZeroPlus--; if (coD2==1) nZeroPlus--;
                            if (coD1==2) nZeroMinus--; if (coD2==2) nZeroMinus--;
                            if (cD1==11) w--; if (cD2==11) w--;
                            if (cD1==12) b--; if (cD2==12) b--;
                        } else {
                            // calculate new x and z
                            int newnZeroPlus=nZeroPlus, newnZeroMinus=nZeroMinus, newnZero=nZero, newW=w, newB=b, newX, newZ=z;
                            if (coD1==0) newnZero--; if (coD2==0) newnZero--;
                            if (coD1==1) newnZeroPlus--; if (coD2==1) newnZeroPlus--;
                            if (coD1==2) newnZeroMinus--; if (coD2==2) newnZeroMinus--;
                            if (cD1==11) newW--; if (cD2==11) newW--;
                            if (cD1==12) newB--; if (cD2==12) newB--;
                            if (newB+newnZeroMinus>newW+newnZeroPlus) newX=p0-goodPairsDone-1-newW-newnZeroPlus-newnZero;
                            else newX=p0-goodPairsDone-1-newB-newnZeroMinus-newnZero;
                            if (newX>x) continue; 
                            if (newX<0) newX=0;
                            if (currRound%2==0) {
                                if (newB>newW) newZ=p0-goodPairsDone-1-newW-newnZeroMinus-newnZeroPlus-newnZero;
                                else newZ=p0-goodPairsDone-1-newB-newnZeroMinus-newnZeroPlus-newnZero;
                                if(newZ>z) continue;
                                if ((cD1==11 && cD2==11) || (cD1==12 && cD2==12)) {if(x<1 || z<1 || newZ==z) continue; z--; if (newX>x) continue; newX=-1;}
                                else if (coD1>0 && coD2>0 && cD1%2==cD2%2) newX=-1;
                                else newX=x;    // do not decrease!!
                            } else { if (coD1>0 && coD2>0) newX=-1;}  // force later decrease of x
                            nZeroPlus=newnZeroPlus; nZeroMinus=newnZeroMinus; nZero=newnZero;
                            w=newW; b=newB;
                            if (newX<x) {x--; if (x<0) continue;}
                            if (currRound%2==0 && x<z) continue;
                            if (currRound%2==1 && (coD1>30 || coD2>30) && coD1>20 && coD2>20 && cD1==cD2) {n--; if (n<0) continue;}
                        }
//                        addExpl("group=", ""+group, "paired ID=",""+ID1,"and ID=",""+ID2);
//                        addExpl("", "", "x=",""+x," z=",""+z);
                        roundsDetail[addedPairs++][0][currRound-1]=""+ID1+"-"+ID2+"-0-0";   // fix the pair
                        goodPairsDone++;
                        pDone--; if (pDone==0) break;    // done requested pairs = terminate this group
                    }
                    if (jIndexForUnblock==0) {  // workaround for impossibility to split a big group!! exchange S1<->S2
                        avoidBlocking = false; visits=0; engineStartTime=System.currentTimeMillis();
                        addExpl("workaround. force one exchange S1<->S2", "", "");
                        index=M1-1; // force exchange later in nextPermutation()
                    }
                    if (index==-2) { index=-1; continue; }
                    if (index==-1) break;
                    if (pDone>0) continue;      // not completed requested pairs. try a new permutation
                    if (immediatelyReturn) return true;
                    if (goodPairsDone>0) {
                    if (addedPairs==(availableTeams+1)/2) {     // pairing ended !!
                        if (mild_verbose && explain) { 
                            addExpl("","","");
                            for (k=aP; k<addedPairs; k++) {     // if requested list paired elements
                                S= roundsDetail[k][0][currRound-1].split("-");
                                ID1=Integer.valueOf(S[0]);
                                ID2=Integer.valueOf(S[1]);
                                addExpl("group=", ""+group, "paired ID=",""+ID1,"and ID=",""+ID2);
                            }
                            addExpl("","","");
                        }
                        aP2=addedPairs; normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); return true;  // all paired. finished!!
                    }

                    floatsB5=floatsB6=0; ID1=ID2=lastID5=lastID6=0; done=true; 
                    if (homogeneous) {      // reread the group and verify if unpaired may downfloat
                        for (j=0; j<nTeams; j++) {  // (part of C6 rule)     
                            boolean alreadyPaired = false;
                            for (k=addedPairs; k>aP;) {     // skip paired elements
                                S= roundsDetail[--k][0][currRound-1].split("-");
                                ID1=Integer.valueOf(S[0]);
                                ID2=Integer.valueOf(S[1]);
                                i1Abs=indexAt(ID1-1, tempIndex);
                                i2Abs=indexAt(ID2-1, tempIndex);
                                if (i1Abs==tILoc[toPermute[j]][0] || i2Abs==tILoc[toPermute[j]][0]) {
                                        alreadyPaired = true; break; }
                            }
                            if (!alreadyPaired) {
                                i1=toPermute[j];
                                i1Abs=tILoc[i1][0];
                                ID1 = tempIndex[i1Abs]+1;
                                fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                                if (tILoc[i1][2]<group) {fcB5=false; fcB6=true;} // already floated; set B6
                                if (fcB5) { // check can downfloat. 
                                    floatsB5++; lastID5=ID1;
                                }
                                if (fcB6) { 
                                    floatsB6++; lastID6=ID1;
                                }
                            }
                        }
                        if (floatsB5>downfloatsB5) {  // not good. try a new permutation
                            if (strong_verbose && explain) addExpl("ID=",""+lastID5,"cannot downfloat!","retry");
                            done=false; 
                        }  
                        if (floatsB6>downfloatsB6) {  // not good. try a new permutation
                            if (strong_verbose && explain) addExpl("ID=",""+lastID6,"cannot downfloat!","retry");
                            done=false; 
                        }  
                    } else {            // heterogeneous. evaluate upfloats
                        for (k=addedPairs; k>aP;) {     // see paired elements
                                S= roundsDetail[--k][0][currRound-1].split("-");
                                ID2=Integer.valueOf(S[1]);
                                if (!allowedUpfloaterB5(ID2)) { // check can upfloat
                                    floatsB5++; lastID5=ID2;
                                }
                                if (!allowedUpfloaterB6(ID2)) { // check can upfloat
                                    floatsB6++; lastID6=ID2;
                                }
                        }
                        if (floatsB5>upfloatsB5) {  // not good. try a new permutation
                            if (strong_verbose && explain) addExpl("ID=",""+lastID5,"cannot upfloat!","retry");
                            done=false; 
                        }  
                        if (floatsB6>upfloatsB6) {  // not good. try a new permutation
                            if (strong_verbose && explain) addExpl("ID=",""+lastID6,"cannot upfloat!","retry");
                            done=false; 
                        }  
                    }
                    if (!done) continue;    // go to a new permutation
                    for (k=aP; k<addedPairs; k++) {             // flush out paired elements
                        S= roundsDetail[k][0][currRound-1].split("-");
                        ID1=Integer.valueOf(S[0]);
                        ID2=Integer.valueOf(S[1]);
                        if (mild_verbose && explain) addExpl("group=", ""+group, "paired ID=",""+ID1,"and ID=",""+ID2);
                        i1Abs=indexAt(ID1-1, tempIndex);
                        i2Abs=indexAt(ID2-1, tempIndex);
                        for (j=0; j<nTeams; j++)
                            if (i1Abs==tILoc[toPermute[j]][0] || i2Abs==tILoc[toPermute[j]][0]) 
                                tILoc[toPermute[j]][3] = 0;
                    }
                    if (mild_verbose && explain) addExpl("","","");
                    }

                    if (homogeneous) {
                            if (lastCompletedGroup<group) lastCompletedGroup=group;
                            reminder&=~REMINDER_FOR_C12;   // a solution was found! drop reminder for C.12
                            wishCanReturn2=NO_REMINDER; score1=0; 
                            for (j=0; j<nTeams; j++)    //  move any residual players down to the next score bracket
                                if (tILoc[toPermute[j]][3] == group) { // apply C.6 (second sentence)
                                    i1=toPermute[j];
                                    i1Abs=tILoc[i1][0];
                                    ID1 = tempIndex[i1Abs]+1;
                                    if (strong_verbose && explain) addExpl("ID=",""+ID1,"moved down to group:",""+(group+1));
                                    tILoc[i1][3]++;
                                    wishCanReturn2=REMINDER_FOR_C12;  // put reminder for C12
                                    score1 += teamScores[ID1-1][0];
                                }
                            if (!avoidB2 && maybeNeededAvoidB2 && prevScore>0 && score1>prevScore) {   // Istanbul 2012
                                addedPairs=aP; tILoc = duplicate(tIPassed);
                                messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12;   // pass back information
                                if ((reminder&REMINDER_FOR_C9)>0) return false;
                                gotoDecreasep=true; break;          // workaround to go to set avoidB2=true and restart
                            } 
                            if (!avoidC12 && (wishCanReturn&REMINDER_FOR_C12)>0 && group<lowestGroup && prevScore>0 && score1>prevScore) {   // Istanbul 2012
                                addedPairs=aP; tILoc = duplicate(tIPassed);
                                messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12;   // pass back information
                                if ((reminder&REMINDER_FOR_C9)>0) return false;
                                avoidB2=true; gotoDecreasep=true; break;          // workaround to go to set avoidC12=true and restart
                            } 
                            prevScore=score1; 
                            // prepare recursion for next group
                            messages=new int [7];
                            messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[6]=0;
                            messages[5]=messagesPassed[5];
                            if (wishCanReturn==NO_REMINDER) messages[5]=0;
                            wishCanReturn=wishCanReturn2;
                            if (avoidC12) wishCanReturn=NO_REMINDER;
                            if (strong_verbose && explain) addExpl("done group:",""+group,"move to the next one");
                            aP2=addedPairs;
                            if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
                            if (solveDutch(group+1, tILoc, wishCanReturn, messages, immediatelyReturn)) {normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); return true;} // try next group
                            if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0) messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12; // pass back the flag
                            if ((messages[0]&RETURN_C13)>0) {messagesPassed[0]|=RETURN_C13; // pass back the flag
                                if (group<lowestGroup-1) {
                                    if ((reminder&REMINDER_FOR_C9)>0) return false;
                                    if (mild_verbose && explain) {
                                        addExpl("=========================================","","");
                                        addExpl("Apply C13. Join groups:",""+(group+1),"to:",""+lowestGroup);
                                    }
                                    lowestGroup = (short)(group+1);
                                    upfloatsB5=upfloatsB6=0; downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;
                                    xStart=originalX; zStart=originalZ; nA7d=messagesPassed[6];
                                    wishCanReturn=NO_REMINDER; avoidC12=true; index=-1;   // restart permutations
                                    for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                    }
                                    visits=0; engineStartTime=System.currentTimeMillis();
                                    addedPairs=aP; tILoc = duplicate(tIPassed); continue;
                                } 
                            }
                            EngineThread.yield();               // (n.b. this is a java function to synchronize threads, not part of the algorithm)
                            explain=jCheckBox6.isSelected() || verbosity_level>=0;
                            if (explain && !jDialog4.isVisible()) {
                                jTextArea1.setText("");
                                jDialog4.setVisible(true); // show explain window (and action button ...)
                                addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                                EngineThread.yield();
                            }
                            addedPairs=aP; tILoc = duplicate(tIPassed);
                            messagesPassed[5]=messages[5];
                            if ((messages[0]&RETURN_DECREASE_P)>0) {
                                reminder&=~REMINDER_FOR_C12; avoidB2=avoidC12=true; gotoDecreasep=true; break;}
                            if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0 && !avoidC12) {
                                avoidB2=true; gotoDecreasep=true; break;}
                            if (group==lowestGroup-1 && ((p>MPAFC&&(messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0)||visits>2000000)) {
                                visits=0;
                                messages[0]&=~RETURN_CONDITIONAL_DECREASE_P;
                                if ((reminder&REMINDER_FOR_C9)>0) messagesPassed[0]|=RETURN_DECREASE_P;
                                reminder&=~REMINDER_FOR_C12; avoidB2=avoidC12=true;
                                gotoDecreasep=true; break;
                            }
                            if (strong_verbose && explain) addExpl("unable to pair the group:",""+(group+1),"");
                            if (strong_verbose && explain) addExpl("","try a new pairing in group:",""+group);
                            if ((wishCanReturn2&REMINDER_FOR_C12)==0) {    // there was no reminder. avoid not useful loops and go to decrease p
                                if (group < lowestGroup) {
                                    gotoDecreasep=true;
                                    if (strong_verbose && explain) addExpl("group=",""+group, "avoid not useful loops.", "going to decrease p"); 
                                    if ((reminder&REMINDER_FOR_C9)>0) {messagesPassed[0]=messagesPassed[0]|RETURN_DECREASE_P; return false;}
                                    avoidC12=true;
                                    break;
                                }
                            } 
                    } else {
                        wishCanReturn=REMINDER_FOR_C9;
                        if (strong_verbose && explain) addExpl("done heterogeneous part of the group:", ""+group, "move to the remainder");
                        messages=new int [7];
                        messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[6]=0;  
                        if (p*2==nTeams) wishCanReturn=NO_REMINDER;
                        else {
                            messages[1]=p0-p+99999;
                            messages[2]=x+99999;
                            if (allowColourChangeA7e) wishCanReturn|=REMINDER_FOR_A7e;
                            if (allowColourChangeA7d) {wishCanReturn|=REMINDER_FOR_A7d; messages[6]=n;}
                            if (avoidB2) wishCanReturn|=REMINDER_FOR_maybeNeededAvoidB2;
                            messages[3]=downfloatsB5*CUTOFF+downfloatsB6;
                            messages[4]=x-z;
                        }
                        messages[5]=prevScore;
                        if (avoidC12) wishCanReturn|=REMINDER_FOR_AVOIDING_C12;
                        else if (p0*2<nTeams) wishCanReturn|=REMINDER_FOR_C12;
                        aP2=addedPairs;
                        if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
                        if (solveDutch(group, tILoc, wishCanReturn, messages, immediatelyReturn)) {normal_explain(group, S1, homogeneous || (reminder&REMINDER_KEEP_HETEROGENEOUS)>0, B3, xStart, maybeAllowedColourChangeA7e||maybeAllowedColourChangeA7d, zStart, aP, aP2, tIPassed); return true;} // go to the homogeneous part
                        if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0) messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12; // pass back the flag
                        if ((messages[0]&RETURN_C13)>0) { messagesPassed[0]|=RETURN_C13; // pass back the flag
                                if (group<lowestGroup-1) {
                                    if (mild_verbose && explain) {
                                        addExpl("=========================================","","");
                                        addExpl("Apply C13. Join groups:",""+(group+1),"to:",""+lowestGroup);
                                    }
                                    lowestGroup = (short)(group+1); 
                                    upfloatsB5=upfloatsB6=0; downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;
                                    xStart=originalX; zStart=originalZ; nA7d=messagesPassed[6];
                                    wishCanReturn=NO_REMINDER; avoidC12=true; index=-1;   // restart permutations
                                    visits=0; engineStartTime=System.currentTimeMillis();
                                } 
                                for (j=0; j<availableTeams; j++)    // move elements to the last score bracket
                                        if (tIPassed[j][3] >= lowestGroup) {
                                            i1Abs=tIPassed[j][0];
                                            ID1 = tempIndex[i1Abs]+1;
                                            tIPassed[j][3] = (short)(lowestGroup); 
                                } 
                                if (index==-1) {addedPairs=aP; tILoc = duplicate(tIPassed); continue;}
                        }
                        EngineThread.yield();               // (n.b. this is a java function to synchronize threads, not part of the algorithm)
                        explain=jCheckBox6.isSelected() || verbosity_level>=0;
                        if (explain && !jDialog4.isVisible()) {
                            jTextArea1.setText("");
                            jDialog4.setVisible(true); // show explain window (and action button ...)
                            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
                            EngineThread.yield();
                        }
                        addedPairs=aP; tILoc = duplicate(tIPassed);
                        prevScore=messages[5];
                        if ((messages[0]&REMINDER_FOR_maybeNeededAvoidB2)>0) maybeNeededAvoidB2=true;
                        if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0 && !avoidC12) {
                            avoidB2=true; reminder&=~REMINDER_FOR_C12; gotoDecreasep=true; break;}
                        if (group==lowestGroup-1 && ((p>MPAFC&&(messages[0]&RETURN_CONDITIONAL_DECREASE_P)>0)||visits>2000000)) {
                            visits=0;
                            messages[0]&=~RETURN_CONDITIONAL_DECREASE_P;
                            if ((reminder&REMINDER_FOR_C9)>0) messagesPassed[0]|=RETURN_DECREASE_P;
                            gotoDecreasep=true; reminder&=~REMINDER_FOR_C12; avoidB2=avoidC12=true; break;
                        }
                        if ((messages[0]&RETURN_DECREASE_P)>0) {
                            gotoDecreasep=true; reminder&=~REMINDER_FOR_C12; avoidB2=avoidC12=true; break;}
                        if (strong_verbose && explain) addExpl("unable to pair the homogeneous part of the group:", ""+group, "retry");
                        if ((wishCanReturn&REMINDER_FOR_C9)==0 && (wishCanReturn&REMINDER_FOR_C12)==0) {    // there was no reminder. avoid not useful loops and go to decrease p
                                if (group < lowestGroup) {
                                    gotoDecreasep=true;
                                    if (strong_verbose && explain) addExpl("group=", ""+group, "avoid not useful loops.", "going to decrease p"); 
                                    if ((reminder&REMINDER_FOR_C9)>0) {messagesPassed[0]=messagesPassed[0]|RETURN_DECREASE_P; return false;}
                                    break;
                                }
                        } 
                    }
                } while (nextPermutation(permutation, index, homogeneous, M1, S1));   // C.7 & C.8 rules
                EngineThread.yield();                       // (n.b. this is a java function to synchronize threads, not part of the algorithm)
                if (immediatelyReturn) return false;
                // ended by the user?
                if (!engineRunning) {
                    if (avoidBlocking) { 
                        engineRunning=true; avoidBlocking=false; visits=0; engineStartTime=System.currentTimeMillis();
                        if(avoidC12||group==lowestGroup) {
                            if (explain) {addExpl("","",""); addExpl("user action!","going to decrease p","");}
                            if ((reminder&REMINDER_FOR_C9)>0) {messagesPassed[0]|=RETURN_DECREASE_P|REMINDER_FOR_AVOIDING_C12; return false;}
                            reminder&=~REMINDER_FOR_C12; 
                        } else if (explain) {addExpl("","",""); addExpl("user action!","going to drop C12","");}
                        avoidB2=true; gotoDecreasep=true;
                        if ((reminder&REMINDER_FOR_C9)>0) avoidC12=true;
                    } else return true;  
                }
                // no solution found!!
                if ((reminder&REMINDER_FOR_C9)>0) { 
                    if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C9.", "going back");
                    messagesPassed[5]=prevScore;
                    if (avoidC12) messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12; // pass back the flag
                    if (maybeNeededAvoidB2) messagesPassed[0]|=REMINDER_FOR_maybeNeededAvoidB2;
                    if (gotoDecreasep) messagesPassed[0]|=RETURN_DECREASE_P;
                    return false;       // C9 rule (first sentence)
                }
                if (!gotoDecreasep) {
                    if (!homogeneous && upfloatsB6<maxUpfloatsB6) {   // C.10a drop upfloats B6
                        upfloatsB6++; 
                        if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.a.", "drop upfloats B6"); continue;  
                    }
                    if (!homogeneous && upfloatsB5<maxUpfloatsB5) {   // C.10b drop upfloats B5
                        upfloatsB5++; 
                        upfloatsB6=0;
                        if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.b.", "drop upfloats B5"); continue;   
                    }
                    if (downfloatsB6<maxDownfloatsB6) {       //  C.10c drop downfloats B6
                        downfloatsB6++; 
                        upfloatsB5=upfloatsB6=0;
                        if (homogeneous) {upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;}
                        if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.c.", "drop downfloats B6"); continue;
                    }  
                    if (downfloatsB5<maxDownfloatsB5) {       //  C.10d drop downfloats B5
                        downfloatsB5++; 
                        upfloatsB5=upfloatsB6=0;
                        if (homogeneous) {upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;}
                        floatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-floatsB5*CUTOFF;   // passed or 0
                        if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.d.", "drop downfloats B5"); continue;
                    }  
                    if ((reminder&REMINDER_KEEP_HETEROGENEOUS)>0) return false;
                    if (currRound%2==0) {            
                        if (zStart<xStart) {zStart++; 
                        upfloatsB5=upfloatsB6=0;
                        if (homogeneous) {upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;}
                        downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;   // passed or 0
                        if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                        maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                        if (strong_verbose && explain) addExpl("group=", ""+group, "", "", "z="+zStart); continue;}   
                        zStart=originalZ;  
                    }
                }
                // restore C.3 requirements and continue
                upfloatsB5=upfloatsB6=0;
                if (homogeneous) {upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;}
                downfloatsB5=messagesPassed[3]/CUTOFF; downfloatsB6=messagesPassed[3]-downfloatsB5*CUTOFF;   // passed or 0
                if (!jCheckBox1.isSelected()) { upfloatsB5=upfloatsB6=downfloatsB5=downfloatsB6=99999;
                                                maxUpfloatsB5=maxUpfloatsB6=maxDownfloatsB5=maxDownfloatsB5=0;}
                if (!gotoDecreasep) {
                    if (xStart<p0) {
                        if (allowColourChangeA7d && nA7d<xStart);
                        else {
                            xStart++;                                                // Apply C.10e
                            if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.e.", "increase x and restart S2"); 
                            zStart=originalZ; 
                            nA7d=messagesPassed[6]; 
                            continue;
                        }
                    }
                    if (maybeAllowedColourChangeA7d && currRound%2==1 && !allowColourChangeA7d) {  // Apply C.10f
                        xStart=originalX; if (xStart==0) xStart=1; nA7d=1; allowColourChangeA7d=true;                         
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.f and A7.d", "allow absolute-absolute", "nA7d="+nA7d);
                        continue;
                    }
                    if (maybeAllowedColourChangeA7d && currRound%2==1 && allowColourChangeA7d && nA7d<xStart) {
                        nA7d++;                                               
                        if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C10.f and A7.d", "allow absolute-absolute", "nA7d="+nA7d);
                        continue;
                    }
                }
                gotoDecreasep=allowColourChangeA7d=false; zStart=originalZ; nA7d=messagesPassed[6];
                if (group==lowestGroup) {       // score bracket is the lowest one. 
//                        if (originalS1<originalS2-1) {if (strong_verbose && explain) addExpl("group=", ""+group, "a blocking situation detected in the lowest group.", "more floaters may be requested.");
//                                      messagesPassed[0]=messagesPassed[0]|RETURN_CONDITIONAL_DECREASE_P;} // a blocking situation detected in the lowest group!!
                        if (homogeneous) {
                            if (strong_verbose && explain) addExpl("group=", ""+group, "p cannot be decreased.", "going back");
                            return false; 
                        }
                } 
                if (maybeNeededAvoidB2 && !avoidB2) {if (mild_verbose && explain) addExpl("group:",""+group,"drop B2 rule and retry", ""); // Apply C.10g
                            avoidB2=true; xStart=originalX; continue;}
                if ((reminder&REMINDER_FOR_C12)>0) {    // Apply C.12
                    if (strong_verbose && explain) addExpl("group=", ""+group, "Apply C12.", "going back");
                    return false;       // C12 rule. heterogeneous undo the previous pairings
                }
                if (group<lowestGroup && (wishCanReturn&REMINDER_FOR_C12)>0 && !avoidC12) {avoidC12=true; // 2nd try without C12
                    if (mild_verbose && explain) addExpl("group:",""+group,"going to break C12", "");
                    xStart=originalX; continue;
                }
                if (group<lowestGroup && (wishCanReturn&REMINDER_FOR_C9)>0 && p0>p) {
                    if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "in the remainder group");
                    p0--; 
                } else {
                        if (mild_verbose && explain) addExpl("group=", ""+group, "Apply C14. decrease p", "");
                        if (group==lowestGroup && !homogeneous && nTeams%2==1 && p<S1) {  // Apply C.13
                            if (strong_verbose && explain) addExpl("unable to pair the group:",""+group,"");
                            return false; } 
                        p--;  M1--; if (homogeneous) p0--; else p0=(S2+p)/2;
                        if (p==0) {
                            if (homogeneous) break;
                            else {     // Istanbul 2012 rule C.14.b.2
                                // this may happen if (last) group is like [x] [y z] and x does not pair (goes to take the bye)
                                homogeneous=true; S1=M1=nTeams/2; S2=nTeams-S1; upfloatsB5=upfloatsB6=99999; maxUpfloatsB5=maxUpfloatsB6=0;
                                p0=p=S1; xStart=originalX; if (mild_verbose && explain) addExpl("group=", ""+group, "retry as homogeneous", ""); 
                                if (S1==S2 && p0==S1) {downfloatsB5=downfloatsB6=99999; maxDownfloatsB5=maxDownfloatsB6=0;}
                                continue; 
                            } 
                        }
                        if (homogeneous) {S1=p; S2=nTeams-S1;}
                }
                avoidC12=true;
                if (originalX>0) originalX--; xStart=originalX;
                if (originalZ>0) originalZ--; zStart=originalZ;
                maxUpfloatsB5=originalMaxUpfloatsB5; maxUpfloatsB6=originalMaxUpfloatsB6;
                maxDownfloatsB5=originalMaxDownfloatsB5; maxDownfloatsB6=originalMaxDownfloatsB6;
                if (maxUpfloatsB5>p) maxUpfloatsB5=p; 
                if (maxUpfloatsB6>p) maxUpfloatsB6=p; 
                if (maxDownfloatsB5>nTeams-p0*2) maxDownfloatsB5=nTeams-p0*2;
                if (maxDownfloatsB6>nTeams-p0*2) maxDownfloatsB6=nTeams-p0*2;
                if (homogeneous) {maxUpfloatsB5=maxUpfloatsB6=0; upfloatsB5=upfloatsB6=99999;} // some optimization to accelerate computation
            }
        }
        
        //  comes here when C.14a rule (second sentence)
        if (group>=lowestGroup) return false;       // cannot be paired !!!!!
        addedPairs=aP; tILoc = duplicate(tIPassed);
        str1=""; str2="";
        for (j=0; j<nTeams; j++) {   //  move any residual players down to the next score bracket. C14 rule
                i1=toPermute[j]; i1Abs=tILoc[i1][0]; ID1 = tempIndex[i1Abs]+1;
                if (tILoc[i1][3] == group) {
                    tILoc[toPermute[j]][3]++; 
                    str1="group:"; str2="move to the next one";
                } 
        }
        if (strong_verbose && explain) addExpl(str1,""+group, str2);
        messages=new int [7];
        messages[0]=NORMAL_RETURN; messages[1]=messages[2]=messages[3]=messages[4]=messages[5]=messages[6]=0;
        if ((messagesPassed[0]&RETURN_C13)>0) messages[0]|=REMINDER_FOR_C13; // pass forward the flag
        reminder=NO_REMINDER;
        if (avoidC12) reminder=REMINDER_FOR_AVOIDING_C12;
        if (solveDutch(group+1, tILoc, reminder, messages, immediatelyReturn)) return true;  // finished!! 
        if ((messages[0]&RETURN_C13)>0) messagesPassed[0]|=RETURN_C13; // pass back the flag
        if ((messages[0]&REMINDER_FOR_AVOIDING_C12)>0) messagesPassed[0]|=REMINDER_FOR_AVOIDING_C12; // pass back the flag
        if ((messages[0]&RETURN_DECREASE_P)>0) messagesPassed[0]|=RETURN_DECREASE_P; // pass back the flag
        if (strong_verbose && explain) addExpl("unable to pair the group:",""+(group+1),"");
        addedPairs=aP; return false;
 
        // end of C section !!!!
                
    }
    
    public int calculateSolutionB3(int group, int nTeams, int S1, int p, int p0, boolean homogeneous, short [] permutation, short [] toPermute, short [][] tILoc) {
        int j, i1, i2, i1Abs, i2Abs, ID1, ID2, coD1, coD2, cD1, cD2, score1, score2, weight=0, goodPairs=0, nBye=0;
        boolean fcB5, fcB6, B2;
        for (j=0;j<p;j++) {              // C6 rule
                        i1=permutation[j];
                        i2=permutation[j+S1];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tILoc[i1][0];
                        i2Abs=tILoc[i2][0];
                        ID1 = tempIndex[i1Abs]+1;
                        ID2 = tempIndex[i2Abs]+1;
                        if (wasPlayed(ID1, ID2)) return Integer.MAX_VALUE;              // check B1 rule
                        coD1=cD1=tILoc[i1][1]; coD2=cD2=tILoc[i2][1];
                        if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                        if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                        if (cD1==0 || cD2==0) {                 // one of the two colours is 0
                        } else if ((coD1+coD2)>40 && (coD1+coD2)<50 && coD1==coD2) {   // same colour due W-W or B-B not allowed?
                            B2=true;
                            if (jCheckBox1.isSelected()) {
                                score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                                if (score1 < score2) score1=score2;     // take the highest of the two
                                if (lastRound && score1>score50) {B2=false;}
                            } else B2=false;
                            if (B2) return Integer.MAX_VALUE; else weight+=40000;   // B2 rule
                        } else if ((coD1+coD2)>50 && cD1==cD2) {        // same colour due W?-W or B?-B
                            weight+=10000;
                        } else if ((cD1+cD2)>20 && cD1==cD2) {          // same colour due w-w or b-b
                            weight+=2000; 
                        } else if (cD1%2==cD2%2) {          // same colour due but can change
                            weight+=1500;                   // (two are worse than one strong)
                        } 
                        score1=teamScores[ID1-1][0]; score2=teamScores[ID2-1][0];
                        if (score1>score2) {
                            if (!allowedUpfloaterB5(ID2)) { // check can upfloat
                                    if (!homogeneous) weight+=10;
                            }
                            if (!allowedUpfloaterB6(ID2)) { // check can upfloat
                                    if (!homogeneous) weight+=1;
                            }
                            fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                            if (tILoc[i1][2]<tILoc[i2][2]) {fcB5=false; fcB6=true;} // already floated; set B6
                            if (fcB5) weight+=100; // check can downfloat. 
                            if (fcB6) weight+=50; 
                        }
                        else if (score1<score2) {
                            if (!allowedUpfloaterB5(ID1)) { // check can upfloat
                                    if (!homogeneous) weight+=10;
                            }
                            if (!allowedUpfloaterB6(ID1)) { // check can upfloat
                                    if (!homogeneous) weight+=1;
                            }
                            fcB5=!allowedDownfloaterB5(ID2); fcB6=!allowedDownfloaterB6(ID2);
                            if (tILoc[i2][2]<tILoc[i1][2]) {fcB5=false; fcB6=true;} // already floated; set B6
                            if (fcB5) weight+=100; // check can downfloat. 
                            if (fcB6) weight+=50; 
                        }
                        weight+=(score1-score2)*(score1-score2)*100000;
                        goodPairs++;
        }
        int index=p0-p;  // calculate the new S1 of the reminder group (homogeneous by definition), if any
        if (p<p0) {
            for (j=S1+p;j<S1+p0;j++) {
                        i1=permutation[j];
                        i2=permutation[j+index];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tILoc[i1][0];
                        i2Abs=tILoc[i2][0];
                        ID1 = tempIndex[i1Abs]+1;
                        ID2 = tempIndex[i2Abs]+1;
                        if (wasPlayed(ID1, ID2)) return Integer.MAX_VALUE;              // check B1 rule
                        coD1=cD1=tILoc[i1][1]; coD2=cD2=tILoc[i2][1];
                        if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                        if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                        if (cD1==0 || cD2==0) {                 // one of the two colours is 0
                        } else if ((coD1+coD2)>40 && (coD1+coD2)<50 && coD1==coD2) {   // same colour due W-W or B-B not allowed!
                            B2=true;
                            if (jCheckBox1.isSelected()) {
                                score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                                if (score1 < score2) score1=score2;     // take the highest of the two
                                if (lastRound && score1>score50) {B2=false;}
                            } else B2=false;
                            if (B2) return Integer.MAX_VALUE; else weight+=40000;   // B2 rule
                        } else if ((coD1+coD2)>50 && cD1==cD2) {        // same colour due W?-W or B?-B
                            weight+=10000;
                        } else if ((cD1+cD2)>20 && cD1==cD2) {          // same colour due w-w or b-b
                            weight+=2000; 
                        } else if (cD1%2==cD2%2) {          // same colour due but can change
                            weight+=1500; 
                        } 
                        score1=teamScores[ID1-1][0]; score2=teamScores[ID2-1][0];
                        weight+=(score1-score2)*(score1-score2)*100000;
                        goodPairs++;
            }
        }
        if (p0*2<nTeams) {      // there are residual elements to be floatted
//            i2=toPermute[nTeams-1];
//            i2Abs=tILoc[i2][0];
//            ID2 = tempIndex[i2Abs]+1;
//            score2=teamScores[ID2-1][0];
            score2=0;
            for (j=p; j<S1;j++) {       // left in the S1 heterogeneous side
                i1=permutation[j];
                i1=toPermute[i1];
                i1Abs=tILoc[i1][0];
                ID1 = tempIndex[i1Abs]+1;
                fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                if (tILoc[i1][2]<tILoc[i1][3]) {fcB5=false; fcB6=true;} // already floated; set B6
                if (fcB5) weight+=100; // check can downfloat. 
                if (fcB6) weight+=50; 
                score1=teamScores[ID1-1][0]; 
                weight+=(score1-score2)*(score1-score2)*100000;
                if (group==lowestGroup && nTeams%2==1) 
                    if (canTakeTheBye(i1Abs)) nBye++;
            }
            for (j=S1+p0+index; j<nTeams; j++) {    // left at the end
                i1=permutation[j];
                i1=toPermute[i1];
                i1Abs=tILoc[i1][0];
                ID1 = tempIndex[i1Abs]+1;
                fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                if (tILoc[i1][2]<tILoc[i1][3]) {fcB5=false; fcB6=true;} // already floated; set B6
                if (fcB5) weight+=100; // check can downfloat. 
                if (fcB6) weight+=50; 
                score1=teamScores[ID1-1][0]; 
                weight+=(score1-score2)*(score1-score2)*100000;   
                if (group==lowestGroup && nTeams%2==1) 
                    if (canTakeTheBye(i1Abs)) nBye++;
            }
            if (group==lowestGroup && nBye!=1) return Integer.MAX_VALUE;    // none or more than one can take the BYE
        }
        return weight;
    }
    
    public boolean QuicklyCheckMayBePaired(int group, int nTeams, int M1, int S1, int p, int p0, boolean homogeneous, int reminder, short [] permutation, short [] toPermute, short [][] tILoc) {
        int j, i1, i2, i1Abs, i2Abs, ID1, ID2, coD1, coD2, cD1, cD2, score1, score2, goodPairs=0, index=0;
        boolean B2;
        nextPermutation(permutation, -1, homogeneous, M1, S1);  // populate array
        do {
            goodPairs=0;
            for (j=0;j<p;j++) {              // C6 rule
                        i1=permutation[j];
                        index=j+S1; if (index>=nTeams) break;
                        i2=permutation[index];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tILoc[i1][0];
                        i2Abs=tILoc[i2][0];
                        ID1 = tempIndex[i1Abs]+1;
                        ID2 = tempIndex[i2Abs]+1;
                        if (wasPlayed(ID1, ID2)) break;              // check B1 rule
                        coD1=cD1=tILoc[i1][1]; coD2=cD2=tILoc[i2][1];
                        if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                        if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                        if (cD1==0 || cD2==0) {                 // one of the two colours is 0
                        } else if ((coD1+coD2)>40 && (coD1+coD2)<50 && coD1==coD2) {   // same colour due W-W or B-B not allowed?
                            B2=true;
                            if (jCheckBox1.isSelected()) {
                                score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                                if (score1 < score2) score1=score2;     // take the highest of the two
                                if (lastRound && score1>score50) {B2=false;}
                            } else B2=false;
                            if (B2) break;   // B2 rule
                        } 
                        goodPairs++;
            }
            if (goodPairs!=p && (group<lowestGroup || (reminder&REMINDER_FOR_C12)==0)) continue;
            int indexS1=p0-p;  // calculate the new S1 of the reminder group (homogeneous by definition), if any
            if (p<p0) {
                for (j=S1+p;j<S1+p0;j++) {
                        i1=permutation[j];
                        index=j+indexS1; if (index>=nTeams) break;
                        i2=permutation[index];
                        i1=toPermute[i1];
                        i2=toPermute[i2];
                        i1Abs=tILoc[i1][0];
                        i2Abs=tILoc[i2][0];
                        ID1 = tempIndex[i1Abs]+1;
                        ID2 = tempIndex[i2Abs]+1;
                        if (wasPlayed(ID1, ID2)) break;              // check B1 rule
                        coD1=cD1=tILoc[i1][1]; coD2=cD2=tILoc[i2][1];
                        if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                        if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                        if (cD1==0 || cD2==0) {                 // one of the two colours is 0
                        } else if ((coD1+coD2)>40 && (coD1+coD2)<50 && coD1==coD2) {   // same colour due W-W or B-B not allowed!
                            B2=true;
                            if (jCheckBox1.isSelected()) {
                                score1 = teamScores[ID1-1][0]; score2 = teamScores[ID2-1][0];
                                if (score1 < score2) score1=score2;     // take the highest of the two
                                if (lastRound && score1>score50) {B2=false;}
                            } else B2=false;
                            if (B2) break;   // B2 rule
                        } 
                        goodPairs++;
                }
            }
            if (goodPairs==nTeams/2) { 
                if (group==lowestGroup && nTeams%2==1) {
                    i1=permutation[nTeams-1];
                    i1=toPermute[i1];
                    i1Abs=tILoc[i1][0];
                    ID1 = tempIndex[i1Abs]+1;
                    if (!canTakeTheBye(i1Abs)) continue;
                }
                return true;
            }
        } while (nextPermutation(permutation, index, homogeneous, M1, S1));
        return false;
    }
    
    //--------------------------------------------------------
    // Systematically generate permutations. Adapted by the work of Michael Gilleland http://www.merriampark.com/mgresume.htm
    // Generate next permutation (algorithm from Rosen p. 284)
    //--------------------------------------------------------

    public boolean nextPermutation(short[] a, int index, boolean homogeneous, int M1, int S1) {

        short temp; int j=0, k, r, s;
        if (index==-1) {    // flag to reset the permutation array
            for (k = 0; k < a.length; k++) a[k] = (short)k;  // populate permutation array
            return true;
        }

        if (index>=a.length-2) {
            index=a.length-2;
            // Find largest index j with a[j] < a[j+1] 
            try {
                j = index;
                while (a[j] > a[j+1]) j--;
            } catch (ArrayIndexOutOfBoundsException ex) {return false;}  // permutations ended!!
              catch (IndexOutOfBoundsException ex) {return false;}
        
            if (j<S1 && homogeneous) return exchangeS1S2(a, S1);    // for heterogeneous score bracket no exchanges!
            else if (j<S1 && !homogeneous) {
                if (M1==S1) return false;
                return nextCombinationS1(a, M1, S1);  // for heterogeneous score bracket find next combination C(S1, M1)
            }     

            // Find index k such that a[k] is smallest integer
            // greater than a[j] to the right of a[j] 
            k = a.length - 1;
            while (a[j] > a[k]) k--;
            
            // Interchange a[j] and a[k]
            temp = a[k]; a[k] = a[j]; a[j] = temp;
            
            // Put tail end of permutation after jth position in increasing order
            r = a.length - 1; s = j + 1;
            while (r > s) {
                temp = a[s]; a[s] = a[r]; a[r] = temp;
                r--; s++;
            }
        } else {
            // this part is added by E.C. to accelerate permutations cutting the ones right to j
            j=index;
            // Put tail end of permutation after jth position in decreasing order
                for (s=j+1; s<a.length - 1; s++) 
                    for (r=s+1; r<a.length; r++)
                        if (a[s] < a[r]) {
                            temp = a[s]; a[s] = a[r]; a[r] = temp;
                        }

            // Find index k such that a[k] is smallest integer
            // greater than a[j] to the right of a[j] 
            k = a.length - 1;
            while (a[j] > a[k]) k--;
        
            if (j==k) {     // try to the left 
                while (a[j] > a[j+1]) j--;
                
                if (j<S1 && homogeneous) return exchangeS1S2(a, S1);    // for heterogeneous score bracket no exchanges!
                else if (j<S1 && !homogeneous) {
                    if (M1==S1) return false;
                    return nextCombinationS1(a, M1, S1);  // for heterogeneous score bracket find next combination C(S1, M1)
                }     

                k = a.length - 1;
                while (a[j] > a[k]) k--;
            }
        
            // Interchange a[j] and a[k]
            temp = a[k]; a[k] = a[j]; a[j] = temp;

            // Put tail end of permutation after jth position in increasing order
                for (s=j+1; s<a.length - 1; s++) 
                    for (r=s+1; r<a.length; r++)
                        if (a[s] > a[r]) {
                            temp = a[s]; a[s] = a[r]; a[r] = temp;
                        }
        }
        return true;
    }
   
    public boolean nextCombinationS1(short comb[], int k, int n) {
        // set next combination C(n, k)
	    int i = k - 1;
	    ++comb[i];
	    while ((i > 0) && (i<n) && (comb[i] >= n - k + 1 + i)) { --i; ++comb[i]; }
	    if (comb[0] > n - k) return false;  /* Combination (n-k, n-k+1, ..., n) reached. no more */
	    /* comb now looks like (..., x, n, n, n, ..., n). Turn it into (..., x, x + 1, x + 2, ...) */
	    for (i = i + 1; i < k; ++i) comb[i] = (short)(comb[i - 1] + 1);
            int missing=-1, pos=k-1;             // complete S1 with missing values
            for (i=0;;) {
                if (missing+1<comb[i]) { pos++; missing++; comb[pos]=(short)missing; continue; }
                missing=comb[i]; i++; if (i==k) break;  // finished !!
            }
            // Put S2 in increasing order
            for (i=n; i<comb.length; i++) comb[i] = (short)i;
	    return true;
    }

    public boolean exchangeS1S2(short[] a, int S1) {
        short temp; int i, j, k, r, s, n, diff, sumA, sumB, sumA1, sumB2, sum1, sum2, S2, elS1, elS2, newelS1, newelS2, decr;
        boolean found;
        String [] R1, R2;
        // C.8 rule
        if (a.length <= 2) return false;    // both S1 and S2 with one element. no exchanges
        
        // Put S1 in increasing order
        for (s=0; s<S1 - 1; s++) 
            for (r=s+1; r<S1; r++)
                if (a[s] > a[r]) {
                    temp = a[s]; a[s] = a[r]; a[r] = temp;
                }
        // Put S2 in increasing order
        for (s=S1; s<a.length - 1; s++) 
            for (r=s+1; r<a.length; r++)
                if (a[s] > a[r]) {
                    temp = a[s]; a[s] = a[r]; a[r] = temp;
                }
        // find how many already exchanged  
        n=0;
        for (k=0; k<S1; k++) 
            if (a[k]>=S1) n++; 
        if (S1==1 || n==0) {    // if S1=1 or first time "normal" exchanges are good
            j=S1-1;
            // find the lowest in S2 over a[j]
            r=0;
            for (k=S1; k<a.length; k++) 
                if (a[k]>a[j]) {r=k; break;}
            if (r==0) return false;         // exchanges ended
            if (j==0 && r==a.length-1) return false;         // exchanges ended
            // exchange a[j] and a[r]
            temp=a[j]; a[j]=a[r]; a[r]=temp;
        } else {                            // the most complex situation!!
            if (n==1) {
                // find the exchanged one
                r=S1;
                j=S1-1;
                elS2=a[j]; elS1=a[r]; 
                diff=elS2-elS1;
                for (k = 0; k < a.length; k++) a[k] = (short)k;  // populate permutation array
                decr=0; //if (a.length%2==0) decr=1;
                if (elS1-1>=decr && elS2-1>=S1) {newelS1=elS1-1; newelS2=elS2-1;}  // same diff
                else {
                    diff++;
                    newelS1=S1-1; newelS2=newelS1+diff;
                    while (newelS2>a.length-1-decr) {newelS1--; newelS2--;}
                }
                if (newelS1<decr) {  // single exchanges ended! exchange two
                    if (S1>1) {
                        temp=a[S1-2]; a[S1-2]=a[S1]; a[S1]=temp;
                        temp=a[S1-1]; a[S1-1]=a[S1+1]; a[S1+1]=temp;
                    } else return false;    // exchanges ended
                } else {
                    temp=a[newelS1]; a[newelS1]=a[newelS2]; a[newelS2]=temp;   // do exchanges
                }
            } else { 
                // continue trying to exchange the same amount of elements
                short [] exchA=new short[n]; short [] exchB=new short[n];  // stores the last exchanged ones
                n=sumA=sumB=0;
                for (k=0; k<S1; k++) 
                    if (a[k]>=S1) {exchB[n++]=a[k]; sumB+=a[k];}
                n=0;
                for (k=S1; k<a.length; k++) 
                    if (a[k]<S1) {exchA[n++]=a[k]; sumA+=a[k];} 
                S2=a.length-S1; diff=sumB-sumA;
                // generate combinations C(S1,n) downward
                short [] b = new short[n];
                for (k=0; k<n; k++) b[k]=(short)k;
                int cs1=S1;
                for (k=1; k<n; k++) cs1*=(S1-k);
                for (k=2; k<=n; k++) cs1/=k;
                String [] CS1=new String[cs1];  // array to store all combinations within S1
                cs1=0;
                CS1[cs1]="-"; for (k=n-1; k>=0; k--) CS1[cs1]+=(S1-b[k]-1)+"-"; cs1++; 
                for (;;) {
                    i = n - 1;
                    try {
                        while (b[i] == S1 - n + i) {i--; if (i<0) break;}
                        if (i<0) break;
                        b[i]++;
                        for (j = i + 1; j < n; j++) b[j] = (short)(b[i] + j - i);
                        CS1[cs1]="-"; for (k=n-1; k>=0; k--) CS1[cs1]+=(S1-b[k]-1)+"-"; cs1++; 
                    } catch (ArrayIndexOutOfBoundsException ex) {break;}
                      catch (IndexOutOfBoundsException ex) {break;}
                }
                // generate combinations C(S2,n) upward
                short [] c = new short[n];
                for (k=0; k<n; k++) c[k]=(short)k;
                int cs2=S2;
                for (k=1; k<n; k++) cs2*=(S2-k);
                for (k=2; k<=n; k++) cs2/=k;
                String [] CS2=new String[cs2];  // array to store all combinations within S2
                cs2=0;
                CS2[cs2]="-"; for (k=0; k<n; k++) CS2[cs2]+=(S1+c[k])+"-"; cs2++;
                for (;;) {
                    i = n - 1;
                    try {
                        while (c[i] == S2 - n + i) {i--; if (i<0) break;}
                        if (i<0) break;
                        c[i]++;
                        for (j = i + 1; j < n; j++) c[j] = (short)(c[i] + j - i); 
                        CS2[cs2]="-"; for (k=0; k<n; k++) CS2[cs2]+=(S1+c[k])+"-"; cs2++;
                    } catch (ArrayIndexOutOfBoundsException ex) {break;}
                      catch (IndexOutOfBoundsException ex) {break;}
                }
                // find coordinates in the virtual matrix
                String A="-"; for (k=0; k<n; k++) A+=exchA[k]+"-";
                String B="-"; for (k=0; k<n; k++) B+=exchB[k]+"-";
                i=0; for (i=0; i<cs1; i++) if (CS1[i].equals(A)) break;
                j=0; for (j=0; j<cs2; j++) if (CS2[j].equals(B)) break;
                found=false; j++;
                for (;j<cs2;j++) {              // find the following combination with the same sum in S2
                    R2=CS2[j].split("-"); sum2=0;
                    for (k=1; k<=n; k++) sum2+=Integer.valueOf(R2[k]); 
                    if (sum2!=sumB) continue;
                    found=true; break;
                }
                if (!found) {
                    i++;
                    for (;i<cs1;i++) {              // find the following combination with the same sum in S1
                        R1=CS1[i].split("-"); sum1=0;
                        for (k=1; k<=n; k++) sum1+=Integer.valueOf(R1[k]); 
                        if (sum1!=sumA) continue;
                        for (j=0;j<cs2;j++) {              // find first combination with the same sum in S2
                            R2=CS2[j].split("-"); sum2=0;
                            for (k=1; k<=n; k++) sum2+=Integer.valueOf(R2[k]); 
                            if (sum2!=sumB) continue;
                            found=true; break;
                        }
                        break;
                    }
                }
                if (!found) {
                    for (i=0;i<cs1;i++) {              // find the first combination with decreased sum in S1 and decreased S2
                        R1=CS1[i].split("-"); sum1=0;
                        for (k=1; k<=n; k++) sum1+=Integer.valueOf(R1[k]); 
                        if (sum1!=sumA-1) continue;
                        for (j=0;j<cs2;j++) {              // find first combination with the same sum in S2
                            R2=CS2[j].split("-"); sum2=0;
                            for (k=1; k<=n; k++) sum2+=Integer.valueOf(R2[k]); 
                            if (sum2!=sumB-1) continue;
                            found=true; break;
                        }
                        break;
                    }
                }
                if (!found) {
                    diff++; i=0;                    // increase difference and
                    R1=CS1[i].split("-"); sumA1=0;  // find first combination with the highest sum in S1
                    for (k=1; k<=n; k++) sumA1+=Integer.valueOf(R1[k]); 
                    for(;;) {
                      for (;i<cs1;i++) {
                        R1=CS1[i].split("-"); sum1=0;
                        for (k=1; k<=n; k++) sum1+=Integer.valueOf(R1[k]); 
                        if (sum1!=sumA1) continue;
                        sumB2=diff+sumA1;
                        for (j=0;j<cs2;j++) {              // find first combination with the same sum in S2
                            R2=CS2[j].split("-"); sum2=0;
                            for (k=1; k<=n; k++) sum2+=Integer.valueOf(R2[k]); 
                            if (sum2!=sumB2) continue;
                            found=true; break;
                        }
                        if (found) break;
                      }
                      if (found) break;
                      sumA1--; if (sumA1<1) break;
                    }
                }
                if (found) {
                    for (k = 0; k < a.length; k++) a[k] = (short)k;  // populate permutation array
                    R1=CS1[i].split("-");
                    R2=CS2[j].split("-");
                    for (k=1; k<=n; k++) {
                        newelS1=Integer.valueOf(R2[k]);
                        newelS2=Integer.valueOf(R1[k]);
                        a[newelS1]=(short)newelS2; a[newelS2]=(short)newelS1;  // do exchanges 
                    }
                } else {
                    if (n==S1) return false;                        // exchanges ended?
                    // exchange one more item
                    n++;
                    for (k = 0; k < a.length; k++) a[k] = (short)k;  // populate permutation array
                    for (k=0; k<n; k++) {
                        a[S1-n+k]=(short)(k+S1); a[k+S1]=(short)(S1-n+k);  // do exchanges
                    }
                }
            }
        }
        // Put S1 in increasing order
        for (s=0; s<S1 - 1; s++) 
            for (r=s+1; r<S1; r++)
                if (a[s] > a[r]) {
                    temp = a[s]; a[s] = a[r]; a[r] = temp;
                }
        // Put S2 in increasing order
        for (s=S1; s<a.length - 1; s++) 
            for (r=s+1; r<a.length; r++)
                if (a[s] > a[r]) {
                    temp = a[s]; a[s] = a[r]; a[r] = temp;
                }
        return true;
    }

    private boolean canTakeTheBye(int i1) {
        int ID1,r,j;
        String [] S;
        int winsOrDrawBye = 0;           // how many bye
        ID1 = tempIndex[i1]+1;
        for (r=0;r<maxRound-1;r++) {  // loop played rounds
                    for (j=0;j<(addedRows+1)/2;j++) {   // loop pairs
                        S = roundsDetail[j][0][r].split("-");
                        if (S.length!=4) continue;     // some kind of unexpected error?
                        if (S[0].equals(""+ID1) && S[3].indexOf('f')>0 && integerScore(S[2])>0) winsOrDrawBye++;
                        if (S[1].equals(""+ID1) && S[3].indexOf('f')>0 && integerScore(S[3])>0) winsOrDrawBye++;
                    }
        }
        if (winsOrDrawBye>=tournamentPairing || isRetired(i1)) return false;
        else return true;  
    }

    private short[][] duplicate(short[][] passed) {
        short[][] ret= new short[availableTeams][4];
        int i,j;
        for (i=0; i<availableTeams; i++)
            for (j=0; j<4; j++) ret[i][j]=passed[i][j];
        return ret;
    }

    private void normal_explain(int group, int S1, boolean homogeneous, boolean B3, int x, boolean maybeAllowedColourChange, int z, short from, short to, short[][] tILoc) {
        int k, j, i1Abs, ID1, ID2, cD, cD1, cD2, prevGroup=0, weight=0, score1, score2, coD1, coD2, group1, group2, nA7d;
        String [] S; String flagUp, flagDown, line="", colour, space;
        boolean fcB5, fcB6;
        boolean explain=jCheckBox6.isSelected() || verbosity_level>=0;
        char upArrow=0x2191, downArrow=0x2193;    // UTF-16 encoding of arrows
        float fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;;
        if (!explain) return;
        if (from==to) return;       // empty group. don't show
        if (!jDialog4.isVisible()) {
            jTextArea1.setText("");
            jDialog4.setVisible(true); // show explain window (and action button ...)
            addExpl("pairs to be generated for round", currRound, "swiss Dutch FIDE algorithm");
            EngineThread.yield();
        }
        if (group>lowestGroup) {
                    S= roundsDetail[from][0][currRound-1].split("-");
                    ID1=Integer.valueOf(S[0]);
                    score1=0;
                    for (j=0; j<availableTeams; j++) {
                                    i1Abs=tILoc[j][0];
                                    if (ID1 == tempIndex[i1Abs]+1) score1=teamScores[ID1-1][0]; 
                     }
                     floatWeight+=score1*score1; 
        } else {
            line=Main.localizedText("group=")+group;
            if (B3) line+=" B.3 (min sq diff)";
            x=z=nA7d=0; weight+=floatWeight; floatWeight=0;
            for (k=from; k<to; k++) {             // recalculate parameters x and z
                    S= roundsDetail[k][0][currRound-1].split("-");
                    ID1=Integer.valueOf(S[0]);
                    ID2=Integer.valueOf(S[1]);
                    coD1=coD2=cD1=cD2=score1=score2=0; group1=group2=99999;
                    for (j=0; j<availableTeams; j++) {
                                    i1Abs=tILoc[j][0];
                                    if (ID1 == tempIndex[i1Abs]+1) { group1=tILoc[j][2]; coD1=cD1=tILoc[j][1]; score1=teamScores[ID1-1][0]; }
                                    if (ID2 == tempIndex[i1Abs]+1) { group2=tILoc[j][2]; coD2=cD2=tILoc[j][1]; score2=teamScores[ID2-1][0]; }
                     }
                     weight+=(score1-score2)*(score1-score2); 
                     if (group1<group2) floatWeight+=score1*score1; else if (group1>group2) floatWeight+=score2*score2;
                     if (cD1>30) cD1-=10; if (cD2>30) cD2-=10;
                     if (cD1>20) cD1-=10; if (cD2>20) cD2-=10;
                     if (cD1==0 || cD2==0) {                 // one of the two colours is 0
                     } else if ((coD1+coD2)>50 && cD1==cD2) {        // same colour due W?-W or B?-B
                                x++; z++; nA7d++;
                     } else if ((cD1+cD2)>20 && cD1==cD2) {          // same colour due w-w or b-b
                                x++; z++; 
                     } else if (cD1%2==cD2%2) {          // same colour due but can change
                                x++; 
                     }
            }
            if (z>0) maybeAllowedColourChange=true;
            if (B3) line+=" "+Main.localizedText("weight")+" "+weight*fMolt*fMolt;
            if (homogeneous) line+=" "+Main.localizedText("homogeneous"); else line+=" "+Main.localizedText("heterogeneous");
            line+=" "+"x="+x; if (maybeAllowedColourChange) if (currRound%2==0) line+=" "+"z="+z; else if (nA7d>0) line+=" "+"nA7d="+nA7d; 
            line+="\n["; space="";
            for (j=0; j<availableTeams; j++) {
             if (tILoc[j][3]==group) {
                if (B3 && prevGroup>0 && tILoc[j][2]!=prevGroup) {line+="]"; space="[";}
                i1Abs=tILoc[j][0];
                ID1 = tempIndex[i1Abs]+1;
                cD=tILoc[j][1];
                colour=flagUp=flagDown=""; 
                if (allowedUpfloaterB5(ID1)) flagUp+="-"; else flagUp+=upArrow;        // cannot upfloat (B5)
                if (allowedUpfloaterB6(ID1)) flagUp+="-"; else flagUp+=upArrow;        // cannot upfloat (B6)
                fcB5=!allowedDownfloaterB5(ID1); fcB6=!allowedDownfloaterB6(ID1);
                if (tILoc[j][2]<group) {fcB5=false; fcB6=true;} // already floated; set B6
                if (!fcB5) flagDown+="-"; else flagDown+=downArrow;      // cannot downfloat (B5)
                if (!fcB6) flagDown+="-"; else flagDown+=downArrow;      // cannot downfloat (B6)
                if (flagUp.equals("--")) flagUp="";
                if (flagDown.equals("--")) flagDown="";
                if (cD==0) colour+=".0";
                else if (cD==1) colour+="w?";   // mild can change
                else if (cD==2) colour+="b?";
                else if (cD==11) colour+="w";   // strong
                else if (cD==12) colour+="b";
                else if (cD==21) colour+="W";   // absolute
                else if (cD==22) colour+="B";
                else if (cD==31) colour+="W?";  // absolute can change
                else if (cD==32) colour+="B?";
                line+=space+ID1+colour+flagUp+flagDown;
                space=" ";
                S1--; if (S1==0 && !B3) {line+="]"; space="[";}
                prevGroup=tILoc[j][2];
              }
            } if (!line.endsWith("]"))line+="]";
            line+="\n";
        }
        for (k=from; k<to; k++) {             // explain paired elements
                        S= roundsDetail[k][0][currRound-1].split("-");
                        ID1=Integer.valueOf(S[0]);
                        ID2=Integer.valueOf(S[1]);
                        if (ID2>0) line+=Main.localizedText("paired ID=")+ID1+" "+Main.localizedText("and ID=")+ID2+"\n";
                        else line+="ID="+ID1+" "+Main.localizedText("takes the BYE");
         }
         globalExplainText=line+"\n"+globalExplainText;
        
    }
    
    private void swissSimple(boolean explain) {
        // 'Swiss Simple (based on Rating)'. Engine implementation
            /* rules: 
                - White and Black colours tend to be equal and alternated between games. Three colour sequence 
                    or delta colour grater than two is allowed only at the last round. In case of exactly equal 
                    colour history, the lower ID alternates the last played colour
                - Forfeit and BYE games are considered played without opponent and colour
                - if odd players, last ranked, but only one time in the tournament, gets the BYE (*)
                - Starting from the top of the ranking, a player is tried to be paired within the same
                    score group. If odd, a player of the dominating colour is floated to the next score group,
                    provided that it may have a valid opponent (weak constrains are: the opponent 1. cannot be 
                    upfloated two times running and not as two rounds before, 2. has the opposite colour due). 
                    Finally 1.st half is paired vs 2.nd half making permutations of the 2.nd half if needed because 
                    of colour criteria, trying to make as many regular color-due pairs as possible first (*), 
                    or match already played.
                    If impossible to pair, all elements of the score group are allowed to be mixed.
                    If impossible to pair, the group is first grown with two more floaters; if gone to the end,
                    the last pair is broken and the procedure is repeated including all left unpaired.
                n.b. (*) = this simplifies the swiss Dutch algorithm
            */
        int i, j, k, nw, nb, reserve;
        int i1, i2, compatible, availableTeams, score, prevScore, deltaScore, ID, cD, bC,ID2, score2;
        int [] sup, inf;
        boolean takeAll, ex, forceUpfloater;
        String [] S;
        String flag;
        float fMolt;
        if (currRound==Integer.valueOf(rounds)+1) { // are you going over last round ?? ...
                if (tournamentPairing==1) {         // ... if a regular tournament this is not of course allowed. notify it
                    Object[] options = {Main.localizedText("OK")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                    return;
                }
        }
        availableTeams = countAvailableTeams();         // obtain dinamically the number of teams available to play
        if (explain) {
            addExpl("pairs to be generated for round", currRound, "Swiss Simple (based on Rating) algorithm");
            addExpl("available",availableTeams,"to be paired");
            addExpl("","","");
        }
        // if odd, set BYE to last ranked not taken before 
        if ((availableTeams % 2) == 1 ) {
            int lnb=lastNotBYE(false);
            roundsDetail[(availableTeams+1)/2-1][0][currRound-1]=""+lnb+"-0-0-0";
            if (explain) addExpl("odd -> BYE set to ID",lnb,"");
        }
        tI = new int [availableTeams][2];   // pointer to IDs
        if (maxRound==1) firstRoundSwiss(explain);    // first round!
        else { 
            if (explain) {
                prevScore=9999; i=0;
                for (i1=0; i1<addedRows; i1++) 
                    if (!isAlreadyPaired(i1)) {
                        ID=tempIndex[i1]+1;
                        cD=colourDue(i1);
                        fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                        score=teamScores[ID-1][0];
                        if (score<prevScore) {
                            addExpl("","","");
                            addExpl("score group", ++i, "score", score*fMolt, "rank", i1+1);
                            prevScore=score;
                        }
                        flag="";
                        if(!canChangeColourDue(i1, cD)) flag+="C";   // flag cannot change colour due
                        if (!allowedUpfloater(ID)) flag+="U";       // flag cannot upfloat
                        addExpl("ID", ID, "colour due", colourPreference[10+cD], "flag:", flag);
                    }
                addExpl("","","");
            }
            nTeams=0;       // this will store the size of the score group to be paired
            takeAll=false;  // this will be set true when gone to the end and some pair is broken, so all remaining unpaired are taken
            for (;;) {        // the main loop. try to pair top-down. 
                if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                // 1.st look at the starting team of higher score group not yet paired
                if (addedPairs==availableTeams/2) break;  // finished !!
                i1 = 0; 
                i=0; nw=0; nb=0; reserve=-1; forceUpfloater=false;
                for (;;) {
                    if (isAlreadyPaired(i1)) i1++;
                    else break;
                    if (i1==addedRows) break;
                }
                if (i1==addedRows) break;   // finished !!
                ID=tempIndex[i1]+1;
                cD=colourDue(i1);
                tI[i][0]=i1; tI[i++][1]=cD;       // store the element 
                if (cD==1) nw++; else nb++;       // count colour due
                if (explain) {
                    fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                    score=teamScores[ID-1][0];
                    addExpl("member of this group ID", ID, "colour due", colourPreference[10+cD],"","");
                }
                // 2.nd look at the team ending this score group
                i2 = i1+1;
                for (;;) {
                    if (i2>=addedRows) {    // at the end of candidates
                        if (!takeAll && reserve>0) {
                            i2=reserve;     // if a reserve left, 1.st try to get it back
                            reserve=-1;
                            forceUpfloater=true;
                        }
                        else break;
                    }
                    if (!isAlreadyPaired(i2)) {
                        ID=tempIndex[i2]+1;
                        if (teamScores[tempIndex[i1]][0]==teamScores[tempIndex[i2]][0] || takeAll) {
                            cD=colourDue(i2);
                            tI[i][0]=i2; tI[i++][1]=cD;       // store the element 
                            if (cD==1) nw++; else nb++;       // count colour due
                            if (explain) {
                                fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                                score=teamScores[ID-1][0];
                                addExpl("member of this group ID", ID, "colour due", colourPreference[10+cD], "", "");
                            }
                        } else {
                            if (i>=nTeams && i%2==0) break;      // done! more than requested and even
                            if (reserve>=0 && 
                                teamScores[tempIndex[reserve]][0]>teamScores[ID-1][0]) {
                                // ... but a reserve, upfloater, was left unpaired in the middle of a group trying to span more than one score group! 
                                i2=reserve;  // get back the reserve ...
                                reserve=-1;
                                forceUpfloater=true;    // ... and force upfloater to enter next if for sure
                                ID=tempIndex[i2]+1;
                                if (explain) addExpl("get back the reserve ID", ID, "allow to upfloat");
                            }
                            ex=false;   // ... but check may have at least one opponent in this group!
                            for (j=0; j<i; j++) { 
                                if (establishCompatibility(tI[j][0],i2,optimizationRequested,"")>0) { ex=true; break; }  
                            }
                            if (ex) {
                                bC=balanceColours(tI, i);    // need to select a balancing colour due one ...
                                cD=colourDue(i2);
                                if (forceUpfloater || (allowedUpfloater(ID) && (bC==0 || cD==bC))) {
                                    ex=false;       // before to add element, check not added yet ...
                                    for (j=0; j<i; j++) {   // ... because may come here two times
                                        if (tI[j][0]==i2) { ex=true; break; }  
                                    }
                                    if (!ex) {
                                        tI[i][0]=i2; tI[i++][1]=cD;       // store the element 
                                        if (cD==1) nw++; else nb++;
                                        if (explain) {
                                            fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                                            score=teamScores[ID-1][0];
                                            addExpl("upfloated ID", ID, "colour due", colourPreference[10+cD], "", "");
                                        }
                                    }
                                } else if (reserve==-1) {
                                    reserve=i2;  // this element was temp. discharged, because of upfloater or wrong color due, set reserve
                                    ID=tempIndex[i2]+1;
                                    if (explain) {
                                        if (allowedUpfloater(ID)) addExpl("set as reserve ID", ID, "colour due", colourPreference[10+cD], "", "");
                                        else addExpl("set as reserve ID", ID, "cannot be moved up!");
                                    }
                                } else if (explain) addExpl("ID", ID, "skipped, there is already a reserve");
                            } else {
                                if (explain) addExpl("ID", ID, "not compatible with previous one(s)!");
                            }
                        }
                    }
                    i2++;
                }
                if (i2==addedRows && (i%2==1 || i<nTeams)) {
                    if (explain && !takeAll) addExpl("last group. from now include all left unpaired","","");
                    takeAll=true;   // the last remaining unpaired for any reason. force take all
                    continue;
                }
                if (nTeams==0 && i>2) { // not a specific request and more than 2 candidates?
                    ID=tempIndex[tI[i-2][0]]+1;
                    score=teamScores[ID-1][0];
                    ID2=tempIndex[tI[i-1][0]]+1;
                    score2=teamScores[ID2-1][0];
                    if (score!=score2) {    // if score different, the last one is an upfloater ...
                        cD=tI[--i][1];      // ... decrement counter to discharge it ... 
                        if (cD==1) nw--; else nb--; 
                        if (explain) addExpl("ID", ID2, "discharged, because upfloater not needed now.");
                        i2=tI[i][0];    // the upfloater. look to downfloat an element that can be paired with
                        for (j=i-1; j>=0; j--) 
                            if (establishCompatibility(tI[j][0],i2,false,"")>0 && cD!=tI[j][1]) break; // 1.st of the opposite colour due
                            else if (explain) {
                                ID=tempIndex[tI[j][0]]+1;
                                addExpl("ID", ID, "not compatible with following one(s)!");
                            } 
                        if (j<0) 
                        for (j=i-1; j>=0; j--) 
                            if (establishCompatibility(tI[j][0],i2,optimizationRequested,"")>0) break;  // 2.nd no matter colours!
                            else if (explain) {
                                ID=tempIndex[tI[j][0]]+1;
                                addExpl("ID", ID, "not compatible with following one(s)!");
                            } 
                        if (j<0) j=i-1;             // 3.rd the last one
                        ID=tempIndex[tI[j][0]]+1;
                        cD=tI[j][1];      // ... and the last one of the odd score group
                        if (cD==1) nw--; else nb--;           
                        if (explain) addExpl("ID", ID, "discharged, because of odd score group!");
                        for (k=j; k<i-1;k++) {      // shift up the following ones, if any
                            tI[k][0]=tI[k+1][0];
                            tI[k][1]=tI[k+1][1];
                        } 
                        i--;    // decrement counter
                    }
                }
                nTeams=i; // size of the score group to be paired. elements are already ordered
                if (takeAll) {      // when 'take all' a large group may be selected! Check the group not too much heterogeneous (this may  
                                    // happen in an exteme condition, i.e. at 6th round of a tournament with only 10 players or teams).
                                    // In this situation the normal swiss 'S1 vs S2' may alter the tournament and secondary prizes win!
                    if (i1+1<=availableTeams/2) {   // if true, the group extends from half ranking to the bottom, ... too much!!
                    // ask to switch to Amalfi rating !!  this seems to be an optimization at the moment
                        Object[] options = {Main.localizedText("Yes"), Main.localizedText("No")};
                        int n = JOptionPane.showOptionDialog(this,
                            Main.localizedText("Extreme situation found! Score group is too much heterogeneous.")+"\n"+
                            Main.localizedText("I suggest you to switch to Amalfi rating system! Abort?"),
                            Main.localizedText("Info"),
                            JOptionPane.OK_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]);
                            if (n==0) { // if Yes abort and then manually switch to Amalfi rating
                                if (explain) addExpl("pairing impossible! quit", "", ""); 
                                return;    
                            }
                    }
                }
                // ready to pair the group!
                sup=new int [0];
                inf=new int [nTeams];
                solution=new String [nTeams/2];
                k=0;
                for (i=0; i<nTeams/2; i++) {inf[k++]=i+1;inf[k++]=i+nTeams/2+1;}  // populate the array (i.e. for 6 elements: 1-4-2-5-3-6
                                                                                  // so the first solution could be 1-4, 2-5, 3-6)
                // 1.st try normal swiss way, i.e. 1.st half vs 2.nd half, permuting only 2.nd half
                deltaColour=Math.abs(nw-nb); // this is the number of teams that cannot be paired with colour constrain
                if (!optimizationRequested) deltaColour=nTeams;
                if (explain) {
                        addExpl("group of",nTeams,"trying to pair 1.st half vs 2.nd half");
                        if (deltaColour>0) addExpl("for sure,",deltaColour/2,"ID will not get correct colour due");
                        for (k=0; k<nTeams/2; k++) addExpl("ID",tempIndex[tI[k][0]]+1,"");
                        if (nTeams>2) addExpl("-------","","");
                        for (k=nTeams/2; k<nTeams; k++) addExpl("ID",tempIndex[tI[k][0]]+1,"");
                }
                visits=0;
                alreadyExplored = "";
                if (!solveColumnS1vsS2paranoic(nTeams, sup, inf)  || visits>10000) { // look if any solution found ...
                    // ... if none, try permuting all elements of the group. this is time consuming!
                   if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                   if (visits>1000 && !explain) {
                        jTextArea1.setText("");
                        jDialog4.setVisible(true); // show explain window (and action button ...)
                        explain = true;
                    }
                    if (explain) addExpl("visits",visits,"none found. try mixing all");
                    deltaColour=Math.abs(nw-nb);
                    visits=0;
                    alreadyExplored = "";
                    if (!solveColumnAllparanoic(nTeams, sup, inf) || visits>50000) { // look if any solution found
                        if (explain) addExpl("visits",visits,"none found.");
                        if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                        if (addedPairs+nTeams/2==availableTeams/2) {
                            if (explain && !takeAll) addExpl("last group. from now include all left unpaired","","");
                            takeAll=true; // at the end include all teams
                            orderPairs();       // ... start to break the low order pair ...
                            addedPairs--;      
                            if (addedPairs<0 || visits>50000) {   // ... but if all pairs are broken return with an error
                                            if (explain) addExpl("pairing impossible! quit", "", "");
                                            Object[] options = {Main.localizedText("OK")};
                                            int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                            return;
                            }
                            if (explain) addExpl("pair broken", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                            roundsDetail[addedPairs][0][currRound-1]="0-0-0-0"; // ... reset the pair ...
                        } 
                        if (explain) addExpl("pairing impossible. grow the group and retry", "", "");
                        nTeams++; nTeams++;   // request two more floaters
                        continue;
                    }
                }
                // successful! fix pairs 
                if (explain) addExpl("visits",visits,"done. pairs generated"); 
                for (i=0; i<nTeams/2; i++) {
                    roundsDetail[addedPairs++][0][currRound-1]=solution[i]+"-0-0";
                    if (explain) {
                        S=solution[i].split("-");
                        i1=Integer.valueOf(S[0]);
                        i2=Integer.valueOf(S[1]);
                        deltaScore=teamScores[i1-1][0]-teamScores[i2-1][0];
                        fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                        addExpl("",solution[i],"delta score=",deltaScore*fMolt);
                    }
                }
                if (explain) addExpl("","","");
                nTeams=0;   // reset to non specific request
            }
        }
        if (explain) {
            addExpl("done.","","");
            jDialog4.toFront();
        }
        addedPairs = (short)((availableTeams+1)/2);   // take care of odd teams
        loadCurrentRound();
    }
    
    private void swissPerfectColours(boolean explain) {
        // 'swiss Perfect Colours'. Engine implementation
            /* This is an attempt to code a swiss tournament with the following rules: 
                - first and last rounds are generated with standard swiss rules (see swissDubov() for details)
                - except of that, white and black colours are equal and exactly alternated between games. 
                    Note that, to achieve this goal the elements of a pair may have different score, especially 
                    when many rounds have been played. It is your responsibility to switch to normal swiss if  
                    pairs become too much heterogeneous, if you like. The program warns you to switch to
                    Dubov system when there is only one player remained at the top of the ranking or only two 
                    rounds are left to play.
                - Forfeit and BYE games are considered played without opponent and colour
                - if odd players, last ranked of the dominating colour, but only one time in the tournament, 
                    gets the BYE
                - Two groups are created: S1 and S2, each containing half players.
                    S1 has colour due white and is ordered by decr. score, incr. ARO and incr. rating
                    S2 has colour due black and is ordered by decr. score, decr. rating and decr. ARO
                    (like in the Dubov system), then S1 is tried to be paired vs S2. If some resulting match 
                    has been already played, small permutations of the S2 group are performed.
                - The top ranked player plays in 1.st board with white colour, but shifts with black.
                    Looking at it's position, you may evaluate if pairs become too much heterogeneous. In that
                    case, reset the round and switch to normal swiss (Dutch or Dubov).
            */
        int i,j,k;
        int i1, i2, availableTeams, bC, cD, ARO,ARO1,ARO2, score1,score2, deltaScore;
        int [] sup, inf;
        boolean takeAll, ex;
        String [] S;
        float fMolt;
        if (currRound==1) {swissDubov(explain); return;}                // first and last round = normal swiss
        if (currRound>=Integer.valueOf(rounds)) {swissDubov(explain); return;}
        if (currRound==Integer.valueOf(rounds)-1 ||     // rounds-2 to play or ...
                (teamScores[tempIndex[0]][0]>teamScores[tempIndex[1]][0]) ) { // ... only one at the top of the ranking
            Object[] options = {Main.localizedText("Yes"), Main.localizedText("No")};
            int n = JOptionPane.showOptionDialog(this,
                    Main.localizedText("I suggest you it's time to switch to Dubov system! Should I?"),
                    Main.localizedText("Info"),
                    JOptionPane.OK_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (n==0) {tournamentType=1; swissDubov(explain); return;}     // if Yes
        }
        availableTeams = countAvailableTeams();         // obtain dinamically the number of teams available to play
        if (explain) {
            addExpl("pairs to be generated for round", currRound, "swiss Perfect Colours algorithm");
            addExpl("available",availableTeams,"to be paired");
            addExpl("","","");
        }
        // if odd, set BYE to last ranked of the dominating colour due not taken before 
        if ((availableTeams % 2) == 1 ) {
            int lnb=lastNotBYE(true);
            roundsDetail[(availableTeams+1)/2-1][0][currRound-1]=""+lnb+"-0-0-0";
            if (explain) addExpl("odd -> BYE set to ID",lnb,"");
        }
        tI = new int [availableTeams][3];   // // stores pointer to ID, colour due and ARO
        i=0;
        for (i1=0; i1<addedRows; i1++) {        // populate array (all together)
            if (isAlreadyPaired(i1)) continue;  // this tests also isRetired()
            tI[i][0]=i1; tI[i][1]=colourDue(i1); tI[i++][2]= calculateAROteam(tempIndex[i1]+1, -1);     // store the element 
        }
        nTeams=i;  // size of the group to be paired (all available).
        // need 1.st half colour due white, 2.nd half colour due black
        for (i1=0; i1<nTeams/2; i1++) 
                    if (tI[i1][1]==2) 
                        for (i2=nTeams/2; i2<nTeams; i2++)
                            if (tI[i2][1]==1) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                                break;
                            }
        // order 1.st half decreasing score, increasing ARO, increasing Rating;
        //       2.nd half decreasing score, decreasing Rating, decreasing ARO
        for (i1=0; i1<nTeams/2-1; i1++)
                    for (i2=i1+1; i2<nTeams/2; i2++) {
                        ARO1=tI[i1][2];
                        ARO2=tI[i2][2];
                        score1=teamScores[tempIndex[tI[i1][0]]][0];
                        score2=teamScores[tempIndex[tI[i2][0]]][0];
                        if (score1<score2 ||
                           (score1==score2 &&
                              (ARO1>ARO2 ||
                              (ARO1==ARO2 && getElo(tempIndex[tI[i1][0]]+1,0)>getElo(tempIndex[tI[i2][0]]+1,0))))) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                        }
                    }
        for (i1=nTeams/2; i1<nTeams-1; i1++)
                    for (i2=i1+1; i2<nTeams; i2++) {
                        ARO1=tI[i1][2];
                        ARO2=tI[i2][2];
                        score1=teamScores[tempIndex[tI[i1][0]]][0];
                        score2=teamScores[tempIndex[tI[i2][0]]][0];
                        if (score1<score2 ||
                           (score1==score2 &&
                              (getElo(tempIndex[tI[i1][0]]+1,0)<getElo(tempIndex[tI[i2][0]]+1,0) ||
                              (getElo(tempIndex[tI[i1][0]]+1,0)==getElo(tempIndex[tI[i2][0]]+1,0) && ARO1<ARO2)))) {
                                // swap i1 - i2
                                i=tI[i1][0];
                                cD=tI[i1][1];
                                ARO=tI[i1][2];
                                tI[i1][0]=tI[i2][0];
                                tI[i1][1]=tI[i2][1];
                                tI[i1][2]=tI[i2][2];
                                tI[i2][0]=i;
                                tI[i2][1]=cD;
                                tI[i2][2]=ARO;
                        }
                    }
        // ready to pair the group!
        sup=new int [0];
        inf=new int [nTeams];
        solution=new String [nTeams/2];
        k=0;
        for (i=0; i<nTeams/2; i++) {inf[k++]=i+1;inf[k++]=i+nTeams/2+1;}  // populate the array (i.e. for 6 elements: 1-4-2-5-3-6
                                                                          // so the first solution could be 1-4, 2-5, 3-6)
        visits=0;
        if (explain) {
            addExpl("group of",nTeams,"trying to pair 1.st half vs 2.nd half");
            fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
            for (i1=0; i1<nTeams/2; i1++) {
                score1=teamScores[tempIndex[tI[i1][0]]][0];
                addExpl("ID",tempIndex[tI[i1][0]]+1,"score",score1*fMolt,"ARO",tI[i1][2]);
            }
            if (nTeams>2) addExpl("--------------------------","","");
            for (i2=nTeams/2; i2<nTeams; i2++) {
                score2=teamScores[tempIndex[tI[i2][0]]][0];
                addExpl("ID",tempIndex[tI[i2][0]]+1,"score",score2*fMolt,"rating",getElo(tempIndex[tI[i2][0]]+1,0));
            }
        }
        if (!solveColumnS1vsS2(nTeams, sup, inf) || visits>10000) { // look if any solution found
                        if (explain) { 
                            addExpl("visits",visits,"none found.");
                            addExpl("pairing impossible! quit", "", "");
                        }
                        Object[] options = {Main.localizedText("OK")};
                        int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                        return;
        }
        // successful! fix pairs 
        if (explain) addExpl("visits",visits,"done. pairs generated"); 
        for (i=0; i<nTeams/2; i++) {
                    roundsDetail[addedPairs++][0][currRound-1]=solution[i]+"-0-0";
                    if (explain) {
                        S=solution[i].split("-");
                        i1=Integer.valueOf(S[0]);
                        i2=Integer.valueOf(S[1]);
                        deltaScore=teamScores[i1-1][0]-teamScores[i2-1][0];
                        fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                        addExpl("",solution[i],"delta score=",deltaScore*fMolt);
                    }
        }
        if (explain) { 
                    addExpl("","","");
                    addExpl("done.","","");
                    jDialog4.toFront();
        }
        addedPairs = (short)((availableTeams+1)/2);   // take care of odd teams
        loadCurrentRound();                           // show up the pairing
    }
     
    private void AmalfiRating(boolean explain) {
        // 'Amalfi Rating'. Engine implementation
            /* General rules are: 
                - White and Black colours tend to be equal and alternated between games. Three colour sequence 
                    or delta colour grater than two is allowed only for the last round. In case of exactly equal 
                    colour history, the player with the lower ID alternates the last played colour
                - Forfeit and BYE games are considered played without opponent and colour
                - if odd players, last ranked, but only one time in the tournament, gets the BYE
                - Starting from the top of the ranking, a player is tried to be paired with the one that follows 
                    it of a number of positions equal to the number of rounds left to play. If the opponent is 
                    not legal, a nearer one is first tried and if necessary a farer one. If none legal, the previous 
                    calculated pair is broken and a new one is tried 
            */
        int increment, i, i1, i2, availableTeams, compatible, deltaScore, visits=0, cD, ID;
        String [] S, notAllowed;
        float fMolt;
        String flag;
        boolean found;
        availableTeams = countAvailableTeams();         // obtain dinamically the number of teams available to play
        increment = Integer.valueOf(rounds)-currRound+1;    // calculate the increment of ranking to meet
        if (increment>availableTeams/2) increment = availableTeams/2; // exception. do not go over half of the teams (i.e. switch to swiss)
        if (increment<1) {                      // are you going over last round ?? ...
                if (tournamentPairing==1) {         // ... if a regular tournament this is not of course allowed. notify it
                    Object[] options = {Main.localizedText("OK")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                    return;
                }
                else increment = 1;                 // this is an extension to return and more tournament
        }
        if (explain) {
            addExpl("pairs to be generated for round", currRound, "Amalfi Rating algorithm");
            addExpl("available",availableTeams,"to be paired");
            addExpl("increment",increment,"");
            addExpl("","","");
        }
        // if odd, set BYE to last ranked not taken before 
        if ((availableTeams % 2) == 1 ) {
            int lnb=lastNotBYE(false);
            roundsDetail[(availableTeams+1)/2-1][0][currRound-1]=""+lnb+"-0-0-0";
            if (explain) addExpl("odd -> BYE set to ID",lnb,"");
        }
        tI = new int [availableTeams][2];   // // stores pointer to ID
        i=0;
        for (i1=0; i1<addedRows; i1++) {        // populate array (all together)
            if (isAlreadyPaired(i1)) continue;  // this tests also isRetired()
            tI[i][0]=i1; tI[i++][1]=colourDue(i1);                       // store the element 
        }
        nTeams=i;  // size of the group to be paired (all available).
        if (explain) {
            for (i=0; i<nTeams; i++) {        // show the IDs
                i1 = tI[i][0]; cD = tI[i][1];
                ID = tempIndex[i1]+1;
                flag = "";
                if(!canChangeColourDue(i1, cD)) flag+="C";   // flag cannot change colour due
                addExpl("ID", ID, "colour due", colourPreference[10+cD], "flag:", flag);
            }
            addExpl("","","");
        }
        i1 = 0; notAllowed= new String[addedRows/2];  
        for (i=0; i<addedRows/2; i++) notAllowed[i]="";     // reset the broken pairs array
        for (;;) {        // the main loop. try to pair (starting from the top of ranking)
                for (;;) {
                    if (isAlreadyPaired(tI[i1][0])) i1++;
                    else break;
                    if (i1==nTeams) break;
                }
                // 2.nd look for a compatible one
                i2 = i1 + increment;
                if (i2>=nTeams) i2=nTeams-1;
                for (;;) {      // second loop (in upper ranking direction)
                    compatible=0;
                    if (!isAlreadyPaired(tI[i2][0])) compatible = establishCompatibility(tI[i1][0], tI[i2][0], optimizationRequested, notAllowed[addedPairs]);
                    // remind: establishCompatibility() returns 0 if the pair is not compatible, 1 if optimal 'i1-i2', 2 if optimal 'i2-i1'
                    if (compatible>0) {
                        if (compatible==1) roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[tI[i1][0]]+1)+"-"+(tempIndex[tI[i2][0]]+1)+"-0-0";
                        else roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[tI[i2][0]]+1)+"-"+(tempIndex[tI[i1][0]]+1)+"-0-0";
                        if (explain) addExpl("pair added", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                        addedPairs++;   // increment the pairs counter 
                        i1++;       // the pair has done! increment pointer, exit second loop and continue with the main loop ...
                        for (i=addedPairs; i<addedRows/2; i++) notAllowed[i]=""; // ... but first reset forward broken pairs
                        break;
                    } else {
                        visits++;
                        i2--;       // this pair is not legal, so go up one position in ranking and try again ...
                        if (i2==i1) {  // ... until the position of the first team is reached. Now try going down
                            i2 = i1 + increment;
                            if (i2>=nTeams) i2=nTeams-1;
                            for (;;) {      // third loop (in lower ranking direction)
                                visits++;
                                if (!engineRunning) {addExpl("","",""); addExpl("user action!","",""); return;} 
                                compatible=0;
                                if (!isAlreadyPaired(tI[i2][0])) compatible = establishCompatibility(tI[i1][0], tI[i2][0], optimizationRequested, notAllowed[addedPairs]);
                                if (compatible>0) {
                                    if (compatible==1) roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[tI[i1][0]]+1)+"-"+(tempIndex[tI[i2][0]]+1)+"-0-0";
                                    else roundsDetail[addedPairs][0][currRound-1]=""+(tempIndex[tI[i2][0]]+1)+"-"+(tempIndex[tI[i1][0]]+1)+"-0-0";
                                    if (explain) addExpl("pair added", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                                    addedPairs++;   // increment the pairs counter 
                                    i1++;  // the pair has done! increment pointer, exit third loop and continue with the main loop ...
                                    for (i=addedPairs; i<addedRows/2; i++) notAllowed[i]=""; // ... but first reset forward broken pairs
                                    break;
                                } else {
                                    i2++;  // this pair is not legal, so go down one position in ranking and try again ...
                                    if (visits++>10000 && !explain) {
                                        jTextArea1.setText("");
                                        jDialog4.setVisible(true); // show explain window (and action button ...)
                                        explain = true;
                                    }
                                    if (visits>200000) { // look if any solution found
                                        if (explain) { 
                                            addExpl("visits",visits,"none found.");
                                            addExpl("pairing impossible! quit", "", "");
                                        }
                                        Object[] options = {Main.localizedText("OK")};
                                        int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                        return;
                                    }
                                    if (i2>=nTeams) { // ... until the last position is reached. In that case, ...  
                                        addedPairs--;       // ... start to break the last done pair ...
                                        if (addedPairs<0) {   // ... but if all pairs are broken return with an error
                                            if (explain) addExpl("pairing impossible! quit", "", "");
                                            Object[] options = {Main.localizedText("OK")};
                                            int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                            return;
                                        }
                                        // before to reset, mark this pair as broken, to avoid to consider it be good
                                        // again later. This also prevents a 'ping-pong' between two or more tree levels.
                                        notAllowed[addedPairs]+=";"+roundsDetail[addedPairs][0][currRound-1];
                                        // get back the two indexes from the pair and calculate the point to restart
                                        // from the main loop (the lower index)
                                        S = roundsDetail[addedPairs][0][currRound-1].split("-");
                                        i1 = Integer.valueOf(S[0]);  // ID of White team
                                        i2 = Integer.valueOf(S[1]);  // ID of Black team
                                        i1 = indexAt(i1-1, tempIndex);  // get the ranking from the array
                                        i2 = indexAt(i2-1, tempIndex); 
                                        found = false;
                                        for (i=0; i<nTeams; i++) {
                                            if (i1==tI[i][0]) {    // look at the position in the array
                                                i1=i;
                                                found = true;
                                                break;
                                            }
                                            if (i2==tI[i][0]) {    // look at the position in the array
                                                i1=i;
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (explain) addExpl("pair broken", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                                        roundsDetail[addedPairs][0][currRound-1]="0-0-0-0"; // ... reset the pair ...
                                        if (!found) {
                                            if (explain) addExpl("pairing impossible! quit", "", "");
                                            Object[] options = {Main.localizedText("OK")};
                                            int n = JOptionPane.showOptionDialog(this,
                                                Main.localizedText("Cannot perform pairing!"),
                                                Main.localizedText("Info"),
                                                JOptionPane.OK_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
                                            return;
                                        }
                                        break;                          // ... and finally exit third loop and continue with the main loop
                                    }
                                }
                            }
                            break;      // exit second loop and continue with the main loop
                        }
                    }
                }
                // when all pairs are done, this finishes the algorithm
                if (addedPairs == availableTeams/2) break; 
        }
        if (explain) { 
            addExpl("","",""); 
            addExpl("done.","",""); 
            for (i=0; i<addedPairs; i++) {
                S=roundsDetail[i][0][currRound-1].split("-");
                i1=Integer.valueOf(S[0]);
                i2=Integer.valueOf(S[1]);
                deltaScore=teamScores[i1-1][0]-teamScores[i2-1][0];
                fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                addExpl("",roundsDetail[i][0][currRound-1].replace("-0-0",""),"delta score=",deltaScore*fMolt);
            }
            addExpl("","",""); 
            addExpl("done.","",""); 
            jDialog4.toFront();
        }
        addedPairs = (short)((availableTeams+1)/2);   // take care of odd teams
        loadCurrentRound(); 
    }
    private void roundRobin(boolean explain) {
        // 'round robin'. Engine implementation
            /* This is an attempt to code an all-play-all tournament. 
                - Rounds are generated according to the official Berger tables.
                - if requested, return match are generated with exchanged colours
            */
        int i, i1, i2, r, aR, deltaScore;
        boolean chgCol=false;
        String [] S;
        float fMolt;
        aR=addedRows;
        if (explain) {
            addExpl("pairs to be generated for round", currRound, "round robin algorithm");
            addExpl("",aR,"to be paired");
            addExpl("","","");
        }
        if (addedRows%2==1) {
            aR++;   // odd teams? add one!
            if (explain) addExpl("odd -> increment of one",aR,"");
        }
        if ((currRound-1) % (aR-1) == 0 )
            if ((currRound==aR && tournamentPairing==1) ||     // ... over last round of  a regular tournament
                (currRound==2*aR-1 && tournamentPairing==2)) { // ... over last round of  a return tournament
                Object[] options = {Main.localizedText("OK")};
                int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Tournament is finished!"),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                return;
            }
        // here count only currRound and aR
        r=(currRound-1)%(aR-1)+1;
        chgCol=((currRound-1)/(aR-1))%2==1;
        if (!calculateBergerTable(r, aR)) return;
        // resulting array into tempIndex[], then to pair take 0-(n-1), 1-(n-2), 2-(n-3) ... (n/2-1)-(n/2)
        addedPairs=0;
        for (i=0; i<aR/2; i++) {
                i1=tempIndex[i];
                i2=tempIndex[aR-i-1];
                if (chgCol) roundsDetail[addedPairs][0][currRound-1]=""+i2+"-"+i1+"-0-0";
                else roundsDetail[addedPairs][0][currRound-1]=""+i1+"-"+i2+"-0-0";
                if (explain) addExpl("pair added", roundsDetail[addedPairs][0][currRound-1].replace("-0-0",""), "");
                addedPairs++;
        }
        i1=tempIndex[0];
        if (addedRows<aR) {
            roundsDetail[0][0][currRound-1]=""+i1+"-0-0-0";
            if (explain) addExpl("odd -> BYE set to ID",i1,"");
        }
        if (explain) { 
            addExpl("","",""); 
            addExpl("done.","",""); 
            for (i=0; i<addedPairs; i++) {
                S=roundsDetail[i][0][currRound-1].split("-");
                if (S.length!=4) continue;      // empty row?
                i1=Integer.valueOf(S[0]);
                i2=Integer.valueOf(S[1]);
                if (i1==0 || i2==0) continue;   // BYE?
                deltaScore=teamScores[i1-1][0]-teamScores[i2-1][0];
                fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
                addExpl("",roundsDetail[i][0][currRound-1].replace("-0-0",""),"delta score=",deltaScore*fMolt);
            }
            addExpl("","",""); 
            addExpl("done.","",""); 
            jDialog4.toFront();
        }
        loadCurrentRound();                           // show up the pairing
    }
    
    private int countAvailableTeams() {
        // this simply counts how many teams are not marked as '(W)' i.e. are not 'retired'
        int n=0, i;
        for (i=0; i<addedRows; i++) {
            if (!isRetired(i)) n++;
        }
        return n;
    }
    private int lastNotBYE(boolean balanceColours) {
        // returns the ID of the last ranked team, not having had a Forfeit or BYE before, if a regular tournament,
        // or as extension having had max one if a return tournament
        // (forfeit or BYE is marked with 'f' appended to the results string)
        int dominating=0, ID;
        if (maxRound>1 && balanceColours) {dominating=countColoursDue(); nonePlayedAlternate = 0;} // returns 1=more whites, 2=more blacks
        for (int i=addedRows-1; i>=0; i--) {  // go back in ranking
            ID=tempIndex[i]+1;
            if (canTakeTheBye(i))
                if (dominating>0) {
                    if (colourDue(i)==dominating) return ID;
                }
                else return ID; 
        }
        return tempIndex[addedRows-1]+1; // this clearly is an error condition! return the last one
    }
    
    private int countColoursDue() {
        int i,nw=0,nb=0,cD;
        for (i=0; i<addedRows; i++) 
            if (!isRetired(i)) {
                cD=colourDue(i);
                if (cD==1) nw++;
                else if (cD==2) nb++;
            }
        if (nw>nb) return 1;
        if (nw<nb) return 2;
        return 0;
    }
    
    private boolean isAlreadyPaired(int i){
        // test if a Team is currently not available (because it is already paired or retired?) 
        if (i<0 || i>=addedRows) return true;    // check out of range?
        for (int j=0;j<(addedRows+1)/2;j++) {   // loop pairs
                    String [] S = roundsDetail[j][0][currRound-1].split("-");
                    if (S.length!=4) continue;     // some kind of unexpected error?
                    if (S[0].equals(""+(tempIndex[i]+1))) return true; // is it the first one of a pair?
                    if (S[1].equals(""+(tempIndex[i]+1))) return true; // is it the second one of a pair?
        }
        return isRetired(i);
    }
    
    private boolean isRetired(int i) {
        // test if the team is marked as 'retired'
        // (remind: this is handled by adding '(W)' to the team name)
        String team="";
        TableModel myTM = jTable1.getModel();
        team = ""+myTM.getValueAt(sortIndex[tempIndex[i]]-1,0); // get the team name from the grid object
        if (team.contains("(W)")) return true;
        return false;
    }

    private int establishCompatibility(int i1, int i2, boolean optimizeColours, String notAllowed) {
        // this is the most complex part of the algorithm ... to establish the compatibility of two teams.
        // it returns 0 if the pair is not legal, 1 for better 'i1-i2' and 2 for better 'i2-i1'.
        // If colour optimization is required, White and Black colours tend to be equal and alternated between games. 
        // Except for Dubov, three colour sequence or delta colour grater than two is allowed for the last round. 
        // Except for Dubov, in case of exactly equal colour history, the player with the lower ID alternates the 
        // last played colour.
        String col1="",col2="";
        int nw1=0, nw2=0, nb1=0, nb2=0, r, j, ID1, ID2;
        String [] S;
        boolean white1, white2, played1, played2;
        if (i1<0 || i1>=addedRows) return 0;    // check out of range?
        if (i2<0 || i2>=addedRows) return 0;
        if (i1==i2) return 0;                   // this should never happen!!!
        ID1 = tempIndex[i1]+1;
        ID2 = tempIndex[i2]+1;
        // has the intended pair been marked 'not a good one' for any reason?  
        if (notAllowed.contains(";"+ID1+"-"+ID2)) return 0;
        if (notAllowed.contains(";"+ID2+"-"+ID1)) return 0;        
        if (maxRound>1) {   // starting from the 2.nd round, test if game already played and obtain colour history
            int times = 0;          // played how many times?
            white1=false;           // remember the last game colour to alternate it if necessary in return match
            for (r=0;r<maxRound-1;r++) {  // scan rounds
                played1=false;  played2=false;
                for (j=0;j<(addedRows+1)/2;j++) {   // scan pairs
                    S = roundsDetail[j][0][r].split("-");
                    if (S.length!=4) continue;     // some kind of unexpected error?
                    // test the pair, but avoid *** Forfeit ***
                    if (S[0].equals(""+ID1) && S[1].equals(""+ID2) &&
                                S[3].indexOf('f')<=0) { times++; white1=true;}
                    if (S[1].equals(""+ID1) && S[0].equals(""+ID2) &&
                                S[3].indexOf('f')<=0) { times++; white1=false;}
                    // store colour history, but set *** Forfeit = no colour *** 
                    if (S[0].equals(""+ID1)) {
                        played1=true;
                        if (S[1].equals("0") || S[3].indexOf('f')>0) col1=" "+col1;
                        else { col1+="W"; nw1++; }
                    }
                    if (S[1].equals(""+ID1)) {
                        played1=true;
                        if (S[3].indexOf('f')>0) col1=" "+col1; 
                        else { col1+="B"; nb1++; }
                    }
                    if (S[0].equals(""+ID2)) {
                        played2=true;
                        if (S[1].equals("0") || S[3].indexOf('f')>0) col2=" "+col2;
                        else { col2+="W"; nw2++; }
                    } 
                    if (S[1].equals(""+ID2)) {
                        played2=true;
                        if (S[3].indexOf('f')>0) col2=" "+col2;
                        else { col2+="B"; nb2++; }
                    }
                }
                if (tournamentPairing!=3 && times>=tournamentPairing) return 0;   // if not a good pair return
                if (!played1) col1=" "+col1;
                if (!played2) col2=" "+col2;
            }
            if (times>0) {
                // this is an extension for return match. the pair is good and has played before, so assign 
                // colours opposite of the colours played with. This is a priority criteria
                if (white1) return 2;
                return 1;    
            }
        }
        if (!optimizeColours) return 1;  // if not requested further colour optimization, simply return 'i1-i2'
        if (maxRound==1) {
            // 1.st round. assign colour to top ranked team and then alternate colours
            if (addedPairs == 0) {
                if (checkerRunning) {
                    if (ID1==firstWhite) return 1;
                    else return 2;
                }
                Object[] options = {Main.localizedText("Yes"), Main.localizedText("No"), Main.localizedText("draw lots")};
                int n = JOptionPane.showOptionDialog(this,
                    Main.localizedText("Assign white to top ranked player?"),
                    Main.localizedText("Info"),
                    JOptionPane.OK_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
                if (n==0) return 1;     // if Yes
                if (n==1) return 2;     // if No
                if (Math.random()<0.5) return 2;    // very simple implementation of draw lots (probability of white = 50%)
            } else {
                S = roundsDetail[addedPairs-1][0][0].split("-");  // take last pair of the current round
                if (S.length!=4) return 0;     // some kind of unexpected error?
                if (Integer.valueOf(S[0]) < Integer.valueOf(S[1])) {
                    // last pair was 'low-high'
                    if (ID1<ID2) {   
                        // return 'i2-i1'  to have higher ID first
                        return 2;
                    }
                }
                else {
                    // last pair was 'high-low'
                    if (ID1>ID2) { 
                        // return 'i2-i1'  to have lower ID first
                        return 2;
                    }
                }
            }
        }
        else if ((maxRound % Integer.valueOf(rounds)) >0 || tournamentType==0 || tournamentType==1 || tournamentType==3) {
            // 2.nd to last but one round (or all for Dutch, Dubov and Perfect Colours). check delta colour and colour sequence
            if (nw1<nw2) {
                // less White. assign White, but ...
                if (col1.endsWith("WW") || col2.endsWith("BB")) return 0; // ... avoid three colour sequence
                else return 1;
            }
            else if (nw1>nw2) {
                // more White. assign Black, but ...
                if (col2.endsWith("WW") || col1.endsWith("BB")) return 0; // ... avoid three colour sequence
                else return 2;
            }
            else if (nb1<nb2) {
                // less Black. assign Black, but ...
                if (col1.endsWith("BB") || col2.endsWith("WW")) return 0; // ... avoid three colour sequence
                else return 2;
            }
            else if (nb1>nb2) {
                // more Black. assign White, but ...
                if (col1.endsWith("WW") || col2.endsWith("BB")) return 0; // ... avoid three colour sequence
                else return 1;
            }
            else {
                // equal White.  this is the most complex situation!
                if (col1.endsWith("WW") && col2.endsWith("WW")) return 0; // if none can take White, avoid three colour sequence
                if (col1.endsWith("BB") && col2.endsWith("BB")) return 0; // if none can take Black, avoid three colour sequence
                for (r=currRound-2;r>=0;r--) {      // go to see back the colour sequence 
                        white1=white2=false;
                        if (col1.substring(r,r+1).equals("W")) white1=true; 
                        if (col2.substring(r,r+1).equals("W")) white2=true; 
                        if (!white1 && white2) {   // 'B-W'?
                            nw1++; nb2++;   // intended to play opposite of last colour i.e. 'W-B' ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 1;
                        } else if (white1 && !white2) {  // 'W-B'?
                            nb1++; nw2++;   // intended to play opposite of last colour i.e. 'B-W' ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 2;
                        }
                }
                // no solution found. all colours again equal!!
                if (tournamentType==1 || tournamentType==3) {
                    // for Dubov or Perfect Colours systems assign white to the player with the higher ARO, lower Elo, lower ID 
                    white1 = true;
                    if (calculateAROteam(ID1, -1)>calculateAROteam(ID2, -1)) ;
                    else if (calculateAROteam(ID1, -1)==calculateAROteam(ID2, -1)) {
                        if (getElo(ID1, 0)<getElo(ID2, 0)) ; 
                        else if (getElo(ID1, 0)==getElo(ID2, 0)) {
                            if (ID1>ID2) white1=false;
                        }
                        else white1=false;
                    } else  white1 = false;
                    if (white1) {  
                            nw1++; nb2++;   // intended to play White ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 1;
                    } else { 
                            nb1++; nw2++;   // intended to play Black ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 2;
                    }
                } else {
                    if (tournamentType==0) return 3;    // Dutch
                    if (ID1<ID2) {
                        // the first one of the pair has the lower ID. alternate this
                        if (col1.endsWith("W")) {  
                            nb1++; nw2++;   // intended to play opposite of last colour ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 2;
                        } else {  
                            nw1++; nb2++;   // intended to play opposite of last colour ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 1;
                        }
                    } else { // the second one of the pair has the lower ID. alternate this  
                        if (col2.endsWith("B")) {  
                            nb1++; nw2++;   // intended to play opposite of last colour ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 2;
                        } else {  
                            nw1++; nb2++;   // intended to play opposite of last colour ...
                            if (Math.abs(nw1-nb1)>2) return 0; // ... but test delta colour
                            if (Math.abs(nw2-nb2)>2) return 0;
                            return 1;
                        }
                    }
                }
            }
        }  else {    // last round (less Dubov or Perfect Colours). do not optimize colours very well!
            if (nw1<nw2) return 1;      // less White. assign White
            else if (nw1>nw2) return 2; // more White. assign Black
            else if (nb1<nb2) return 2; // less Black. assign Black
            else if (nb1>nb2) return 1; // more Black. assign White
            else {  // equal White
                for (r=currRound-2;r>=0;r--) {      // go to see back the colour sequence 
                        white1=white2=false;
                        if (col1.substring(r,r+1).equals("W")) white1=true; 
                        if (col2.substring(r,r+1).equals("W")) white2=true; 
                        if (!white1 && white2) {   // 'B-W'?
                            return 1;
                        } else if (white1 && !white2) {  // 'W-B'?
                            return 2;
                        }
                }
                // no solution found. all colours equal again!!
                if (ID1<ID2) {
                    // the first one of the pair has the lower ID. alternate this
                    if (col1.endsWith("W")) return 2;
                } else // the second one of the pair has the lower ID. alternate this  
                    if (col2.endsWith("B")) return 2;
            }
        }       
        return 1;    
    }
    
    private int colourDue(int i) {
        // returns the opposite of last colour played, or 0 if not valid games
        if (currRound==1) return 3; // avoid runtime errors
        int r,j,nw=0,nb=0; 
        boolean found;
        String[] S;
        String col="";
        if (tournamentType == 3) {      // Perfect Colours. need to evaluate only the last round?
            r=maxRound-2;
            for (j=0;j<(addedRows+1)/2;j++) {
                S = roundsDetail[j][0][r].split("-");
                if (S.length!=4) continue;     // some kind of unexpected error?
                if (S[0].equals(""+(tempIndex[i]+1)) && S[3].indexOf('f')<=0) return 2; // check for Forfeit or BYE!
                if (S[1].equals(""+(tempIndex[i]+1)) && S[3].indexOf('f')<=0) return 1; // check for Forfeit or BYE!
            } 
        } 
        // need to evaluate the history
        for (r=0;r<maxRound-1;r++) {  // scan previous rounds
                found=false;
                for (j=0;j<(addedRows+1)/2;j++) {   // scan pairs
                    S = roundsDetail[j][0][r].split("-");
                    if (S.length!=4) continue;     // some kind of unexpected error?
                    // store colour history, but set *** forfeit = no colour *** 
                    if (S[0].equals(""+(tempIndex[i]+1))) {
                        if (S[1].equals("0") || S[3].indexOf('f')>0) col=" "+col;
                        else { col+="W"; nw++; }
                        found=true;
                        break;
                    }
                    if (S[1].equals(""+(tempIndex[i]+1))) {
                        if (S[3].indexOf('f')>0) col=" "+col;  
                        else { col+="B"; nb++; }
                        found=true;
                        break;
                    }
                }
                if (!found) col=" "+col;   // not paired at all!
        }
        if (col.endsWith("WW")) return 2;
        if (col.endsWith("BB")) return 1;
        if (nw>nb) return 2;
        if (nw<nb) return 1;
        if (col.endsWith("W")) return 2;
        if (col.endsWith("B")) return 1;
        // none played!!
        if (nonePlayedAlternate==1) {nonePlayedAlternate=2; return 2;}
        if (nonePlayedAlternate==2) {nonePlayedAlternate=1; return 1;}
        S = roundsDetail[0][0][0].split("-");
        if (S.length==4)    
            if ((tempIndex[i]+1)%2==1) {
                // odd. assign the colour the lowest ID played
                if (S[1].equals("1")) {nonePlayedAlternate=2; return 2;}
                else {nonePlayedAlternate=1; return 1;}
            } else {
                // even. assign the opposite colour the lowest ID played
                if (S[0].equals("1")) {nonePlayedAlternate=2; return 2;}
                else {nonePlayedAlternate=1; return 1;}
            }
        return 1;
    }
    
    private boolean canChangeColourDue(int i, int cD) {
    // evaluate if this candidate can change its colour due
        if (!optimizationRequested) return true;
        int r,j,nw=0,nb=0; 
        boolean found;
        String[] S;
        String col="";
        if (tournamentType==2 || tournamentType==4)      // only for Simple or Amalfi!
            if ((maxRound % Integer.valueOf(rounds))==0) return true; // last round? force can change
        // need to evaluate the history
        for (r=0;r<maxRound-1;r++) {  // scan previous rounds
                found=false;
                for (j=0;j<(addedRows+1)/2;j++) {   // scan pairs
                    S = roundsDetail[j][0][r].split("-");
                    if (S.length!=4) continue;     // some kind of unexpected error?
                    // store colour history, but set *** forfeit = no colour *** 
                    if (S[0].equals(""+(tempIndex[i]+1))) {
                        if (S[1].equals("0") || S[3].indexOf('f')>0) col=" "+col;
                        else { col+="W"; nw++; }
                        found=true;
                        break;
                    }
                    if (S[1].equals(""+(tempIndex[i]+1))) {
                        if (S[3].indexOf('f')>0) col=" "+col; 
                        else { col+="B"; nb++; }
                        found=true;
                        break;
                    }
                }
                if (!found) col=" "+col;   // not paired at all!
        }
        if (cD==1)  { nb++; col+="B"; }   // add intended colour 
        else        { nw++; col+="W"; }
        if (col.endsWith("WWW")) return false;
        if (col.endsWith("BBB")) return false;
        if (Math.abs(nw-nb)>2) return false;
        return true;
    }
    
    private int balanceColours(int[][] tI, int n) {
        // returns 0 if equal whites and blacks, 1 if white or 2 if black suggested colour to balance
        int nw=0, nb=0, k;
        for (k=0; k<n; k++) {
            if (tI[k][1]==1) nw++;
            if (tI[k][1]==2) nb++;
        }
        if (nw>nb) return 2;
        if (nw<nb) return 1;
        return 0;
    }

    private void orderPairs() {
    // order pairs by score or ranking 
        int k1,k2,ID1,ID2,IDA,IDB, score1, score2, scoreA, scoreB, sumA, sumB;
        String [] S;
        String pair1,pair2;
        for (k1=0; k1<addedPairs-1; k1++)
            for (k2=k1+1; k2<addedPairs; k2++) {
                pair1=roundsDetail[k1][0][currRound-1];
                S=pair1.split("-");
                if (S.length != 4) continue;
                ID1=Integer.valueOf(S[0]).shortValue(); // the one
                ID2=Integer.valueOf(S[1]).shortValue(); // the other one
                if (ID1==0) continue;
                score1=teamScores[ID1-1][0];
                IDA=ID1; scoreA=score1; sumA=score1; 
                if (ID2>0) {
                    score2=teamScores[ID2-1][0]; sumA+=score2;
                    if (score2>score1) {scoreA=score2; IDA=ID2;}
                    if (score2==score1 && ID2<ID1) IDA=ID2;
                } else scoreA=0;    // bye put at the bottom list
                pair2=roundsDetail[k2][0][currRound-1];
                S=pair2.split("-");
                if (S.length != 4) continue;
                ID1=Integer.valueOf(S[0]).shortValue(); // the one
                ID2=Integer.valueOf(S[1]).shortValue(); // the other one
                if (ID1==0) continue;
                score1=teamScores[ID1-1][0];
                IDB=ID1; scoreB=score1; sumB=score1; 
                if (ID2>0) {
                    score2=teamScores[ID2-1][0]; sumB+=score2;
                    if (score2>score1) {scoreB=score2; IDB=ID2;}
                    if (score2==score1 && ID2<ID1) IDB=ID2;
                } else scoreB=0;    // bye put at the bottom list
                if (scoreB>scoreA || (scoreB==scoreA && sumB>sumA) ||
                        (scoreB==scoreA && sumB==sumA && IDB<IDA) ) {                  // swap pair
                    roundsDetail[k1][0][currRound-1]=pair2;
                    roundsDetail[k2][0][currRound-1]=pair1;
                    for (int j=1; j<=maxPlayersPerTeam; j++) {      // for teams swap players
                        pair1=roundsDetail[k1][j][currRound-1];
                        roundsDetail[k1][j][currRound-1]=roundsDetail[k2][j][currRound-1];
                        roundsDetail[k2][j][currRound-1]=pair1;
                    }
                }
            }
    }

    private void addFloaters() {
    // add any upfloater or downfloater to the arrays based on score difference at previous round 
        int k,i1,i2,score1,score2;
        String [] S;
        String pair;
        for (k=0; k<addedPairs; k++) {
                pair=roundsDetail[k][0][currRound-1];
                S=pair.split("-");
                if (S.length != 4) continue;
                i1=Integer.valueOf(S[0]).shortValue(); // the one
                i2=Integer.valueOf(S[1]).shortValue(); // the other one
                score1=-1;                           // BYE is considered downfloat!!
                if (i1>0) score1=teamScores[i1-1][0];
                score2=-1;                           // BYE is considered downfloat!!
                if (i2>0) score2=teamScores[i2-1][0];
                if (score1>score2) {
                    if (i2>0)
                      if (upfloaters[currRound-1].indexOf(";"+i2+";")<0)
                        upfloaters[currRound-1]=upfloaters[currRound-1]+i2+";"; // register i2
                    if (downfloaters[currRound-1].indexOf(";"+i1+";")<0)
                        downfloaters[currRound-1]=downfloaters[currRound-1]+i1+";"; // register i1
                }
                if (score1<score2) {
                    if (i1>0)
                      if (upfloaters[currRound-1].indexOf(";"+i1+";")<0)
                        upfloaters[currRound-1]=upfloaters[currRound-1]+i1+";"; // register i1
                    if (downfloaters[currRound-1].indexOf(";"+i2+";")<0)
                        downfloaters[currRound-1]=downfloaters[currRound-1]+i2+";"; // register i2
                }
        }
    }

    private void updateFloaters() {
    // update arrays of upfloaters and downfloaters after results of the current round
        upfloaters[currRound-1]="upfloaters of round "+(currRound)+" ;";    // reset the arrays
        downfloaters[currRound-1]="downfloaters of round "+(currRound)+" ;";
        addFloaters();
        int k,i1,i2,score1,score2;
        String [] S;
        String pair;
        for (k=0; k<addedPairs; k++) {
                pair=roundsDetail[k][0][currRound-1];
                S=pair.split("-");
                if (S.length != 4) continue;
                if (S[3].indexOf('f')<0) continue;  // I'm interested only Forfeit results!
                i1=Integer.valueOf(S[0]).shortValue(); // the one
                i2=Integer.valueOf(S[1]).shortValue(); // the other one
                if (i1==0 || i2==0) continue;       // I'm interested only Forfeit results!
                if (upfloaters[currRound-1].indexOf(";"+i1+";")>0)
                        upfloaters[currRound-1]=upfloaters[currRound-1].replaceAll(";"+i1+";",";"); // unregister i1
                if (upfloaters[currRound-1].indexOf(";"+i2+";")>0)
                        upfloaters[currRound-1]=upfloaters[currRound-1].replaceAll(";"+i2+";",";"); // unregister i2
                if (downfloaters[currRound-1].indexOf(";"+i1+";")>0)
                        downfloaters[currRound-1]=downfloaters[currRound-1].replaceAll(";"+i1+";",";"); // unregister i1
                if (downfloaters[currRound-1].indexOf(";"+i2+";")>0)
                        downfloaters[currRound-1]=downfloaters[currRound-1].replaceAll(";"+i2+";",";"); // unregister i2
                score1=integerScore(S[2]); if (score1>0) downfloaters[currRound-1]=downfloaters[currRound-1]+i1+";"; // register i1
                score2=integerScore(S[3]); if (score2>0) downfloaters[currRound-1]=downfloaters[currRound-1]+i2+";"; // register i2
       }
    }

    private boolean solveColumnS1vsS2normal(int n, int[] sup, int[] inf) {
        // this is the recursive algorithm to look at a solution for pairing the group 1.st half vs 2.nd half
        // permutations are performed only for the 2.nd half!
        // 'normal' colour criteria assignment is adopted. White and Black colours must be equal!
        int i,i1,i2,k,j;
        if (n==0) {
            visits++; // we are at a node. solution found! store and terminate
            k=0;
            for (i=0; i<nTeams; i++,i++) {
                i1=tI[sup[i]-1][0];
                i2=tI[sup[i+1]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==1)    // look for the colour assignment
                    solution[k++]=""+(tempIndex[tI[sup[i]-1][0]]+1)+"-"+(tempIndex[tI[sup[i+1]-1][0]]+1);
                else
                    solution[k++]=""+(tempIndex[tI[sup[i+1]-1][0]]+1)+"-"+(tempIndex[tI[sup[i]-1][0]]+1);
            }
            return true;
        } else {
            if (!engineRunning) return false;
            // some tree level ... prepare arrays for recusion
            int[] sup2= new int[nTeams-n+2], inf2=new int[n-2];
            for (i=0; i<nTeams-n; i++) sup2[i]=sup[i];        // copy sup[]
            sup2[nTeams-n]=inf[0];                            // add first element of inf[]
            for (i=1; i<n; i++,i++) {                         // look for the second element to add 
                if (visits>10000) return true;                // this is a time waste control
                i1=tI[inf[0]-1][0];
                i2=tI[inf[i]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==0) { visits++; continue; }  // already played? no good colours? skip.
                sup2[nTeams-n+1]=inf[i];                      // add second element of inf[]
                k=0;                                          // now, go to copy inf[] without the two elements added to sup[] ...
                for (j=1;j<n; j++,j++)                        // ... in a way that order S1-S2 is maintained!
                    if (j<i) {inf2[k++]=inf[j+1]; inf2[k++]=inf[j];}
                    else if (j>i) {inf2[k++]=inf[j-1]; inf2[k++]=inf[j];}
                if (solveColumnS1vsS2normal(n-2, sup2, inf2)) return true;   // recurse into
            }
        }
        return false;
    }

    private boolean solveColumnAllnormal(int n, int[] sup, int[] inf) {
        // this is the recursive algorithm to look at a solution for pairing the group, 
        // permuting all elements.
        // 'normal' colour criteria assignment is adopted. White and Black colours must be equal!
        int i,i1,i2,k,j;
        if (n==0) {
            visits++; // we are at a node. solution found! store and terminate
            k=0;
            for (i=0; i<nTeams; i++,i++) {
                i1=tI[sup[i]-1][0];
                i2=tI[sup[i+1]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==1)    // look for the colour assignment
                    solution[k++]=""+(tempIndex[tI[sup[i]-1][0]]+1)+"-"+(tempIndex[tI[sup[i+1]-1][0]]+1);
                else
                    solution[k++]=""+(tempIndex[tI[sup[i+1]-1][0]]+1)+"-"+(tempIndex[tI[sup[i]-1][0]]+1);
            }
            return true;
        } else {
            if (!engineRunning) return false;
            // some tree level ... prepare arrays for recusion
            int[] sup2= new int[nTeams-n+2], inf2=new int[n-2];
            for (i=0; i<nTeams-n; i++) sup2[i]=sup[i];        // copy sup[]
            sup2[nTeams-n]=inf[0];                            // add first element of inf[]
            for (i=1; i<n; i++) {                             // look for the second element to add
                if (visits>50000) return true;                // this is a time waste control
                i1=tI[inf[0]-1][0];
                i2=tI[inf[i]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==0) { visits++; continue; }  // already played? no good colours? skip.
                sup2[nTeams-n+1]=inf[i];                      // add second element of inf[]
                k=0;                                          // now, go to copy inf[] without the two elements added to sup[] ...
                for (j=1;j<n; j++)                            // ... in this way S1 and S2 are mixed
                    if (j!=i) inf2[k++]=inf[j];               
                if (solveColumnAllnormal(n-2, sup2, inf2)) return true;   // recurse into
            }
        }
        return false;
    }

    private boolean solveColumnS1vsS2(int n, int[] sup, int[] inf) {
        // this is the recursive algorithm to look at a solution for pairing the group 1.st half vs 2.nd half
        // permutations are performed only for the 2.nd half!
        // colour assignment is a strong priority. White and Black colours must be equal!
        int i,i1,i2,k,j;
        if (n==0) {
            visits++; // we are at a node. solution found! store and terminate
            k=0;
            for (i=0; i<nTeams; i++,i++) {
                if (tI[sup[i]-1][1]==1)  // look for the colour assignment
                    solution[k++]=""+(tempIndex[tI[sup[i]-1][0]]+1)+"-"+(tempIndex[tI[sup[i+1]-1][0]]+1);
                else
                    solution[k++]=""+(tempIndex[tI[sup[i+1]-1][0]]+1)+"-"+(tempIndex[tI[sup[i]-1][0]]+1);
            }
            return true;
        } else {
            if (!engineRunning) return false;
            // some tree level ... prepare arrays for recusion
            int[] sup2= new int[nTeams-n+2], inf2=new int[n-2];
            for (i=0; i<nTeams-n; i++) sup2[i]=sup[i];        // copy sup[]
            sup2[nTeams-n]=inf[0];                            // add first element of inf[]
            for (i=1; i<n; i++,i++) {                         // look for the second element to add
                if (visits>10000) return true;                // this is a time waste control
                if (tI[inf[0]-1][1]==tI[inf[i]-1][1]) { visits++; continue; } // same colour due? skip
                i1=tI[inf[0]-1][0];
                i2=tI[inf[i]-1][0];
                if (establishCompatibility(i1, i2, false, "")==0) { visits++; continue; }  // already played? skip
                sup2[nTeams-n+1]=inf[i];                      // add second element of inf[]
                k=0;                                          // now, go to copy inf[] without the two elements added to sup[] ...
                for (j=1;j<n; j++,j++)                        // ... in this way the order S1-S2 is maintained!
                    if (j<i) {inf2[k++]=inf[j+1]; inf2[k++]=inf[j];}
                    else if (j>i) {inf2[k++]=inf[j-1]; inf2[k++]=inf[j];}
                if (solveColumnS1vsS2(n-2, sup2, inf2)) return true;   // recurse into
            }
        }
        return false;
    }
        
    private boolean solveColumnS1vsS2paranoic(int n, int[] sup, int[] inf) {
        // this is the recursive algorithm to look at a solution for pairing the group 1.st half vs 2.nd half
        // permutations are performed only for the 2.nd half!
        // paranoic pair colour optimization, because White and Black colours may be not equal!
        int i,i1,i2,k,j;
        if (n==0) {
            visits++; // we are at a node. store and finish!
            k=0;
            for (i=0; i<nTeams; i++,i++) {
                i1=tI[sup[i]-1][0];
                i2=tI[sup[i+1]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==1)    // look for the colour assignment
                    solution[k++]=""+(tempIndex[tI[sup[i]-1][0]]+1)+"-"+(tempIndex[tI[sup[i+1]-1][0]]+1);
                else
                    solution[k++]=""+(tempIndex[tI[sup[i+1]-1][0]]+1)+"-"+(tempIndex[tI[sup[i]-1][0]]+1);
            }
            return true;
        } else {
            if (!engineRunning) return false;  // user action?
            // some tree level ... prepare arrays for recusion
            int[] sup2= new int[nTeams-n+2], inf2=new int[n-2];
            for (i=0; i<nTeams-n; i++) sup2[i]=sup[i];        // copy sup[]
            sup2[nTeams-n]=inf[0];                            // add first element of inf[]
            for (i=1; i<n; i++,i++) {                         // look for the second element to add 
                if (visits>10000) return true;                // this is a time waste control
                if (n>deltaColour && tI[inf[0]-1][1]==tI[inf[i]-1][1]) { // to quickly converge, same colour due? skip ...
                            visits++;                                   // ... as far as pairs of opponent colours are available!
                            continue; 
                } 
                i1=tI[inf[0]-1][0];
                i2=tI[inf[i]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==0) { visits++; continue; }  // already played? no good colours? skip.
                sup2[nTeams-n+1]=inf[i];                      // add second element of inf[]
                k=0;                                          // now, go to copy inf[] without the two elements added to sup[] ...
                for (j=1; j<n; j++,j++)                        // ... in a way that order S1-S2 is maintained!
                    if (j<i) {inf2[k++]=inf[j+1]; inf2[k++]=inf[j];}
                    else if (j>i) {inf2[k++]=inf[j-1]; inf2[k++]=inf[j];}
                if (solveColumnS1vsS2paranoic(n-2, sup2, inf2)) return true;   // recurse into
            }
            if (n==2 || n==deltaColour) return false;   // not the case ...
            if (alreadyExplored.indexOf("-"+inf[0]+"-"+inf[1]+"-")>=0) ;  // a cycled pair returns. assume completed
            else {
                alreadyExplored += "-"+inf[0]+"-"+inf[1]+"-"; // store cycled pair 
                inf2=new int[n];
                k=0;                                        // push the first pair of inf[] at the bottom ...
                for (j=2; j<n; j++) {                       // ... in a way that order S1-S2 is maintained!
                    inf2[k++]=inf[j]; 
                }
                inf2[k++]=inf[0]; inf2[k++]=inf[1];
                return solveColumnS1vsS2paranoic(n, sup, inf2);   // retry
            }
            if (n<nTeams) return false;   // further processing only at the root level !!
            // all tree explored and no solution was found! 
            deltaColour += 2;             // try to allow two more elements have colour changed ...
            if (deltaColour>nTeams) return false; // ... but if all possibilites expired then abort!
            alreadyExplored = "";
            return solveColumnS1vsS2paranoic(n, sup, inf);   // recurse into
        }
    }

    private boolean solveColumnAllparanoic(int n, int[] sup, int[] inf) {
        // this is the recursive algorithm to look at a solution for pairing the group, 
        // permuting all elements.
        // paranoic pair colour optimization, because White and Black colours may be not equal!
        int i,i1,i2,k,j;
        if (n==0) {
            visits++; // we are at a node. store and finish!
            k=0;
            for (i=0; i<nTeams; i++,i++) {
                i1=tI[sup[i]-1][0];
                i2=tI[sup[i+1]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==1)    // look for the colour assignment
                    solution[k++]=""+(tempIndex[tI[sup[i]-1][0]]+1)+"-"+(tempIndex[tI[sup[i+1]-1][0]]+1);
                else
                    solution[k++]=""+(tempIndex[tI[sup[i+1]-1][0]]+1)+"-"+(tempIndex[tI[sup[i]-1][0]]+1);
            }
            return true;
        } else {
            if (!engineRunning) return false;  // user action?
            // some tree level ... prepare arrays for recusion
            int[] sup2= new int[nTeams-n+2], inf2=new int[n-2];
            for (i=0; i<nTeams-n; i++) sup2[i]=sup[i];        // copy sup[]
            sup2[nTeams-n]=inf[0];                            // add first element of inf[]
            for (i=1; i<n; i++) {                             // look for the second element to add 
                if (visits>50000) return true;                // this is a time waste control
                if (n>deltaColour && tI[inf[0]-1][1]==tI[inf[i]-1][1]) { // same colour due? skip
                            visits++; 
                            continue; 
                } 
                i1=tI[inf[0]-1][0];
                i2=tI[inf[i]-1][0];
                if (establishCompatibility(i1, i2, optimizationRequested, "")==0) { visits++; continue; }  // already played? no good colours? skip.
                sup2[nTeams-n+1]=inf[i];                      // add second element of inf[]
                k=0;                                          // now, go to copy inf[] without the two elements added to sup[] ...
                for (j=1;j<n; j++)                            // ... in this way S1 and S2 are mixed
                    if (j!=i) inf2[k++]=inf[j];               
                if (solveColumnAllparanoic(n-2, sup2, inf2)) return true;   // recurse into
            }
            if (n==2 || n==deltaColour) return false;   // not the case ...
            if (alreadyExplored.indexOf("-"+inf[0]+"-")>=0) ;  // a cycled element returns. assume completed
            else {
                alreadyExplored += "-"+inf[0]+"-"; // store cycled element 
                inf2=new int[n];
                k=0;                                        // push the first element of inf[] at the bottom
                for (j=1; j<n; j++) {                       
                    inf2[k++]=inf[j]; 
                }
                inf2[k++]=inf[0]; 
                return solveColumnAllparanoic(n, sup, inf2);   // retry
            }
            if (n<nTeams) return false;   // further processing only at the root level !!
            // all tree explored and no solution was found! 
            deltaColour += 2;             // try to allow two more elements have colour changed ...
            if (deltaColour>nTeams) return false; // ... but if all possibilites expired then abort!
            alreadyExplored = "";
            return solveColumnAllparanoic(n, sup, inf);   // recurse into
        }
    }

    private boolean calculateBergerTable(int r, int n) {
        // accepts r = round number and n = number of teams
        // sets resulting array into tempIndex[], then to pair take 0-(n-1), 1-(n-2), 2-(n-3) ... (n/2-1)-(n/2)
        int i, j, nr=0;
        short first;
        for (i=0; i<n; i++) {
            tempIndex[i]=(short)(i+1);  // populate array as 1.st round
        }
        if (r==1) {                             // 1.st round? 
            if (addedRows<n) tempIndex[n-1]=0;  // odd? last is always BYE
            return true;                        // finished!
        }
        // next rounds are interleaved, so calculate how many rotations are needed
        if (r%2 == 0) nr=n/2+r/2-1;     // 2.nd half -> interleave 1.st half
        else nr=r/2;                    // 1.st half 
        for (j=0; j<nr; j++) {
            first=tempIndex[0];
            for (i=0; i<n-2; i++) {         // rotate but let the last one to stay
                tempIndex[i]=tempIndex[i+1];  
            }
            tempIndex[n-2]=first;
        }
        if (addedRows<n) tempIndex[n-1]=0;  // odd? last is always BYE
        else if (r%2 == 0) {         // if even switch first and last elements
            first=tempIndex[0];
            tempIndex[0]=tempIndex[n-1];
            tempIndex[n-1]=first;
        }
        return true;
    }
    
    private int calculateAROindividual(int i0, short lboard, short uboard) {
        // calculate Average Rating Opponent for individual players (also in a team tournament)
        int ARO=0, n=0, round,j,i1,i2,k,team,board,p1,p2;
        boolean played;
        String s;
        String [] S;
        team = i0/maxPlayersPerTeam +1;
        k = i0 % maxPlayersPerTeam +1;
        for (round=0; round<currRound; round++) {
                    played=false;
                    for (j=0; j<addedPairs; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0 || i2==0) continue;           // empty row or forfeit
                        if (team!=i1 && team!=i2) continue;     // not right pair
                        for (board=lboard; board<=uboard; board++) {
                            s = roundsDetail[j][board][round];
                            S = s.split("-");
                            if (S.length != 4) continue;
                            p1 = Integer.valueOf(S[0]);
                            p2 = Integer.valueOf(S[1]);
                            if (p1==0 || p2==0 || S[3].indexOf('f')>0) continue;  // forfeit? do not consider!
                            if (team==i1 && k==p1) {
                                ARO += getElo(i2, p2);
                                n++;
                                played=true;
                                break;
                            } else if (team==i2 && k==p2) {
                                ARO += getElo(i1, p1);
                                n++;
                                played=true;
                                break;
                            }
                        }
                        if (played) break;
                    }
                    if (!played) {
                                ARO += getElo(team, k); // not played for any reason = sum itself Elo
                                n++;
                    }
        }
        if (n>0) ARO = (int)Math.floor(1f*ARO/n);
        return ARO;
    }

    private int calculateAROteam(int team, int delta) {
        // calculate Average Rating Opponent for teams
        int ARO=0, n=0, round,j,i1,i2;
        boolean played;
        String s;
        String [] S;
        for (round=0; round<currRound+delta; round++) {
                    played=false;
                    for (j=0; j<addedRows/2; j++) {
                        s = roundsDetail[j][0][round];
                        S = s.split("-");
                        if (S.length != 4) continue;
                        i1 = Integer.valueOf(S[0]);
                        i2 = Integer.valueOf(S[1]);
                        if (i1==0 || i2==0) continue;           // empty row or forfeit
                        if (team!=i1 && team!=i2) continue;     // not right pair
                        if (S[3].indexOf('f')>0) break;         // forfeit? do not consider!
                        if (team==i1) {
                                ARO += getElo(i2, 0);
                                n++;
                                played=true;
                                break;
                        } else if (team==i2) {
                                ARO += getElo(i1, 0);
                                n++;
                                played=true;
                                break;
                        }
                    }
                    if (!played) {
                                ARO += getElo(team, 0); // not played for any reason = sum itself Elo
                                n++;
                    }
        }
        if (n>0) ARO = (int)Math.floor(1f*ARO/n);
        return ARO;
    }
    
    private int getElo(int team, int element) {
        // gets the Elo from the storage table
        TableModel myTM=jTable1.getModel();
        String player, playerM, EloString;
        String [] S;
        int Elo=0;
        try {
            if (element==0) EloString = (""+myTM.getValueAt(sortIndex[team-1]-1,1)).trim();
            else {
                player = ""+myTM.getValueAt(sortIndex[team-1]-1,element+1);
                playerM = minimalPlayerData(player);
                S=playerM.split(";");
                EloString=S[3].replaceAll("\\D","");  // strip non digit and convert to integer
            }
            Elo=Integer.valueOf(EloString); 
        } catch (NumberFormatException ex) {}
          catch (ArrayIndexOutOfBoundsException ex) {}
          catch (IndexOutOfBoundsException ex) {}
        return Elo;
    }
    
    private boolean allowedUpfloaterB5(int ID) {
    // check if this candidate may be upfloated
        if (!optimizationRequested) return true;
        String upfl = ";"+ID+";";
        if (currRound>=2)
            if (upfloaters[currRound-2].indexOf(upfl)>=0) return false; // not two times in a run. B5 rule
        return true;
    }
  
    private boolean allowedDownfloaterB5(int ID) {
    // check if this candidate may be downfloated
        if (!optimizationRequested) return true;
        String downfl = ";"+ID+";";
        if (currRound>=2)
            if (downfloaters[currRound-2].indexOf(downfl)>=0) return false; // not two times in a run. B5 rule
        return true;
    }
    
    private boolean allowedUpfloaterB6(int ID) {
    // check if this candidate may be upfloated
        if (!optimizationRequested) return true;
        String upfl = ";"+ID+";";
        if (currRound>=3) 
            if (upfloaters[currRound-3].indexOf(upfl)>=0) return false; // not as two rounds before. B6 rule
        return true;
    }
  
    private boolean allowedDownfloaterB6(int ID) {
    // check if this candidate may be downfloated
        if (!optimizationRequested) return true;
        String downfl = ";"+ID+";";
        if (currRound>=3) 
                if (downfloaters[currRound-3].indexOf(downfl)>=0) return false; // not as two rounds before. B6 rule
        return true;
    }
   
    private boolean allowedUpfloater(int ID) {
    // check if this candidate may be upfloated
        if (!optimizationRequested) return true;
        String upfl = ";"+ID+";";
        int r, nup=1, totRounds;
        totRounds=Integer.valueOf(rounds);
        if ((maxRound % totRounds)==0) return true; // last round=OK
        if (currRound>=2)
            if (upfloaters[currRound-2].indexOf(upfl)>=0) return false; // not two times in a run. B5 rule
        if (currRound>=3) {
            if (tournamentType==1) {    // rule for Dubov
                for (r=0; r<currRound-1; r++) 
                    if (upfloaters[r].indexOf(upfl)>=0) nup++;
                if (totRounds<10 && nup>3) return false;   // not so many times in a tournament!
                if (totRounds>9 && nup>4) return false;
            } else                      // rule for swiss based on rating
                if (upfloaters[currRound-3].indexOf(upfl)>=0) return false; // not as two rounds before. B6 rule
        }
        return true;
    }

    private void addExpl(String str1, int numb, String str2) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        jTextArea1.append(str1+" "+numb+" "+str2+"\n");
        goBottom(jTextArea1);
    }
   
    private void addExpl(String str1, String str2, String str3) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        jTextArea1.append(str1+" "+str2+" "+str3+"\n");
        goBottom(jTextArea1);
    }
   
    private void addExpl(String str1, String str2, String str3, String str4, String str5) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        if (!str4.equals("")) str4=Main.localizedText(str4);
        if (!str5.equals("")) str5=Main.localizedText(str5);
        jTextArea1.append(str1+" "+str2+" "+str3+" "+str4+" "+str5+"\n");
        goBottom(jTextArea1);
    }
   
    private void addExpl(String str1, String str2, String str3, String str4) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        if (!str4.equals("")) str4=Main.localizedText(str4);
        jTextArea1.append(str1+" "+str2+" "+str3+" "+str4+"\n");
        goBottom(jTextArea1);
    }
    
    private void addExpl(String str1, String str2, String str3, float numb1) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        jTextArea1.append(str1+" "+str2+" "+str3+" "+numb1+"\n");
        goBottom(jTextArea1);
    }

    private void addExpl(String str1, int numb1, String str2, int numb2) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        jTextArea1.append(str1+" "+numb1+" "+str2+" "+numb2+"\n");
        goBottom(jTextArea1);
    }

    private void addExpl(String str1, int numb1, String str2, float numb2, String str3, int numb3) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        jTextArea1.append(str1+" "+numb1+" "+str2+" "+numb2+" "+str3+" "+numb3+"\n");
        goBottom(jTextArea1);
    }

    private void addExpl(String str1, int numb1, String str2, String str3, String str4, String str5) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        if (!str4.equals("")) str4=Main.localizedText(str4);
        if (!str5.equals("")) str5=Main.localizedText(str5);
        jTextArea1.append(str1+" "+numb1+" "+str2+" "+str3+" "+str4+" "+str5+"\n");
        goBottom(jTextArea1);
    }

    private void addExpl(String str1, String str2, String str3, String str4, String str5, String str6) {
        // interface to output explanations
        if (!str1.equals("")) str1=Main.localizedText(str1);
        if (!str2.equals("")) str2=Main.localizedText(str2);
        if (!str3.equals("")) str3=Main.localizedText(str3);
        if (!str4.equals("")) str4=Main.localizedText(str4);
        if (!str5.equals("")) str5=Main.localizedText(str5);
        if (!str6.equals("")) str6=Main.localizedText(str6);
        jTextArea1.append(str1+" "+str2+" "+str3+" "+str4+" "+str5+str6+"\n");
        goBottom(jTextArea1);
    }

    private void goBottom(javax.swing.JTextArea TA) {
        // go bottom of the control by emulating <ctrl><end>
        KeyEvent evtCtrlEndPressed = new KeyEvent(TA, KeyEvent.KEY_PRESSED, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_END, '\0');
        KeyEvent evtCtrlEndReleased = new KeyEvent(TA, KeyEvent.KEY_RELEASED, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_END, '\0');
        TA.dispatchEvent(evtCtrlEndPressed);
        TA.dispatchEvent(evtCtrlEndReleased);
    }
    
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // OPEN icon has been clicked ...
        int i1, i2, pair, i, j, k, r;
        String line, result, code, colour, sex, birth, ID, title;
        if (engineRunning) return;
        if (!safeExitAllowed) {   // data could be lost!!
            Object[] options = {Main.localizedText("Yes, save it"), Main.localizedText("No, don't worry"), Main.localizedText("do nothing, stay live")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Warning! There is unsaved data. Want to save it ?"),
                Main.localizedText("What to do ?"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);
            switch (n) {
                case JOptionPane.YES_OPTION:  jButton11ActionPerformed(evt); return;
                case JOptionPane.NO_OPTION:   break;
                case JOptionPane.CANCEL_OPTION: return;
            }            
        }
        // read data from disc. Open File dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(Main.localizedText("JavaPairing Files (.txt)"), "txt");
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(currentDirectory);
        chooser.setSelectedFile(selectedFile);
        JPanel accessoryPanel = new JPanel(new GridLayout(3, 1));
        accessoryPanel.setBorder(BorderFactory.createTitledBorder(Main.localizedText("options")));
        JCheckBox checkbox = new JCheckBox(Main.localizedText("merge data"), false);
        checkbox.setToolTipText(Main.localizedText("check to merge players from a tournament file with in memory data"));
        checkbox.setEnabled(maxRound==0 && selectedFile!=null);
        accessoryPanel.add(checkbox);
        JCheckBox checkbox2 = new JCheckBox(Main.localizedText("FIDE"), false);
        checkbox2.setToolTipText(Main.localizedText("check to open (import) a FIDE report"));
        checkbox2.setEnabled(true);
        accessoryPanel.add(checkbox2);
        JCheckBox checkbox3 = new JCheckBox(Main.localizedText("check"), false);
        checkbox3.setToolTipText(Main.localizedText("check to test a FIDE report"));
        checkbox3.setEnabled(true);
        accessoryPanel.add(checkbox3);
        chooser.setAccessory(accessoryPanel);
        chooser.setDialogTitle("JavaPairing. "+Main.localizedText("open tournament"));
        int returnVal=0;
        if (!checkerRunning) {
            returnVal = chooser.showDialog(this, Main.localizedText("open tournament"));
        }
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            boolean merge=false;
            boolean FIDE=false;
            File currentDirectory2 = null;
            File selectedFile2 = null;
            if (checkerRunning) {
                FIDE=true;
                selectedFile2 = new File(FileToBeChecked);
            } else {
                merge=checkbox.isSelected();
                FIDE=checkbox2.isSelected();
                checkerRunning=checkbox3.isSelected();
                currentDirectory2 = chooser.getCurrentDirectory();
                selectedFile2 = chooser.getSelectedFile();
            }
            try {
                // read disc file 
                BufferedReader in = new BufferedReader(new FileReader(selectedFile2));
                if (!in.ready()) throw new IOException();
                if (merge) {
                    // skip setup data
                    in.readLine();
                    in.readLine();
                    in.readLine();
                    in.readLine();
                    in.readLine();
                    int maxBoards2=0, maxPlayersPerTeam2=0;
                    try {
                        in.readLine();
                        in.readLine();
                        in.readLine();
                        in.readLine();
                        maxBoards2 = parseInt(in.readLine());
                        maxPlayersPerTeam2 = parseInt(in.readLine());
                    } catch (NumberFormatException ex) {} 
                    if (maxBoards2<1 || maxBoards2>MAXBOARDS || maxPlayersPerTeam2<1 || maxPlayersPerTeam2>MAXPLAYERSPERTEAM) {
                        Object[] options = {Main.localizedText("OK")};
                        int n = JOptionPane.showOptionDialog(this,
                            Main.localizedText("Warning! This seems not to be a JavaPairing saved tournament!"),
                            Main.localizedText("wrong selection ?"),
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]); 
                        in.close();
                        abort=true;
                        jButton14ActionPerformed(null);  // quit the program ... maybe the best solution ?
                        return;
                    }
                    in.readLine();         // skip tie break criteria
                    int addedRows2 = parseInt(in.readLine());   // number of Teams
                    in.readLine();    // skip number of rounds
                    // read Teams & Players data
                    
                    TableModel myTM = jTable1.getModel();
                    for (r=0; r<MAXROUNDS; r++) {
                        upfloaters[r]="upfloaters of round "+(r+1)+" ;";
                        downfloaters[r]="downfloaters of round "+(r+1)+" ;";
                        for (i=0; i<MAXPAIRS; i++) 
                            for (j=0; j<=MAXBOARDS; j++) roundsDetail[i][j][r]="0-0-0-0";
                    }
                    for (i=addedRows;i<MAXTEAMS;i++) {
                        sortIndex[i]=0;
                        tempIndex[i]=(short)i;
                        teamScores[i][2] = 0;       // reset acceleration
                        for (j=0;j<MAXPLAYERSPERTEAM+2;j++) myTM.setValueAt("",i,j);  // empty table
                    }
                    sortAscending = false;
                    lastSortedCol = -1;
                    addedPairs = 0;
                    if (maxPlayersPerTeam>1 || maxPlayersPerTeam2==1) { // Team->Team; individual->individual; individual->Team
                        for (i=addedRows;i<addedRows+addedRows2;i++) {
                            if ((line = in.readLine()) == null) return;   // error !
                            if (!(line.equals("null") || line.equals(""))) { 
                                sortIndex[i]=(short)(i+1);
                                myTM.setValueAt(in.readLine(),i,0);     // read Team name
                                int np=parseInt(in.readLine());          // number of Players
                                String s, S[], Elom=""; int Elop=0; int n=0;
                                for (j=0;j<np;j++) {
                                    s=in.readLine();
                                    try {
                                        myTM.setValueAt(s,i,j+2);
                                    } catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                    try {
                                        S=s.split(";");
                                        Elop=0;
                                        try {
                                            Elop=Integer.valueOf(S[6]); // first consider FIDE rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        try {
                                            if (Elop==0) Elop=Integer.valueOf(S[8]); // second National rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        if (Elop>0) {Elom+=Elop+";"; n++;}
                                    } catch (NumberFormatException ex) {
                                        //
                                    }
                                }
                                if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),i,1);    // mean Elo
                            }
                        }
                        addedRows+=addedRows2;
                    } else {                        // convert Team->individual
                        i=addedRows;
                        for (k=0;k<addedRows2;k++) {
                            if ((line = in.readLine()) == null) return;   // error !
                            if (!(line.equals("null") || line.equals(""))) { 
                                in.readLine();     // skip Team name
                                int np=parseInt(in.readLine());          // number of Players
                                String s, S[]; int Elop=0; int n=0;
                                for (j=0;j<np;j++) {
                                    s=in.readLine();
                                    S=s.split(";");
                                    try {
                                        myTM.setValueAt(""+S[0],i,0);
                                        myTM.setValueAt(s,i,2);
                                    } catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                    try {
                                        Elop=0;
                                        try {
                                            Elop=Integer.valueOf(S[6]); // first consider FIDE rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        try {
                                            if (Elop==0) Elop=Integer.valueOf(S[8]); // second National rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        myTM.setValueAt(""+Elop,i,1);    //  Elo
                                    } catch (NumberFormatException ex) {
                                        //
                                    }
                                    sortIndex[i]=(short)(i+1); i++;
                                }
                            }
                        }
                        addedRows=(short)i;
                    }
                    missingResults=false;
                    safeExitAllowed=false;
                } else {
                    TableModel myTM = jTable1.getModel();
                    for (r=0; r<MAXROUNDS; r++) {
                            upfloaters[r]="upfloaters of round "+(r+1)+" ;";
                            downfloaters[r]="downfloaters of round "+(r+1)+" ;";
                            for (i=0; i<MAXPAIRS; i++) 
                                for (j=0; j<=MAXBOARDS; j++) roundsDetail[i][j][r]="0-0-0-0";
                    }
                    for (i=0;i<MAXTEAMS;i++) {
                            sortIndex[i]=0;
                            tempIndex[i]=(short)i;
                            teamScores[i][2] = 0;       // reset acceleration
                            for (j=0;j<MAXPLAYERSPERTEAM+2;j++) myTM.setValueAt("",i,j);  // empty table
                    }
                    sortAscending = false;
                    lastSortedCol = -1;
                    currentDirectory=currentDirectory2;
                    selectedFile=selectedFile2;
                    openedFile = selectedFile.getPath().toLowerCase();
                    // read setup data
                    tournamentName = in.readLine().trim();
                    if (FIDE && tournamentName.startsWith("012")) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        try {
                            tournamentName=tournamentName.substring(4).trim();  // read (import) FIDE report, essential data
                        } catch (Exception ex) {tournamentName="";}
                        rounds = "0";
                        maxRound = tournamentType = 0;
                        short round=0;
                        tournamentPairing = teamOrder = maxBoards = maxPlayersPerTeam = 1;
                        flagsTieBreak = "126000";     // default to Buchholz cut1, Buchholz total, ARO
                        code=""; line="";
                        for (;;) {
                            if(!in.ready()) break;
                            line=in.readLine();
                            if (line.length()<4) continue;
                            code=line.substring(0, 3);
                            if (code.equals("001")) break;
                            line=line.substring(4).trim();
                            if (code.equals("022")) tournamentPlace=line;
                            if (code.equals("032")) fed=line;
                            if (code.equals("042")) tournamentDate1=line;
                            if (code.equals("052")) tournamentDate2=line;
                            if (code.equals("062")) addedRows = parseInt(line);   // number of players
                            if (code.equals("102")) tournamentArbiter=line;
                        }
                        adaptCols(jTable1);
                        adaptRows(maxPlayersPerTeam);
                        for (k=0; k<maxPlayersPerTeam+2; k++) jTable1.getColumnModel().getColumn(k).setHeaderRenderer(null);
                        i=0;
                        for (;code.equals("001");){ // read players and rounds data
                            sortIndex[i]=(short)(i+1);
                            try {
                                sortIndex[i]=Integer.valueOf(line.substring(4, 8).trim()).shortValue();
                            } catch (StringIndexOutOfBoundsException ex) {}
                              catch (NumberFormatException ex) {}
                            i1=sortIndex[i];
                            sex=title=fed=ID=birth="";
                            if (line.length()>9) 
                                sex=line.substring(9, Math.min(line.length(), 10)).trim();     // read sex
                            if (line.length()>10) 
                                title=line.substring(10, Math.min(line.length(), 13)).trim();     // read title
                            if (line.length()>14) 
                                myTM.setValueAt(line.substring(14, Math.min(line.length(), 47)).trim(),i,0);     // read player name
                            int Elo=0;
                            try {
                                Elo=Integer.valueOf(line.substring(48, 52)); // FIDE rating
                            } catch (StringIndexOutOfBoundsException ex) {}
                              catch (NumberFormatException ex) {}
                            myTM.setValueAt(""+Elo,i,1);
                            if (line.length()>53) 
                                fed=line.substring(53, Math.min(line.length(), 56)).trim();     // read fed
                            if (line.length()>57) 
                                ID=line.substring(57, Math.min(line.length(), 68)).trim();     // read ID
                            if (line.length()>69) 
                                birth=line.substring(69, Math.min(line.length(), 79)).trim();     // read birth
                            myTM.setValueAt(""+myTM.getValueAt(i,0)+";"+fed+";"+birth+";"+sex+";"+title+";"+ID+";"+myTM.getValueAt(i,1)+";;;;",i,2);
                            round=0;
                            try {
                              for (k=91;;k+=10) {
                                if (k>line.length()) break;             // end loop
                                code=line.substring(k, k+8);
                                if (code.trim().equals("")) break;      // end loop
                                if (code.trim().equals("-")) {round++; continue;} // not paired
                                pair=0; i2=0; result=""; colour=""; boolean bye=false;
                                try {
                                    i2=Integer.valueOf(code.substring(0, 4).trim()); // ID opponent
                                } catch (NumberFormatException ex) {}
                                colour=code.substring(5, 6); result=code.substring(7, 8);
                                if (i2==0) {    // bye ?
                                    if (result.equals("1") || result.equals("+")) result="1";
                                    else if (result.equals("0") || result.equals("-")) result="0";
                                    else if (result.equals("=") ) result="\u00bd";
                                    else  result="0";
                                    boolean found=false; 
                                    if (result.equals("1")) {    // win forfeit
                                        String S[]=null;
                                        for (pair=0; pair<MAXPAIRS; pair++) {   // look for first free pair or win/draw forfeit
                                            if (roundsDetail[pair][0][round].equals("0-0-0-0")) break;
                                            S=roundsDetail[pair][0][round].split("-");
                                            if (S[1].equals("0") && (S[2].equals("1") || S[2].equals("\u00bd")) && S[3].equals("0f") ) { found=true; break; }
                                        }
                                        if (found) { 
                                            roundsDetail[pair][0][round] =""+S[0]+"-"+i1+"-"+S[2]+"-"+result+"f";
                                            roundsDetail[pair][1][round] = "1-1-"+S[2]+"-"+result+"f";
                                        } else {
                                            roundsDetail[pair][0][round] =""+i1+"-"+"0-"+result+"-0f";
                                            roundsDetail[pair][1][round] = "1-0-"+result+"-0f";
                                        }
                                    }
                                    else if (result.equals("\u00bd")) {    // draw forfeit
                                        String S[]=null;
                                        for (pair=0; pair<MAXPAIRS; pair++) {   // look for first free pair or win/draw forfeit
                                            if (roundsDetail[pair][0][round].equals("0-0-0-0")) break;
                                            S=roundsDetail[pair][0][round].split("-");
                                            if (S[1].equals("0") && (S[2].equals("1") || S[2].equals("\u00bd")) && S[3].equals("0f") ) { found=true; break; }
                                        }
                                        if (found) { 
                                            roundsDetail[pair][0][round] =""+S[0]+"-"+i1+"-"+S[2]+"-"+result+"f";
                                            roundsDetail[pair][1][round] = "1-1-"+S[2]+"-"+result+"f";
                                        } else {
                                            roundsDetail[pair][0][round] =""+i1+"-"+"0-"+result+"-0f";
                                            roundsDetail[pair][1][round] = "1-0-"+result+"-0f";
                                        }
                                    } else ;    // loss forfeit. do nothing
                                } else {        // regular game of forfeit
                                    boolean found=false; String S[]=null;
                                    for (pair=0; pair<MAXPAIRS; pair++) {   // look for the pair
                                        if (roundsDetail[pair][0][round].equals("0-0-0-0")) break;
                                        S = roundsDetail[pair][0][round].split("-");
                                        if (S[0].equals(""+i1) && S[1].equals(""+i2) ||
                                            S[0].equals(""+i2) && S[1].equals(""+i1)    ) { found=true; break; }
                                    }
                                    if (found) {
                                        S[2]=(""+S[2]).substring(0,1);
                                        if (colour.equals("-")) {   // forfeit
                                            if (result.equals("1") || result.equals("+")) result=S[2]+"-1f";
                                            else if (result.equals("0") || result.equals("-")) result=S[2]+"-0f";
                                            else if (result.equals("=") ) result=S[2]+"-\u00bdf";
                                            else  result=S[2]+"-0f";
                                            roundsDetail[pair][0][round]=""+S[0]+"-"+S[1];
                                        }
                                        else if (colour.equals("b")) {
                                            if (result.equals("1")) result=S[2]+"-1";
                                            else if (result.equals("+")) result=S[2]+"-1f";
                                            else if (result.equals("0")) result=S[2]+"-0";
                                            else if (result.equals("-")) result=S[2]+"-0f";
                                            else if (result.equals("=") ) result=S[2]+"-\u00bd";
                                            else  result=S[2]+"-0";
                                            roundsDetail[pair][0][round]=""+S[0]+"-"+S[1];
                                        }
                                        else {
                                            if (result.equals("1") || result.equals("+")) result="1-"+S[3];
                                            else if (result.equals("0") || result.equals("-")) result="0-"+S[3];
                                            else if (result.equals("=") ) result="\u00bd-"+S[3];
                                            else  result="0-"+S[3];
                                            roundsDetail[pair][0][round]=""+S[0]+"-"+S[1];
                                        }
                                        roundsDetail[pair][0][round] += "-"+result;
                                        roundsDetail[pair][1][round] = "1-1-"+result;
                                    } else {
                                        if (colour.equals("-")) {   // forfeit
                                            if (result.equals("1") || result.equals("+")) result="1-0f";
                                            else if (result.equals("0") || result.equals("-")) result="0-1f";
                                            else if (result.equals("=") ) result="\u00bd-\u00bdf";
                                            else  result="0-0f";
                                            roundsDetail[pair][0][round]=""+i1+"-"+i2;
                                        }
                                        else if (colour.equals("b")) {
                                            if (result.equals("1")) result="0-1";
                                            else if (result.equals("+")) result="0-1f";
                                            else if (result.equals("0")) result="1-0";
                                            else if (result.equals("-")) result="1-0f";
                                            else if (result.equals("=") ) result="\u00bd-\u00bd";
                                            else  result="0-0";
                                            roundsDetail[pair][0][round]=""+i2+"-"+i1;
                                        }
                                        else {
                                            if (result.equals("1")) result="1-0";
                                            else if (result.equals("+")) result="1-0f";
                                            else if (result.equals("0")) result="0-1";
                                            else if (result.equals("-")) result="0-1f";
                                            else if (result.equals("=") ) result="\u00bd-\u00bd";
                                            else  result="0-0";
                                            roundsDetail[pair][0][round]=""+i1+"-"+i2;
                                        }
                                        roundsDetail[pair][0][round] += "-"+result;
                                        roundsDetail[pair][1][round] = "1-1-"+result;
                                    }
                                    
                                }
                                round++; if (round>maxRound) maxRound=round; 
                              }
                            } catch (ArrayIndexOutOfBoundsException ex) {}
                              catch (IndexOutOfBoundsException ex) {}
                            if(!in.ready()) break;
                            line=in.readLine();
                            if (line.length()<4) break;
                            code=line.substring(0, 3);
                            i++;
                        }
                        rounds=""+maxRound;
                        batchOrder=true;
                        for (currRound=1; currRound<=maxRound; currRound++) {
                            jSpinner1.setValue(currRound);
                            if (currRound>1) orderPairs();
                            updateFloaters();  
                        }
                        batchOrder=false;
                    } else {
                        this.setTitle("JavaPairing -> '"+tournamentName+"'");
                        tournamentPlace = in.readLine().trim();
                        fed = in.readLine().trim();
                        String[] tournamentDates = in.readLine().split(";");
                        tournamentDate1=""; tournamentDate2="";
                        if (tournamentDates.length>0) tournamentDate1 = tournamentDates[0].trim();
                        if (tournamentDates.length>1) tournamentDate2 = tournamentDates[1].trim();
                        tournamentArbiter = in.readLine().trim();
                        tournamentType = teamOrder =0;
                        try {
                            tournamentType = parseInt(in.readLine());
                            tournamentPairing = parseInt(in.readLine());
                            rounds = in.readLine().substring(0,4).trim();
                            teamOrder = parseInt(in.readLine());
                            maxBoards = parseInt(in.readLine());
                            maxPlayersPerTeam = parseInt(in.readLine());
                        } catch (NumberFormatException ex) {} 
                        if (tournamentType<0 || tournamentType>6 || teamOrder<1 || teamOrder>4 || 
                            maxBoards<1 || maxBoards>MAXBOARDS || maxPlayersPerTeam<1 || maxPlayersPerTeam>MAXPLAYERSPERTEAM) {
                            Object[] options = {Main.localizedText("OK")};
                            int n = JOptionPane.showOptionDialog(this,
                                Main.localizedText("Warning! This seems not to be a JavaPairing saved tournament!"),
                                Main.localizedText("wrong selection ?"),
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                options,
                                options[0]); 
                            in.close();
                            abort=true;
                            jButton14ActionPerformed(null);  // quit the program ... maybe the best solution ?
                            return;
                        }
                        adaptCols(jTable1);
                        adaptRows(maxPlayersPerTeam);
                        for (k=0; k<maxPlayersPerTeam+2; k++) jTable1.getColumnModel().getColumn(k).setHeaderRenderer(null);
                        flagsTieBreak = (in.readLine()+"000000").substring(0,6);         // string with tie break criteria
                        addedRows = parseInt(in.readLine());   // number of Teams
                        maxRound = parseInt(in.readLine());    // number of rounds
                        // read Teams & Players data
                        
                        addedPairs = 0;
                        for (i=0;i<addedRows;i++) {
                            if ((line = in.readLine()) == null) return;   // error !
                            if (!(line.equals("null") || line.equals(""))) { 
                                if (maxRound>0) sortIndex[parseInt(line)-1] = (short)(i+1);  // set sort index
                                else sortIndex[i]=(short)(i+1);
                                myTM.setValueAt(in.readLine(),i,0);     // read Team name
                                int np=parseInt(in.readLine());          // number of Players
                                String s, S[], Elom=""; int Elop=0; int n=0;
                                for (j=0;j<np;j++) {
                                    s=in.readLine();
                                    try {
                                        myTM.setValueAt(s,i,j+2);
                                    } catch (ArrayIndexOutOfBoundsException ex) {}
                                      catch (IndexOutOfBoundsException ex) {}
                                    try {
                                        S=s.split(";");
                                        Elop=0;
                                        try {
                                            Elop=Integer.valueOf(S[6]); // first consider FIDE rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        try {
                                            if (Elop==0) Elop=Integer.valueOf(S[8]); // second National rating
                                        } catch (NumberFormatException ex) {}
                                          catch (ArrayIndexOutOfBoundsException ex) {}
                                          catch (IndexOutOfBoundsException ex) {}
                                        if (Elop>0) {Elom+=Elop+";"; n++;}
                                    } catch (NumberFormatException ex) {
                                        //
                                    }
                                }
                                if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),i,1);    // mean Elo
                            }
                        }
                        missingResults=false;
                        safeExitAllowed=true;
                        if (maxRound>0) {
                            // read rounds data 
                            addedPairs=(short)((addedRows+1)/2);
                            for (r=0; r<maxRound; r++) {   // for each round
                                if ((line = in.readLine()) == null) return;  // header row. if not error !
                                for (i=0; i<addedPairs; i++) // for each pair
                                for (j=0;j<=maxBoards;j++) {
                                    if ((line = in.readLine()) == null) return;  // error !
                                    if (!(line.equals("null") || line.equals(""))) {
                                        line=line.replace("0.5","\u00bd");  // conversion of half point
                                        roundsDetail[i][j][r]=line;
                                    }
                                }
                            }
                            if (tournamentType!=5) {
                                // read floaters, except for 'round robin'
                                line = in.readLine();  // explain line
                                for (r=0; r<maxRound; r++) {   // for each round
                                    if ((line = in.readLine()) == null) break;  
                                    if (!line.trim().equals("")) {
                                        String lines[] = line.split("\\|");
                                        String line1="", line2="";
                                        if (lines.length>0) line1=lines[0];
                                        if (lines.length>1) line2=lines[1];
                                        upfloaters[r]=line1;
                                        downfloaters[r]=line2;
                                    }
                                }
                            }
                        }
                    }
                    if (tournamentName.equals("")) { 
                        tournamentName=selectedFile2.getName();
                        tournamentName=tournamentName.substring(0,tournamentName.length()-4);
                    }
                    this.setTitle("JavaPairing -> '"+tournamentName+"'");
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                in.close();
                currRound=maxRound;
                SpinnerNumberModel model=null;
                if (maxRound>0) {
                    model = new SpinnerNumberModel(maxRound, 1, maxRound, 1);
                    jTextField4.setText(""+maxRound);
                    jButton6.setEnabled(false);
                    jMenuItem2.setEnabled(false);
                    jMenuItem9.setEnabled(false);
                    jMenuItem10.setEnabled(false);
                    jMenuItem11.setEnabled(false);
                }
                else {
                    model = new SpinnerNumberModel(0, 0, 0, 1);
                    jTextField4.setText("");
                    jButton6.setEnabled(true);
                    jMenuItem2.setEnabled(true);
                    jMenuItem9.setEnabled(true);
                    jMenuItem10.setEnabled(true);
                    jMenuItem11.setEnabled(true);
                }
                jSpinner1.setModel(model);
                if (maxPlayersPerTeam==1) {
                    // individual tournament
                    // jTextField1.setEnabled(false); // disable team name field
                    jButton1.setText(Main.localizedText("Add Player"));
                    jLabel1.setEnabled(false);
                    jTextField1.setEnabled(false);
                    jButton9.setEnabled(false);      // disable 'team ranking' button
                } else {
                    // team tournament
                    // jTextField1.setEnabled(true); // enable team name field
                    jButton1.setText(Main.localizedText("Add Team"));
                    jLabel1.setEnabled(true);
                    jTextField1.setEnabled(true);
                    jButton9.setEnabled(true);      // enable 'team ranking' button
                }
//                jButton31.setEnabled(false);      // disable 'output current round' button
                jTextField1.setText("");    // blank Team name
                jTextPane1.setText("");     // blank output buffer
                jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
                if (tournamentType<2) jCheckBox1.setEnabled(false); else jCheckBox1.setEnabled(true);  // FIDE ENGINES do not modify settings
                if (tournamentType<2) jCheckBox1.setSelected(true);
                jTabbedPane1.setSelectedIndex(0);   // go to registration page!
                if (checkerRunning) {
                    EngineThread = new Thread(new checkTournament(this));   // launch the checker
                    EngineThread.start();  // run in a different thread, so it is interruptable 
                }
            } catch (IOException ex) {}
        }
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // SAVE icon has been clicked ...
        if (engineRunning) return;
        // save data to disc. open File dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            Main.localizedText("JavaPairing Files (.txt)"), "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(currentDirectory);
        chooser.setSelectedFile(selectedFile);
        chooser.setDialogTitle("JavaPairing. "+Main.localizedText("save tournament data"));
        int returnVal = chooser.showDialog(this, Main.localizedText("save tournament data"));
        int i,j,r;
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getCurrentDirectory();
            selectedFile = chooser.getSelectedFile();
            String s=selectedFile.getPath();    
            if (!s.endsWith(".txt"))  s+=".txt";   // check if file extension exists
            try {
                if (selectedFile.exists() && !s.toLowerCase().equals(openedFile)) {
                    Object[] options = {Main.localizedText("Yes"), Main.localizedText("No")};
                    int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("Overwrite")+" '"+s+"' ?",
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]);
                    if (n==1) return;
                    openedFile = s.toLowerCase();
                }
                // save to disc file 
                FileWriter fOut = new FileWriter(s);
                String lineSeparator = System.getProperty( "line.separator" );
                // write setup data
                fOut.write(tournamentName+lineSeparator);
                fOut.write(tournamentPlace+lineSeparator);
                fOut.write(fed+lineSeparator);
                fOut.write(tournamentDate1+";"+tournamentDate2+lineSeparator);
                fOut.write(tournamentArbiter+lineSeparator);
                fOut.write(""+tournamentType+"     // "+"tournament type (0=swiss Dutch, 1=swiss Dubov, 2=Swiss Simple, 3=swiss Perfect Colours, 4=Amalfi Rating, 5=round robin, 6=by hand)"+lineSeparator);
                fOut.write(""+tournamentPairing+"     // "+"tournament pairing (1=regular, 2=allow return, 3=absolutely free)"+lineSeparator);
                fOut.write(rounds+"     // "+"Rounds "+lineSeparator);     // number of Rounds to consider
                fOut.write(""+teamOrder+"     // "+"Team order criteria (1=Elo, 2= random, 3=alphabetical, 4=as entered)"+lineSeparator);
                fOut.write(""+maxBoards+"     // "+"max boards to play"+lineSeparator); 
                fOut.write(""+maxPlayersPerTeam+"     // "+"max Players per Team"+lineSeparator);
                fOut.write(flagsTieBreak+"     // "+"string with tie break criteria"+lineSeparator);
                fOut.write(""+addedRows+"     // "+"total Teams entered"+lineSeparator);     // number of Teams
                fOut.write(""+maxRound+"     // "+"total rounds yet generated"+lineSeparator);      // number of Rounds
                // write Teams & Players data
                TableModel myTM = jTable1.getModel();
                for (i=0; i<addedRows; i++)  { // scan table rows
                    fOut.write(""+(indexAt(i+1,sortIndex)+1)+"     // "+"sort index of the following Team"+lineSeparator);  // write sort index
                    fOut.write(""+myTM.getValueAt(i,0)+lineSeparator);  // write Team name
                    int n=0;
                    for (j=0; j<maxPlayersPerTeam; j++)   // scan table columns
                        if ((""+myTM.getValueAt(i,j+2)).compareTo(" ")>0) n=j+1;
                    fOut.write(""+n+"     // "+"Number of Players"+lineSeparator);  // write n
                    for (j=0; j<n; j++)   // scan table columns
                        fOut.write(""+myTM.getValueAt(i,j+2)+lineSeparator);
                }
                // write rounds data 
                for (r=0; r<maxRound; r++)  {  // for each round
                    fOut.write(""+(r+1)+"     // "+"round number. follows groups of "+(maxBoards+1)+" lines for each match (Teams & boards pairing & results)"+lineSeparator);
                    for (i=0; i<(addedRows+1)/2; i++) {  // for each pair
                        for (j=0;j<=maxBoards;j++)      // conversion of half point
                            fOut.write(roundsDetail[i][j][r].replace("\u00bd", "0.5")+lineSeparator);
                    }
                }
                if (tournamentType!=5) {
                    // write down floaters, except for 'round robin'
                    fOut.write("// for each round, floater set follows."+lineSeparator);
                    for (r=0; r<maxRound; r++)  {  // for each round
                        fOut.write(upfloaters[r]+"|"+downfloaters[r]+lineSeparator);
                    }
                }
                fOut.close();
                safeExitAllowed = true;
                Object[] options = {Main.localizedText("OK")};
                int n = JOptionPane.showOptionDialog(this,
                        Main.localizedText("done."),
                        Main.localizedText("Info"),
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

            } catch (IOException ex) {
                
            }
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // SETUP icon has been clicked ...
        if (engineRunning) return;
        // open tournament data window
        int index;
        String s;
        char c;
        jTextField8.setText(tournamentName);
        jTextField9.setText(tournamentPlace);
        jTextField5.setText(fed);
        jTextField10.setText(tournamentDate1);
        jTextField12.setText(tournamentDate2);
        jTextField11.setText(tournamentArbiter);
        jList1.setSelectedIndex(tournamentType);
        switch (teamOrder) {
            case 1: jRadioButton1.setSelected(true); break;
            case 2: jRadioButton2.setSelected(true); break;
            case 3: jRadioButton3.setSelected(true); break;
            case 4: jRadioButton4.setSelected(true); break;
        }
        jTextField6.setText(rounds);
        switch (tournamentPairing) {
            case 1: jRadioButton5.setSelected(true); break;
            case 2: jRadioButton6.setSelected(true); break;
            case 3: jRadioButton7.setSelected(true); break;
        }
        jTextField7.setText(""+maxBoards);
        jTextField13.setText(""+maxPlayersPerTeam);
        try {
            s=flagsTieBreak.substring(0,1); 
            index=Integer.parseInt(s, 16);
            jComboBox1.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        try {
            s=flagsTieBreak.substring(1,2); 
            index=Integer.parseInt(s, 16);
            jComboBox2.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        try {
            s=flagsTieBreak.substring(2,3); 
            index=Integer.parseInt(s, 16);
            jComboBox3.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        try {
            s=flagsTieBreak.substring(3,4); 
            index=Integer.parseInt(s, 16);
            jComboBox4.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        try {
            s=flagsTieBreak.substring(4,5); 
            index=Integer.parseInt(s, 16);
            jComboBox5.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        try {
            s=flagsTieBreak.substring(5,6); 
            index=Integer.parseInt(s, 16);
            jComboBox6.setSelectedIndex(index);
        }    
        catch (NullPointerException ex) {}
        catch (StringIndexOutOfBoundsException ex) {}
        catch (NumberFormatException ex) {}
        jDialog2.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // DISCARD CHANGES button has been clicked ...
        // discard tournament data
        jDialog2.dispose();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // SAVE CHANGES button has been clicked ...
        if (engineRunning) return;
        // save tournament data
        int index, n;
        String s;
        tournamentName = jTextField8.getText();
        this.setTitle("JavaPairing -> '"+tournamentName+"'");
        tournamentPlace = jTextField9.getText();
        fed = jTextField5.getText();
        tournamentDate1 = jTextField10.getText();
        tournamentDate2 = jTextField12.getText();
        tournamentArbiter = jTextField11.getText();
        tournamentType = (short)jList1.getSelectedIndex();
        if (tournamentType<2) jCheckBox1.setEnabled(false); else jCheckBox1.setEnabled(true);  // FIDE ENGINES do not modify settings
        if (tournamentType<2) jCheckBox1.setSelected(true);
        if (jRadioButton5.isSelected()) tournamentPairing=1;
        if (jRadioButton6.isSelected()) tournamentPairing=2;
        if (jRadioButton7.isSelected()) tournamentPairing=3;
        allowAcceleration=false;
        if (jCheckBox9.isSelected()) allowAcceleration=true;
        rounds = jTextField6.getText(); n=0;
        try {
            n=Integer.valueOf(rounds);
        } catch (NumberFormatException ex) {}
        if (n>MAXROUNDS) n=MAXROUNDS;
        jTextField6.setText(""+n); rounds = jTextField6.getText();
        if (jRadioButton1.isSelected()) teamOrder=1;
        if (jRadioButton2.isSelected()) teamOrder=2;
        if (jRadioButton3.isSelected()) teamOrder=3;
        if (jRadioButton4.isSelected()) teamOrder=4;
        maxBoards = parseInt(jTextField7.getText());
        maxPlayersPerTeam = parseInt(jTextField13.getText());
        if (maxBoards<1) maxBoards=1;
        if (maxPlayersPerTeam<1) maxPlayersPerTeam=1;
        if (maxBoards>MAXBOARDS) maxBoards=MAXBOARDS;
        if (maxPlayersPerTeam>MAXPLAYERSPERTEAM) maxPlayersPerTeam=MAXPLAYERSPERTEAM;
        if (maxBoards>maxPlayersPerTeam) maxBoards=maxPlayersPerTeam;
        adaptCols(jTable1);
        adaptRows(maxPlayersPerTeam);
        if (maxPlayersPerTeam==1) {
            // individual tournament
            // jTextField1.setEnabled(false); // disable team name field
            jButton1.setText(Main.localizedText("Add Player"));
            jLabel1.setEnabled(false);
            jTextField1.setEnabled(false); 
            jButton9.setEnabled(false);      // disable 'team ranking' button
        } else {
            // team tournament
            // jTextField1.setEnabled(true); // enable team name field
            jButton1.setText(Main.localizedText("Add Team"));
            jLabel1.setEnabled(true);
            jTextField1.setEnabled(true);
            jButton9.setEnabled(true);      // enable 'team ranking' button
        }
        flagsTieBreak = "";
        index = jComboBox1.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        index = jComboBox2.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        index = jComboBox3.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        index = jComboBox4.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        index = jComboBox5.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        index = jComboBox6.getSelectedIndex(); s=Integer.toHexString(index); flagsTieBreak+=s.trim();
        safeExitAllowed = false;
        jDialog2.dispose();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // QUIT icon has been clicked ...
        WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        this.processWindowEvent(windowClosing);

    }//GEN-LAST:event_jButton14ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // this will be called by the dispatcher on closing the main window ...
        if (engineRunning) {
            EngineThread.interrupt();
            engineRunning=false;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        // check if there is data to save before exiting, and ask the operator
        if (safeExitAllowed) {
            if (jDialog4.isVisible()) jDialog4.dispose();
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.dispose(); System.exit(0);
        }
        else {
            // data could be lost!! ASK
            Object[] options = {Main.localizedText("Yes, save it"), Main.localizedText("No, don't worry"), Main.localizedText("do nothing, stay live")};
            int n = JOptionPane.showOptionDialog(this,
                Main.localizedText("Warning! There is unsaved data. Want to save it ?"),
                Main.localizedText("What to do ?"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);
            switch (n) {
                case JOptionPane.YES_OPTION:    // go to save data ...
                    jButton11ActionPerformed(null);
                    if (!safeExitAllowed) return; // if not really saved ... do nothing; else go next case
                case JOptionPane.NO_OPTION:     // unconditionally close window
                    safeExitAllowed = true;
                    if (jDialog4.isVisible()) jDialog4.dispose();
                    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    this.dispose(); System.exit(0);
                    return;
                case JOptionPane.CANCEL_OPTION:   // do nothing
                        return;
            }            
        }        
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        jButton6ActionPerformed(null);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        jButton5ActionPerformed(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // remove selected Team from table 
        editingRow = (short)jTable1.getSelectedRow();
        if (editingRow>=addedRows) return;   // do nothing beyond added rows
        if (maxRound>0) return;             // do nothing after 1.st round!! 
        TableModel myTM = jTable1.getModel();
        addedRows--;
        for (int i=editingRow; i<addedRows; i++)        // shift up all following teams
        for (int j=0; j<maxPlayersPerTeam+2; j++)
            myTM.setValueAt(myTM.getValueAt(i+1,j),i,j);
        for (int j=0; j<maxPlayersPerTeam+2; j++)
            myTM.setValueAt("",addedRows,j);
        safeExitAllowed = false;
        jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
        if (maxPlayersPerTeam>1) jTextField1.requestFocus(); else jTextField3.requestFocus();     
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // edit selected Team
        jButton2ActionPerformed(evt);       // reset data entry
        String[] S; int k;
        editingRow = (short)jTable1.getSelectedRow();
        if (editingRow>=addedRows) return;   // do nothing beyond added rows
        TableModel myTM = jTable1.getModel();
        TableModel myTM6 = jTable6.getModel();
        jTextField1.setText(""+myTM.getValueAt(editingRow,0));
        for (int j=0; j<maxPlayersPerTeam; j++) {
            S=(""+myTM.getValueAt(editingRow,j+2)).split(";");
            for (k=0; k<S.length; k++) myTM6.setValueAt(S[k], j, k);
        }
        if (maxPlayersPerTeam>1) jTextField1.requestFocus(); else jTextField3.requestFocus();
        jButton1.setEnabled(false);
        jButton3.setEnabled(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // replace Team with edited data
        String s="", Elom=""; int Elop; int n=0, k;
        TableModel myTM = jTable1.getModel();
        TableModel myTM6 = jTable6.getModel();
        TableCellEditor t = jTable6.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        for (int j=0; j<maxPlayersPerTeam; j++) {
            s="";
            for (k=0; k<10; k++) s=s+(""+myTM6.getValueAt(j,k)).trim()+";";
            if (s.compareTo(";;;;;;;;;;")!=0) myTM.setValueAt(s,editingRow,j+2);
            else myTM.setValueAt("",editingRow,j+2);
            Elop=0;
            try {
                s=""+myTM6.getValueAt(j,6); // Elo FIDE
                Elop=Integer.valueOf(s);
            } catch (NumberFormatException ex) {}
            try {
                s=""+myTM6.getValueAt(j,8); // Elo NAT
                if (Elop==0) Elop=Integer.valueOf(s);
            } catch (NumberFormatException ex) {}
            if (Elop>0) {Elom+=Elop+";"; n++;}
        }
        if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),editingRow,1);    // mean Elo
        s = jTextField1.getText();                // Team name. if empty set the first player's name
        if (s.isEmpty()) s= ""+myTM6.getValueAt(0,0);
        if (maxPlayersPerTeam==1) {
            if (s.contains("(W)")) s= (""+myTM6.getValueAt(0,0)).trim()+" (W)";
            else s= ""+myTM6.getValueAt(0,0);
        }
        myTM.setValueAt(s,editingRow,0);
        safeExitAllowed = false;
        jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
        jButton2ActionPerformed(evt);
    }//GEN-LAST:event_jButton3ActionPerformed
        
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // reset data-entry fields
        int k;
        jTextField1.setText("");
        TableModel myTM6 = jTable6.getModel();
        TableCellEditor t = jTable6.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        for (int j=0; j<maxPlayersPerTeam; j++) 
            for (k=0; k<10; k++) myTM6.setValueAt("", j, k);
        jTable6.clearSelection();
        jTable6.setEditingRow(0);
        jButton1.setEnabled(true);
        jButton3.setEnabled(false);
        if (maxPlayersPerTeam>1) jTextField1.requestFocus(); else jTextField3.requestFocus();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // add Team to table
        String s="", Elom=""; int Elop; int n=0, k;
        TableModel myTM = jTable1.getModel();
        TableModel myTM6 = jTable6.getModel();
        TableCellEditor t = jTable6.getCellEditor();// complete editing if  
        if (t!=null) t.stopCellEditing();           // left in edit mode
        for (int j=0; j<maxPlayersPerTeam; j++) {
            s="";
            for (k=0; k<10; k++) s=s+(""+myTM6.getValueAt(j,k)).trim()+";";
            if (s.compareTo(";;;;;;;;;;")!=0) myTM.setValueAt(s,addedRows,j+2);
            else myTM.setValueAt("",addedRows,j+2);
            Elop=0;
            try {
                s=""+myTM6.getValueAt(j,6); // Elo FIDE
                Elop=Integer.valueOf(s);
            } catch (NumberFormatException ex) {}
            try {
                s=""+myTM6.getValueAt(j,8); // Elo NAT
                if (Elop==0) Elop=Integer.valueOf(s);
            } catch (NumberFormatException ex) {}
            if (j<maxBoards && Elop>0) {Elom+=Elop+";"; n++;}
        }
        if (n>0) myTM.setValueAt(calculate_mean_Elo(Elom, n),addedRows,1);    // mean Elo
        s = jTextField1.getText();               // Team name. if empty set the first player's name
        if (s.isEmpty()) s = ""+myTM6.getValueAt(0,0);
        myTM.setValueAt(s,addedRows,0);
        Rectangle rect = jTable1.getCellRect(addedRows, 0, true);    // make the row visible (scroll)
        jTable1.scrollRectToVisible(rect);
        jTable1.setRowSelectionInterval(addedRows,addedRows);       // enhance the added row
        sortIndex[addedRows]=(short)(addedRows+1);                  // assign temp values
        tempIndex[addedRows]=(short)(addedRows);
        addedRows++;
        safeExitAllowed = false;
        jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
        jButton2ActionPerformed(evt);        
    }//GEN-LAST:event_jButton1ActionPerformed

private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
// read simulation parameters:
    try {
        simulationPlayers = Integer.valueOf(jTextField57.getText());
        simulationPercDraw = Integer.valueOf(jTextField58.getText());
        simulationPercWinWhite = Integer.valueOf(jTextField59.getText());
        simulationPercWinStronger = Integer.valueOf(jTextField60.getText());
        simulationForfeit = Integer.valueOf(jTextField61.getText());
        simulationRetired = Integer.valueOf(jTextField62.getText());
    } catch (NullPointerException ex) {}
      catch (NumberFormatException ex) {}
    simulationAcceleration = jCheckBox8.isSelected();
    jDialog6.dispose();
    if (simulationPlayers<0) simulationPlayers=0;
    if (simulationPercDraw<0) simulationPercDraw=0;
    if (simulationPercWinWhite<0) simulationPercWinWhite=0;
    if (simulationPercWinStronger<0) simulationPercWinStronger=0;
    if (simulationForfeit<0) simulationForfeit=0;
    if (simulationRetired<0) simulationRetired=0;
    if (simulationPlayers>MAXTEAMS) simulationPlayers=MAXTEAMS;
    if (simulationPercDraw>100) simulationPercDraw=100;
    if (simulationPercWinWhite>100) simulationPercWinWhite=100;
    if (simulationPercWinStronger>100) simulationPercWinStronger=100;
    if (simulationRetired>simulationPlayers) simulationRetired=simulationPlayers;
}//GEN-LAST:event_jButton47ActionPerformed

private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
// an Item was selected for verbosity level (Beginner, Intermediate, Expert)
    verbosity_level=jComboBox7.getSelectedIndex();
    strong_verbose=(verbosity_level>1);  
    mild_verbose=(verbosity_level>0);
}//GEN-LAST:event_jComboBox7ActionPerformed

private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
// copy jtextarea2 to clipboard:
        StringSelection contents = new StringSelection(jTextArea2.getText());
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(contents, null);             // send text to clipboard

}//GEN-LAST:event_jButton48ActionPerformed

private void jTable5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseReleased
    // selected a pair to give results
        int i0=0,i1=0,j0=0,j1=0,j2;
        short row = 0;
        readResults();      // read previuos results
        if (evt==null) row = (short)jTable5.getSelectedRow();
        else {row = (short)jTable5.rowAtPoint(evt.getPoint()); jTable5.setRowSelectionInterval(row, row);}
        if (row<0 || row>=addedPairs) {
            jTable7.setVisible(false);
            return;
        }
        prevRow = row;      // save this as previous for next iteration
        TableModel myTM = jTable1.getModel();
        TableModel myTM5 = jTable5.getModel();
        TableModel myTM7 = jTable7.getModel();      // reset table7
        String team1 = ""+myTM5.getValueAt(row,1);
        String team2 = ""+myTM5.getValueAt(row,2);
        String [] S, s1 = team1.split("\\|");   // get first Team 
        if (s1[0].compareTo("null")==0 || s1[0].compareTo("")==0) {
            jTable7.setVisible(false);
            return;     // avoid processing blank rows
        }
        String [] s2 = team2.split("\\|");   // get second Team
        i0 = Integer.valueOf(s1[0]).intValue();  // index to first Team
        if (i0==0) {
            jTable7.setVisible(false);
            return;      // not paired ??
        }
        i1 = Integer.valueOf(s2[0]).intValue();  // index to second Team
        j0 = sortIndex[i0-1]-1;              // absolute Team position
        if (i1>0) j1 = sortIndex[i1-1]-1;              // absolute Team position
        TableColumn col0 = jTable7.getColumnModel().getColumn(0);
        TableColumn col1 = jTable7.getColumnModel().getColumn(1);
        String[] players0 = new String[maxPlayersPerTeam+1];
        String[] players1 = new String[maxPlayersPerTeam+1];
        for (int j=0;j<maxPlayersPerTeam;j++) {                     // set lists of players
            players0[j] = minimalPlayerData(""+myTM.getValueAt(j0,j+2));
            if (i1>0) players1[j] = minimalPlayerData(""+myTM.getValueAt(j1,j+2));
            else players1[j] = "BYE";
            S = players0[j].split(";");
            if (S[0].equals("")) players0[j] = "";
            S = players1[j].split(";");
            if (S[0].equals("")) players1[j] = "";
        }
        if (maxPlayersPerTeam>1) {
            players1[maxPlayersPerTeam] = players0[maxPlayersPerTeam] = ""; // left one row empty to make the game cleanable
            col0.setCellEditor(new MyComboBoxEditor(players0));             // set cell editor (only team tournament)
            col1.setCellEditor(new MyComboBoxEditor(players1));
        }
        j1=j2=0;
        for (int j=0;j<maxBoards;j++) {                     // scan boards
            myTM7.setValueAt("",j,0);
            myTM7.setValueAt("",j,1);
            myTM7.setValueAt("0-0",j,2);
            String[] s = roundsDetail[row][j+1][currRound-1].split("-");  // get the match
            if (s.length != 4) {                    // match not initialized. set default not retired
                    if (j1<maxPlayersPerTeam) for(;;) {
                        if (players0[j1].contains("(W)")) j1++;
                        else { myTM7.setValueAt(players0[j1],j,0); break; }
                    }
                    if (j2<maxPlayersPerTeam) for(;;) {
                        if (players1[j2].contains("(W)")) j2++;
                        else { myTM7.setValueAt(players1[j2],j,1); break; }
                    }
            }
            else {
                int p0 = Integer.valueOf(s[0]).intValue();  // index to first player
                int p1 = Integer.valueOf(s[1]).intValue();  // index to second player
                String result = s[2]+"-"+s[3];              // the result of the match
                if (p0==0 && p1==0 && result.equals("0-0")) {
                    // I may assume that values were not yet initialized. Set default not retired
                    if (j1<maxPlayersPerTeam) for(;;) {
                        if (players0[j1].contains("(W)")) j1++;
                        else { myTM7.setValueAt(players0[j1],j,0); break; }
                    }
                    if (j2<maxPlayersPerTeam) for(;;) {
                        if (players1[j2].contains("(W)")) j2++;
                        else { myTM7.setValueAt(players1[j2],j,1); break; }
                    }
                } else {
                    // put stored values (editing mode)
                    if (p0>0) myTM7.setValueAt(players0[p0-1],j,0);
                    else myTM7.setValueAt("BYE",j,0);
                    if (i1>0 && p1>0) myTM7.setValueAt(players1[p1-1],j,1);
                    else myTM7.setValueAt("BYE",j,1);
                }
                myTM7.setValueAt(result,j,2);
            }
            j1++; j2++;
        }
        jTable7.setVisible(true);
        if (maxPlayersPerTeam>1) {
            jTable7.setRowSelectionInterval(0,0);
            jTable7.setEditingRow(0);
            jTable7.requestFocusInWindow();
        } else {
            jTable7.clearSelection();
        }
}//GEN-LAST:event_jTable5MouseReleased

private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
// when a Team is selected on the left, clear and repaint on the right
        short row = (short)jTable3.getSelectedRow();
        if (row<0 || row>=addedRows) return;
        jTable4.clearSelection();
        jTable4.repaint();
}//GEN-LAST:event_jTable3MouseReleased

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // DB selection changed; reset fields
        int i, j;
        String title=jDialog1.getTitle();
        int pos=title.indexOf(" (");
        if (pos>0) title=title.substring(0,pos);
        title+=" ("+jComboBox8.getSelectedItem()+")";
        jDialog1.setTitle(title);
        TableModel TM8=jTable8.getModel();
        ListModel L2=jList2.getModel();
        String[] S=new String[12];
        for (i=0; i<12; i++) S[i]="";
        jList2.setListData(S);                
        jList2.invalidate();
        jTextField2.setText(" ");
        jTextField55.setText(" ");
        for (i=0; i<=10; i++) for (j=0; j<10; j++) TM8.setValueAt("", i, j);
        String[] S2=new String[1];
        S2[0]="";
        jList3.setListData(S2);
        jTextField3.requestFocusInWindow();
    }//GEN-LAST:event_jComboBox8ActionPerformed

public class simulateTournament implements Runnable {
    private EnterFrame theMainForm=null; 
    public simulateTournament(EnterFrame EF) {
        theMainForm = EF;
        jButton43.setEnabled(true);
    }
   // @Override
    public void run() {  
    TableModel myTM = jTable1.getModel();
    int Elo, round, Rounds, pair, games, ID1, ID2, pDraw, pWhite, pBlack, forfeited, retired, forfeitedThisRound, retiredThisRound;
    int maxForfeitAllowed, totDraw=0, totPlayed=0;
    double deltaElo, random, cutoff, forfeit=0, retire=0;
    String team;
    String S[]=null;
    long seed;
    if (engineRunning) return;
    jDialog6.setVisible(true);      // open parameters dialog (modal)
    if (simulationPlayers<2) return;
    Rounds=Integer.valueOf(rounds).shortValue();
    if (Rounds<1) return;
    jDialog7.setVisible(true);      // open log dialog 
    jTextArea2.append("\n"+"\n"+tournamentName+"\n");
    goBottom(jTextArea2);
    engineRunning=true;
    theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    jTabbedPane1.setSelectedIndex(1);   // go to round page
                    int i,j,r;
                    for (r=0; r<MAXROUNDS; r++) {
                            upfloaters[r]="upfloaters of round "+(r+1)+" ;";
                            downfloaters[r]="downfloaters of round "+(r+1)+" ;";
                            for (i=0; i<MAXPAIRS; i++) 
                                for (j=0; j<=MAXBOARDS; j++) roundsDetail[i][j][r]="0-0-0-0";
                    }
                    for (i=0;i<MAXTEAMS;i++) {
                            sortIndex[i]=0;
                            tempIndex[i]=(short)i;
                            teamScores[i][2] = 0;       // reset acceleration
                            for (j=0;j<MAXPLAYERSPERTEAM+2;j++) myTM.setValueAt("",i,j);  // empty table
                    }
                    sortAscending = false;
                    lastSortedCol = -1;
    deltaElo=1.0*MAXTEAMS/(simulationPlayers-1); if (deltaElo>25) deltaElo=25;
    for (addedRows=0;addedRows<simulationPlayers;addedRows++) {
                Elo=(int)Math.round(2500-deltaElo*addedRows);
                team = Main.localizedText("Player")+(addedRows+1);
                myTM.setValueAt(team,addedRows,0);      // player name
                myTM.setValueAt(""+Elo,addedRows,1);    // Elo
                myTM.setValueAt(""+myTM.getValueAt(addedRows,0)+";;;;;;"+myTM.getValueAt(addedRows,1)+";;;;",addedRows,2);
                Rectangle rect = jTable1.getCellRect(addedRows, 0, true);    // make the row visible (scroll)
                jTable1.scrollRectToVisible(rect);
                jTable1.setRowSelectionInterval(addedRows,addedRows);       // enhance the added row
                sortIndex[addedRows]=(short)(addedRows+1);                  // assign temp values
                tempIndex[addedRows]=(short)(addedRows);
    }
    jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
    jTabbedPane1.setSelectedIndex(0);   // go to registration page
    batchOrder=true;
    games=addedRows/2;
    if (Rounds==1) retire=1.5*simulationRetired/games;
    else retire=1.5*simulationRetired/(Rounds-1)/games;
    if (simulationForfeit>0) forfeit=1.5/simulationForfeit/games;
    Random generatorStart = new Random();
    seed=generatorStart.nextLong(); Random generatorRetire = new Random(seed);    // start sequence of pseudorandom uniform values   
    seed=generatorStart.nextLong(); Random generatorForfeit = new Random(seed);   // start sequence of pseudorandom uniform values   
    seed=generatorStart.nextLong(); Random generatorResult = new Random(seed);    // start sequence of pseudorandom uniform values   
    seed=generatorStart.nextLong(); Random generatorCutoff = new Random(seed);    // start sequence of pseudorandom uniform values   
    forfeited=retired=maxForfeitAllowed=0;
    games=(addedRows+1)/2;
    for (round=0; round<Rounds; round++) { 
        optimizationRequested=jCheckBox1.isSelected();
        if (!jCheckBox6.isSelected() && jDialog4.isVisible()) jDialog4.setVisible(false);   // hide explain window
        maxRound= currRound= (short)(round+1);
        SpinnerNumberModel model = new SpinnerNumberModel(currRound, 1, maxRound, 1);
        jSpinner1.setModel(model);      // update the spinner control
        jTextField4.setText(""+currRound);  // update the toolbar control
        missingResults=false;
        addedPairs=(short)((addedRows+1)/2);
        loadCurrentRound();
        addedPairs=0;         // so teams are always presented ordered
        nonePlayedAlternate=0; // flag to alternate colour for late entries
        if (simulationAcceleration) 
            if (tournamentType == 0 || tournamentType==1 || tournamentType == 2 || tournamentType == 3) calculateAcceleration();
        if (jCheckBox6.isSelected() && tournamentType<6) {
            jTextArea1.setText("");
            jDialog4.setVisible(true); // show explain window (and action button ...)
        }
        addedPairs=0;
        visits=0;
        if      (tournamentType == 0) swissDutch(jCheckBox6.isSelected());
        else if (tournamentType == 1) swissDubov(jCheckBox6.isSelected());
        else if (tournamentType == 2) swissSimple(jCheckBox6.isSelected());
        else if (tournamentType == 3) swissPerfectColours(jCheckBox6.isSelected());
        else if (tournamentType == 4) AmalfiRating(jCheckBox6.isSelected());
        else if (tournamentType == 5) roundRobin(jCheckBox6.isSelected());
        if (!engineRunning) {
            batchOrder=false;
            missingResults=false;
            jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
            jTabbedPane1.setSelectedIndex(0);   // go to registration page
            theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        if (currRound>1) orderPairs();

        forfeitedThisRound=retiredThisRound=0;
        for (pair=0; pair<games; pair++) {
            S=roundsDetail[pair][0][round].split("-");
            ID1=Integer.valueOf(S[0]);
            ID2=Integer.valueOf(S[1]); 
            if (ID2>0) {
                // check it's time to forfeit
                if (simulationForfeit>0) {
                    if (currRound==Rounds)  maxForfeitAllowed=Rounds/simulationForfeit;
                    else maxForfeitAllowed=(int)(1.0*Rounds/simulationForfeit+0.5);
                }
                if (simulationForfeit>0 && forfeited<maxForfeitAllowed && generatorForfeit.nextDouble()<forfeit && forfeitedThisRound==0) {
                    if (currRound==1 && Math.random()<0.5) {    // half the prob. for late entrants
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-0-0f";   
                        roundsDetail[pair][1][round]="1-1-0-0f";
                        jTextArea2.append("- "+Main.localizedText("Current round")+" "+currRound+": "+Main.localizedText("late entrants")+" "+ID1+", "+ID2+"\n");
                        goBottom(jTextArea2);
                    }
                    else if (Math.random()<0.5) {
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-1-0f";   
                        roundsDetail[pair][1][round]="1-1-1-0f";
                        jTextArea2.append("- "+Main.localizedText("Current round")+" "+currRound+": "+Main.localizedText("Forfeit")+" "+ID1+"-"+ID2+"\n");
                        goBottom(jTextArea2);
                    } else {
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-0-1f";   
                        roundsDetail[pair][1][round]="1-1-0-1f";
                        jTextArea2.append("- "+Main.localizedText("Current round")+" "+currRound+": "+Main.localizedText("Forfeit")+" "+ID1+"-"+ID2+"\n");
                        goBottom(jTextArea2);
                    }
                    forfeited++; forfeitedThisRound++;
                } else {
                    // 1st, generate random results based on setup percentages
                    random=generatorResult.nextDouble()*100;
                    pDraw=pWhite=pBlack=0;
                    if (random<simulationPercDraw) pDraw=1;     
                    else if (random<(simulationPercDraw+simulationPercWinWhite)) pWhite=1;
                    else pBlack=1;
                    // 2nd, in a random percentage of wins assign win stronger. the setup percentage is modulated by ID difference
                    cutoff=1.0*simulationPercWinStronger/(100-simulationPercDraw)+1.0*Math.abs(ID2-ID1)/(addedRows-1)-0.5;
                    if (pDraw==0 && generatorCutoff.nextDouble()<cutoff) {
                        if (ID2>ID1 && pBlack==1) {pWhite=1; pBlack=0;}
                        if (ID1>ID2 && pWhite==1) {pWhite=0; pBlack=1;}
                    }
                    // finally, store result
                    if (pDraw>=pWhite && pDraw>=pBlack) {
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-\u00BD-\u00BD";   // assign draw
                        roundsDetail[pair][1][round]="1-1-\u00BD-\u00BD"; 
                        totDraw++; totPlayed++;
                    } else if (pWhite>=pBlack) {
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-1-0";   // assign win white
                        roundsDetail[pair][1][round]="1-1-1-0";  
                        totPlayed++;
                    } else {
                        roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-0-1";   // assign win black
                        roundsDetail[pair][1][round]="1-1-0-1";  
                        totPlayed++;
                    }
                }
            } else if (ID1>0) {
                roundsDetail[pair][0][round]=""+ID1+"-0-1-0f";      // fix Bye
                roundsDetail[pair][1][round]="1-0-1-0f";
            }
        }
        if (tournamentType!=5) updateFloaters();  // not for 'round robin'
        for (pair=0; pair<games; pair++) {
            S=roundsDetail[pair][0][round].split("-");
            ID1=Integer.valueOf(S[0]);
            ID2=Integer.valueOf(S[1]); 
            if (ID2>0) {
                // check it's time to retire
                if (currRound<Rounds && retired<simulationRetired && generatorRetire.nextDouble()<retire && retiredThisRound==0) {
                    if (Math.random()<0.5) {
                        team=""+myTM.getValueAt(ID1-1,0)+"(W)";
                        myTM.setValueAt(team,ID1-1,0);
                        jTextArea2.append("- "+Main.localizedText("Current round")+" "+(currRound+1)+": "+Main.localizedText("retired player:")+" "+ID1+"\n");
                        goBottom(jTextArea2);
                    } else {
                        team=""+myTM.getValueAt(ID2-1,0)+"(W)";
                        myTM.setValueAt(team,ID2-1,0);
                        jTextArea2.append("- "+Main.localizedText("Current round")+" "+(currRound+1)+": "+Main.localizedText("retired player:")+" "+ID2+"\n");
                        goBottom(jTextArea2);
                    }
                    retired++; retiredThisRound++;
                } 
            }
        }
        if (currRound==Rounds-2)
            if (addedRows-countAvailableTeams()<simulationRetired && Math.random()<0.5) {
                ID1=generatorRetire.nextInt(addedRows)+1;
                team=""+myTM.getValueAt(ID1-1,0);
                if (!team.endsWith("(W)")) {
                    myTM.setValueAt(team+"(W)",ID1-1,0);
                    jTextArea2.append("- "+Main.localizedText("Current round")+" "+(currRound+1)+": "+Main.localizedText("retired player:")+" "+ID1+"\n");
                    goBottom(jTextArea2);
                } 
            }
        if (currRound==Rounds-1)
            while (addedRows-countAvailableTeams()<simulationRetired) {
                ID1=generatorRetire.nextInt(addedRows)+1;
                team=""+myTM.getValueAt(ID1-1,0);
                if (!team.endsWith("(W)")) {
                    myTM.setValueAt(team+"(W)",ID1-1,0);
                    jTextArea2.append("- "+Main.localizedText("Current round")+" "+(currRound+1)+": "+Main.localizedText("retired player:")+" "+ID1+"\n");
                    goBottom(jTextArea2);
                } 
            }
    }
    while (simulationForfeit>0 && forfeited<Rounds/simulationForfeit) {
        pair=generatorForfeit.nextInt(games); round=Rounds-1;
        if (roundsDetail[pair][0][round].equals("0-0-0-0")) continue;
        if (roundsDetail[pair][0][round].indexOf("f")<0) {
            S=roundsDetail[pair][0][round].split("-");
            ID1=Integer.valueOf(S[0]);
            ID2=Integer.valueOf(S[1]);
            if (Math.random()<0.5) {
                roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-1-0f";   
                roundsDetail[pair][1][round]="1-1-1-0f";
                jTextArea2.append("- "+Main.localizedText("Current round")+" "+currRound+": "+Main.localizedText("Forfeit:")+" "+ID1+"-"+ID2+"\n");
                goBottom(jTextArea2);
            } else {
                roundsDetail[pair][0][round]=""+ID1+"-"+ID2+"-0-1f";   
                roundsDetail[pair][1][round]="1-1-0-1f";
                jTextArea2.append("- "+Main.localizedText("Current round")+" "+currRound+": "+Main.localizedText("Forfeit:")+" "+ID1+"-"+ID2+"\n");
                goBottom(jTextArea2);
            }
            forfeited++; 
        }    
    }
    if (tournamentType!=5) updateFloaters();  // not for 'round robin'
    batchOrder=false;
    missingResults=false;
    jTextField54.setText(Main.localizedText("added")+" "+addedRows+"; "+Main.localizedText("active")+" "+countAvailableTeams());
    jTabbedPane1.setSelectedIndex(0);   // go to registration page
    theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    engineRunning=false;
    jTextArea2.append(Main.localizedText("done!")+" (draw "+Math.round(totDraw*1000.0/totPlayed)/10.0+"%)");
    goBottom(jTextArea2);
    jDialog7.toFront();
    }
}

public class checkTournament implements Runnable {
    private EnterFrame theMainForm=null; 
    public checkTournament(EnterFrame EF) {
        theMainForm = EF;
    }
   // @Override
    public void run() {  
    int i, j, round, pairs, goodPairs, IDo1, IDo2, IDr1, IDr2;
    String team, result;
    String S[]=null, originalPairs[]=new String[MAXTEAMS];
    TableModel myTM = jTable1.getModel();
    if (engineRunning) return;
    batchOrder=true;
    if (maxRound<1) return;
    if (immediatelyExitAfterCalculation) tournamentName=FileToBeChecked;
    else jDialog7.setVisible(true);      // open log dialog 
    jTextArea2.append("JavaPairing checker rev. 2.9 september 2015.\n");
    System.out.print("JavaPairing checker rev. 2.9 september 2015.\n");
    goBottom(jTextArea2);
    theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    for (round=maxRound-1; round>=0; round--) {  // trasverse rounds in reverse order
        pairs=(addedRows+1)/2;
        optimizationRequested=jCheckBox1.isSelected();
        jButton43.setEnabled(true);
        for (j=0; j<addedRows; j++) {
            team=""+myTM.getValueAt(j,0);
            if (!team.endsWith("(W)"))          // set the retired flag
                myTM.setValueAt(team+"(W)",j,0);
        }
        EngineThread.yield();
        goodPairs=0;
        for (j=0; j<pairs; j++) {
            S=roundsDetail[j][0][round].split("-");
            if (S.length != 4) continue;
            IDo1=Integer.valueOf(S[0]);
            IDo2=Integer.valueOf(S[1]);
            S=roundsDetail[j][1][round].split("-");
            if (possiblePlayerResults[6].equals(S[2]+"-"+S[3])) continue;  // workaround do not consider pair with result 1/2-1/2f
            if (possiblePlayerResults[11].equals(S[2]+"-"+S[3])) continue;  // workaround do not consider pair with result 1/2-0f
            if (possiblePlayerResults[13].equals(S[2]+"-"+S[3])) continue;  // workaround do not consider pair with result 1-1f
            if (possiblePlayerResults[14].equals(S[2]+"-"+S[3])) continue;  // workaround do not consider pair with result 1-1/2f
            if (possiblePlayerResults[15].equals(S[2]+"-"+S[3])) continue;  // workaround do not consider pair with result 1/2-1f
            originalPairs[goodPairs]=roundsDetail[j][0][round];   // 2.nd store original pairs
            goodPairs++;
            if (IDo1>0) {
                i=sortIndex[IDo1-1]-1;
                team=""+myTM.getValueAt(i,0);
                if (team.endsWith("(W)")) {         // reset the retired flag
                    team=team.substring(0,team.length()-3);
                    myTM.setValueAt(team,i,0);
                }
            }
            if (IDo2>0) {
                i=sortIndex[IDo2-1]-1;
                team=""+myTM.getValueAt(i,0);
                if (team.endsWith("(W)")) {         // reset the retired flag
                    team=team.substring(0,team.length()-3);
                    myTM.setValueAt(team,i,0);
                }
            }
        }
        pairs=goodPairs;
        if (round==0) {
            S=originalPairs[0].split("-");
            firstWhite=Integer.valueOf(S[0]);
        }
        jButton18ActionPerformed(null);                     // remove current round 
        EngineThread.yield();
        maxRound++;                     // increment round counter
        if (maxRound==1) doSortOrder=true;  // if first round, sort order (i.e. ID assignment) will be done in orderTeams() routine
        currRound = maxRound;           // align the current round to the max
        SpinnerNumberModel model = new SpinnerNumberModel(currRound, 1, maxRound, 1);
        jSpinner1.setModel(model);      // update the spinner control
        jTextField4.setText(""+currRound);  // update the toolbar control
        for (i=0; i<MAXPAIRS; i++)      // reset pairings & results array
        for (j=0; j<=maxBoards; j++) roundsDetail[i][j][currRound-1]="0-0-0-0"; // i.e. "ID1-ID2-result1-result2"
        for (i=0; i<MAXTEAMS; i++) {     // reset team, player scores and acceleration
                teamScores[i][0] = teamScores[i][1] = teamScores[i][2] =0;
        }
        missingResults=true;
        loadCurrentRound();   // this routine ultimately calls calculateTeamScores() and orderTeams() to establish the current ranking
        addedPairs=0;         // so teams are always presented ordered
        nonePlayedAlternate=0; // flag to alternate colour for late entries
        if (simulationAcceleration) 
            if (tournamentType == 0 || tournamentType==1 || tournamentType == 2 || tournamentType == 3) calculateAcceleration();
        if (jCheckBox6.isSelected() && tournamentType<6) {
            jTextArea1.setText("");
            jDialog4.setVisible(true); // show explain window (and action button ...)
        } else jDialog4.setVisible(false);
        addedPairs=0; engineRunning=true;
        visits=0; 
        if      (tournamentType == 0) swissDutch(jCheckBox6.isSelected());
        else if (tournamentType == 1) swissDubov(jCheckBox6.isSelected());
        else if (tournamentType == 2) swissSimple(jCheckBox6.isSelected());
        else if (tournamentType == 3) swissPerfectColours(jCheckBox6.isSelected());
        else if (tournamentType == 4) AmalfiRating(jCheckBox6.isSelected());
        else if (tournamentType == 5) roundRobin(jCheckBox6.isSelected());
        if (!engineRunning) {
            batchOrder=false;
            missingResults=false;
            theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            jTextArea2.append("\n");
            System.out.print("\n");
            jTextArea2.append(tournamentName+". round: "+currRound+"  aborted!!\n");
            System.out.print(tournamentName+". round: "+currRound+"  aborted!!\n");
            jTextArea2.append("\n");
            System.out.print("\n");
            jButton18ActionPerformed(null);                     // remove current round 
            jButton43.setEnabled(false);
            EngineThread.yield();
            continue;
        }
        engineRunning=false;
        EngineThread.yield();
        orderPairs();
        EngineThread.yield();
        result="";
        for (j=0; j<pairs; j++) {           // 5.th check pairs
            S=originalPairs[j].split("-");
            if (S.length != 4) continue;
            IDo1=Integer.valueOf(S[0]);
            IDo2=Integer.valueOf(S[1]);
            S=roundsDetail[j][0][round].split("-");
            if (S.length != 4) continue;
            IDr1=Integer.valueOf(S[0]);
            IDr2=Integer.valueOf(S[1]);
            if (IDo1==IDr1 && IDo2==IDr2) ; 
            else if (IDo1==IDr2 && IDo2==IDr1 && originalPairs[j].endsWith("f")) ;
            else {   
                result+="\n   "+"original: "+(""+IDo1+"-"+IDo2+"         ").substring(0, 10) +"   calculated: "+IDr1+"-"+IDr2;
            }
        }
        if (result.equals("")) {jTextArea2.append(tournamentName+". round: "+(""+currRound+"  ").substring(0,2)+" OK\n");
            System.out.print(tournamentName+". round: "+(""+currRound+"  ").substring(0,2)+" OK\n");
        }
        else   {jTextArea2.append(tournamentName+". round: "+currRound+result+"\n");
            System.out.print(tournamentName+". round: "+currRound+result+"\n");
        }
        jButton18ActionPerformed(null);                     // remove current round 
        goBottom(jTextArea2);
        EngineThread.yield();
    }
    batchOrder=false;
    theMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    engineRunning=false;
    jTextArea2.append(Main.localizedText("\n"));
    System.out.print("\n");
    goBottom(jTextArea2);
    jDialog7.toFront();
    jButton43.setEnabled(false);
    if (immediatelyExitAfterCalculation) System.exit(0);
    checkerRunning=false;
    }
}
    
private int indexAt(int value, short[] array) {
        // scan the array and find index to the value
        int j=0;
        for (;;j++) if (array[j]==value) break;
        return j;
    }

    private short parseInt(String string) {
        // transform first 3 char of the string to number
        int n=3;
        if (string.length()<n) n=string.length();
        string=string.substring(0,n).trim();
        return Integer.valueOf(string).shortValue();
    }

    private String stringscore(long score) {
        // render score in characters
        String z="";
        long i=score/100;    // integer part
        long d=score-i*100;  // fractional part
        if (i>0) z=z+i;
        if (d==0);
        else if (d==50) z=z+"\u00BD";
        else z=z+"."+d;
        if (z.compareTo("")==0) z="0";
        return z;
    }

    private short integerScore(String z) {
        // render score in numeric
        short s=0; int p;
        z="0"+z;
        p=z.indexOf("f");       // check *forfeit*
        if (p>=0) z=z.substring(0,p);
        p=z.indexOf("\u00BD");  // check half point
        if (p>=0) {
            s=5;
            z=z.substring(0,p);
        }
        s=(short)(s+Integer.valueOf(z).shortValue()*10);
        return s;
    }

    private String HTMLscore(long score) {
        // render 1/2 to HTML
        String result=stringscore(score);
        result=result.replace(".25","&frac14;");
        result=result.replace("\u00BD","&frac12;");
        result=result.replace(".75","&frac34;");
        result=result.replace(".33","&#x2153;");
        result=result.replace(".34","&#x2153;");
        result=result.replace(".66","&#x2154;");
        result=result.replace(".67","&#x2154;");
        return result;
    }
    
    private void loadCurrentRound() {
        // load current round when round counter changed
        if (currRound==0) return;
        TableModel myTM = jTable1.getModel();
        short i,j,k,r,value,i0,i1; String values, pair, S[];
        calculateTeamScores(-1);  // this initializes tempIndex & sortIndex by ranking criteria
//        jButton31.setEnabled(true);      // enable 'output current round' button
        myTableModel myTM2 = new myTableModel((addedRows+1)/2, 2, true);
        if (maxPlayersPerTeam==1) {
            myTM2.setColumnName(0, Main.localizedText("player A"));
            myTM2.setColumnName(1, Main.localizedText("player B"));
        } else {
            myTM2.setColumnName(0, Main.localizedText("Team A"));
            myTM2.setColumnName(1, Main.localizedText("Team B"));
        }
        jTable2.setModel(myTM2);
        myTableModel myTM3 = new myTableModel(addedRows, 1);
        if (maxPlayersPerTeam==1) myTM3.setColumnName(0, Main.localizedText("player A"));
        else myTM3.setColumnName(0, Main.localizedText("Team A"));
        jTable3.setModel(myTM3);
        myTableModel myTM4 = new myTableModel(addedRows, 1);
        if (maxPlayersPerTeam==1) myTM4.setColumnName(0, Main.localizedText("player B"));
        else myTM4.setColumnName(0, Main.localizedText("Team B"));
        jTable4.setModel(myTM4);
        for (k=0; k<addedPairs; k++) {   // reset pairing table rows
                    myTM2.setValueAt("",k,0);
                    myTM2.setValueAt("",k,1);
                    pairFrom[k][0] = pairFrom[k][1] = 0;
        }
        float fMolt=1; if (maxPlayersPerTeam==1) fMolt/=2;
        for (i=0; i<addedRows; i++) {   // scan Team table rows
                    i0 = tempIndex[i];
                    values=""+(i0+1)+"| "+myTM.getValueAt(sortIndex[i0]-1,0)+"  | "+fMolt*teamScores[i0][0]+" | ";
                    values+=1f/10f*teamScores[i0][1]+" |";
                    if ((k = findPartner((short)(i0+1))) < 0) {
                        // not yet paired
                        if (isRetired(i)) {
                            myTM3.setValueAt("",i,0);   
                            myTM4.setValueAt("",i,0);
                        } else {
                            myTM3.setValueAt(values,i,0);   
                            myTM4.setValueAt(values,i,0);
                        }                        
                    }
                    else
                    {                                   // already paired
                        myTM3.setValueAt("",i,0);  
                        myTM4.setValueAt("",i,0);
                        pair=roundsDetail[k][0][currRound-1];
                        S = pair.split("-");
                        if (S.length != 4) continue;
                        i0=Integer.valueOf(S[0]).shortValue(); // the one
                        i1=Integer.valueOf(S[1]).shortValue(); // the other one
                        values=""+i0+"| "+myTM.getValueAt(sortIndex[i0-1]-1,0)+" | "+fMolt*teamScores[i0-1][0]+" | "+ 1f*teamScores[i0-1][1]/10;
                        myTM2.setValueAt(values,k,0);
                        if (i1>0) {
                            values=""+i1+"| "+myTM.getValueAt(sortIndex[i1-1]-1,0)+" | "+fMolt*teamScores[i1-1][0]+" | "+ 1f*teamScores[i1-1][1]/10;
                            myTM2.setValueAt(values,k,1);
                        } else myTM2.setValueAt("0| BYE",k,1);
                        for (short i2=0;i2<addedRows;i2++) {
                            if (i0==tempIndex[i2]+1) pairFrom[k][0] = i2;
                            if (i1==tempIndex[i2]+1) pairFrom[k][1] = i2;
                        }
                    }
        }
        
    }

    private short findPartner(short i) {
        // scan the array to look if any partner ...
        String pair, S[]; short k,i0,i1;
        for (k=0; k<(addedRows+1)/2; k++) {
            pair=roundsDetail[k][0][currRound-1];
            S=pair.split("-");
            if (S.length != 4) continue;
            try {
                i0=Integer.valueOf(S[0]).shortValue(); // the one
                i1=Integer.valueOf(S[1]).shortValue(); // the other one
                if (i0==i1) continue;   // non initialized?
                if (i==i0 || i==i1) return k;
            } catch (NumberFormatException ex) {}
        }
        return -1;
    }
   
    private void adaptCols(JTable jT) {
        TableColumn jC; 
        int j;
        int maxWidth=jT.getColumnModel().getColumn(2).getMaxWidth();  // take width of the 1.st board
        int minWidth=jT.getColumnModel().getColumn(2).getMinWidth();  // take width of the 1.st board
        for (j=0; j<maxPlayersPerTeam; j++) {
            jC = jT.getColumnModel().getColumn(j+2);
            jC.setMinWidth(minWidth);
            jC.setMaxWidth(maxWidth);
            jC.setPreferredWidth(minWidth);
        }
        for (j=maxPlayersPerTeam; j<MAXPLAYERSPERTEAM; j++) {
            jC = jT.getColumnModel().getColumn(j+2);
            jC.setMinWidth(0);
            jC.setMaxWidth(0);
            jC.setPreferredWidth(0);
        }
     }
    
    private void adaptRows(short rows) {
        myTableModel myTM6 = new myTableModel(rows, 10, true);
        myTM6.setColumnName(0, Main.localizedText("Player Name"));
        myTM6.setColumnName(1, Main.localizedText("FED"));
        myTM6.setColumnName(2, Main.localizedText("Birthday"));
        myTM6.setColumnName(3, Main.localizedText("Sex"));
        myTM6.setColumnName(4, Main.localizedText("Title"));
        myTM6.setColumnName(5, Main.localizedText("ID FIDE"));
        myTM6.setColumnName(6, Main.localizedText("Elo FIDE"));
        myTM6.setColumnName(7, Main.localizedText("ID NAT"));
        myTM6.setColumnName(8, Main.localizedText("Elo NAT"));
        myTM6.setColumnName(9, Main.localizedText("K"));
        jTable6.setModel(myTM6);
        TableModel TM6 = jTable6.getModel();
        for (int j=0; j<rows; j++)
            for (int k=0; k<10; k++) TM6.setValueAt("", j, k);
        jTable6.getColumnModel().getColumn(0).setMinWidth(120);  // adapt to text
    }

    private void calculateAcceleration() {
        // calculate the acceleration needs.
        int nGroups=1;
        int i, addedPoints=0, p;
        int availableTeams = countAvailableTeams();
        int nRoundsToPlay = Integer.valueOf(rounds)-maxRound+1;
        int nTopScore;
        float fMolt=1f; if (maxPlayersPerTeam==1) fMolt/=2;
        
        if (nRoundsToPlay>1)        // last round = no acceleration
        for (;;) {
            nTopScore=0;
            for (i=0; i<availableTeams; i++)        // calculate how many top scored
                if (teamScores[tempIndex[0]][0]==teamScores[tempIndex[i]][0]) nTopScore++;
                else break;
            if (nTopScore>=Math.pow(2, nRoundsToPlay)) {
                nGroups*=2;
                p=(int)Math.ceil(1d*availableTeams/nGroups);
                if (p%2 == 1) p++;
                for (i=0; i<p; i++) 
                        teamScores[i][2]+=2;     // increase acceleration on half of the group
                addedPoints+=2;
                loadCurrentRound(); // apply changes
            }
            else break;
        }
        if (nGroups>1) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(this,
                Main.localizedText("Acceleration set for")+" "+nGroups+" "+Main.localizedText("groups")+". "+
                    Main.localizedText("Top score group added")+" "+fMolt*addedPoints+" "+Main.localizedText("points"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        }
        else if (nGroupsAccelerated > nGroups) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(this,
                Main.localizedText("Acceleration removed"),
                Main.localizedText("Info"),
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        }
        nGroupsAccelerated = nGroups;
    }

    private String[] splitCSV(String s) {
        // split csv text
        boolean quoted=false;
        int i, len=s.length();
        char c, delimiter, quotes;
        if (quotesDB[activeDB].length()==0) return s.split("\\"+delimiterDB[activeDB]);    // normal split
        quotes=quotesDB[activeDB].charAt(0);
        delimiter=delimiterDB[activeDB].charAt(0);
        if (delimiter!=';')           // change delimiter outside quoted strings to ; (this assumes ; is not present inside quoted strings!!)
            for (i=0; i<len; i++) { 
                c=s.charAt(i);
                if (c==quotes) quoted = !quoted;
                if (c==delimiter && !quoted) s=s.substring(0,i)+";"+s.substring(i+1,len);
                if (c==';' && quoted) return null;  // this is an error condition, cannot use ; as field delimiter!!
            }
        s=s.replace(""+quotes,"");   // strip quotes
        return s.split(";");    // split
    }

    public class PrintMe implements Printable { 
        /* PrintMe is a class that lets you print documents on
         * the fly for free ;) Printing in JDK 1.2 - 1.5 is hard.
         * With this, you just simply call it in your application
         * and add your text component like JTextPane:
         *
         * new PrintMe().print(YourJTextComponent);
         * Reminder: Just make sure there is a text on your component ;P
         *
         * Author : Jan Michael Soan
         * WebSite: http://www.jmsoan.com
         * Date   : 04/17/2004 
         * Time   : 2:20 PM 
         *
         * last modified by Eugenio Cervesato 14 oct 2009
         * to set A4 sheet, reduce margins and correctly scale
        */
        private int currentPage = -1;
        private JTextPane printPane; 
        private double pageEndY = 0;
        private double pageStartY = 0;
        private boolean scaleWidthToFit = true; 
        private PageFormat pFormat;
        private PrinterJob pJob;
        protected PrintMe(JTextPane pane) {
            pFormat = new PageFormat();
            pJob = PrinterJob.getPrinterJob();
            printPane=pane;
        }
       // @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            double scale = 1.0;
            Graphics2D graphics2D;
            View rootView;
            graphics2D = (Graphics2D) graphics;
            rootView = printPane.getUI().getRootView(printPane);
            if ((scaleWidthToFit) && (printPane.getWidth() > pageFormat.getImageableWidth())) {
                scale = pageFormat.getImageableWidth()/printPane.getWidth();
                graphics2D.scale(scale,scale);
            }
            graphics2D.setClip((int) (pageFormat.getImageableX()/scale),
                (int) (pageFormat.getImageableY()/scale),
                (int) (pageFormat.getImageableWidth()/scale),
                (int) (pageFormat.getImageableHeight()/scale));
            if (pageIndex > currentPage) {
                currentPage = pageIndex;
                pageStartY += pageEndY;
                pageEndY = graphics2D.getClipBounds().getHeight();
            }
            graphics2D.translate(graphics2D.getClipBounds().getX(), graphics2D.getClipBounds().getY());
            Rectangle allocation = new Rectangle(0,
                (int) -pageStartY,
                (int) (printPane.getWidth()),
                (int) (printPane.getHeight()));
            if (printView(graphics2D,allocation,rootView)) {
                return Printable.PAGE_EXISTS;
            }
            else {
                pageStartY = 0;
                pageEndY = 0;
                currentPage = -1;
                return Printable.NO_SUCH_PAGE;
            }
        }
        protected void print() {
            printDialog();
        }
        private void printDialog() {
            if (pJob.printDialog()) {
                // modified by Eugenio Cervesato 07 oct 2009
                Paper x = pFormat.getPaper();
                x.setSize(595, 842);  // sheet size A4 in dots
                x.setImageableArea(28,28,539,786); // about one cm. margins
                pFormat.setPaper(x);
                pJob.setPrintable(this,pFormat);
                try {
                    pJob.print();
                }
                catch (PrinterException printerException) {
                    pageStartY = 0;
                    pageEndY = 0;
                    currentPage = -1;
                    System.out.println("Error Printing Document");
                }
            }
        }
        private boolean printView(Graphics2D graphics2D, Shape allocation, View view) {
            boolean pageExists = false;
            Rectangle clipRectangle = graphics2D.getClipBounds();
            Shape childAllocation;
            View childView;
            if (view.getViewCount() > 0) {
                for (int i = 0; i < view.getViewCount(); i++) {
                    childAllocation = view.getChildAllocation(i,allocation);
                    if (childAllocation != null) {
                        childView = view.getView(i);
                        if (printView(graphics2D,childAllocation,childView)) {
                            pageExists = true;
                        }
                    }
                }
            } else {
                if (allocation.getBounds().getMaxY() >= clipRectangle.getY()) {
                    pageExists = true;
                    if ((allocation.getBounds().getHeight() > clipRectangle.getHeight()) &&
                        (allocation.intersects(clipRectangle))) {
                        view.paint(graphics2D,allocation);
                    } else {
                        if (allocation.getBounds().getY() >= clipRectangle.getY()) {
                            if (allocation.getBounds().getMaxY() <= clipRectangle.getMaxY()) {
                                view.paint(graphics2D,allocation);
                            } else {
                                if (allocation.getBounds().getY() < pageEndY) {
                                    pageEndY = allocation.getBounds().getY();
                                }
                            }
                        }
                    }
                }
            }
            return pageExists;
        }
        private boolean getScaleWidthToFit() {
            return scaleWidthToFit;
        }
        private void setScaleWidthToFit(boolean scaleWidth) {
            scaleWidthToFit = scaleWidth;
        }
    }
    
    private void write_to_log(boolean clear) {
        try {
            Writer outLog = new OutputStreamWriter(new FileOutputStream("log.txt", true), "UTF-8");            
            BufferedWriter fout = new BufferedWriter(outLog);
            jTextArea1.write(fout);
            outLog.close();
            if (clear) jTextArea1.setText("");
        } catch (IOException ex) {}
    }
        
    private void read_from_log() {
        try {
            Reader inLog = new InputStreamReader(new FileInputStream("log.txt"),"UTF-8");            
            BufferedReader fin = new BufferedReader(inLog);            
            jTextArea1.read(fin, inLog);
            inLog.close();
        } catch (IOException ex) {}
    }
    
    public class Node {
        private short[] localStorage=null;
        private int w=0;
        private Node nextNode=null;
        public Node(short [] permutation, int weight){
            localStorage=permutation.clone();
            w=weight;
        }
        public void setNextNode(Node n) {nextNode=n;}
        public int getWeight() {return w;}
        public void setWeight(int weight) {w=weight;}
        public short [] getPermutation() {return localStorage;}
        public Node getNextNode() {return nextNode;}
    }
    
    private String calculate_mean_Elo(String Elom, int n) {
        int i, j, Elo=0, Elo1, Elo2; String S[], temp;
        S=Elom.split(";");
        for (i=0; i<n-1; i++)       // order decreasing Elo
        for (j=i+1; j<n; j++) {
           try {
               Elo1=Integer.valueOf(""+S[i]);
           } catch (NumberFormatException ex) {Elo1=0;}
           try {
               Elo2=Integer.valueOf(""+S[j]);
           } catch (NumberFormatException ex) {Elo2=0;}
           if (Elo2>Elo1) {temp=S[i]; S[i]=S[j]; S[j]=temp;}
        }
        if (n>maxBoards) n=maxBoards;
        for (i=0; i<n; i++) {
           try {
               Elo+=Integer.valueOf(""+S[i]);
           } catch (NumberFormatException ex) {}
        }
        return ""+Math.round(1f*Elo/n);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JDialog jDialog6;
    private javax.swing.JDialog jDialog7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField61;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    
    public javax.swing.JDialog jDialog1p = jDialog1;
    public javax.swing.JDialog jDialog2p = jDialog2;
    public javax.swing.JDialog jDialog3p = jDialog3;
    public javax.swing.JDialog jDialog4p = jDialog4;
    public javax.swing.JDialog jDialog5p = jDialog5;
    public javax.swing.JDialog jDialog6p = jDialog6;
    public javax.swing.JDialog jDialog7p = jDialog7;
    public javax.swing.JPopupMenu jPopupMenu1p = jPopupMenu1;
    public javax.swing.JPopupMenu jPopupMenu2p = jPopupMenu2;
    public javax.swing.JPopupMenu jPopupMenu3p = jPopupMenu3;
    public javax.swing.JTabbedPane jTabbedPane1p = jTabbedPane1;
    public javax.swing.JPanel jPanel1p = jPanel1;
// max dimension of arrays
    private final short MAXTEAMS=500;
    private final short MAXPLAYERSPERTEAM=10;
    private final short MAXBOARDS=10;
    private final short MAXROUNDS=18;
    private final short MAXPLAYERS=(short)(MAXTEAMS*MAXPLAYERSPERTEAM);
    private final short MAXPAIRS=(short)(MAXTEAMS/2);
    private final short MINGAMESFIDE=1;
    private final double MINSCOREFIDE=0.5;
    private final short MINELOFIDE=1000;
    private final float KELOFIDEBASE=40f;
    private final short MAXROWSPERPAGE=48;
    private final int MPAFC=6;              // max # of pairs that can be completely explored by the engine avoiding factorial complexity
    private final long MAXWAITTIME=30*1000;  // max wait time before to signal factorial complexity in millisec
    
    public EnterFrame MainForm;
    private boolean safeExitAllowed = true;
    private boolean abort=false;
    private short addedRows = 0;
    private short editingRow = 0;
    private String tournamentName="";
    private String tournamentPlace="";
    private String fed="";
    private String tournamentDate1="";
    private String tournamentDate2="";
    private String tournamentArbiter="";
    private int tournamentPairing=0;
    private String rounds="9";
    private File [] selectedDB={new File(""), new File(""), new File(""), new File(""), new File("")};
    private String[] delimiterDB={"","","","",""}, quotesDB={"","","","",""};
    private String[][] indexesDB={null,null,null,null,null};
    private int activeDB=0;                 // index to active player database
    private short tournamentType=0; // set default Dutch
    private short teamOrder=0;
    private File currentDirectory=null;
    private File selectedFile=null;
    private File selectedSchema=null;
    private short currRound = 0;
    private short maxRound = 0;
    private short addedPairs = 0;
    private short prevRow=-1;
    private short[] sortIndex = new short[MAXTEAMS];  // of Teams 
    private short[] tempIndex = new short[MAXPLAYERS];  // Players array
    private short[][] teamScores = new short[MAXTEAMS][3];  //  (0-Team, 1-Individual, 2-Acceleration)
    private short[] playerScore = new short[MAXPLAYERS];      
    private short[] tempScore = new short[MAXPLAYERS];      
    private long[][] Z = new long[MAXPLAYERS][6];                  // tie break values array
    private String[][][] roundsDetail = new String[MAXPAIRS][MAXBOARDS+1][MAXROUNDS];  // max pairs x (0-Teams & 1... boards) x max rounds
    private short[][] pairFrom = new short[MAXPAIRS][2];                    // temp buffer for pairs
    private String[] upfloaters = new String[MAXROUNDS];
    private String[] downfloaters = new String[MAXROUNDS];
    private String[] possiblePlayerResults = {"0-0","1-0","\u00BD-\u00BD","0-1","0-0f","1-0f","\u00BD-\u00BDf","0-1f",
                                              "\u00BD-0","0-\u00BD","1-1","\u00BD-0f","0-\u00BDf","1-1f","1-\u00BDf","\u00BD-1f"}; 
    private String[] pairingSystems = {"swiss Dutch", "swiss Dubov", "Swiss Simple", "swiss Perfect Colours", "Amalfi Rating",
                                    "round robin", "by hand"};
    private String[] tieBreaks = {"", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger", "Direct Encounter", "ARO",
                                   "TPR", "Won Games", "Games With Black", "Score %", "Weighted Boards"};
    private String flagsTieBreak;
    private String WhiteColor = "W";    // used for cross-tables and boards
    private String BlackColor = "B";
    private MouseEvent prevevt=null;
    private long doubleClickTime=300;
    private long doubleClickSpace=5;
    private boolean doSortOrder=false;  // flag for 1.st round ordering
    
    private short maxBoards=4;          // dynamic N. boards to play
    private short maxPlayersPerTeam=6;  // dynamic max Players per Team
    private boolean rankingByAge=false;
    private boolean rankingByCategory=false;
    private boolean rankingByELO=false;
    private String[] group= new String[5];
    private String[] groupLimit= new String[5];
    private int nTeams;
    private String[] solution=null;
    private int [][] tI;
    private int visits;
    private String alreadyExplored="";
    private int deltaColour;
    private boolean optimizationRequested=false;
    private short nonePlayedAlternate=0;
    private int nGroupsAccelerated=1;
    private short [] deltaPerformance =   // table for 0 to 50% of score
                    { -800, -677,-589,-538,-501,-470,-444,-422,-401,-383,-366,
                            -351,-336,-322,-309,-296,-284,-273,-262,-251,-240,
                            -230,-220,-211,-202,-193,-184,-175,-166,-158,-149,
                            -141,-133,-125,-117,-110,-102, -95, -87, -80, -72,
                             -65, -57, -50, -43, -36, -29, -21, -14,  -7,   0 };
    private String openedFile = "";
    private boolean engineRunning=false;
    private String allText="";      // this and the following ones to convert HTML tables to text
    private boolean tableOpened=false;
    private int rowTable=0, colTable=0, maxColTable=0, colSpan=0;
    private String[][] arrTable= new String[MAXPLAYERS*2][MAXROUNDS+4];
    private String colHeader = "";   // this and the following ones to sort columns on Table1
    private Icon sortIcon = null;
    private boolean sortAscending = false;
    private int lastSortedCol = -1;
    private int returnValueFromshowOptionDialog=-1;
    private int availableTeams, lnb, group_lnb, lowestGroup, score50;
    private boolean lastRound;
    private String [] alreadySeen;
    private final int NORMAL_RETURN=0;
    private final int RETURN_C13=1;
    private final int RETURN_DECREASE_P=2;
    private final int RETURN_CONDITIONAL_DECREASE_P=4;
    private final int CUTOFF=1000;
    private final int NO_REMINDER=1;
    private final int REMINDER_FOR_C9=2;
    private final int REMINDER_FOR_C12=4;
    private final int REMINDER_FOR_A7e=8;
    private final int REMINDER_FOR_A7d=16;
    private final int REMINDER_FOR_C13=32;
    private final int REMINDER_FOR_AVOIDING_C12=64;
    private final int REMINDER_KEEP_HETEROGENEOUS=128;
    private final int REMINDER_FOR_maybeNeededAvoidB2=256;
    private final int REMINDER_FORCE_HOMOGENEOUS=512;
    private String [] colours, gamesPlayed;
    private Thread EngineThread;
    private boolean missingResults=false;
    private short maxRowsPerPage=MAXROWSPERPAGE;
    private String[] colourPreference = {"no colour preference", "mild white but can change", "mild black but can change", "", "", "", "", "", "", "", 
                                         "","white", "black", "", "", "", "", "", "", "", 
                                         "", "absolute white", "absolute black", "", "", "", "", "", "", "",
                                         "", "absolute white but can change", "absolute black but can change"   };
    private boolean batchOrder=false;   // to order pairs after read (import) FIDE report 
    private int statsPlayers, statsGames, statsGamesPlayed, statsBye, statsForfeit, statsDraw, statsWinWhite, statsWinStronger;
    private int simulationPlayers, simulationPercDraw, simulationPercWinWhite, simulationPercWinStronger, simulationForfeit, simulationRetired;
    private boolean simulationAcceleration;
    private String[] verbosities = {"Beginner", "Intermediate", "Expert"};
    private int verbosity_level=-1;          // set 0=beginner, 1=intermediate, 2=expert
    private boolean strong_verbose=(verbosity_level>1);  
    private boolean mild_verbose=(verbosity_level>0);    
    private String globalExplainText;
    private boolean explain, avoidBlocking, allowAcceleration;
    private int lastCompletedGroup, lastVisitedGroup, floatWeight;
    private long engineStartTime=0;
    private boolean checkerRunning=false;
    public String FileToBeChecked="";
    private boolean immediatelyExitAfterCalculation=false;
    private int firstWhite;
 
/*
 * Teams are stored in jTable1 rows as entered but can be reordered by the user. 
 * Anyway, the sort order, according to the setup option, is established at round 1 
 * and stored into sortIndex[], which is later used for reference only.
 * tempIndex[] is used to store Team ranking at each following round,
 * relative to the sortIndex[] array. (note, in Java array starts at 0 index) 
 * i.e. for 6 teams:
 * jTable1      sortIndex      tempIndex         Team ranking
 * "Team 1"         2              0             1st "Team 2"
 * "Team 2"         6              3             2nd "Team 4"
 * "Team 3"         1              5             3rd "Team 3"
 * "Team 4"         4              4             4th "Team 5"
 * "Team 5"         5              2             5th "Team 1"
 * "Team 6"         3              1             6th "Team 6"
 *
 * also, teamScores[][] and playerScore[] are relative to sortIndex.
*/
    
}
