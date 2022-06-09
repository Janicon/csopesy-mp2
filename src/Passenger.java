import java.util.Random;

public class Passenger implements Runnable {

    enum State {
        WANDERING,
        INLINE,
        DONE
    }

    private Random random = new Random();
    private State state;
    private int id;
    private Car car;
    // private Station station;
    private Object sharedObj = new Object(); // might need shared lock between car and passenger to invoke notify

    public Passenger(int id) {
        this.id = id; // might not be needed
        this.state = State.WANDERING;
    }

    /*
        Passengers should wander around the park for a random amount
        of time before getting in line for the roller coaster.

        - The line in this case may not necessarily be first come first
          served.
     */
    public void wander() {

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
        while(Station.carInStation.getState() != Car.State.LOADING) { // assumed car state

            // wait if car is not yet in loading state
            synchronized (sharedObj) {
                try {
                    sharedObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Execute rest of the code
        assignCar(Station.carInStation); // assign the car to the passenger
        state = State.DONE; // the passenger has boarded

        // TODO: Situations where the car becomes full

    }

    public void unboard() {

        // Passengers cannot unboard until the car has invoked unload
        if(car.getState() != Car.State.UNLOADING) {

            // wait again until notified by car thread once unloading
            synchronized (sharedObj) {
                try {
                    sharedObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void assignCar(Car car) {
        this.car = car;
    }

    @Override
    public void run() {

        wander(); // wander first

        if(state == State.INLINE) // try to board once in line
            board();

        unboard();
    }
}
