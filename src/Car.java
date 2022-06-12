import java.util.concurrent.Semaphore;

public class Car implements Runnable{
	private int id, maxCapacity;
	Semaphore loadZone, unloadZone, slotsAvailable, boardFinished, slotsTaken, unboardFinished, totalPassengers;
	
	Car(int id, int maxCapacity, Semaphore unloadZone, Semaphore loadZone, Semaphore slotsAvailable, Semaphore boardFinished, 
			Semaphore unboardFinished, Semaphore slotsTaken, Semaphore totalPassengers){
        this.slotsTaken = slotsTaken;
		this.id = id;
		this.loadZone = loadZone;
		this.maxCapacity = maxCapacity;
        this.unloadZone = unloadZone;
		this.slotsAvailable = slotsAvailable;
		this.boardFinished = boardFinished;
		this.unboardFinished = unboardFinished;
		this.totalPassengers = totalPassengers;
    }

	@Override
	public synchronized void run() {
		while(!Thread.currentThread().isInterrupted()) {
			load();
			runCar();
			unload();
		}

		System.out.println("Car " + id + " is done.");
	}

	void load() {
		try {
			loadZone.acquire();

			// enter load zone first, then check passenger count
			if(totalPassengers.availablePermits() < maxCapacity) {
				// release lock and interrupt thread
				loadZone.release();
				Thread.currentThread().interrupt();
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(Thread.currentThread().isInterrupted())
			return;

		System.out.println("Car " + id + " enters load station");
		slotsAvailable.release(maxCapacity);

		try {
			boardFinished.acquire();
		} catch(InterruptedException e) {
		}
		
		System.out.println("All Aboard Car " + id);

		loadZone.release();
	}
	
	void runCar() {

		if(Thread.currentThread().isInterrupted())
			return;

		System.out.println("Car " + id + " begins trip");
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}
	}
	
	void unload() {

		if(Thread.currentThread().isInterrupted())
			return;

		try {
			unloadZone.acquire();
		} catch(InterruptedException e) {
		}

		System.out.println("Car " + id + " enters unload station");

		slotsTaken.release(maxCapacity);
		
		try {
			unboardFinished.acquire(maxCapacity);
		} catch(InterruptedException e) {
		}
		
		System.out.println("All Ashore from Car " + id);
		
		unloadZone.release();
	}
	
	

}
