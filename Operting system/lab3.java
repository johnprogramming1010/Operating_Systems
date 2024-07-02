import java.util.*;

/* This lab will be done in two trials. In the first trials we will work with
the hard-coded table and run it as is. Then we will add lines 49 and 54. The 
second trial is your responsibility. You will comment out the hard-coded table 
and use the table_gen() method. You also need to change line 51-56 (thread 
creation and start) to use an array of threads. A loop will also be needed. 
Additionally, you need to use the join() method appropriately, so that the 
main() does not finish before all threads complete their sorting task. */

//Type your name here: John James

public class lab3{

//   static int table[][] = {{2, 1, 8, 7, 9, 6, 5, 4, 3},
//                           {16, 23, 45, 33, 99, 89, 67, 77}, 
//                           {101, 234, 541, 789, 333, 492, 829, 699, 978}
//                           };
   
    
   static int[][] table_gen(int c){
      int[][] temptab = new int[10][c];
      Random rand = new Random();
      for(int i=0;i<10; i++){
         for(int j=0;j<c; j++){
            temptab[i][j] = rand.nextInt(1000);
         }
      }
      return temptab;
   }
   
   public static void printTable(int[][] tab){  
      for(int x=0; x<tab.length; x++){
      System.out.print("Row-"+ x + ":");
         for(int y=0; y<tab[x].length; y++)
            System.out.print(tab[x][y] + " ");
      System.out.println();
      }    
    }  
   
   //Once the trial with hard-coded array is done, comment that part and 
   // uncomment the table_gen() method; uncomment the following line also
   
   static int[][] table = table_gen(15); // replace z by a number > 10

   public static void main(String[] args){
      
      System.out.println("\nThis is: " + Thread.currentThread().getName()); //To indicate the thread identity
      
	  // print table before sorting  
	  System.out.println("Table before sorting..");
	  printTable(table);
      
    //  First trial
    //  // add another thread to sort row 0
    //  Thread th0 = new Thread(new sorters(0)); // thread sorting row 1
    //  th0.start();
    //  try{
    //      th0.join();
    //  }catch(Exception e){}
	  
    //  Thread th1 = new Thread(new sorters(1)); // thread sorting row 1
    //  th1.start(); 
    //  try{
    //      th1.join();
    //  }catch(Exception e){}
     
    //  //add another thread to sort row 2
    //  Thread th2 = new Thread(new sorters(2)); // thread sorting row 1
    //  th2.start(); 
    //  try{
    //      th2.join();
    //  }catch(Exception e){}
     
    //Do we need thread join()?
    //  Second trial
    // You also need to change line 51-56 (thread 
    // creation and start) to use an array of threads. A loop will also be needed. 
    // Additionally, you need to use the join() method appropriately, so that the 
    // main() does not finish before all threads complete their sorting task. */
        Thread[] threads = new Thread[10];
        for (int i = 0; i<10; i++){
            threads[i] = new Thread(new sorters(i));
            threads[i].start();
            try{
                threads[i].join();
            }catch(Exception e){}
        }
      
	    System.out.println("Table after sorting..");
        printTable(table);
    }  


   static class sorters implements Runnable{
      int row;
   
      public sorters(int r){
         row = r;
      }
   
      public void run(){
         System.out.println("\nThis is: " + Thread.currentThread().getName());
         Arrays.sort(table[row]);
      }
   }

}