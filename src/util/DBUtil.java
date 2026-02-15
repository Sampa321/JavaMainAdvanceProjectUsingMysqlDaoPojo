package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
   private static final String URL = "jdbc:mysql://localhost:3306/bankmanagement";
   private static final String username = "root";
   private static final String password = "sampa321";

   public static Connection getConnection() throws SQLException {
       return DriverManager.getConnection(URL,username,password);
   }
}
