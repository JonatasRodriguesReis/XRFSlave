/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

/**
 *
 * @author jonat
 */
public class Teste {
    public static void main(String[] args) {
        HSSFWorkbook excelDoc;
        try {
            excelDoc = ExcelToHtmlUtils.loadXls(new File("C://Users//jonat//Documents//NetBeansProjects//XRFSlave//fax//Folder2//TotalReport.xls"));
            ExcelToHtmlConverter converter = new ExcelToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
            );

            converter.processWorkbook(excelDoc);

            Document htmlDoc = converter.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDoc);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory transfFactory = TransformerFactory.newInstance();
            Transformer serializer = transfFactory.newTransformer();
            
            serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "html");
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);

            out.close();

            String result = new String(out.toByteArray());
            System.out.println(result);
            
            //OutputStream os = new FileOutputStream("C:\\xampp\\htdocs\\ReportsFiles\\hello.pdf");
            //InputStream in = new FileInputStream("C:\\xampp\\htdocs\\ReportsFiles\\teste.html");
            //GeraPDF.convert("", os);        	
            //os.close();
            
            //GeraPDF.gerar(XHTMLConvert.convertToXHTML(result.replace("&nbsp;"," ")));
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
         catch (ParserConfigurationException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (TransformerConfigurationException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (TransformerException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}
