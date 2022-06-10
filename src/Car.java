import java.util.concurrent.Semaphore;

public class Car implements Runnable{

	enum State { WANDERING, INLINE, DONE }
	
	Station station;
	Semaphore loadZone, unloadZone;
	private Thread thread;
	
	Car(int id, Semaphore unloadZone, Semaphore loadZone){
        this.loadZone = loadZone;
        this.unloadZone = unloadZone;
        thread = new Thread(this);
        thread.start(); 
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	void load() {
		loadZone.acquire();
		station.load();
		loadZone.release();
	}
	
	void runCar() {
		station.runCar();
	}
	
	void unload() {
		unloadZone.acquire();
		station.unload();
		unloadZone.release();
	}
	
	

}
