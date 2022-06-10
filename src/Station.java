class Station {

    private int numCars;
    private int numPassengers;
    private int carCapacity;

    private int carInLoading; /* tracks the id of the car occupying the loading area. if no car is occupying it the value is -1 */
    private int carInUnloading; /* tracks the id of the car occupying the unloading area. if no car is occupying it the value is -1 */

    private int numPassengersInCar[]; /* tracks the current number of passengers currently in each car */
    private int carOfPassenger[]; /* tracks the id of the car that each passenger i is riding */

    public Station(int numPassengers, int carCapacity, int numCars) {

        this.numPassengers = numPassengers;
        this.carCapacity = carCapacity;
        this.numCars = numCars;

        this.carInLoading = -1; 
        this.carInUnloading = -1; 

        this.numPassengersInCar = new int[numCars];
        this.carOfPassenger = new int[numPassengers];

        for (int i = 0; i < numPassengers; i++) {
            carOfPassenger[i] = -1; /* each passenger is initially riding no car */
        }

    }

    /* parameter i is the id of the car to invoke this function */
    public synchronized boolean load(int i) {

        System.out.println("Car " + i + " called load");
        
        /* while there is a car in loading area, this car with id i will wait */
        while (carInLoading != -1) {
            try {
                System.out.println("Car " + i + " waits for car " + carInLoading + " to load");
                wait();
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Car " + i + " is done waiting and assigned to loading area");

        /* since no more car in loading area, assign this car to the loading area */
        carInLoading = i;

        /* if not enough passengers left to fill up capacity of car, then this car cannot continue with this function */
        if (numPassengers < carCapacity) {
            return true;
        }

        /* while this car is not yet full, wait for more passengers to board() */
        while (numPassengersInCar[carInLoading] != carCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        carInLoading = -1; /* car is done loading so assign back the -1 value */

        return false;
    }

    /* parameter i is the id of the car to invoke this function */
    public synchronized boolean unload(int i) {

        System.out.println("Car " + i + " called unload");

        /* while there is a car in unloading area, this car with id of i will wait */
        while (carInUnloading != -1) {
            try {
                System.out.println("Car " + i + " waits for car " + carInUnloading + " to unload");
                wait();
            } catch (InterruptedException e) {

            }
        }

        carInUnloading = i;
        notifyAll(); /* notify the passengers who were waiting to board */

        /* wait while the passengers of car with id i have not yet finished unboarding */
        while (numPassengersInCar[i] > 0) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Car " + i + " has unloaded all of its passengers");
        carInUnloading = -1; 

        /* if there are not enough passengers to fill the capacity and there are no more passengers riding in cars, code is done executing */
        if (numPassengers < carCapacity && countPassengersStillInCar() == 0) {
            return true;
        } else {
            return false;
        }
        
    }

    /* parameter i is the id of the passenger to invoke this function */
    public synchronized void board(int i) {

        numPassengers--;

        while (carInLoading == -1) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Passenger " + i + " has boarded car " + carInLoading);
        carOfPassenger[i] = carInLoading;
        numPassengersInCar[carInLoading] += 1; 
        notifyAll(); /* notify the car that is currently loading passengers */

    }

    /* parameter i is the id of the passenger to invoke this function */
    public synchronized void unboard(int i) {

        System.out.println("Passenger " + i + " is ready to unboard");

        /* while the car in the unloading area is not the car that this passenger i is in, it will wait for that car */
        while (carInUnloading != carOfPassenger[i]) {
            try {
                System.out.println("Passenger " + i + " is waiting to unboard car " + carOfPassenger[i]);
                wait();
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Passenger " + i + " has unboarded car " + carOfPassenger[i]);
        numPassengersInCar[carInUnloading] -= 1;
        notifyAll(); /* notify the car that is currently unloading passengers */

    }

    /* helper function for getting the total count of passengers still on board cars */
    public int countPassengersStillInCar() {
        int count = 0;
        for (int i = 0; i < numCars; i++) {
            count += numPassengersInCar[i];
        }
        return count;
    }

}
