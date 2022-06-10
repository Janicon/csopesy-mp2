import java.util.concurrent.Semaphore;

public class Driver {
    public static void main(String[] args) {
        int numPassengers, maxCapacity, numCars;
        Car[] cars;
        Passenger[] passengers;

        // For testing
        numPassengers = 5;
        maxCapacity = 2;
        numCars = 2;

        Semaphore slotsAvailable, loadZone, unloadZone, boardFinished;
        slotsAvailable = new Semaphore(0);
        loadZone = new Semaphore(1);
        unloadZone = new Semaphore(1);
        boardFinished = new Semaphore(0);

        for(int i = 0; i < numPassengers; i++) {
            Thread thread = new Thread(new Passenger(i, slotsAvailable, boardFinished));
            thread.start();
        }

        for(int i = 0; i < numCars; i++) {
            Thread thread = new Thread(new Car(i, maxCapacity, unloadZone, loadZone, slotsAvailable, boardFinished));
            thread.start();
        }
    }
}
