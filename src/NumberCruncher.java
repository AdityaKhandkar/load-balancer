import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Aditya on 3/21/2019.
 */

class NumberCruncher implements Application {

    @Override
    public String start(int data) {
        return Integer.toString(randomNumberCruncher(data));
    }

    @Override
    public String type() {
        return "Number Cruncher";
    }

    private int randomNumberCruncher(int num) {
        if(num % 2 == 0) {
            sleep(new Random().nextInt(num) + (num / 2));
        }
        return findNthFib(num);
    }

    private void sleep(long millis) {
        try {
            Print.out("Going to sleep.");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println("In Number Cruncher: " + e.getMessage());
        }
    }

    // Inefficient fibonacci function
    private int findNthFib(int n) {
        if(n <= 1) return n;
        return findNthFib(n - 1) + findNthFib(n - 2);
    }
}
