import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {

        ArrayList<Thread> threads = new ArrayList<Thread>();
        int numPassengers, maxCapacity, numCars, numTrips;
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of Passengers: ");
        numPassengers = sc.nextInt();

        System.out.println();

        System.out.print("Car Capacity: ");
        maxCapacity = sc.nextInt();

        System.out.println();

        System.out.print("Number of Cars: ");
        numCars = sc.nextInt();

        System.out.println();

        if(maxCapacity == 0 || numCars == 0) {
            numTrips = 0;
        } else {
            numTrips = numPassengers / maxCapacity;
        }

        Semaphore slotsAvailable = new Semaphore(0);
        Semaphore loadZone = new Semaphore(1);
        Semaphore unloadZone = new Semaphore(1);
        Semaphore boardFinished = new Semaphore(0);
        Semaphore unboardFinished = new Semaphore(0);
        Semaphore slotsTaken = new Semaphore(0);
        Semaphore totalPassengers = new Semaphore(numPassengers);
        Semaphore nTrips = new Semaphore(numTrips);

        for(int i = 0; i < numPassengers; i++) {
            Thread thread = new Thread(new Passenger(i, slotsAvailable, boardFinished, slotsTaken, unboardFinished, totalPassengers,
                    maxCapacity, numCars, loadZone, nTrips));
            threads.add(thread);
            thread.start();
        }

        for(int i = 0; i < numCars; i++) {
            Thread thread = new Thread(new Car(i, maxCapacity, numCars, unloadZone, loadZone,
                    slotsAvailable, boardFinished, unboardFinished, slotsTaken, totalPassengers, nTrips));
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