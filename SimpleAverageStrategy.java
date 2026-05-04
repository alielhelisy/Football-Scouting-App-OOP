package Project;

public class SimpleAverageStrategy implements RatingStrategy {
    @Override
    public double compute(int[] stars) {
        if (stars == null || stars.length == 0) return 0.0;
        long sum = 0;
        for (int s : stars) sum += s;
        return sum * 1.0 / stars.length;
    }
}