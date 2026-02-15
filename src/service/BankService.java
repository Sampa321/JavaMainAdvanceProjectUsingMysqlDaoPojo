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

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

            //completion of balance update message
            System.out.println("Deposit successfully!\nDeposit amount ; "+amount + "\nAvailable Balance : "+acc.getBalance());
        }catch (InvalidAmountException | AccountNotFoundException | AccountClosedException | SQLException e)
        {
            System.out.println("Error : "+e.getMessage());
        }

    }
    public void  transfer(long accNumber,long reAcc,double amount)
    {
        //get senders account
        //get receivers account
        //check if these accounts are valid or not.
        //check if specified withdrawal amount is less than the permitted limits.
        //if yes, then initiate transaction
        //deduct from senders account, create a transaction
        //add amount to receivers account, create a transaction
        //
    }
    public void transactionHistory(long accNumber)
    {

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
            System.out.format("Account Number : "+accNumber+"\nAccount Type :"+acc.getAccountType()+"\nAccount Status : "+acc.getStatus()+"\nAccount Balance :"+acc.getBalance()+"\nAccount Opening Date :"+acc.getOpeningDate());
            System.out.println("==========================================");
        }
        catch (AccountNotFoundException | SQLException e)
        {
            System.out.println("Error :"+e.getMessage());
        }
    }
    public void updateCustomerDetails(String fname,String lname,String  email,String pno,String address){

    }
}
