import java.util.concurrent.Semaphore;

public class Car implements Runnable{
	private int id, maxCapacity;
	Semaphore loadZone, unloadZone, slotsAvailable, boardFinished;
	private Thread thread;
	
	Car(int id, int maxCapacity, Semaphore unloadZone, Semaphore loadZone, Semaphore slotsAvailable, Semaphore boardFinished){
        this.id = id;
		this.loadZone = loadZone;
		this.maxCapacity = maxCapacity;
        this.unloadZone = unloadZone;
		this.slotsAvailable = slotsAvailable;
		this.boardFinished = boardFinished;
        thread = new Thread(this);
        thread.start(); 
    }

	@Override
	public void run() {
		while(true) {
			load();
			runCar();
			unload();
			break;
		}
	}

	void load() {
		System.out.println("Car " + id + " enters load station");
		try {
			loadZone.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		slotsAvailable.release(maxCapacity);

		try {
			boardFinished.acquire();
		} catch(InterruptedException e) {
		}

		loadZone.release();
	}
	
	void runCar() {
		System.out.println("Car " + id + " leaves station");
		try {
			Thread.sleep(1000); // Decide on fixed duration
		} catch(InterruptedException e) {}
	}
	
	void unload() {
		System.out.println("Car " + id + " enters unload station");

		try {
			unloadZone.acquire();
		} catch(InterruptedException e) {
		}

		// TODO: Check for something to do

		unloadZone.release();
	}
	
	

}
