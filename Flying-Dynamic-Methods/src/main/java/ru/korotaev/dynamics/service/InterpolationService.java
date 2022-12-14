package ru.korotaev.dynamics.service;

import org.springframework.stereotype.Service;

@Service
public class InterpolationService {

    private final double[][] dataCya = {{0.01, 0.25},
            {0.55, 0.25},
            {0.8, 0.25},
            {0.9, 0.2},
            {1.0, 0.3},
            {1.06, 0.31},
            {1.1, 0.25},
            {1.2, 0.25},
            {1.3, 0.25},
            {1.4, 0.25},
            {2.0, 0.25},
            {2.6, 0.25},
            {3.4, 0.25},
            {6.0, 0.25},
            {10.0, 0.25}
    };

    public double Cya(double M) {
        double Cya = 0;
        for (int i = 0; i < 15; ++i) {
            if (M > dataCya[i][0] && M < dataCya[i + 1][0]) {
                Cya = dataCya[i][1] + (M - dataCya[i][0]) * (dataCya[i + 1][1] - dataCya[i][1]) / (dataCya[i + 1][0] - dataCya[i][0]);
            }
        }
        if (M > dataCya[14][0]) {
            Cya = dataCya[14][1];
        }
        if (M < dataCya[0][0]) {
            Cya = dataCya[0][1];
        }
        return Cya;
    }

    public double lineInterpolation(double xValue, double[] xData, double[] yData) {
        double tgA = (yData[1] - yData[0]) / (xData[1] - xData[0]);
        double a = yData[0] - tgA * xData[0];
        return a + tgA * xValue;
    }
}
