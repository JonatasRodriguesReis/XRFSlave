/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import org.apache.commons.io.FileUtils;


/**
 *
 * @author francisco.pereira
 */
public class Teste {
    public static void main(String[] args) throws IOException,InterruptedException  {
        //Path faxFolder = Paths.get("//javari/IQC_F2/Jose_Alberto");
        XRFSlave xrf = new XRFSlave();
        xrf.testTray();
		String base = "\\\\javari\\IQC_F2\\Data";
		Path faxFolder = Paths.get("\\\\javari\\IQC_F2\\Data");
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
                        if(fileName.contains("Teste")){
                            System.out.println("File Created:" + fileName);
                            File srcDir = new File(base + "\\" + "Teste");
                            File destDir = new File("C:\\Users\\francisco.pereira\\Documents\\fax\\Teste");
                            //FileUtils.cleanDirectory(destDir); 
                            FileUtils.copyDirectory(srcDir, destDir);
                            break;
                            
                        

                        }
                    }			
		}
	    }
	    valid = watchKey.reset();

	} while (valid);
    }
}
