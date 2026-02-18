package dao;

import exception.AccountNotFoundException;
import model.Account;
import model.Customer;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerDAO {
    public int createCustomer(Customer customer) throws SQLException
    {
        String sql = "INSERT INTO Customers(FirstName,LastName,Email,PhoneNumber,Address) VALUES(?,?,?,?,?)";
        //DB Connection

        //statement prepare
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);){
            //execute the statements
            ps.setString(1,customer.getFirstName());
            ps.setString(2,customer.getLastName());
            ps.setString(3,customer.getEmail());
            ps.setString(4,customer.getPhone());
            ps.setString(5,customer.getAddress());

            int updatedRows = ps.executeUpdate();  //Return -1 if not create customer  or return 0 if occur problem
            if(updatedRows == 0)
            {
                return -1;
            }
            //store the response
            ResultSet keys = ps.getGeneratedKeys();
            //process the response as per your requirement
            if(keys.next())
            {
                return keys.getInt(1); //getInt write for customerID IS INTEGER
            }
        }
        //ps.close();  //ps depend on connection so 1st close prepareStatement then Connection.
        //connection.close();
        return -1;
    }


    public Customer getPhoneNumber(String phoneNumber) throws SQLException
    {
        //sql query preparation
        String sql = "SELECT * FROM customers WHERE PhoneNumber = ?";  //simply reading the data not modify


        //create connection, prepare the sql statement for execution
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);)
        {
            ps.setString(1, phoneNumber);

            //execute the query
            ResultSet record = ps.executeQuery(); //for read use executeQuery() and resultset is a interface , this will store the row return by the DBMS of ResultSet upon execution of SQL statement


            //extract details from the result set and create an object of account class
            boolean flag = record.next();
            if (!flag)
            {
                System.out.println("This phone Number does not valid at GG bank😔");
            }
            Customer obj = new Customer(
                    record.getString("FirstName"),
                    record.getString("LastName"),
                    record.getString("Email"),
                    record.getString("PhoneNumber"),
                    record.getString("Address")
            );
            return obj;
        }
    }


    public void updateFirstName(Customer customer) throws SQLException
    {
        String sql = "UPDATE customers set FirstName = ? WHERE PhoneNumber =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,customer.getFirstName());
            ps.setString(2,customer.getPhone());
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateLastName(Customer customer) throws SQLException
    {
        String sql = "UPDATE customers set LastName = ? WHERE PhoneNumber =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,customer.getLastName());
            ps.setString(2,customer.getPhone());
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateEmail(Customer customer) throws SQLException
    {
        String sql = "UPDATE customers set Email = ? WHERE PhoneNumber =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,customer.getEmail());
            ps.setString(2,customer.getPhone());
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updatePhoneNumber(Customer customer) throws SQLException
    {
        String sql = "UPDATE customers set PhoneNumber = ? WHERE PhoneNumber =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,customer.getPhone());
            ps.setString(2,customer.getPhone());
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateAddress(Customer customer) throws SQLException
    {
        String sql = "UPDATE customers set Address = ? WHERE PhoneNumber =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,customer.getAddress());
            ps.setString(2,customer.getPhone());
            //execute the query
            ps.executeUpdate();
        }
    }
}
