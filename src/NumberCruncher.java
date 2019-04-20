import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Aditya on 3/21/2019.
 */

class NumberCruncher implements Application {

    @Override
    public String start(long data) {
        return Long.toString(randomNumberCruncher(data));
    }

    @Override
    public String type() {
        return "Number Cruncher";
    }

    private long randomNumberCruncher(long num) {
        return findNthFib(num);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println("In Number Cruncher: " + e.getMessage());
        }
    }

    private long findNthFib(long n) {
        if(n % 5 == 0) sleep(1000);
        if(n <= 1) return n;
        return findNthFib(n - 1) + findNthFib(n - 2);
    }
}
