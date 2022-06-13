import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        int numPassengers, maxCapacity, numCars, numTrips;
        Scanner sc = new Scanner(System.in);

        // For testing
        numPassengers = 6;
        maxCapacity = 2;
        numCars = 2;

        System.out.print("Number of Passengers: ");
        numPassengers = sc.nextInt();

        System.out.println();

        System.out.print("Car Capacity: ");
        maxCapacity = sc.nextInt();

        System.out.println();

        System.out.print("Number of Cars: ");
        numCars = sc.nextInt();

        System.out.println();

        numTrips = numPassengers / maxCapacity;

        Semaphore slotsAvailable, loadZone, unloadZone, boardFinished, unboardFinished, slotsTaken, totalPassengers, nTrips;
        slotsAvailable = new Semaphore(0);
        loadZone = new Semaphore(1);
        unloadZone = new Semaphore(1);
        boardFinished = new Semaphore(0);
        unboardFinished = new Semaphore(0);
        slotsTaken = new Semaphore(0);
        totalPassengers = new Semaphore(numPassengers);
        nTrips = new Semaphore(numTrips);


        for(int i = 0; i < numPassengers; i++) {
            Thread thread = new Thread(new Passenger(i, slotsAvailable, boardFinished, slotsTaken, unboardFinished, totalPassengers,
                    maxCapacity, loadZone, nTrips));
            threads.add(thread);
            thread.start();
        }

        for(int i = 0; i < numCars; i++) {
            Thread thread = new Thread(new Car(i, maxCapacity, unloadZone, loadZone, slotsAvailable, boardFinished, unboardFinished, slotsTaken,
                    totalPassengers, nTrips));
            threads.add(thread);
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {

        }

        System.out.println("All rides completed");

    }
}
