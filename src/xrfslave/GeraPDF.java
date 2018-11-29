/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;



import com.lowagie.text.DocumentException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 *
 * @author jonat
 */
public class GeraPDF {
    public static void convert(String input, OutputStream out) throws DocumentException{
        convert(new ByteArrayInputStream(input.getBytes()), out);
    }
    public static void convert(InputStream input, OutputStream out) throws DocumentException{
    	Tidy tidy = new Tidy();        	
    	Document doc = tidy.parseDOM(input, null);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        renderer.layout();       
        renderer.createPDF(out);        		
    }	
}
