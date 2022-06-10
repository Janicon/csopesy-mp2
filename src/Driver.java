import java.util.concurrent.Semaphore;

public class Driver {
    public static void main(String[] args) {
        //System.out.println("CSOPESY MP2");

        // int numPassengers, int carCapacity, int numCars
        Station station = new Station(3, 2, 1);
        Semaphore slotsAvailable = new Semaphore(2);
        Semaphore loadZone = new Semaphore(1);
        Semaphore unloadZone = new Semaphore(1);

        for(int i = 0; i < 3; i++) {
            Thread thread = new Thread(new Passenger(i, station, slotsAvailable));
            thread.start();
        }

        for(int i = 0; i < 1; i++) {
            Thread thread = new Thread(new Car(i, unloadZone, loadZone, slotsAvailable));
            thread.start();
        }
    }
}
