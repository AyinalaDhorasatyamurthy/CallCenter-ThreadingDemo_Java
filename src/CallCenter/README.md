# 📞 Call Center Threading Demo (Java)

This Java project simulates a **multi-threaded call center** environment where multiple customer calls are handled by a limited number of agents using **thread synchronization** and **semaphores**.

---

## 🧠 Objective

The goal is to demonstrate **thread management**, **concurrency**, and **resource sharing** in a call center scenario:

- Customers arrive randomly and try to get assistance.
- There is a **limited number of waiting seats**.
- If a customer finds a seat, they wait until an agent is available.
- If all seats are full, the customer leaves.
- Agents pick up calls one by one and process them.

---

## 🧵 Key Concepts Used

- **Multithreading**: Customers and agents run in separate threads.
- **Semaphores**: Used for synchronizing access to shared resources (waiting seats, customer queue).
- **Mutex (Mutual Exclusion)**: Ensured via a semaphore (`seatLock`) to avoid race conditions on shared data.
- **Queue**: Maintains the order of waiting customers (FIFO).

---

## 🏗️ Project Structure

### Classes

| Class | Role |
|-------|------|
| `CallCenter` | Core logic for managing seats, synchronization, and queues |
| `Agent` | Thread representing a call center agent who handles customers |
| `Customer` | Thread representing a customer trying to call |
| `CustomerServiceQueue` | Main class to initiate the simulation |

---

## ⚙️ How It Works

1. **User Inputs**:
   - Number of waiting seats
   - Number of customer calls
   - Number of agents available

2. **Execution Flow**:
   - Each customer runs in a separate thread and attempts to acquire a waiting seat.
   - If successful, the customer waits until an agent becomes available.
   - Each agent runs in a loop, handling one customer at a time using `semaphores`.
   - Calls are simulated with a `Thread.sleep()` to represent call duration.

3. **Synchronization**:
   - `Semaphore customerQueue`: Signals when customers are waiting.
   - `Semaphore agentReady`: Signals when agents are available.
   - `Semaphore seatLock`: Ensures atomic access to seat availability and customer queue.

---

## ✅ Example Output

```
Enter the number of waiting seats: 3
Enter the number of customers calling: 5
Enter the number of agents available: 2

Customer 1 is waiting. Available seats: 2
Customer 1 is now waiting to speak with an agent.
Agent 1 is attending Customer 1. Free seats: 3
Agent 1 is now speaking to Customer 1.
...
Customer 4 left (No available waiting seats).
```

---

## 🖥️ How to Run

1. Compile the program:
   ```bash
   javac CustomerServiceQueue.java
   ```

2. Run the program:
   ```bash
   java CallCenter.CustomerServiceQueue
   ```

3. Enter values when prompted.

---

## 📌 Requirements

- Java 8 or above
- Basic understanding of multithreading and semaphores

---

## 👨‍💻 Author

**A. Dhorasatyamurthy**  
B.Tech Computer Science (CSE), Amrita Vishwa Vidyapeetham  
[GitHub](https://github.com/AyinalaDhorasatyamurthy)