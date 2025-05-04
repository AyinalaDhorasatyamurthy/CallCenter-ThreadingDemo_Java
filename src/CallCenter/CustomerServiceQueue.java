package CallCenter;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class CallCenter {
    private final int waitingSeats; 
    private int availableSeats; 
    // Semaphores for synchronization
    private final Semaphore customerQueue = new Semaphore(0); // Tracks waiting customers
    private final Semaphore agentReady = new Semaphore(0); // Signals when an agent is available
    private final Semaphore seatLock = new Semaphore(1); // Mutex to protect seat count access
    private final Queue<Integer> waitingCustomers = new LinkedList<>(); // FIFO queue for customers

    public CallCenter(int waitingSeats) {
        this.waitingSeats = waitingSeats;
        this.availableSeats = waitingSeats;
    }

    // Customers (Callers) 
    public void callCustomerService(int customerId) {
        try {
            seatLock.acquire(); 
            if (availableSeats > 0) { 
                availableSeats--;
                waitingCustomers.add(customerId); 
                System.out.println("Customer " + customerId + " is waiting. Available seats: " + availableSeats);

                seatLock.release(); 
                customerQueue.release();

                
                System.out.println("Customer " + customerId + " is now waiting to speak with an agent.");
                
                agentReady.acquire(); 
            } else { 
                System.out.println("Customer " + customerId + " left (No available waiting seats).");
                seatLock.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Agents pick up calls
    public void handleCall(int agentId) {
        while (true) {
            try {
                customerQueue.acquire(); 
                seatLock.acquire();

                int customerId = waitingCustomers.poll(); 
                availableSeats++; // Free up a waiting seat
                System.out.println("Agent " + agentId + " is attending Customer " + customerId + ". Free seats: " + availableSeats);

                seatLock.release();

                
                System.out.println("Agent " + agentId + " is now speaking to Customer " + customerId + ".");

                Thread.sleep(3000); 
                
                System.out.println("Agent " + agentId + " finished a call with Customer " + customerId + ".");
                agentReady.release(); 

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//Agent Thread
class Agent implements Runnable {
    private final CallCenter center;
    private final int agentId;

    public Agent(CallCenter center, int agentId) {
        this.center = center;
        this.agentId = agentId;
    }

    @Override
    public void run() {
        center.handleCall(agentId);
    }
}

//Customer Thread
class Customer implements Runnable {
    private final CallCenter center;
    private final int customerId;

    public Customer(CallCenter center, int customerId) {
        this.center = center;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        center.callCustomerService(customerId);
    }
}

//Main Class
public class CustomerServiceQueue {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of waiting seats: ");
        int waitingSeats = scanner.nextInt();
        System.out.print("Enter the number of customers calling: ");
        int numCustomers = scanner.nextInt();
        System.out.print("Enter the number of agents available: ");
        int numAgents = scanner.nextInt();
        scanner.close();

        CallCenter center = new CallCenter(waitingSeats);

        // Start agent threads 
        Thread[] agentThreads = new Thread[numAgents];
        for (int i = 0; i < numAgents; i++) {
            agentThreads[i] = new Thread(new Agent(center, i + 1));
            agentThreads[i].start();
        }  

        // Start customer threads
        for (int i = 1; i <= numCustomers; i++) {
            new Thread(new Customer(center, i)).start();
            try {
                Thread.sleep((int) (Math.random() * 2000)); // Simulate random customer arrivals
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}