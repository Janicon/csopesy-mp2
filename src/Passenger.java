import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger implements Runnable {

    private Random random = new Random();
    private int id;
    private Semaphore slotsAvailable;
    private Semaphore boardFinished;
    private Semaphore slotsTaken;
    private Semaphore unboardFinished;
    
    public Passenger(int id, Semaphore slotsAvailable, Semaphore boardFinished, Semaphore slotsTaken, Semaphore unboardFinished) {
        this.id = id;
        this.slotsTaken = slotsTaken;
        this.slotsAvailable = slotsAvailable;
        this.boardFinished = boardFinished;
        this.unboardFinished = unboardFinished;
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
            slotsAvailable.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Passenger " + id + " has boarded.");

        if(slotsAvailable.availablePermits() == 0)
            boardFinished.release();
    }

    public void unboard() {
    	
    	try {
    		slotsTaken.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
        System.out.println("Passenger " + id + " has unboarded.");
        
        if(slotsTaken.availablePermits() == 0)
            unboardFinished.release();
    }

    @Override
    public void run() {
        wander();
        board();
        unboard();
    }
}
