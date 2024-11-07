package com.udemy.projeto.util;

import com.udemy.projeto.exception.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

   private static Connection connection = null;

   public static Connection getConnection(){
       if(connection == null) {
           try {
               String url = loadProperties().getProperty("dburl");
               connection = DriverManager.getConnection(url, loadProperties());
               return connection;
           } catch (SQLException e) {
               throw new DbException("Erro ao estabelecer conexão com banco de dados. Cause: " + e.getMessage());
           }
       } else return connection;
   }

   public static void closeConnection(){
       if(connection != null) {
           try {
               connection.close();
           } catch (SQLException e) {
               throw new DbException("Erro ao fechar conexão.  Cause: " + e.getMessage());
           }
       }
   }

   public static void closeStatement(Statement statement) {
       if(statement != null) {
           try {
               statement.close();
           } catch (SQLException e) {
               throw new DbException("Erro ao fechar Statement " + statement + ". Caused by: " + e.getMessage());
           }
       }
   }

    public static void closeResultSet(ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar ResultSet " + resultSet + ". Caused by: " + e.getMessage());
            }
        }
    }

   private static Properties loadProperties(){
       try (FileInputStream fr = new FileInputStream("db.properties")){
            Properties properties = new Properties();
            properties.load(fr);
            return properties;
       } catch (IOException e) {
           throw new RuntimeException("Erro ao obter dados da conexão. Cause: " + e.getMessage());
       }
   }

}
