/*
 * Main.java
 *
 * Last revised on 18 April 2013 
 *
 * this starts up the form defined in EnterFrame.java
 * and does internationalization
 *
---------------------------------------------------------------------------
    Copyright (C) 2009-2013  Eugenio Cervesato from Cordenons (PN) - Italy.

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
 */

package eu.chessdata.paring.originaljavaparing;
import eu.chessdata.paring.originaljavaparing.EnterFrame;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.*;
/**
 *
 * @author Eugenio Cervesato
 */
public class Main {
   
    /** Creates a new instance of Main */
    public Main() {
    }
        static FileWriter fOut;
        static String lineSeparator;
        static boolean createLocale=false;          // this is for localization
        static ResourceBundle captions;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Optionally passed language, country and file to be checked. i.e. xx YY "filename.trf",
        // where xx is the language and YY the country. If none passed, JRE defaults to system values.
        // A file named 'JavaPairing_xx_YY.properties' must be included in the distribution
        // with the strings on the right part translated to your language. You may supply it!
        // If not, it defaults to English.
        // The third parameter is a complete file name of a tournament FIDE report to be checked
        Locale userLocale = Locale.getDefault();
        if (args.length>=2) {
            Locale.setDefault(Locale.US);
            userLocale = new Locale(args[0], args[1]);
        }
        // set System look&feel so that the application shows native on each platform
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {          	
                UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (Exception e) {  /* if not, don't worry! default runs */ }
        if (createLocale) {                 // this needs only once, to create
            try {                           // the resource file with the strings to translate
                fOut = new FileWriter("JavaPairing.properties");  // create the destination file
                lineSeparator = System.getProperty( "line.separator" );
                EnterFrame EF = new EnterFrame();    // create the form 
                scanComponents(EF);                  // scan components to extract strings
                scanComponents(EF.jDialog1p);
                scanComponents(EF.jDialog2p);
                scanComponents(EF.jDialog3p);
                scanComponents(EF.jDialog4p);
                scanComponents(EF.jDialog5p);
                scanComponents(EF.jDialog6p);
                scanComponents(EF.jDialog7p);
                scanComponents(EF.jPopupMenu1p);
                scanComponents(EF.jPopupMenu2p);
                scanComponents(EF.jPopupMenu3p);
                fOut.close();
                EF.setVisible(true);                // view the form
            } catch (IOException ex) {}
        }
        else {                                      // ordinary run
            try {                                   // open the localized file and translate strings
                captions = ResourceBundle.getBundle("JavaPairing", userLocale);
            } catch (MissingResourceException                     ex) {} // if none don't worry, it defaults to English
            EnterFrame EF = new EnterFrame();    // create the form 
            scanComponents(EF);                  // scan components to translate strings
            scanComponents(EF.jDialog1p);
            scanComponents(EF.jDialog2p);
            scanComponents(EF.jDialog3p);
            scanComponents(EF.jDialog4p);
            scanComponents(EF.jDialog5p);
            scanComponents(EF.jDialog6p);
            scanComponents(EF.jDialog7p);
            scanComponents(EF.jPopupMenu1p);
            scanComponents(EF.jPopupMenu2p);
            scanComponents(EF.jPopupMenu3p);
            String fileToCheck="";
            if (args.length==3) fileToCheck=args[2];
            if (args.length==2 && args[1].length()>2) fileToCheck=args[1];
            if (args.length==1 && args[0].length()>2) fileToCheck=args[0];
            if (fileToCheck.length()>0) EF.initializeTournament(EF, fileToCheck);
            else {
                if (!EF.initializeTournament()) {EF.dispose(); return;}         // show modal dialog for individual/team/open tournament
                EF.setVisible(true);                // view the main form
                int w=EF.getWidth();
                int w1=EF.jTabbedPane1p.getWidth();
                int w2=EF.jPanel1p.getWidth();
                if (w1>w2) w2=w1;                   // try to adjust form size (needed on some OS)
                w2+=30;
                Dimension d=EF.getPreferredSize();
                if (w<w2) {d.setSize(w2,d.height); EF.setMinimumSize(d); EF.setPreferredSize(d); EF.invalidate();}
            }
        }
    }
    
