import java.util.concurrent.Semaphore;

public class Station {

    public int boardedCount = 0;
    public Semaphore boardedCountMutex = new Semaphore(1);

    public Station() {

    }

}