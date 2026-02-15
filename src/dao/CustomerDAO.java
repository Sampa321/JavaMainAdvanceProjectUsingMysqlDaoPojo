package dao;

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


}
