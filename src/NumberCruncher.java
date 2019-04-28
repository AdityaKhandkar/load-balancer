import java.util.Random;

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
        int sleepTime = new Random().nextInt(num);

        if(num % 2 == 0) {
            sleepTime /= 2;
        } else if (num % 3 == 0) {
            sleepTime /= 3;
        } else if (num % 5 == 0) {
            sleepTime /= 5;
        } else if (num % 7 == 0) {
            sleepTime /= 7;
        }

        sleep(sleepTime * 250);

        return findNthFib(num);
    }

    private void sleep(long millis) {
        try {
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
