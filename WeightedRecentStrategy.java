package Project;

public class WeightedRecentStrategy implements RatingStrategy {
    @Override
    public double compute(int[] stars) {
        if (stars == null || stars.length == 0) return 0.0;
        long wsum = 0, w = 0;
        for (int i = 0; i < stars.length; i++) {
            int weight = i + 1;
            wsum += (long) stars[i] * weight;
            w += weight;
        }
        return wsum * 1.0 / w;
    }
}