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
        Random r = new Random();
//        int n = r.nextInt((int)num) + 5;
        try {
            Thread.sleep((num / 10) * 1000);
            return findNthFib((int)num);
        } catch (InterruptedException e) {
            System.err.println("Can't sleep");
            return -1;
        }
    }

    private long findNthFib(int n) {
        if(n <= 1) return n;
        return findNthFib(n - 1) + findNthFib(n - 2);
    }
}
