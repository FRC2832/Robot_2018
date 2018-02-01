package org.usfirst.frc.team2832.robot;

import java.util.Arrays;

/**
 *
 */
public class LinearInterpolation {

    private double[] xValues, yValues;

    public LinearInterpolation(double[] xValues, double[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
    }

    /**
     *
     * @param x
     * @return
     */
    public double interpolate(double x) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("X and Y must be the same length");
        }
        if (xValues.length == 1) {
            throw new IllegalArgumentException("X must contain more than one value");
        }
        double[] dx = new double[xValues.length - 1];
        double[] dy = new double[xValues.length - 1];
        double[] slope = new double[xValues.length - 1];
        double[] intercept = new double[xValues.length - 1];

        // Calculate the line equation (i.e. slope and intercept) between each point
        for (int i = 0; i < xValues.length - 1; i++) {
            dx[i] = xValues[i + 1] - xValues[i];
            if (dx[i] == 0) {
                throw new IllegalArgumentException("X must be montotonic. A duplicate " + "xValues-value was found");
            }
            if (dx[i] < 0) {
                throw new IllegalArgumentException("X must be sorted");
            }
            dy[i] = yValues[i + 1] - yValues[i];
            slope[i] = dy[i] / dx[i];
            intercept[i] = yValues[i] - xValues[i] * slope[i];
        }

        // Perform the interpolation here
        double y;
        if ((x > xValues[xValues.length - 1]) || (x < xValues[0])) {
            y = Double.NaN;
        } else {
            int loc = Arrays.binarySearch(xValues, x);
            if (loc < -1) {
                loc = -loc - 2;
                y = slope[loc] * x + intercept[loc];
            } else {
                y = yValues[loc];
            }
        }

        return y;
    }

    public static void main(String[] args) {
        LinearInterpolation interpolation = new LinearInterpolation(new double[]{0, 5, 10}, new double[]{0, 5, 20});
        System.out.println("Interpolation of 3: " + interpolation.interpolate(3));
        System.out.println("Interpolation of 8: " + interpolation.interpolate(8));
    }
}
