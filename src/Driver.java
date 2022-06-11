import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Driver {
    public static void main(String[] args) {
        int numPassengers, maxCapacity, numCars;
        Scanner sc = new Scanner(System.in);

        // For testing
        numPassengers = 6;
        maxCapacity = 2;
        numCars = 2;

        /*
        System.out.print("Number of Passengers: ");
        numPassengers = sc.nextInt();

        System.out.println();

        System.out.print("Car Capacity: ");
        maxCapacity = sc.nextInt();

        System.out.println();

        System.out.print("Number of Cars: ");
        numCars = sc.nextInt();

        System.out.println();
        */

        Semaphore slotsAvailable, slotsTaken;
        Semaphore loadZone, unloadZone;
        Semaphore boardFinished, unboardFinished;
        Semaphore numFinished;
        Semaphore isDone;

        slotsAvailable = new Semaphore(0);
        loadZone = new Semaphore(1);
        unloadZone = new Semaphore(1);
        boardFinished = new Semaphore(0);
        unboardFinished = new Semaphore(0);
        slotsTaken = new Semaphore(0);
        numFinished = new Semaphore(numPassengers);
        isDone = new Semaphore(1);

        for(int i = 0; i < numPassengers; i++) {
            Thread thread = new Thread(new Passenger(i, slotsAvailable, boardFinished, slotsTaken,
                    unboardFinished, numFinished, isDone));
            thread.start();
        }

        for(int i = 0; i < numCars; i++) {
            Thread thread = new Thread(new Car(i, maxCapacity, unloadZone, loadZone,
                        slotsAvailable, boardFinished, unboardFinished, slotsTaken, numFinished, isDone));
            thread.start();
        }

        try {
            isDone.acquire();
        } catch(InterruptedException e) {
        }

        while(isDone.availablePermits() == 0);

        System.out.println("Process is Finished");

        return;
    }
}