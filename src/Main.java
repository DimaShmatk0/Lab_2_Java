import javax.naming.SizeLimitExceededException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int length = 1_000_000_000;

        long startGenerate = System.nanoTime();
        int[] arr = generateArray(length);
        long endGenerate = System.nanoTime();
        System.out.printf("Time generate: %.2f ms%n", (endGenerate - startGenerate) / 1_000_000.0);

        for (int countThread = 1; countThread <= 64; countThread *= 2) {
            ParallelMinController pmc = new ParallelMinController(arr, length, countThread);
            long start = System.nanoTime();
            pmc.runController();
            long end = System.nanoTime();
            System.out.printf("Threads: %d | Time: %.2f ms%n", countThread, (end - start) / 1_000_000.0);
            Thread.sleep(1000);
        }
    }

    private static int[] generateArray(int length) {
        int[] arr = new int[length];
        Random rmd = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = rmd.nextInt(1000);
        }
        /*arr[rmd.nextInt(length)] = -1;*/
        arr[166658519] = -1;
        return arr;
    }
}
