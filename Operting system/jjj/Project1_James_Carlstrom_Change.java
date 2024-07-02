import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Project1_James_Carlstrom_Change {
    private static Semaphore semaphore = new Semaphore(1);
    private static int eastbound = 1;
    private static int westbound = 2;
    private static Queue<Integer> carQueue = new LinkedList<>();

    public static void main(String[] args) {
        int num = 0;
        while (true){
            if(num % 2 == 0){
                Thread t1 = new Thread(new Car(eastbound), Integer.toString(num));
                t1.start();
            }
            else {
                Thread t2 = new Thread(new Car(westbound), Integer.toString(num));
                t2.start();
            }
            num ++;
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
                    Thread.sleep((long) (Math.random() * 1000)); // random sleep times to arrive at bridge
                    arrive();
                    Thread.sleep((long) (Math.random() * 1000)); // random sleep times to cross bridge
                    passed();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void arrive() throws InterruptedException {
            boolean canCross = false;
            synchronized (carQueue) {
                if (carQueue.isEmpty() || carQueue.peek() == direction) {
                    canCross = true;
                    carQueue.add(direction);
                }
            }

            if (canCross) {
                semaphore.acquire();
                synchronized (carQueue) {
                    carQueue.remove();
                }
                System.out.println("Car " + Thread.currentThread().getName() + " arrived in the " + (direction == eastbound ? "east" : "west") + "bound direction");
            } else {
                System.out.println("Car " + Thread.currentThread().getName() + " is waiting to cross in the " + (direction == eastbound ? "east" : "west") + "bound direction");
                synchronized (carQueue) {
                    carQueue.add(direction);
                }
            }
        }

        private void passed() {
            System.out.println("Car " + Thread.currentThread().getName() + " passed in the " + (direction == eastbound ? "east" : "west") + "bound direction");
            semaphore.release();
        }
    }
}
