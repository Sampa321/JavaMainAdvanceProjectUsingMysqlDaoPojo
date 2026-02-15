import service.BankService;

import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
       BankService service = new BankService();
       Scanner sc = new Scanner(System.in);
       int choice;
       do{
           System.out.println("\n=========================================");
           System.out.println("Welcome to GG Bank");
           System.out.println("=================================================");
           System.out.println("Choose one of the following option : ");
           System.out.println("1.Create new account");
           System.out.println("2.Close account");
           System.out.println("3.Withdraw money");
           System.out.println("4.Deposit Money");
           System.out.println("5.Transfer money to another account");
           System.out.println("6.View transaction history");
           System.out.println("7.View Account Details");
           System.out.println("8.Update Account Details");
           System.out.println("9.EXIT");

           System.out.print("Enter your choice: ");
           choice = sc.nextInt();
           sc.nextLine();
           switch (choice)
           {
               case 1:
                   System.out.print("Enter Account holder's first name : ");
                   String firstName = sc.nextLine();
                   System.out.print("Enter Account holder's last name : ");
                   String lname = sc.nextLine();
                   System.out.print("Enter Account holder's email : : ");
                   String email = sc.nextLine();
                   System.out.print("Enter Account holder's phone number : ");
                   String pno = sc.nextLine();
                   System.out.print("Enter Account holder's address : ");
                   String address = sc.nextLine();
                   service.createAccount(firstName,lname,email,pno,address);
                   break;
               case 2:
                   System.out.print("Enter bank account number :");
                   long accNumber = sc.nextLong();
                   service.closeAccount(accNumber);
                   break;
               case 3:
                   System.out.print("Enter bank account number :");
                   accNumber = sc.nextLong();
                   System.out.print("Enter the amount to withdraw :");
                   double amount = sc.nextDouble();
                   service.withdraw(accNumber,amount);
                   break;
               case 4:
                   System.out.print("Enter bank account number :");
                   accNumber = sc.nextLong();
                   System.out.print("Enter the amount to deposit :");
                   amount = sc.nextDouble();
                   service.deposit(accNumber,amount);
                   break;
               case 5:
                   System.out.print("Enter your bank account number : ");
                   accNumber = sc.nextLong();
                   System.out.print("Enter receiver bank account number : ");
                   long reAccNumber = sc.nextLong();
                   System.out.println("Enter amount : ");
                   amount = sc.nextDouble();
                   service.transfer(accNumber,reAccNumber,amount);
                   break;
               case 6:
                   System.out.print("Enter bank account number :");
                   accNumber = sc.nextLong();
                   service.transactionHistory(accNumber);
                   break;
               case 7:
                   System.out.print("Enter bank account number :");
                   accNumber = sc.nextLong();
                   service.accDetails(accNumber);
                   break;
               case 8:
                   System.out.print("Enter First name : ");
                   firstName = sc.nextLine();
                   System.out.print("Enter last name : ");
                   lname = sc.nextLine();
                   System.out.println("Enter email : : ");
                   email = sc.nextLine();
                   System.out.println("Enter phone number : ");
                   pno = sc.nextLine();
                   System.out.print("Enter address : ");
                   address = sc.nextLine();
                   service.updateCustomerDetails(firstName,lname,email,pno,address);
               case 9:
                   System.out.print("Thank you for visiting GG bank");
                   break;
               default:
                   System.out.print("Invalid choice!!");
           }
       }while (choice!=9);
    }
}
