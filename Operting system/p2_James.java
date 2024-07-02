import java.util.*;

class Process {
    int id;
    int priority;
    int burstLength;
    int rrBurstLength;
    int pWaitingTime;
    int rrWaitingTime;

    Process(int id, int priority, int burstLength) {
        this.id = id;
        this.priority = priority;
        this.burstLength = burstLength;
        this.pWaitingTime = 0;
        this.rrWaitingTime = 0;
        this.rrBurstLength = burstLength;
    }

    public int getId() {
        return id;
    }
    
    public int getBurstLength() {
        return burstLength;
    }

    public int getPriority() {
        return priority;
    }
    
    public int getPWaitTime() {
        return pWaitingTime;
    }
    
    public void setPWaitTime(int pWaitingTime) {
        this.pWaitingTime = pWaitingTime;
    }
    
    public int getRRWaitTime() {
        return rrWaitingTime;
    }
    
    public void setRRWaitTime(int rrWaitingTime) {
        this.rrWaitingTime = rrWaitingTime;
    }
    
    public void setBurstLength(int burstLength) {
        this.burstLength = burstLength;
    }
    
    public int getRRBurstLength() {
        return rrBurstLength;
    }
    
    public void setRRBurstLength(int rrburstLength) {
        this.rrBurstLength = rrburstLength;
    }
}
public class p2_James {

    static ArrayList<Process> readyQueue = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // create 6 random processes
        for (int i = 0; i < 6; i++) {
            int id = getRandomId();
            int priority = getRandomPriority();
            int burst = getRandomBurst();
            readyQueue.add(new Process(id, priority, burst));
        }

        // display initial snapshot
        printPSnapshot(readyQueue, "Initial Snapshot");

        // get user input for the 7th process
        int id = getNewId(scanner);
        int priority = getNewPriority(scanner);
        int burst = getNewBurst(scanner);
        readyQueue.add(new Process(id, priority, burst));

        // display updated snapshot
        printPSnapshot(readyQueue, "Updated Snapshot");
        
        // calculate waiting time using non-preemptive priority
        nonPreemptivePriority(readyQueue);
        // calculate waiting time using round-robin with time quantum 20
        roundRobin(readyQueue, 20);

        // display waiting times for each process using both algorithms
        System.out.println("\nWaiting times:");
        System.out.println("Process ID | Priority | Burst-length | Scheduling algorithm | Waiting time");
        displayPWaitTimes(readyQueue, "Non-preemptive Priority");
        displayRRWaitTimes(readyQueue, "Round Robin with Time Quantum 20");

        // display average waiting time for each algorithm
        System.out.println("\nAverage waiting time:");
        System.out.println("Non-preemptive Priority: " + getAveragePWaitingTime(readyQueue));
        System.out.println("Round Robin with Time Quantum 20: " + getAverageRRWaitingTime(readyQueue));
        
