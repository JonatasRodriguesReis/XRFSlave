/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import java.util.Iterator;
import java.util.UnknownFormatConversionException;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;


/**
 *
 * @author jonat
 */
public class ExcelTotalReport {
    
    public static void openTotalReport(String path) throws IOException{
        Connection conn = null;
        conn = DBConnection.getConexaoMySQL();
        FileInputStream arquivo;
        try {
            System.out.println(path);
            arquivo = new FileInputStream(new File(path));
  
            Iterator<Row> rowIterator = null;
            
            if(path.contains("xlsx")){
                XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
  
                XSSFSheet sheetAlunos = workbook.getSheetAt(0);
                rowIterator = sheetAlunos.iterator();
            }else if(path.contains("xls")){
                HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
                HSSFSheet sheetAlunos = workbook.getSheetAt(0);
                rowIterator = sheetAlunos.iterator();
            }
            
            if(rowIterator != null){
                int i = 0;
                boolean fim_item = false;
                String judge_item = "O.K.";
                String data_teste_item = "";
                //String nome_item = "";
                String nome_Item = "";

                String sample_no = "";
                String data_teste = "";
                String nome = "";
                String part_number = "";
                String operator = "";
                String judge = "";
                String cd_judge = "";
                String pb_judge = "";
                String hg_judge = "";
                String br_judge = "";
                String cr_judge = "";

                ArrayList<SubItem> lista = new ArrayList<SubItem>();

                int index = 0;
                while (rowIterator.hasNext()) {
                    i +=1;
                    Row row = rowIterator.next();
                    if(i > 3){
                          Iterator<Cell> cellIterator = row.cellIterator();

                          while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch (cell.getColumnIndex()) {
                              case 0:
                                 cell.setCellType(CellType.STRING);
                                 System.out.print(cell.getStringCellValue() );
                                 if(cell.getStringCellValue().length() > 10){
                                    fim_item = true;
                                    while(cell.getStringCellValue().indexOf("\\",index) != -1){
                                        index = cell.getStringCellValue().indexOf("\\",index) + 1;
                                    }

                                    nome_Item = cell.getStringCellValue().substring(index,cell.getStringCellValue().indexOf(".",index));

                                 }
                                 break;
                              case 1:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  sample_no = cell.getStringCellValue();
                                  break;
                              case 7:
                                 String cellValue = "";
                                 if(DateUtil.isCellDateFormatted(cell)){
                                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                  cellValue =  sdf.format(cell.getDateCellValue());                                            
                                 }
                                 data_teste = cellValue;
                                 data_teste_item = cellValue;
                                 System.out.print(" " + cellValue);
                                 break;
                              case 8:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  nome = cell.getStringCellValue();
                                  break;
                              case 10:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  part_number = cell.getStringCellValue();
                                  //nome_item = item;
                                  break;
                              case 11:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  operator = cell.getStringCellValue();
                                  break;
                              case 18:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  judge = cell.getStringCellValue();
                                  if(judge_item.equals("O.K.")){
                                    judge_item = judge;
                                  }
                                  break;
                              case 20:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  cd_judge = cell.getStringCellValue();
                                  break;
                              case 28:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  pb_judge = cell.getStringCellValue();
                                  break;
                              case 36:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  hg_judge = cell.getStringCellValue();
                                  break;
                              case 44:
                                  cell.setCellType(CellType.STRING);
                                  System.out.print(" " + cell.getStringCellValue());
                                  br_judge = cell.getStringCellValue();
                                  break;
                              case 52:
                                  cell.setCellType(CellType.STRING);
                                  System.out.println(" " + cell.getStringCellValue());
                                  cr_judge = cell.getStringCellValue();
                                  break;
                            }
                        }
                        if(!fim_item){
                            SubItem sb = new SubItem(sample_no,data_teste, nome, part_number, operator, judge, cd_judge, pb_judge, hg_judge, br_judge, cr_judge);
                            lista.add(sb);
                        }

                    }    
                    if(fim_item){
                        fim_item = false;
                        //i = 0;
                        index = 0;

                        //System.out.println("Tamanho: " + lista.size());
                        String consultaItem = "select * from item where nome = '"+ nome_Item +"' and data_teste = '" + data_teste_item + "'";
                        Statement st;
                        try {
                            st = conn.createStatement();
                            ResultSet rs = st.executeQuery(consultaItem);
                            if(rs.next() && rs.getString("situacao").equals("NÃO_REALIZADO")){
                                for (SubItem sb : lista) {
                                    try{
                                        // create the java mysql update preparedstatement
                                        String query = "insert into subitem(sample_no,operator,judge,cd_judge,pb_judge,hg_judge,br_judge,cr_judge,item,nome,data_teste) "
                                                + "values(?,?,?,?,?,?,?,?,(select id from item where data_teste = ? and nome = ?),?,?) ";

                                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                                        preparedStmt.setString(1, sb.sample_no);
                                        preparedStmt.setString(2, sb.operator);
                                        preparedStmt.setString(3, sb.judge);
                                        preparedStmt.setString(4, sb.cd_judge);
                                        preparedStmt.setString(5, sb.pb_judge);
                                        preparedStmt.setString(6, sb.hg_judge);
                                        preparedStmt.setString(7, sb.br_judge);
                                        preparedStmt.setString(8, sb.cr_judge);
                                        preparedStmt.setString(9, sb.data_teste);
                                        preparedStmt.setString(10, nome_Item);
                                        preparedStmt.setString(11, sb.nome);
                                        preparedStmt.setString(12, sb.data_teste);

                                        // execute the java preparedstatement
                                        preparedStmt.execute();

                                    }catch(SQLException e){
                                        System.out.println(e.getMessage());
                                    }
                                    /*String source = path.substring(0,path.indexOf("TotalReport.xls")) + nome_Item + "\\Report\\" + sb.sample_no + ".xls";
                                    //File source = new File(path_source); 
                                    exportToPDF(source,"C:\\xampp\\htdocs\\ReportsFiles\\" + sb.data_teste + "_" + sb.sample_no +"_"+ sb.nome +"_" + nome_Item +".html");
                                    */
                                    String path_source = path.substring(0,path.indexOf("TotalReport.xls")) + nome_Item + "\\Report\\" + sb.sample_no + ".xls";
                                    File source = new File(path_source); 
                                    File dest = new File("C:\\xampp\\htdocs\\ReportsFiles\\" + sb.data_teste + "_" + sb.sample_no +"_"+ sb.nome +"_" + nome_Item +".xls");
                                    copiarReport(source, dest);
                                }

                                try{
                                    // create the java mysql update preparedstatement
                                    String query = "update item set judge = ?, situacao = 'REALIZADO', part_number = ? where data_teste = ? and nome = ?";
                                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                                    preparedStmt.setString(1, judge_item);
                                    preparedStmt.setString(2, part_number);
                                    preparedStmt.setString(3, data_teste_item);
                                    preparedStmt.setString(4, nome_Item);

                                    // execute the java preparedstatement
                                    preparedStmt.executeUpdate();
                                }catch(SQLException e){
                                    System.err.println(e.getMessage());
                                }


                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        judge_item = "O.K.";
                        nome_Item = "";

                        sample_no = "";
                        data_teste = "";
                        nome = "";
                        part_number = "";
                        operator = "";
                        judge = "";
                        cd_judge = "";
                        pb_judge = "";
                        hg_judge = "";
                        br_judge = "";
                        cr_judge = "";

                        lista = new ArrayList<SubItem>();
                    }
                    arquivo.close();
                }
            }
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
    }     
   }
   
   private static void exportToPDF(String source, String dest){
       HSSFWorkbook excelDoc;
        try {
            excelDoc = ExcelToHtmlUtils.loadXls(new File(source));
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
            
            FileWriter arq = new FileWriter(dest);
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.print(result);
            //System.out.println(result);
            
            //OutputStream os = new FileOutputStream("C:\\xampp\\htdocs\\ReportsFiles\\hello.pdf");
            //InputStream in = new FileInputStream("C:\\xampp\\htdocs\\ReportsFiles\\teste.html");
            //GeraPDF.convert("", os);          
            //os.close();
            
            //GeraPDF.gerar(XHTMLConvert.convertToXHTML(result.replace("&nbsp;"," ")));
        } catch (IOException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownFormatConversionException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
                 
    }
   
    private static void copiarReport(File source, File dest){
         try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
   /*private static void exportToPDF(File source, String dest){
        FileInputStream input_document;
        try {
            input_document = new FileInputStream(source);
            // Read workbook into HSSFWorkbook
            HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document); 
            // Read worksheet into HSSFSheet
            HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0); 
            // To iterate over the rows
            Iterator<Row> rowIterator = my_worksheet.iterator();
            //We will create output PDF document objects at this point
            PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
            Document iText_xls_2_pdf = new Document(pdf);
            
            //we have two columns in the Excel sheet, so we create a PDF table with two columns
            //Note: There are ways to make this dynamic in nature, if you want to.
            PdfPTable my_table = new PdfPTable(2);
            //We will use the object below to dynamically add new data to the table
            PdfPCell table_cell;
            //Loop through rows.
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next(); 
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next(); //Fetch CELL
                    switch(cell.getCellType()) { //Identify CELL type
                    //you need to add more code here based on
                    //your requirement / transformations
                        case Cell.CELL_TYPE_STRING:
                            //Push the data from Excel to PDF Cell
                            table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
                            //feel free to move the code below to suit to your needs
                            my_table.addCell(table_cell);
                            break;
                    }
                    //next line
                }
            }
            //Finally add the table to PDF document
            iText_xls_2_pdf.add(my_table);                       
            iText_xls_2_pdf.close();                
            //we created our pdf file..
            input_document.close(); //close xls
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (DocumentException ex) {
            Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
        }
   } 
    
   File dest = new File("C:\\xampp\\htdocs\\ReportsFiles\\" + sb.data_teste + "_" + sb.sample_no +"_"+ sb.nome +"_" + nome_Item +".xls");
                                copiarReport(source, dest); */
     
}