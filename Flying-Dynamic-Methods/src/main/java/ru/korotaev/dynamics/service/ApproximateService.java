package ru.korotaev.dynamics.service;

import org.springframework.stereotype.Service;

@Service
public class ApproximateService {

    private final double[][] dataCx = {{0.01, 0.30},
            {0.55, 0.3},
            {0.80, 0.55},
            {0.9, 0.7},
            {1.00, 0.84}};
    private final double[][] dataCx1 = {{1.00, 0.84},
            {1.06, 0.86},
            {1.10, 0.87},
            {1.20, 0.83},
            {1.30, 0.80},
            {1.40, 0.79}};
    private final double[][] dataCx2 = {{1.40, 0.79},
            {2.00, 0.65},
            {2.60, 0.55},
            {3.40, 0.5}
    };
    private final double[][] dataCx3 = {{3.40, 0.50},
            {6.00, 0.45},
            {10.0, 0.40}};

    public double Cxa(double M) {
        double Cxa = 0;
        double[] f1 = new double[14];
        double[] f2 = new double[14];
        double[] f3 = new double[14];
        double[] f4 = new double[14];
        double[] f5 = new double[14];

        if (M >= dataCx[1][0] && M < dataCx[4][0]) {
            for (int i = 1; i < 4; ++i) {
                f1[i] = (dataCx[i + 1][1] - dataCx[i][1]) / (dataCx[i + 1][0] - dataCx[i][0]);
            }
            for (int i = 1; i < 3; ++i) {
                f2[i] = (f1[i + 1] - f1[i]) / (dataCx[i + 2][0] - dataCx[i][0]);
            }
            for (int i = 1; i < 2; ++i) {
                f3[i] = (f2[i + 1] - f2[i]) / (dataCx[i + 3][0] - dataCx[i][0]);
            }

            Cxa = dataCx[1][1]
                    + f1[1] * (M - 0.55)
                    + f2[1] * (M - 0.55) * (M - 0.8)
                    + f3[1] * (M - 0.55) * (M - 0.8) * (M - 0.9);
        }

        if (M >= dataCx1[0][0] && M < dataCx1[5][0]) {
            for (int i = 0; i < 5; ++i) {
                f1[i] = (dataCx1[i + 1][1] - dataCx1[i][1]) / (dataCx1[i + 1][0] - dataCx1[i][0]);
            }
            for (int i = 0; i < 4; ++i) {
                f2[i] = (f1[i + 1] - f1[i]) / (dataCx1[i + 2][0] - dataCx1[i][0]);
            }
            for (int i = 0; i < 3; ++i) {
                f3[i] = (f2[i + 1] - f2[i]) / (dataCx1[i + 3][0] - dataCx1[i][0]);
            }
            for (int i = 0; i < 2; ++i) {
                f4[i] = (f3[i + 1] - f3[i]) / (dataCx1[i + 4][0] - dataCx1[i][0]);
            }
            for (int i = 0; i < 1; ++i) {
                f5[i] = (f4[i + 1] - f4[i]) / (dataCx1[i + 5][0] - dataCx1[i][0]);
            }

            Cxa = dataCx1[0][1]
                    + f1[1] * (M - 1.0)
                    + f2[1] * (M - 1.0) * (M - 1.06)
                    + f3[1] * (M - 1.0) * (M - 1.06) * (M - 1.1)
                    + f4[1] * (M - 1.0) * (M - 1.06) * (M - 1.1) * (M - 1.2)
                    + f5[1] * (M - 1.0) * (M - 1.06) * (M - 1.1) * (M - 1.2) * (M - 1.3);
        }

        if (M >= dataCx2[0][0] && M < dataCx2[3][0]) {
            for (int i = 0; i < 3; ++i) {
                f1[i] = (dataCx2[i + 1][1] - dataCx2[i][1]) / (dataCx2[i + 1][0] - dataCx2[i][0]);
            }
            for (int i = 0; i < 2; ++i) {
                f2[i] = (f1[i + 1] - f1[i]) / (dataCx2[i + 2][0] - dataCx2[i][0]);
            }
            for (int i = 0; i < 1; ++i) {
                f3[i] = (f2[i + 1] - f2[i]) / (dataCx2[i + 3][0] - dataCx2[i][0]);
            }

            Cxa = dataCx2[0][1]
                    + f1[1] * (M - 1.4)
                    + f2[1] * (M - 1.4) * (M - 2.0)
                    + f2[1] * (M - 1.4) * (M - 2.0) * (M - 2.6);
        }

        if (M >= dataCx3[0][0] && M <= dataCx3[2][0]) {
            for (int i = 0; i < 2; ++i) {
                Cxa = dataCx3[i][1] + (M - dataCx3[i][0]) * (dataCx3[i + 1][1] - dataCx3[i][1]) /
                        (dataCx3[i + 1][0] - dataCx3[i][0]);
            }
        }

        if (M >= dataCx[0][0] && M <= dataCx[1][0]) {
            Cxa = dataCx[0][1] + (M - dataCx[0][0]) * (dataCx[1][1] - dataCx[0][1]) /
                    (dataCx[1][0] - dataCx[0][0]);
        }

        return Cxa;

    }

}
