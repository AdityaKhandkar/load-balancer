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
    public String start(InputStream data) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(data))) {
            int i = br.read();
            return Long.toString(randomNumberCruncher(i));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    private long randomNumberCruncher(int num) {
        Random r = new Random(num);
        int n = r.nextInt(6) + 5;
        try {
            Thread.sleep((r.nextInt(n) + 10) * 1000);
            return findNthFib(n*10);
        } catch (InterruptedException e) {
            System.err.println("Sleep not working");
            return -1;
        }
    }

    private long findNthFib(int n) {
        int prev = 0, curr = 1;
        for(int i = 0; i <= n; i++) {
            int temp = prev + curr;
            prev = curr;
            curr = temp;
        }
        return curr;
    }
}
