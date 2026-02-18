package dao;

import model.Transaction;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDate, RelatedAccountNumber, Description) VALUES(?,?,?,?,?,?)";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
        ps.setLong(1, t.getAccountNumber());
        ps.setString(2, t.getTransactionType());
        ps.setDouble(3, t.getAmount());
        ps.setTimestamp(4, Timestamp.valueOf(t.getTransactionDate()));
        if(t.getRelatedAccountNumber() != 0)
        {
            ps.setLong(5,t.getRelatedAccountNumber());
        }
        else {
            ps.setNull(5, Types.BIGINT);
        }
        ps.setString(6,t.getDescription());
        ps.executeUpdate();
        }
    }

    public void addTransferTransaction(Transaction t,Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (AccountNumber,TransactionType, Amount,TransactionDate, RelatedAccountNumber,Description) VALUES(?,?,?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setLong(1,t.getAccountNumber());
            ps.setString(2,t.getTransactionType());
            ps.setDouble(3,t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTransactionDate()));
            if(t.getRelatedAccountNumber() != 0)
            {
                ps.setLong(5,t.getRelatedAccountNumber());
            }
            else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setString(6,t.getDescription());
            ps.executeUpdate();
        }
    }


    public List<Transaction> getAllTransaction(long accNumber) throws SQLException
    {
        List<Transaction> allTransactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE AccountNumber = ? ORDER BY TransactionDate";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
        ){
            ps.setLong(1,accNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //Create a transaction Object and load all the details from the resultSet rs and add it to the arrayList.
                Transaction t = new Transaction(
                        rs.getLong("AccountNumber"),
                        rs.getString("TransactionType"),
                        rs.getDouble("Amount"),
                        rs.getTimestamp("TransactionDate").toLocalDateTime(),
                        rs.getLong("RelatedAccountNumber"),
                        rs.getString("Description")
                );
                t.setTransactionID(rs.getInt("TransactionID"));
                allTransactions.add(t);
            }
        }
        return allTransactions;
    }
}
