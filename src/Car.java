import java.util.concurrent.Semaphore;

public class Car implements Runnable {

	private int id;
	private int maxCapacity;
	private int numCars;
	private Semaphore loadZone;
	private Semaphore unloadZone;
	private Semaphore slotsAvailable;
	private Semaphore boardFinished;
	private Semaphore slotsTaken;
	private Semaphore unboardFinished;
	private Semaphore totalPassengers;
	private Semaphore nTrips;

	public Car(int id, int maxCapacity, int numCars, Semaphore unloadZone, Semaphore loadZone, Semaphore slotsAvailable, Semaphore boardFinished,
			   Semaphore unboardFinished, Semaphore slotsTaken, Semaphore totalPassengers, Semaphore nTrips){
		this.slotsTaken = slotsTaken;
		this.id = id;
		this.numCars = numCars;
		this.loadZone = loadZone;
		this.maxCapacity = maxCapacity;
		this.unloadZone = unloadZone;
		this.slotsAvailable = slotsAvailable;
		this.boardFinished = boardFinished;
		this.unboardFinished = unboardFinished;
		this.totalPassengers = totalPassengers;
		this.nTrips = nTrips;
	}

	@Override
	public synchronized void run() {
		while(!Thread.currentThread().isInterrupted()) {
			load();
			runCar();
			unload();
		}
		//System.out.println("Car " + id + " is done.");
	}

	private void load() {
		try {
			// edge case if car capacity is 0, then terminate car
			if(maxCapacity == 0 || numCars == 0) {
				Thread.currentThread().interrupt();
				return;
			}

			loadZone.acquire();

			// enter load zone first, then check passenger count
			if(totalPassengers.availablePermits() < maxCapacity) {
				// release lock and interrupt thread
				loadZone.release();
				slotsAvailable.release(maxCapacity);
				Thread.currentThread().interrupt();
				return;
			}

			// decrement numTrips
			nTrips.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(Thread.currentThread().isInterrupted())
			return;

		System.out.println("Car " + id + " enters load station");
		slotsAvailable.release(maxCapacity);

		try {
			boardFinished.acquire();
		} catch(InterruptedException e) {}

		System.out.println("All Aboard Car " + id);

		loadZone.release();
	}

	private void runCar() {

		if(Thread.currentThread().isInterrupted())
			return;

		System.out.println("Car " + id + " begins trip");
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}

	}

	private void unload() {

		if(Thread.currentThread().isInterrupted())
			return;

		try {
			unloadZone.acquire();
		} catch(InterruptedException e) {}

		System.out.println("Car " + id + " enters unload station");

		slotsTaken.release(maxCapacity);

		try {
			unboardFinished.acquire(maxCapacity);
		} catch(InterruptedException e) {}

		System.out.println("All Ashore from Car " + id);

		unloadZone.release();
	}

}