package codezilla.foss.nirogya;


import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by chinthaka on 5/13/18.
 */

public class BurntCalories {

    private final static double walkingFactor = 0.57;
    private static double caloriesBurnedPerMile;
    private static double strip;
    private static double stepCountMile; // step/mile
    private static double conversationFactor;
    private static double caloriesBurned;
    private static NumberFormat formatter = new DecimalFormat("#0.00");
    private static double distance;
    private static double[] returnValues;

    public static double[] findBurntCalories(double stepsCount, double height, double weight) {
        caloriesBurnedPerMile = walkingFactor * (weight * 2.2);
        strip = height * 0.415;
        stepCountMile = 160934.4 / strip;
        conversationFactor = caloriesBurnedPerMile / stepCountMile;
        caloriesBurned = stepsCount * conversationFactor;
        distance = (stepsCount * strip) / 100000;
        returnValues = new double[]{caloriesBurned, distance};
        return returnValues;
    }
}