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
import java.sql.SQLException;
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
            arquivo = new FileInputStream(new File(path));
            HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
  
            HSSFSheet sheetAlunos = workbook.getSheetAt(0);
  
            Iterator<Row> rowIterator = sheetAlunos.iterator();
            int i = 0;
            boolean fim_item = false;
            String judge_item = "O.K.";
            String data_teste_item = "";
            String nome_item = "";
            while (rowIterator.hasNext()) {
                i +=1;
                Row row = rowIterator.next();
                if(i > 3){
                      Iterator<Cell> cellIterator = row.cellIterator();
                      String data_teste = "";
                      String nome = "";
                      String item = "";
                      String operator = "";
                      String judge = "";
                      String cd_judge = "";
                      String pb_judge = "";
                      String hg_judge = "";
                      String br_judge = "";
                      String cr_judge = "";
                      while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                          case 0:
                             cell.setCellType(CellType.STRING);
                             System.out.print(cell.getStringCellValue());
                             if(cell.getStringCellValue().length() > 10){
                                fim_item = true;
                             }
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
                              item = cell.getStringCellValue();
                              nome_item = item;
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
                      
                    try{
                        // create the java mysql update preparedstatement
                        String query = "update subitem set operator = ?,judge = ?, cd_judge = ?, pb_judge = ?, hg_judge = ?,"
                                + "br_judge = ?,cr_judge = ? where item = (select id from item where data_teste = ? and nome = ?) and nome = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, operator);
                        preparedStmt.setString(2, judge);
                        preparedStmt.setString(3, cd_judge);
                        preparedStmt.setString(4, pb_judge);
                        preparedStmt.setString(5, hg_judge);
                        preparedStmt.setString(6, br_judge);
                        preparedStmt.setString(7, cr_judge);
                        preparedStmt.setString(8, data_teste);
                        preparedStmt.setString(9, item);
                        preparedStmt.setString(10, nome);

                        // execute the java preparedstatement
                        preparedStmt.executeUpdate();
                    }catch(SQLException e){
                        System.err.println(e.getMessage());
                    }
                }    
                if(fim_item){
                    fim_item = false;
                    i = 0;
                    try{
                        // create the java mysql update preparedstatement
                        String query = "update item set judge = ?, situacao = 'REALIZADO' where data_teste = ? and nome = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, judge_item);
                        preparedStmt.setString(2, data_teste_item);
                        preparedStmt.setString(3, nome_item);

                        // execute the java preparedstatement
                        preparedStmt.executeUpdate();
                    }catch(SQLException e){
                        System.err.println(e.getMessage());
                    }
                    judge_item = "O.K.";
                }
                arquivo.close();
            }
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ExcelTotalReport.class.getName()).log(Level.SEVERE, null, ex);
    }     
   }
    
}
