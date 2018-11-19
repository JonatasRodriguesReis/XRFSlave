/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonat
 */
public class DBConnection {
    public static Connection getConexaoMySQL() {
 
        Connection connection = null;       
        try {
            
            String driverName = "com.mysql.cj.jdbc.Driver";                        
            Class.forName(driverName);
            
            String serverName = "localhost";  
            String mydatabase ="lg_teste";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "root";        //nome de um usuário de seu BD      
            String password = "";      //sua senha de acesso
            connection = DriverManager.getConnection("jdbc:mysql://localhost/lg_teste?useTimezone=true&serverTimezone=UTC", "root", "");
            System.out.println("Conectou!");
            return connection;
        } catch (ClassNotFoundException e) {  //Driver não encontrado
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            //Não conseguindo se conectar ao banco
            System.out.println(e.getMessage());
            return null;

        }

    }
}
