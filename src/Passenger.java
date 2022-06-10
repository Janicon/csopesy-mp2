import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger implements Runnable {

    enum State {
        WANDERING,
        INLINE,
        DONE
    }

    private Random random = new Random();
    private State state;
    private int id;
    private int carID;
    private Station station;
    private Semaphore sem;

    // Semaphore here is the same one assigned to station (probably need a better way to have the semaphores shared)
    public Passenger(int id, Station station, Semaphore numCapacity,
                     Semaphore loadZone, Semaphore unloadZone) {
        this.id = id;
        this.station = station;
        this.state = State.WANDERING;
        this.sem = numCapacity;
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
            Thread.sleep(random.nextInt(5000)); // tentative random time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After wandering for x seconds, set state to inline
        state = State.INLINE;

    }

    public void board() {

        // Passengers cannot board until the car has invoked load
        try {
            // acquire lock (numCapacity -= 1) // if sem == 0, will be stuck in waiting state
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Passenger " + id + " has boarded.");

        // Execute rest of the code
        assignCar(station.getCarInLoading()); // assign the car to the passenger
        state = State.DONE; // the passenger has boarded
    }

    public void unboard() {

        // Passengers cannot unboard until the car has invoked unload
        /*if(car.getState() != Car.State.UNLOADING) {

            // wait again until notified by car thread once unloading
            synchronized (sharedObj) {
                try {
                    sharedObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } */
    }

    public void assignCar(int carID) {
        this.carID = carID;
    }

    @Override
    public void run() {

        wander(); // wander first

        if(state == State.INLINE) // try to board once in line
            board();

        unboard();
    }
}