        // close the scanner
        scanner.close();
    }
    
    // helper method to get a random ID for a process
    public static int getRandomId() {
        Random rand = new Random();
        int id = rand.nextInt(11);
        for (Process p : readyQueue) {
            if (p.getId() == id) {
                return getRandomId();
            }
        }
        return id;
    }

    // helper method to get a random priority for a process
    public static int getRandomPriority() {
        Random rand = new Random();
        return rand.nextInt(10) + 1;
    }

    // helper method to get a random burst length for a process
    public static int getRandomBurst() {
        Random rand = new Random();
        return rand.nextInt(81) + 20;
    }

    // helper method to print snapshot
    public static void printPSnapshot(ArrayList<Process> readyQueue, String snapshotType){
        // Sort the readyQueue by ID before printing
        Collections.sort(readyQueue, Comparator.comparingInt(Process::getId));
        System.out.println(snapshotType + ":");
        System.out.println("ID\tPriority\tBurst Length");
        for (Process p : readyQueue) {
            System.out.println(p.id + "\t" + p.priority + "\t\t" + p.burstLength);
        }
    }
    
    // helper method to print snapshot
    public static void printRRSnapshot(ArrayList<Process> readyQueue, String snapshotType){
        // Sort the readyQueue by ID before printing
        Collections.sort(readyQueue, Comparator.comparingInt(Process::getId));
        System.out.println(snapshotType + ":");
        System.out.println("ID\tPriority\tBurst Length");
        for (Process p : readyQueue) {
            System.out.println(p.id + "\t" + p.priority + "\t\t" + p.rrBurstLength);
        }
    }

   // helper method to get a valid ID for a new process from user input
   public static int getNewId(Scanner scanner) {
        int id;
        while (true) {
            System.out.print("\nEnter a new process ID (0-10): ");
            id = scanner.nextInt();
            if (id < 0 || id > 10) {
                 System.out.println("Invalid input. ID must be between 0 and 10.");
            } 
            else {
                boolean duplicate = false;
            for (Process p : readyQueue) {
                if (p.getId() == id) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                System.out.println("Duplicate ID. Please enter a different ID.");
            }
            else {
               break;
            }
            }
            }
        return id;
    }

    public static int getNewPriority(Scanner scanner) {
        int newPriority;

        while (true) {
            System.out.print("Enter the priority for the new process (1-10): ");
            newPriority = scanner.nextInt();
            if (newPriority < 1 || newPriority > 10) {
                System.out.println("Invalid priority value. Please enter a value between 1 and 10.");
                continue;
            }
            break;
        }
        return newPriority;
    }

    
    public static int getNewBurst(Scanner scanner) {
        int newBurst;
    
        while (true) {
            System.out.print("Enter the burst length for the new process (20-100): ");
            newBurst = scanner.nextInt();
            if (newBurst < 20 || newBurst > 100) {
                System.out.println("Invalid burst length. Please enter a value between 20 and 100.");
                continue;
            }
            break;
        }
        return newBurst;
    }

    // helper method to calculate waiting time for each process using non-preemptive priority
    public static void nonPreemptivePriority(ArrayList<Process> processes) { 
        ArrayList<Process> sortedQueue = new ArrayList<>(processes);
        sortedQueue.sort(Comparator.comparing(Process::getPriority));
                 
        // calculate waiting times
        int waitTime = 0;
        for (int i = 0; i < sortedQueue.size(); i++) {
            if(i==0){
               Process p = sortedQueue.get(i);
               p.setPWaitTime(waitTime);
               waitTime += p.getBurstLength();
            }
            else{
               Process p = sortedQueue.get(i);
               p.setPWaitTime(waitTime);
               waitTime += p.getBurstLength();
            }
        }
    }
    
    // helper method to calculate waiting time for each process using round robin with a given time quantum
    public static void roundRobin(ArrayList<Process> processes, int timeQuantum) {
        // create a copy of the original processes list to avoid modifying the original list
        ArrayList<Process> copy = new ArrayList<>(processes);

        // create a queue to hold the processes that have arrived but not finished
        LinkedList<Process> queue = new LinkedList<>();
        for (Process p : copy) {
            queue.add(p);
        }

        // simulate the round-robin scheduling algorithm
        int currentTime = 0;
        while (!queue.isEmpty()) {
            Process currentProcess = queue.remove();
            int remainingBurst = currentProcess.getRRBurstLength();
            int waitTime = currentTime;
            if (remainingBurst > timeQuantum) {
                // process did not finish within the time quantum
                currentTime += timeQuantum;
                remainingBurst -= timeQuantum;
                currentProcess.setRRBurstLength(remainingBurst);
                queue.add(currentProcess); // add the process back to the queue
            } 
            else {
                // process finished within the time quantum
                currentTime += remainingBurst;
                remainingBurst = 0;
                waitTime -= 0; // assume all processes arrive at time 0
                currentProcess.setRRWaitTime(waitTime);
            }
        }
    }

    // helper method to display Round Robin waiting times for each process using a given scheduling algorithm
    public static void displayRRWaitTimes(ArrayList<Process> waitTimes, String algorithm) {
        for (Process p : waitTimes) {
           System.out.println(p.getId() + "\t\t" + p.getPriority() + "\t\t" + p.getBurstLength() + "\t\t" + algorithm + "\t\t" + p.getRRWaitTime());
        }
    }
    
    // helper method to display Priority waiting times for each process using a given scheduling algorithm
    public static void displayPWaitTimes(ArrayList<Process> waitTimes, String algorithm) {
    ArrayList<Process> sortedQueue = new ArrayList<>(waitTimes);
        for (Process p : sortedQueue) {
           System.out.println(p.getId() + "\t\t" + p.getPriority() + "\t\t" + p.getBurstLength() + "\t\t" + algorithm + "\t\t" + p.getPWaitTime());
        }
    }

    // helper method to calculate average waiting time for Round Robin processes
    public static double getAverageRRWaitingTime(ArrayList<Process> processes) {
        int totalWaitTime = 0;
        for (Process p : processes) {
           totalWaitTime += p.getRRWaitTime();
        }
        return (double) totalWaitTime / processes.size();
    }

    // helper method to calculate average waiting time for Priority processes
    public static double getAveragePWaitingTime(ArrayList<Process> processes) {
        int totalWaitTime = 0;
        for (Process p : processes) {
           totalWaitTime += p.getPWaitTime();
        }
        return (double) totalWaitTime / processes.size();
    }
}