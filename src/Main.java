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
           System.out.println("Choose one of the following options : ");
           System.out.println("1.Create new account");
           System.out.println("2.Close account");
           System.out.println("3.Withdraw money");
           System.out.println("4.Deposit Money");
           System.out.println("5.Transfer money to another account");
           System.out.println("6.View transaction history");
           System.out.println("7.View Account Details");
           System.out.println("8.Update Account Details");
           System.out.println("9.EXIT");
           System.out.println("=================================================");
           System.out.print("Enter your choice : ");
           choice = sc.nextInt();
           sc.nextLine();
           System.out.println("=============================================");
           switch (choice)
           {
               case 1:
                   System.out.print("Enter Account holder's first name  : ");
                   String firstName = sc.nextLine();
                   System.out.print("Enter Account holder's last name   : ");
                   String lname = sc.nextLine();
                   System.out.print("Enter Account holder's email       : ");
                   String email = sc.nextLine();
                   System.out.print("Enter Account holder's phone number : ");
                   String pno = sc.nextLine();
                   System.out.print("Enter Account holder's address      : ");
                   String address = sc.nextLine();
                   service.createAccount(firstName, lname, email, pno, address);
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
                   System.out.print("Enter sender bank account number : ");
                   accNumber = sc.nextLong();
                   System.out.print("Enter receiver bank account number : ");
                   long reAccNumber = sc.nextLong();
                   System.out.print("Enter amount : ");
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
                   System.out.print("Enter Phone number :");
                   String phoneNumber = sc.next();
                   boolean checkPhoneNumber = service.checkPhoneNumber(phoneNumber);
                   while (!checkPhoneNumber)
                   {
                       System.out.println("Please enter the correct Account Number !!");
                       phoneNumber = sc.next();
                       checkPhoneNumber = service.checkPhoneNumber(phoneNumber);
                   }
                   char option;
                   do{
                       System.out.println("-------------------------------------------------------------------------------------");
                       System.out.println("Choose one of the following update :");
                       System.out.println("A.First Name\nB.Last Name\nC.Email\nD.Phone Number\nE.Address\nF.Exit from update");
                       System.out.print("Enter the option : ");
                       option = sc.next().charAt(0);
                       option = Character.toLowerCase(option);
                       sc.nextLine();
                       switch (option)
                       {
                           case 'a':
                               System.out.print("Enter First name : ");
                               firstName = sc.nextLine();
                               service.updateFirstName(firstName,phoneNumber);
                               break;
                           case 'b':
                               System.out.print("Enter last name : ");
                               lname = sc.nextLine();
                               service.updateLastName(lname,phoneNumber);
                               break;
                           case 'c':
                               System.out.print("Enter email : ");
                               email = sc.nextLine();
                               service.updateEmail(email,phoneNumber);
                               break;
                           case 'd':
                               System.out.print("Enter phone number : ");
                               pno = sc.nextLine();
                               service.updatePhoneNumber(pno,phoneNumber);
                               break;
                           case 'e':
                               System.out.print("Enter address : ");
                               address = sc.nextLine();
                               service.updateAddress(address,phoneNumber);
                               break;
                           case 'f':
                               System.out.println("Exists from update!!\nThank You !!");
                               break;
                           default:
                               System.out.println("Wrong option choose!!");
                       }
                   }while (option != 'f');
                   break;
               case 9:
                   System.out.print("Thank you for visiting GG bank!\nHave a wonderful day ahead.");
                   System.out.println();
                   System.out.println("===============================================================");
                   break;
               default:
                   System.out.print("Invalid choice!!");
                   System.out.println("Please enter the correct Option !!");
                   break;
           }
       }while (choice != 9);
    }
}
