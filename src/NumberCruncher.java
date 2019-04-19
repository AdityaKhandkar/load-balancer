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
        sleep((num / 10) * 1000);
        System.out.println("Thread state: " + Thread.currentThread().getState().toString());
        return findNthFib((int)num);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println("In Number Cruncher: " + e.getMessage());
        }
    }

    private long findNthFib(int n) {
        if(n % 5 == 0) sleep(200);
        if(n <= 1) return n;
        return findNthFib(n - 1) + findNthFib(n - 2);
    }
}
