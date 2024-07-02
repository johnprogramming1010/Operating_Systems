// Project 1
// Authors: John James and Jacob Carlstrom
import java.util.concurrent.Semaphore;

public class Project1_James_Carlstrom {
    private static Semaphore east_Semaphore = new Semaphore(1);
    private static Semaphore west_Semaphore = new Semaphore(1);    
    private static int eastbound = 1;
    private static int westbound = 2;
    private static int carNum = 0;
    private static int num_Eastbound_Cars = 0;
    private static int num_Westbound_Cars = 0;
    private static boolean can_Eastbound_Pass = true;
    private static boolean can_Westbound_Pass = true;
    
    public static void main(String[] args) {
        int eastCarCount = 1;
        int westCarcount = 2;
        while (true) {
            carNum++;
            for (int i = 0; i < carNum; i++){
               if (carNum % 2 == 1) {
                  Thread t1 = new Thread(new Car(eastbound), "Car " + (eastCarCount));
                  eastCarCount += 2;
                  t1.start();
                  num_Eastbound_Cars++;
                                 System.out.println(t1   );

               } else{
                  Thread t2 = new Thread(new Car(westbound), "Car " + (westCarcount));
                  westCarcount+=2;
                  t2.start();
                  num_Westbound_Cars++;
                  System.out.println(t2);

               }
            }
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Car implements Runnable {
        private int direction;

        public Car(int direction) {
            this.direction = direction;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep((long) (Math.random() * 3000)); // random sleep times to arrive at bridge
                    arrive();
                    Thread.sleep((long) (Math.random() * 3000)); // random sleep times to cross bridge
                    passed();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void arrive() throws InterruptedException {
            if (direction == eastbound) {
            east_Semaphore.acquire();
                synchronized (Project1_James_Carlstrom.class) {
                    while (!can_Eastbound_Pass) {
                        Project1_James_Carlstrom.class.wait();
                    }
                    System.out.println(Thread.currentThread().getName() + " arrived in the eastbound direction");
                }
            } else {
                west_Semaphore.acquire();
                synchronized (Project1_James_Carlstrom.class) {
                    while (!can_Westbound_Pass) {
                        Project1_James_Carlstrom.class.wait();
                    }
                    System.out.println(Thread.currentThread().getName() + " arrived in the westbound direction");
                }
            }
        }

        private void passed() {
            if (direction == eastbound) {
               synchronized (Project1_James_Carlstrom.class) {
                  System.out.println(Thread.currentThread().getName() + " passed in the eastbound direction");
                  num_Eastbound_Cars--;
                  if (num_Eastbound_Cars == 0) {
                     can_Westbound_Pass = true;
                     can_Eastbound_Pass = false;
                     Project1_James_Carlstrom.class.notifyAll();
                  } else if (num_Westbound_Cars == 0) {
                     can_Eastbound_Pass = true;
                     can_Westbound_Pass = true;
                     Project1_James_Carlstrom.class.notifyAll();
                  }
               }
               east_Semaphore.release();
           } else {
               synchronized (Project1_James_Carlstrom.class) {
                  System.out.println(Thread.currentThread().getName() + " passed in the westbound direction");
                  num_Westbound_Cars--;
                  if (num_Westbound_Cars == 0) {
                     can_Eastbound_Pass = true;
                     can_Westbound_Pass = false;
                     Project1_James_Carlstrom.class.notifyAll();
                  } else if (num_Eastbound_Cars == 0) {
                     can_Eastbound_Pass = true;
                     can_Westbound_Pass = true;
                     Project1_James_Carlstrom.class.notifyAll();
                  }
               }
               west_Semaphore.release();
            }
         }
    }
}