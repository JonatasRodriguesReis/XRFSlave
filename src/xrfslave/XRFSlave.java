/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

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

/**
 *
 * @author jonat
 */
public class XRFSlave {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException,InterruptedException  {
              
        
        //Path faxFolder = Paths.get("//javari/IQC_F2/Jose_Alberto");
        String base = "C:\\Users\\jonat\\Documents\\NetBeansProjects\\XRFSlave\\fax";
	Path faxFolder = Paths.get("C://Users//jonat//Documents//NetBeansProjects//XRFSlave//fax");
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
                                File file = new File( base + "\\" + fileName);
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
                                String month = fileName.substring(0,fileName.indexOf("_"));
                                System.out.println("Novo mês criado:" + fileName);
                                Thread.sleep(5000);
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
