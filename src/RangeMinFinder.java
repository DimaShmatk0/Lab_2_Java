public class RangeMinFinder extends Thread {
    private final int startIndex;
    private final int finishIndex;
    private final int[] arr;
    private int minValue;
    private int minIndex;
    private final ParallelMinController controller;

    public RangeMinFinder(int startIndex, int finishIndex, int[] arr, ParallelMinController controller) {
        this.startIndex = startIndex;
        this.finishIndex = finishIndex;
        this.arr = arr;
        this.controller = controller;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMinIndex() {
        return minIndex;
    }

    @Override
    public void run() {
        minValue = arr[startIndex];
        minIndex = startIndex;

        for (int i = startIndex + 1; i < finishIndex; i++) {
            if (arr[i] < minValue) {
                minValue = arr[i];
                minIndex = i;
            }
        }
        controller.incrementFinishedThread();
    }
}