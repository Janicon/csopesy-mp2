import java.util.concurrent.Semaphore;

public class Car implements Runnable{
	private int id, maxCapacity;
	Semaphore loadZone, unloadZone, slotsAvailable, boardFinished, slotsTaken, unboardFinished, numFinished;
	Semaphore isDone;

	Car(int id, int maxCapacity, Semaphore unloadZone, Semaphore loadZone,
		Semaphore slotsAvailable, Semaphore boardFinished, Semaphore unboardFinished,
		Semaphore slotsTaken, Semaphore numFinished, Semaphore isDone){
		this.slotsTaken = slotsTaken;
		this.id = id;
		this.loadZone = loadZone;
		this.maxCapacity = maxCapacity;
		this.unloadZone = unloadZone;
		this.slotsAvailable = slotsAvailable;
		this.boardFinished = boardFinished;
		this.unboardFinished = unboardFinished;
		this.numFinished = numFinished;
		this.isDone = isDone;
	}

	@Override
	public void run() {
		while(true) {
			load();
			runCar();
			unload();
		}
	}

	void load() {
		try {
			loadZone.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Car " + id + " enters load station");
		slotsAvailable.release(maxCapacity);

		try {
			boardFinished.acquire();
		} catch(InterruptedException e) {
		}

		System.out.println("All Aboard");

		loadZone.release();
	}

	void runCar() {
		System.out.println("Car " + id + " begins trip");
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}
	}

	void unload() {
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

		System.out.println("All Ashore");

		unloadZone.release();
	}



}