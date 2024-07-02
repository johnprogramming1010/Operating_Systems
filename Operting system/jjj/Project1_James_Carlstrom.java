import java.util.concurrent.Semaphore;

public class Project1_James_Carlstrom {
    private static Semaphore semaphore = new Semaphore(1);
    private static int eastbound = 1;
    private static int westbound = 2;

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
            semaphore.acquire();
            System.out.println("Car " + Thread.currentThread().getName() + " arrived in the " + (direction == eastbound ? "east" : "west") + "bound direction");
        }

        private void passed() {
            System.out.println("Car " + Thread.currentThread().getName() + " passed in the " + (direction == eastbound ? "east" : "west") + "bound direction");
            semaphore.release();
        }
    }
}