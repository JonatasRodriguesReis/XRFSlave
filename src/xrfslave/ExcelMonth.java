/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jonat
 */
public class ExcelMonth {
    public static void openMonth(String path,String month) throws IOException{
        Connection conn = null;
        int lastId = -1;
        conn = DBConnection.getConexaoMySQL(XRFSlave.getProp().getProperty("prop.server.ip"));
        try{

            System.out.println(path);
            String query = "insert into statusrohs(month) values(?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, month);
            preparedStmt.execute();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
                
            }
        }catch(SQLException e){
           System.out.println(e.getMessage());
        }
        
        FileInputStream arquivo;
        try {
            arquivo = new FileInputStream(new File(path));

            Iterator<Row> rowIterator = null;
            if(path.contains("xlsx")){
                
                XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
  
                XSSFSheet sheet = workbook.getSheetAt(0);
                rowIterator = sheet.iterator();
                
            }else if(path.contains("xls")){
                HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
                HSSFSheet sheet = workbook.getSheetAt(0);
                rowIterator = sheet.iterator();
            }
            System.out.println(path.substring(path.indexOf(".") + 1));
            
            
            if(rowIterator != null){
                int index = 0;
                while (rowIterator.hasNext()) {
                    String data = "";
                    String item = "";
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    index++;      
                    if(index > 2){

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            switch (cell.getColumnIndex()) {
                              case 9:
                                  cell.setCellType(CellType.STRING);
                                  item = cell.getStringCellValue();
                                  System.out.print(" " + cell.getStringCellValue());
                                  System.out.println(cell.getStringCellValue());
                                  break;
                              case 7:
                                 String cellValue = "";
                                 if(DateUtil.isCellDateFormatted(cell)){
                                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                  cellValue =  sdf.format(cell.getDateCellValue());                                            
                                 }
                                 System.out.print("DATA" + cellValue);
                                 data = cellValue;
                                 break;

                            }
                        }
                        if(!data.equals("") && !item.equals("")){
                            try {
                                String query = "insert into item(nome,data_teste,situacao,statusrohs) values(?,?,?,?)";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);

                                preparedStmt.setString(1, item);
                                preparedStmt.setString(2, data);
                                preparedStmt.setString(3, "NÃO_REALIZADO");
                                preparedStmt.setInt(4, lastId);
                                preparedStmt.execute();
                            } catch (SQLException ex) {
                                Logger.getLogger(ExcelMonth.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            

                        }
                    }      

                }
            }  
            
        arquivo.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
    } 
   }
}
