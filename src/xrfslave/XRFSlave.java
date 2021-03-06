/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.sql.Connection;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author jonat
 */
public class XRFSlave {

    /**
     * @param args the command line arguments
     */
    
    public static JMenuItem quit;
    
    public void testTray() {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About XRF");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit( 0 );
            }
        });
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
        final TrayIcon trayIcon =
                new TrayIcon(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Public\\Pictures\\icone.png"),"XRF Slave",popup);
        trayIcon.setImageAutoSize(true);
        
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
    
    public static Properties getProp() throws FileNotFoundException, IOException{
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(new File("C:\\Users\\francisco.pereira\\Documents\\XRFSlave\\src\\xrfslave\\dados.properties"));
        props.load(file);
        return props;
    } 
    public static void main(String[] args) throws IOException,InterruptedException  {
        Connection conn = null;
        int lastId = -1;
        conn = DBConnection.getConexaoMySQL(XRFSlave.getProp().getProperty("prop.server.ip"));
        //Path faxFolder = Paths.get("//javari/IQC_F2/Jose_Alberto");
        System.out.println(XRFSlave.getProp().getProperty("prop.server.ip"));
        
        XRFSlave xrf = new XRFSlave();
        xrf.testTray();
		String base = "\\\\javari\\IQC_F2\\Data\\Teste";
		Path faxFolder = Paths.get("\\\\javari\\IQC_F2\\Data\\Teste");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY };
	faxFolder.register(watchService, events, com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE);

	boolean valid = true;
	do {
            WatchKey watchKey = watchService.take();
            for (WatchEvent event : watchKey.pollEvents()) {
		WatchEvent.Kind kind = event.kind();
                Thread.sleep( 50 );
		if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
                    String fileName = event.context().toString();
                    if(!(fileName.contains("TMP") || fileName.contains("tmp"))){
                        if(fileName.contains("TotalReport")){
                            System.out.println("File Created:" + fileName);
                            try {
                                //File file = new File( base + "\\" + fileName);
                                Thread.sleep(5000);
                                ExcelTotalReport.openTotalReport(base + "\\" + fileName);
                                
                            } catch (FileNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                            
                        }else{
                            if(fileName.contains("MONTH") && !fileName.contains("~$")){
                                String month = fileName.substring(fileName.indexOf("_") - 6,fileName.indexOf("_"));
                                System.out.println("Novo mês criado:" + fileName);
                                ExcelMonth.openMonth(base + "\\" + fileName, month);
                                
                            }

                        }
                    }			
		}
	    }
	    valid = watchKey.reset();

	} while (valid);
    }
    
}
