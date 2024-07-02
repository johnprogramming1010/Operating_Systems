import java.util.concurrent.*;

class CarThread1 extends Thread{
    Semaphore sem;
    String threadName;
    int carCount;
    public CarThread1(Semaphore sem, int carCount) {
        this.sem = sem;
        this.carCount = carCount;
    }
 
    @Override
    public void run() {
            try {
                System.out.println("Right-bound Car " + carCount + " wants to enter the tunnel");
                Thread.sleep(1000);
                // acquiring the lock
                sem.acquire();
             
                System.out.println("Right-bound Car " + carCount + " is in the tunnel.");
                    Thread.sleep(1000);
            } catch (InterruptedException ie) {
                    System.out.println(ie);
                }
                System.out.println("Right-bound Car " + carCount + " is exiting the tunnel.");
                sem.release();
    }
   
}

class CarThread2 extends Thread{
	Semaphore sem;
    String threadName;
    int carCount;
    public CarThread2(Semaphore sem, int carCount) {
        this.sem = sem;
        this.carCount = carCount;
    }
    public void run() {
    	try {
         System.out.println("Left-bound Car " + carCount + " wants to enter the tunnel.");
          Thread.sleep(1000);
         // acquiring the lock
         sem.acquire();
       
         System.out.println("Left-bound Car " + carCount + " is in the tunnel.");
             Thread.sleep(1000);

     } catch (InterruptedException ie) {
             System.out.println(ie);
         }

         System.out.println("Left-bound Car " + carCount + " is exiting the tunnel.");
         sem.release();
    }
}
 

public class Project1 {
    public static void main(String args[]) throws InterruptedException {
    	int localCarCount = 1;
        Semaphore sem = new Semaphore(1);
        while(true){
        	   CarThread1 rightThread = new CarThread1(sem, localCarCount);
            CarThread2 leftThread = new CarThread2(sem, localCarCount+1);
            rightThread.start();
            leftThread.start();
            localCarCount += 2;
       
            rightThread.join();
            leftThread.join();
        }
    }
}