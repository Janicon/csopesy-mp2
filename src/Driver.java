public class Driver {
    public static void main(String[] args) {

        int numPassengers = 6;
        int carCapacity = 2; /* capacity must be less than the number of passengers */
        int numCars = 3;

        Station station = new Station(numPassengers, carCapacity, numCars); /* Station object to be passed to the cars and passengers */
        Car[] cars = new Car[numCars];
        Passenger[] passengers = new Passenger[numPassengers];

        for (int i = 0; i < numCars; i++) {
            cars[i] = new Car(i, carCapacity, station);
            new Thread(cars[i]).start();
        }

        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new Passenger(i, station);
            new Thread(passengers[i]).start();
        }
    
    }
}
