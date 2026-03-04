import service.BankService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);
        int choice;
        int input;
        do {
            System.out.println("1.You want to create a new account !!\n2.You have already an account.\n3.For exit");
            System.out.print("Choose the input(1 or 2 or 3)           : ");
            input = sc.nextInt();
            while (input !=1 && input!=2 && input!=3)
            {
                System.out.println("Wrong input!!");
                System.out.print("Choose the input(1 or 2 or 3)           : ");
                input = sc.nextInt();
            }
            sc.nextLine();
            switch (input) {
                case 1:
                    System.out.print("Enter Account holder's first name       : ");
                    String firstName = sc.nextLine();
                    while (firstName.isEmpty()) {
                        System.out.print("Enter the correct First name            : ");
                        firstName = sc.nextLine();
                    }
                    System.out.print("Enter Account holder's middle name      : ");
                    String mName = sc.nextLine();

                    System.out.print("Enter Account holder's last name        : ");
                    String lname = sc.nextLine();
                    while (lname.isEmpty()) {
                        System.out.print("Enter the correct last name             : ");
                        lname = sc.nextLine();
                    }
                    System.out.print("Enter Account holder's email            : ");
                    String email = sc.nextLine();
                    while (!email.contains("@gmail.com")) {
                        System.out.print("Enter tha correct email address         : ");
                        email = sc.nextLine();
                    }
                    System.out.print("Enter Account holder's phone number     : ");
                    String pno = sc.nextLine();
                    while (pno.length() != 10) {
                        System.out.print("Please enter correct phone Number       : ");
                        pno = sc.nextLine();
                    }
                    System.out.print("Enter Account holder's Pan  number      : ");
                    System.out.print("                                     : ");
                    Long panNumber = sc.nextLong();
                    while (panNumber == 0) {
                        System.out.print("Please enter correct pan Number         : ");
                        panNumber = sc.nextLong();
                    }
                    System.out.print("Enter Account holder's aadhar number    : ");
                    Long aadharNumber = sc.nextLong();
                    while (aadharNumber == 0) {
                        System.out.print("Please enter correct aadhar Number      : ");
                        aadharNumber = sc.nextLong();
                    }
                    System.out.print("Enter Account holder's address          : ");
                    sc.nextLine();
                    String address = sc.nextLine();
                    while (address.isEmpty()) {
                        System.out.print("Enter Account holder's address          : ");
                        address = sc.nextLine();
                    }
                    System.out.print("Enter Account holder's password         : ");
                    String password = sc.nextLine();
                    service.createAccount(firstName, mName, lname, email, pno, panNumber, aadharNumber, address, password);
                    break;
                case 2:
                    System.out.print("Enter the email                         : ");
                    String email1 = sc.nextLine();
                    System.out.print("Enter your password                     : ");
                    String password1 = sc.nextLine();
                    while ((service.checkEmail(email1) < 0 || service.checkPassword(password1) < 0) && (service.checkPassword(password1) != service.checkEmail(email1))) {
                        System.out.println("Any error email or password!!");
                        System.out.print("Enter the correct email                 : ");
                        email1 = sc.nextLine();
                        System.out.print("Enter the correct password              : ");
                        password1 = sc.nextLine();
                    }
                    int customerId = service.checkEmail(email1);
                    System.out.println(customerId);
                    System.out.println(service.checkPassword(password1));
                    long bankAccNumber = service.getAccountNumber(customerId);
                    do{
                        System.out.println("\n=========================================");
                        System.out.println("Welcome to GG Bank");
                        System.out.println("=================================================");
                        System.out.println("Choose one of the following options : ");
                        System.out.println("1.Change Password");
                        System.out.println("2.Close account");
                        System.out.println("3.Withdraw money");
                        System.out.println("4.Deposit Money");
                        System.out.println("5.Transfer money to another account");
                        System.out.println("6.View transaction history");
                        System.out.println("7.View Account Details");
                        System.out.println("8.Update Account Details");
                        System.out.println("9.EXIT");
                        System.out.println("=================================================");
                        System.out.print("Enter your choice                       : ");
                        choice = sc.nextInt();
                        sc.nextLine();
                        System.out.println("=============================================");
                        switch (choice) {
                            case 1:
                                sc.nextLine();
                                System.out.print("Enter new Password                      : ");
                                password = sc.nextLine();
                                while (password.isEmpty()) {
                                    System.out.print("Enter the correct password              : ");
                                    password = sc.nextLine();
                                }
                                service.updatePassword(password, bankAccNumber);
                                break;

                            case 2:
                                service.closeAccount(bankAccNumber);
                                break;
                            case 3:
                                System.out.print("Enter the amount to withdraw            : ");
                                double amount = sc.nextDouble();
                                service.withdraw(bankAccNumber, amount);
                                break;
                            case 4:
                                System.out.print("Enter the amount to deposit             : ");
                                amount = sc.nextDouble();
                                service.deposit(bankAccNumber, amount);
                                break;
                            case 5:
                                System.out.print("Enter receiver bank account number      : ");
                                long reAccNumber = sc.nextLong();
                                System.out.print("Enter amount                            : ");
                                amount = sc.nextDouble();
                                service.transfer(bankAccNumber, reAccNumber, amount);
                                break;
                            case 6:
                                service.transactionHistory(bankAccNumber);
                                break;
                            case 7:
                                service.accDetails(bankAccNumber);
                                break;
                            case 8:
                                boolean checkAccount = service.checkBankAccountNumber(bankAccNumber);
                                while (!checkAccount) {
                                    System.out.print("Please enter the correct Account Number : ");
                                    bankAccNumber = sc.nextLong();
                                    checkAccount = service.checkBankAccountNumber(bankAccNumber);
                                }
                                char option;
                                do {
                                    System.out.println("-------------------------------------------------------------------------------------");
                                    System.out.println("Choose one of the following update :");
                                    System.out.println("A.First Name\nB.Middle Name\nC.Last Name\nD.Email\nE.Phone Number\nF.Pan Number\nG.Aadhar Number\nH.Address\nI.Exit from update");
                                    System.out.print("Enter the option : ");
                                    option = sc.next().charAt(0);
                                    option = Character.toLowerCase(option);
                                    sc.nextLine();
                                    switch (option) {
                                        case 'a':
                                            System.out.print("Enter First name                        : ");
                                            firstName = sc.nextLine();
                                            while (firstName.isEmpty()) {
                                                System.out.print("Enter the correct First name            : ");
                                                firstName = sc.nextLine();
                                            }
                                            service.updateFirstName(firstName, bankAccNumber);
                                            break;
                                        case 'b':
                                            System.out.print("Enter the middle name                  : ");
                                            mName = sc.nextLine();
                                            service.updateMiddleName(mName, bankAccNumber);
                                            break;
                                        case 'c':
                                            System.out.print("Enter last name                        : ");
                                            lname = sc.nextLine();
                                            while (lname.isEmpty()) {
                                                System.out.print("Enter the correct last name             : ");
                                                lname = sc.nextLine();
                                            }
                                            service.updateLastName(lname, bankAccNumber);
                                            break;
                                        case 'd':
                                            System.out.print("Enter email                             : ");
                                            email = sc.nextLine();
                                            while (!email.contains("@gmail.com")) {
                                                System.out.println("Enter tha correct email address       : ");
                                                email = sc.nextLine();
                                            }
                                            service.updateEmail(email, bankAccNumber);
                                            break;
                                        case 'e':
                                            System.out.print("Enter phone number                      : ");
                                            pno = sc.nextLine();
                                            while (pno.length() != 10) {
                                                System.out.print("Please enter correct phone Number       : ");
                                                pno = sc.nextLine();
                                            }
                                            service.updatePhoneNumber(pno, bankAccNumber);
                                            break;
                                        case 'f':
                                            System.out.print("Enter pan number                       : ");
                                            panNumber = sc.nextLong();
                                            while (panNumber == 0) {
                                                System.out.print("Enter the correct pan Number            : ");
                                                panNumber = sc.nextLong();
                                            }
                                            service.updatePanNumber(panNumber, bankAccNumber);
                                            break;
                                        case 'g':
                                            System.out.print("Enter aadhar number                    : ");
                                            aadharNumber = sc.nextLong();
                                            while (aadharNumber == 0) {
                                                System.out.print("Enter the correct aadhar Number         : ");
                                                aadharNumber = sc.nextLong();
                                            }
                                            service.updateAadharNumber(aadharNumber, bankAccNumber);
                                            break;
                                        case 'h':
                                            System.out.print("Enter address                           : ");
                                            address = sc.nextLine();
                                            while (address.isEmpty()) {
                                                System.out.println("Enter the correct address             : ");
                                                address = sc.nextLine();
                                            }
                                            service.updateAddress(address, bankAccNumber);
                                            break;
                                        case 'i':
                                            System.out.println("Exists from update!!\nThank You !!");
                                            break;
                                        default:
                                            System.out.println("Wrong option choose!!");
                                    }
                                } while (option != 'i');
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
                    }
                    while (choice != 9) ;
                case 3:
                    System.out.println("Thank you !!");
                    break;
            }
        }while (input!=3);
    }
}

