package service;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import exception.AccountClosedException;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;
import model.Account;
import model.Customer;
import model.Transaction;
import receipts.ReceiptGenerator;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BankService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    long generateAccountNumber(){
        long min = 1000000000L;
        long max = 9999999999L;
        return min + (long) (Math.random()*(max-min));
    }

    public void createAccount(String fname,String lname,String email,String phoneNumber,String address){
      try{
          //create a new customer record
          Customer customer = new Customer(fname,lname,email,phoneNumber,address);
          //then get the customerID
          int customerId = customerDAO.createCustomer(customer);
          if(customerId == -1)
          {
              System.out.println("Failed to create customer entry!!");
              return;
          }
          //use the customerId to create a new record bankAccounts table
          long accNumber  = this.generateAccountNumber();
          Account account = new Account(accNumber,customerId,"Savings",0.0,"Active", LocalDate.now());
          if(accountDAO.createAccount(account)){
              System.out.println("Account created successfully.\nYour account Number :"+accNumber);
          }
          else {
              System.out.println("Failed to create bank account");
          }
      }catch (SQLException e){
          System.out.println("Error : "+e.getMessage());
      }
    }
    public void closeAccount(Long accountNumber){
        try {
            //search the specified bank account.
            Account acc = accountDAO.getAccount(accountNumber);
            if(acc == null)
            {
                throw new AccountNotFoundException("Account does not exist at GG Bank");
            }

            //bank account already exists, But, it's already closed.
            if(acc.getStatus().equalsIgnoreCase("closed"))
            {
                throw new AccountClosedException("Account already closed");
            }
            //acc.setStatus("closed");  it is not used because it is closed in java not Database
            //closed the account
            if(accountDAO.closedAccount(accountNumber)){
                System.out.println("Account closed successfully.\nYour account Number :"+accountNumber);
            }
            else {
                System.out.println("Failed to closed bank account");
            }

        }
        catch (AccountNotFoundException | AccountClosedException | SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }
    }
    public void withdraw(long accNumber,double amount)
    {
        try {
            if(amount<=0)
            {
                throw new InvalidAmountException("Amount must be greater than 0");
            }
            //search the specific bank account
            Account acc = accountDAO.getAccount(accNumber);
            if(acc == null)
            {
                throw new AccountNotFoundException("Account does not exist at GG Bank");
            }

            //Bank account already exists.BUT, It is already closed.
            if(acc.getStatus().equalsIgnoreCase("closed"))
            {
                throw new AccountClosedException("Account already closed");
            }

            //2 ACCOUNT TYPES - Savings, current.
            //Savings account - overdraft limit = 0
            //current account - overdraft limit = 5000, bank balance = -5000
            double currentAccountBalance = acc.getBalance();
            double overdraftLimit = 0;
            if(acc.getAccountType().equalsIgnoreCase("current"))
            {
                overdraftLimit = -5000;
            }
            if(currentAccountBalance - amount <overdraftLimit)
            {
                throw new InvalidAmountException("Withdraw amount exceeds the overdraft limit. Cannot initiate transaction.");
            }
            else {
                //current account withdraw logic
                //first update the property - balance - in the account object.
                 acc.setBalance(acc.getBalance()-amount);

                 //Call AccountDao object to update the balance field in the DB by using the Account object.
                accountDAO.updateBalance(acc);

                //long accountNumber, String transactionType, double amount, LocalDateTime transactionDate, long relatedAccount, String description
                Transaction t = new Transaction(accNumber,"Withdrawal",amount, LocalDateTime.now(),0,"Withdrawal from account");
                transactionDAO.addTransaction(t);

                //receipt generation
                ReceiptGenerator.generateReceipt(t);
                //completion of balance update message
                System.out.println("Withdrawal successfully!\nWithdrawal amount ; "+amount + "\nAvailable Balance : "+acc.getBalance());
            }
        }catch (InvalidAmountException | AccountNotFoundException | AccountClosedException | SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
    public void deposit(long accNumber,double amount)
    {
        try {
            if(amount<=0)
            {
                throw new InvalidAmountException("Account must be greater than 0");
            }
            //search the specific bank account
            Account acc = accountDAO.getAccount(accNumber);
            if(acc == null)
            {
                throw new AccountNotFoundException("Amount does not exist at GG Bank");
            }

            //Bank account already exists.BUT, It is already closed.
            if(acc.getStatus().equalsIgnoreCase("closed"))
            {
                throw new AccountClosedException("Account already closed");
            }

            //first update the property - balance - in the account object.
            acc.setBalance(acc.getBalance()+amount);

            //Call AccountDao object to update the balance field in the DB by using the Account object.
            accountDAO.updateBalance(acc);

            //long accountNumber, String transactionType, double amount, LocalDateTime transactionDate, long relatedAccount, String description
            Transaction t = new Transaction(accNumber,"Deposit",amount, LocalDateTime.now(),0,"Deposit to account");
            transactionDAO.addTransaction(t);

            //receipt generation
            ReceiptGenerator.generateReceipt(t);
            //completion of balance update message
            System.out.println("Deposit successfully!\nDeposit amount ; "+amount + "\nAvailable Balance : "+acc.getBalance());
        }catch (InvalidAmountException | AccountNotFoundException | AccountClosedException | SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }

    }
    public void  transfer(long accNumber,long reAccNumber,double amount)
    {
        try(Connection conn = DBUtil.getConnection()) {
            Account senderAcc = accountDAO.getAccount(accNumber);
            //get senders account
            if(senderAcc == null)
            {
                throw new AccountNotFoundException("Sender Account does not exists at GG Bank");
            }
            //get receivers account
            Account receiverAcc = accountDAO.getAccount(reAccNumber);
            if(receiverAcc == null)
            {
                throw new AccountNotFoundException("Receiver Account does not exists at GG Bank");
            }

            //check if these accounts are valid or not.
            if(senderAcc.getStatus().equalsIgnoreCase("Closed"))
            {
                throw new AccountClosedException("Sender Account already closed!!");
            }
            if(receiverAcc.getStatus().equalsIgnoreCase("Closed"))
            {
                throw new AccountClosedException("Receiver Account already closed!!");
            }


            //check if specified withdrawal amount is less than the permitted limits.
            double overdraftLimit = 0;
            double currentAccountBalance = senderAcc.getBalance();
            if(senderAcc.getAccountType().equalsIgnoreCase("current"))
            {
                overdraftLimit = -5000;
            }
            if(currentAccountBalance - amount <overdraftLimit)
            {
                throw new InvalidAmountException("Withdrawal amount exceeds the permitted limits.\nCannot initiate transaction. ");
            }
            else{
                 try {
                     conn.setAutoCommit(false);
                     //deduct from senders account, create a transaction
                     //first update the property - balance - in the account object.
                     senderAcc.setBalance(senderAcc.getBalance()-amount);

                     //Call AccountDao object to update the balance field in the DB by using the Account object.
                     boolean senderAccountUpdateState = accountDAO.transactionUpdateBalance(senderAcc,conn);

                     //long accountNumber, String transactionType, double amount, LocalDateTime transactionDate, long relatedAccount, String description
                     Transaction t1 = new Transaction(accNumber,"Transfer",amount, LocalDateTime.now(),reAccNumber,"Withdrawal from account for transfer");
                     transactionDAO.addTransferTransaction(t1,conn);


                     //if yes, then initiate transaction
                     //add amount to receivers account, create a transaction
                     //first update the property - balance - in the account object.
                     receiverAcc.setBalance(receiverAcc.getBalance()+amount);

                     //Call AccountDao object to update the balance field in the DB by using the Account object.
                     boolean receiverAccountUpdateState =  accountDAO.transactionUpdateBalance(receiverAcc,conn);

                     //long accountNumber, String transactionType, double amount, LocalDateTime transactionDate, long relatedAccount, String description
                     Transaction t2 = new Transaction(reAccNumber,"Transfer",amount, LocalDateTime.now(),accNumber,"Deposit to account via transfer");
                     transactionDAO.addTransferTransaction(t2,conn);

                     if(senderAccountUpdateState && receiverAccountUpdateState)
                     {
                         conn.commit();  //save in database if all transaction success otherwise, rollBack()
                         System.out.println("Transaction successfully!!\nAvailable balance : "+senderAcc.getBalance());
                     }
                     //receipt generation for t1 and t2
                     ReceiptGenerator.generateReceipt(t1);
                     ReceiptGenerator.generateReceipt(t2);

                 }catch (SQLException e)
                 {
                     conn.rollback();
                     System.out.println("Transaction failed : "+e.getMessage());
                     System.out.println("All operation rollback ");
                 }
            }
        }catch (AccountNotFoundException | AccountClosedException | InvalidAmountException | SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
    public void transactionHistory(long accNumber)
    {
        try{
            //check if current account exists in DB, get the bankAccount
            Account senderAcc = accountDAO.getAccount(accNumber);
            if(senderAcc == null)
            {
                throw new AccountNotFoundException("Account does not exists at GG Bank .");
            }
            // Bank account already exists. BUT, it's already closed.
            if(senderAcc.getStatus().equalsIgnoreCase("Closed"))
            {
                throw new AccountClosedException("Account already closed!");
            }

            //if account is available then, get all transaction related to this bankAccount.
            //get the list of all the transaction for this account.
            List<Transaction> allTransaction = transactionDAO.getAllTransaction(accNumber);
            if(allTransaction.isEmpty())
            {
                System.out.println("No transaction were performed for Account - "+accNumber);
            }else {
                for(Transaction t : allTransaction)
                {
                    System.out.println();
                    System.out.println("Date of transaction : "+t.getTransactionDate());
                    System.out.println("Transaction Type    : "+t.getTransactionType());
                    if(t.getTransactionType().equals("Transfer"))
                    {
                        System.out.println("From                    : "+t.getRelatedAccountNumber());
                        System.out.println("To(your)                : "+t.getAccountNumber());
                    }
                    System.out.println("Amount              : ₹"+t.getAmount());
                    System.out.println("Description         : "+t.getDescription());
                    System.out.println();
                }
            }
        }catch(AccountNotFoundException | AccountClosedException | SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }

    public void accDetails(long accNumber){
        try {
            Account acc = accountDAO.getAccount(accNumber);

            if(acc == null)
            {
                throw new AccountNotFoundException("Account does not exists at GG Bank");
            }
            System.out.println("==========================================");
            System.out.println("-----------Account Details----------------");
            System.out.println("Account Number       : "+accNumber);
            System.out.println("Account Type         : "+acc.getAccountType());
            System.out.println("Account Status       : "+acc.getStatus());
            System.out.println("Account Balance      : "+acc.getBalance());
            System.out.println("Account Opening Date : "+acc.getOpeningDate());
            System.out.println("==========================================");
        }
        catch (AccountNotFoundException | SQLException e)
        {
            System.out.println("Error :"+e.getMessage());
        }
    }


    public boolean checkPhoneNumber(String  phoneNumber)
    {
        boolean checkAcc = true;
        try {
            Customer customer = customerDAO.getPhoneNumber(phoneNumber);
            if (customer == null) {
                checkAcc = false;
                throw new AccountNotFoundException("phone number does not exists at GG Bank");
            }
        }
         catch (AccountNotFoundException | SQLException e)
         {
             System.out.println("Error : "+e.getMessage());
         }
        return checkAcc;
    }

    public void updateFirstName(String fName, String pno) {

        try {
            Customer customer = customerDAO.getPhoneNumber(pno);
            customer.setFirstName(fName);
            customerDAO.updateFirstName(customer);
        }catch (SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }


    public void updateLastName(String lName,String pno)
    {
        try {
            Customer customer = customerDAO.getPhoneNumber(pno);
            customer.setLastName(lName);
            customerDAO.updateLastName(customer);
        }catch (SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }

    public void updateEmail(String email,String pno){
        try {
            Customer customer = customerDAO.getPhoneNumber(pno);
            customer.setEmail(email);
            customerDAO.updateEmail(customer);
        }catch (SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }

    public void updatePhoneNumber(String phoneNumber,String pno){

        try {
            Customer customer = customerDAO.getPhoneNumber(pno);
            customer.setPhone(phoneNumber);
            customerDAO.updatePhoneNumber(customer);
        }catch (SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }

    public void updateAddress(String address,String pno){
        try {
            Customer customer = customerDAO.getPhoneNumber(pno);
            customer.setAddress(address);
            customerDAO.updateAddress(customer);
        }catch (SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }
    }
}
