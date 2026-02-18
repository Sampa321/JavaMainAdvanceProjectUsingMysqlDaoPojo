package receipts;

import model.Transaction;


import java.io.*;

public class ReceiptGenerator {
    private static String path = "src/receipts/GenerateReceipts/";
    public static void generateReceipt(Transaction t){
      try{
          File dir = new File(path);   //just create object for directory
          if(!dir.exists())
          {
              dir.mkdirs();  //If not available parent directory then use mkdirs() that create parent directory other,if available parent directory you can use mkdir().
          }
          String fileName = "Receipt_"+t.getAccountNumber()+"_"+System.currentTimeMillis()+".txt";
          File receiptFile = new File(dir,fileName);
          try(BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))){  //You use bufferWriter then increase your performance
              writer.write("========================== GG BANK ==========================");
              writer.newLine();
              writer.write("Date of transaction : "+t.getTransactionDate());
              writer.newLine();
              writer.write("Transaction Type    : "+t.getTransactionType());
              writer.newLine();
              if(t.getTransactionType().equals("Transfer"))
              {
                  writer.write("From                    : "+t.getRelatedAccountNumber());
                  writer.newLine();
                  writer.write("To(your Account)         : "+t.getAccountNumber());
                  writer.newLine();
              }
              writer.write("Amount              : ₹"+t.getAmount());
              writer.newLine();
              writer.write("Description         : "+t.getDescription());
              writer.newLine();
              writer.write("=================================================================");
          }
      }catch (SecurityException | IOException e)
      {
          System.out.println("Error :Failed to generate receipt due to "+e.getMessage()+" problem");
      }
    }
}