    private static void scanComponents(Container pC) {
        String objClass, englishText;
        Component C;
        int n=0;
        objClass = pC.getClass().getName();
        if (objClass.contains("EnterFrame")) {      // this is a Frame
            englishText = ((JFrame) pC).getTitle();
            if (englishText!=null) 
            if (englishText.trim().compareTo("")!=0) 
                if (createLocale) registerText(englishText);
                else ((JFrame) pC).setTitle(localizedText(englishText));
        }
        if (objClass.contains("JDialog")) {      // this is a Dialog
            englishText = ((JDialog) pC).getTitle();
            if (englishText!=null) 
            if (englishText.trim().compareTo("")!=0) 
                if (createLocale) registerText(englishText);
                else ((JDialog) pC).setTitle(localizedText(englishText));
        }
        if (objClass.contains("JPopupMenu")) {      // this is a popup menu
            englishText = ((JPopupMenu) pC).getLabel();
            if (englishText!=null) 
            if (englishText.trim().compareTo("")!=0) 
                if (createLocale) registerText(englishText);
                else ((JPopupMenu) pC).setLabel(localizedText(englishText));
        } 
        try {
            for (;;) {
                C = pC.getComponent(n++);
                objClass = C.getClass().getName();
                if (objClass.contains("JLabel")) {      // this is a label
                    englishText = ((JLabel) C).getText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JLabel) C).setText(localizedText(englishText));
                }
                else if (objClass.contains("JButton")) {      // this is a button
                    englishText = ((JButton) C).getText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JButton) C).setText(localizedText(englishText));
                    englishText = ((JButton) C).getToolTipText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JButton) C).setToolTipText(localizedText(englishText));
                }
                else if (objClass.contains("JCheckBox")) {      // this is a checkbox
                    englishText = ((JCheckBox) C).getText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JCheckBox) C).setText(localizedText(englishText));
                }
                else if (objClass.contains("JRadioButton")) {      // this is a radio button
                    englishText = ((JRadioButton) C).getText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JRadioButton) C).setText(localizedText(englishText));
                }
                else if (objClass.contains("JList")) {      // this is a list
                    ListModel list = ((JList) C).getModel();
                    int i=0, elem=list.getSize();
                    if (elem==0) return;
                    final String[] localized = new String[elem];
                    for (;i<elem;i++) {
                            englishText = ""+list.getElementAt(i);
                            if (englishText!=null) 
                            if (englishText.trim().compareTo("")!=0) 
                                if (createLocale) registerText(englishText);
                                else localized[i] = localizedText(englishText); 
                    }
                    if (!createLocale && elem>0) ((JList) C).setModel(new javax.swing.AbstractListModel() {
                            String[] strings = localized;
                        public int getSize() { return strings.length; }
                        public Object getElementAt(int i) { return strings[i]; }
                        });
                } 
                else if (objClass.contains("JTable") && !objClass.contains("JTableHeader")) {      // this is a table
                    int i=0;
                    try {
                        for (;;i++) {
                            englishText = ""+((JTable) C).getColumnModel().getColumn(i).getHeaderValue();
                            if (englishText!=null) 
                            if (englishText.trim().compareTo("")!=0) 
                                if (createLocale) registerText(englishText);
                                else ((JTable) C).getColumnModel().getColumn(i).setHeaderValue(localizedText(englishText));
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {}
                      catch (IndexOutOfBoundsException ex) {}
                }
                else if (objClass.contains("JMenuItem")) {      // this is a menu item
                    englishText = ((JMenuItem) C).getText();
                    if (englishText!=null) 
                    if (englishText.trim().compareTo("")!=0) 
                        if (createLocale) registerText(englishText);
                        else ((JMenuItem) C).setText(localizedText(englishText));
                } 
                else if (objClass.contains("JTabbedPane")) {      // this is a tabbed pane
                    int i=0;
                    try {
                        for (;;i++) {
                            englishText = ((JTabbedPane) C).getTitleAt(i);
                            if (englishText!=null) 
                            if (englishText.trim().compareTo("")!=0) 
                                if (createLocale) registerText(englishText);
                                else ((JTabbedPane) C).setTitleAt(i, localizedText(englishText));
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {}
                      catch (IndexOutOfBoundsException ex) {}
                    scanComponents((Container) C);     // scan recursive
                }
                else scanComponents((Container) C);     // scan recursive
            }
        } catch (ArrayIndexOutOfBoundsException ex) {}
          catch (IndexOutOfBoundsException ex) {}
    }
    
    private static void registerText(String englishText) {
        String temp;
        try {
            temp = englishText.trim();
            // write the key of max 40 chars (replace punctuation and blanks)
            fOut.write(String.format("%-40.40s", temp.replaceAll("\\p{Punct}", "").replaceAll("\\W", "_")));
            fOut.write(" = ");   // the delimiter
            fOut.write(temp);    // the text to translate
            fOut.write(lineSeparator);
        }
        catch (IOException ex) {}
    }
    public static String localizedText(String englishText) {
        String temp;
        temp = englishText.trim().replaceAll("\\p{Punct}", "").replaceAll("\\W", "_");
        if (temp.length()>40) temp = temp.substring(0,40);  // this is the key
        try {
            return captions.getString(temp);            // return localized string
        } catch (MissingResourceException ex) {}
          catch (NullPointerException ex) {}
        return englishText;                         // if not available, return the original string
    }

}
