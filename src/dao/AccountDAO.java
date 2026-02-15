package dao;

import model.Account;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDate;

public class AccountDAO {
    public boolean createAccount(Account acc) throws SQLException
    {
        String sql = "INSERT INTO  bankAccounts (AccountNumber,CustomerId,AccountType,Balance,Status,OpeningDate) VALUES(?,?,?,?,?,?)";
        //DB Connection

        //statement prepare
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            //execute the statements
            ps.setLong(1, acc.getAccountNumber());
            ps.setInt(2, acc.getCustomerID());
            ps.setString(3, acc.getAccountType());
            ps.setDouble(4, acc.getBalance());
            ps.setString(5, acc.getStatus());
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            int updatedRows = ps.executeUpdate();  //for update or modify use executeUpdate()
            if (updatedRows == 0) {
                return false;  //not create bank acc
            }
        }
        return true;
    }

    public Account getAccount(Long accountNumber) throws SQLException
    {
        //sql query preparation
        String sql = "SELECT * FROM bankAccounts WHERE AccountNumber = ?";  //simply reading the data not modify


        //create connection, prepare the sql statement for execution
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, accountNumber);

            //execute the query
            ResultSet record = ps.executeQuery(); //for read use executeQuery() and resultset is a interface , this will store the row return by the DBMS of ResultSet upon execution of SQL statement

            //extract details from the result set and create an object of account class
            record.next();
            Account obj = new Account(
                    record.getLong("AccountNumber"),
                    record.getInt("CustomerId"),
                    record.getString("AccountType"),
                    record.getDouble("Balance"),
                    record.getString("Status"),
                    record.getDate("OpeningDate").toLocalDate()
            );
            return obj;
        }
    }

    public boolean closedAccount(long accountNumber) throws SQLException{
        String sql = "UPDATE bankAccounts SET Status = 'Closed' WHERE accountNumber = ?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);)
        {
            ps.setLong(1,accountNumber);
            int rowsAffected = ps.executeUpdate();  //r = 0 when account not closed
            return rowsAffected>0; //0>0 false
        }
    }

    public void updateBalance(Account acc) throws SQLException{
        String sql = "UPDATE bankAccounts SET Balance = ? WHERE AccountNumber = ?";
        try(
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            )
        {
            ps.setDouble(1,acc.getBalance());
            ps.setLong(2,acc.getAccountNumber());
            ps.executeUpdate();
        }
    }
}
