package dao;

import model.Transaction;
import util.DBUtil;

import java.sql.*;

public class TransactionDAO {
    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (AccountNumber,TransactionType, Amount,TransactionDate, RelatedAccountNumber,Description) VALUES(?,?,?,?,?,?)";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
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
}
