package dao;

import exception.AccountNotFoundException;
import exception.CustomerNotFoundException;
import model.Account;
import model.Customer;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.RecursiveTask;

public class CustomerDAO {
    public int createCustomer(Customer customer) throws SQLException,CustomerNotFoundException
    {
        String sql = "INSERT INTO Customers(FirstName,MiddleName,LastName,Email,PhoneNumber,PanNumber,AadharNumber,Address,CustomerPassword) VALUES(?,?,?,?,?,?,?,?,?)";
        //DB Connection

        //statement prepare
        //Create Connection
        //Prepare a sql statement
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);){
            //execute the statements
            ps.setString(1,customer.getFirstName());
            ps.setString(2,customer.getMiddleName());
            ps.setString(3,customer.getLastName());
            ps.setString(4,customer.getEmail());
            ps.setString(5,customer.getPhone());
            ps.setLong(6,customer.getPanNumber());
            ps.setLong(7,customer.getAadharNumber());
            ps.setString(8,customer.getAddress());
            ps.setString(9,customer.getPassword());

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


    public Customer getCustomerOfEmail(String email) throws CustomerNotFoundException, SQLException
    {
        String sql = "SELECT * FROM Customers WHERE Email = ?";  //simply reading the data not modify


        //create connection, prepare the sql statement for execution
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);)
        {
            ps.setString(1, email);

            //execute the query
            ResultSet record = ps.executeQuery(); //for read use executeQuery() and resultset is a interface , this will store the row return by the DBMS of ResultSet upon execution of SQL statement

            //extract details from the result set and create an object of account class
            boolean flag = record.next();
            if (!flag)
            {
                throw new CustomerNotFoundException("Customer does not exists !!");
            }
            Customer obj = new Customer(
                    record.getString("FirstName"),
                    record.getString("MiddleName"),
                    record.getString("LastName"),
                    record.getString("Email"),
                    record.getString("PhoneNumber"),
                    record.getLong("PanNumber"),
                    record.getLong("AadharNumber"),
                    record.getString("Address"),
                    record.getString("CustomerPassword")
            );
            return obj;
        }
    }

    public Customer getCustomerOfPassword(String password) throws SQLException,CustomerNotFoundException
    {
        String sql = "SELECT * FROM Customers WHERE CustomerPassword = ?";  //simply reading the data not modify


        //create connection, prepare the sql statement for execution
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);)
        {
            ps.setString(1, password);

            //execute the query
            ResultSet record = ps.executeQuery(); //for read use executeQuery() and resultset is a interface , this will store the row return by the DBMS of ResultSet upon execution of SQL statement


            //extract details from the result set and create an object of account class
            boolean flag = record.next();
            if (!flag)
            {
                throw new CustomerNotFoundException("Customer does not exists !!");
            }
            Customer obj = new Customer(
                    record.getString("FirstName"),
                    record.getString("MiddleName"),
                    record.getString("LastName"),
                    record.getString("Email"),
                    record.getString("PhoneNumber"),
                    record.getLong("PanNumber"),
                    record.getLong("AadharNumber"),
                    record.getString("Address"),
                    record.getString("CustomerPassword")
            );
            return obj;
        }
    }
    public void updatePassword(String password,int customerId) throws SQLException
    {
        String sql = "UPDATE customers set CustomerPassword = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,password);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateFirstName(String fName,int customerId) throws SQLException
    {
        String sql = "UPDATE customers set FirstName = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,fName);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateMiddleName(String mName,int customerId) throws SQLException
    {
        String sql = "UPDATE customers set MiddleName = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,mName);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateLastName(String lName,int customerId) throws SQLException
    {
        String sql = "UPDATE customers set LastName = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,lName);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateEmail(String email, int customerId) throws SQLException
    {
        String sql = "UPDATE customers set Email = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,email);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updatePhoneNumber(String pno, int customerId) throws SQLException
    {
        String sql = "UPDATE customers set PhoneNumber = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,pno);
            ps.setInt(2,customerId);;
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updatePanNumber(Long panNumber, int customerId) throws SQLException
    {
        String sql = "UPDATE customers set PanNumber = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1,panNumber);
            ps.setInt(2,customerId);;
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateAadharNumber(long aadharNumber, int customerId) throws SQLException
    {
        String sql = "UPDATE customers set AadharNumber = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1,aadharNumber);
            ps.setInt(2,customerId);;
            //execute the query
            ps.executeUpdate();
        }
    }

    public void updateAddress(String address, int customerId) throws SQLException
    {
        String sql = "UPDATE customers set Address = ? WHERE CustomerID =?";
        try(Connection connection  = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1,address);
            ps.setInt(2,customerId);
            //execute the query
            ps.executeUpdate();
        }
    }
}
