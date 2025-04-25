public class ParallelMinController {
    private final int length;
    private final int threadNum;
    private final int[] arr;
    private final RangeMinFinder[] arrayRangeMinFinder;
    private int countFinishedThread;
    private final Object lockObject = new Object();

    public ParallelMinController(int[] arr, int length, int threadNum) {
        this.length = length;
        this.threadNum = threadNum;
        this.countFinishedThread = 0;
        this.arr = arr;
        this.arrayRangeMinFinder = new RangeMinFinder[threadNum];
    }

    private int[][] getSegmentRange() {
        int[][] ranges = new int[threadNum][2];
        int baseSize = length / threadNum;
        int remainder = length % threadNum;
        int start = 0;

        for (int i = 0; i < threadNum; i++) {
            int segmentSize = baseSize + (i < remainder ? 1 : 0);
            int end = start + segmentSize;
            ranges[i][0] = start;
            ranges[i][1] = end;
            start = end;
        }
        return ranges;
    }

    public void runController() {
        int[][] arrSegmentRange = getSegmentRange();

        for (int i = 0; i < threadNum; i++) {
            arrayRangeMinFinder[i] = new RangeMinFinder(arrSegmentRange[i][0], arrSegmentRange[i][1], arr, this);
            arrayRangeMinFinder[i].start();
        }

        synchronized (lockObject) {
            while (countFinishedThread < threadNum) {
                try {
                    lockObject.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        int globalMin = arrayRangeMinFinder[0].getMinValue();
        int globalIndex = arrayRangeMinFinder[0].getMinIndex();

        for (int i = 1; i < threadNum; i++) {
            if (arrayRangeMinFinder[i].getMinValue() < globalMin) {
                globalMin = arrayRangeMinFinder[i].getMinValue();
                globalIndex = arrayRangeMinFinder[i].getMinIndex();
            }
        }

        System.out.println("Мінімальне значення: " + globalMin);
        System.out.println("Індекс мінімального значення: " + globalIndex);
    }

    public void incrementFinishedThread() {
        synchronized (lockObject) {
            countFinishedThread++;
            if (countFinishedThread == threadNum) {
                lockObject.notifyAll();
            }
        }
    }
}
