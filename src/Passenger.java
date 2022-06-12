import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger implements Runnable {

    private Random random = new Random();
    private int id, maxCapacity;
    private Semaphore slotsAvailable;
    private Semaphore boardFinished;
    private Semaphore slotsTaken;
    private Semaphore unboardFinished;
    private Semaphore totalPassengers;
    private Semaphore loadZone;
    
    public Passenger(int id, Semaphore slotsAvailable, Semaphore boardFinished, Semaphore slotsTaken, Semaphore unboardFinished, Semaphore totalPassengers,
                     int maxCapacity, Semaphore loadZone) {
        this.id = id;
        this.slotsTaken = slotsTaken;
        this.slotsAvailable = slotsAvailable;
        this.boardFinished = boardFinished;
        this.unboardFinished = unboardFinished;
        this.totalPassengers = totalPassengers;
        this.loadZone = loadZone;
    }

    /*
        Passengers should wander around the park for a random amount
        of time before getting in line for the roller coaster.

        - The line in this case may not necessarily be first come first
          served.
     */
    public void wander() {

        System.out.println("Passenger " + id + " is wandering.");

        try {
            Thread.sleep(random.nextInt(5000));
            System.out.println("Passenger " + id + " is done wandering");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void board() {

        try {
            totalPassengers.acquire();

            // loadZone to see if there is still a car loading
            if(loadZone.availablePermits() == 0) {
                slotsAvailable.acquire();
            }
            else {
                Thread.currentThread().interrupt();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(Thread.currentThread().isInterrupted())
            return;

        System.out.println("Passenger " + id + " has boarded.");

        if(slotsAvailable.availablePermits() == 0)
            boardFinished.release();
    }

    public void unboard() {

        // skip method if thread was interrupted already (means the process should be done)
        if(Thread.currentThread().isInterrupted())
            return;
    	
    	try {
    		slotsTaken.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
        System.out.println("Passenger " + id + " has unboarded.");
        unboardFinished.release();

    }

    @Override
    public void run() {
        wander();
        board();
        unboard();

        System.out.println("Passenger " + id + " is done.");
    }
}
