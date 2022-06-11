import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger implements Runnable {

    private Random random = new Random();
    private int id, maxCapacity;
    private Semaphore slotsAvailable;
    private Semaphore boardFinished;
    private Semaphore slotsTaken;
    private Semaphore unboardFinished;
    private Semaphore numFinished;
    private boolean skipBoarding = false;

    public Passenger(int id, Semaphore slotsAvailable, Semaphore boardFinished,
                     Semaphore slotsTaken, Semaphore unboardFinished, Semaphore numFinished, int maxCapacity) {
        this.id = id;
        this.slotsTaken = slotsTaken;
        this.slotsAvailable = slotsAvailable;
        this.boardFinished = boardFinished;
        this.unboardFinished = unboardFinished;
        this.numFinished = numFinished;
        this.maxCapacity = maxCapacity;
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
    	
    	if(numFinished.availablePermits() < maxCapacity) {
    		skipBoarding = true;
    		boardFinished.release();
    		return;
    	}
    	
        try {
            slotsAvailable.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        try {
            numFinished.acquire();
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
        unboardFinished.release();

        if(numFinished.availablePermits() == 0) {
            boardFinished.release();
        }

    }

    @Override
    public void run() {
        wander();
        board();
        if(!skipBoarding)
        	unboard();
    }
}