package ru.korotaev.dynamics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ru.korotaev.dynamics.constant.Constant.ACCELLER_ON_GRAVITY_ON_ECVATOR;
import static ru.korotaev.dynamics.constant.Constant.Jz;
import static ru.korotaev.dynamics.constant.Constant.Ld_Lc;
import static ru.korotaev.dynamics.constant.Constant.R;
import static ru.korotaev.dynamics.constant.Constant.RADIUS_OF_EARTH;
import static ru.korotaev.dynamics.constant.Constant.Sa;
import static ru.korotaev.dynamics.constant.Constant.Sm;
import static ru.korotaev.dynamics.constant.Constant.m0;
import static ru.korotaev.dynamics.constant.Constant.m_dot;

@Service
@RequiredArgsConstructor
public class DynamicService {

    private final ApproximateService approximateService;
    private final InterpolationService interpolationService;

    public Double getAccelerationOfGravityOnHigh(Double high) {
        return ACCELLER_ON_GRAVITY_ON_ECVATOR *
                (RADIUS_OF_EARTH / (RADIUS_OF_EARTH + high)) *
                (RADIUS_OF_EARTH / (RADIUS_OF_EARTH + high));
    }

    public Double getGeopotencialHigh(Double high) {
        return RADIUS_OF_EARTH * high / (RADIUS_OF_EARTH + high);
    }

    public Double getTemperature(Double geopotentialHigh) {
        if (geopotentialHigh >= -2001 && geopotentialHigh < 0) {
            return 301.15 + (-0.0065) * (geopotentialHigh - (-2000));
        } else if (geopotentialHigh >= 0 && geopotentialHigh < 11000) {
            return 288.15 + (-0.0065) * (geopotentialHigh - 0);
        } else if (geopotentialHigh >= 11000 && geopotentialHigh < 20000) { // b =0
            return 216.65;
        } else if (geopotentialHigh >= 20000 && geopotentialHigh < 32000) {
            return 216.65 + (0.0010) * (geopotentialHigh - 20000);
        } else if (geopotentialHigh >= 32000 && geopotentialHigh < 47000) {
            return 228.65 + (0.0028) * (geopotentialHigh - 32000);
        } else if (geopotentialHigh >= 47000 && geopotentialHigh < 51000) { // b =0
            return 270.65;
        } else if (geopotentialHigh >= 51000 && geopotentialHigh < 71000) {
            return 270.65 + (-0.0028) * (geopotentialHigh - 51000);
        } else if (geopotentialHigh >= 71000 && geopotentialHigh < 85000) {
            return 214.65 + (-0.0020) * (geopotentialHigh - 71000);
        } else if (geopotentialHigh >= 85000 && geopotentialHigh < 94000) {
            return 186.65;
        } else {
            return 273.15;
        }
    }

    public Double getPressure(Double geopotentialHigh, Double temperature) {
        double pressure = 0.0;
        if (geopotentialHigh >= -2001 && geopotentialHigh < 0) {
            pressure = Math.log10(127773.0) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((-0.0065) * R) * Math.log10(temperature / 301.15);
        } else if (geopotentialHigh >= 0 && geopotentialHigh < 11000) {
            pressure = Math.log10(101325) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((-0.0065) * R) * Math.log10(temperature / 288.15);
        } else if (geopotentialHigh >= 11000 && geopotentialHigh < 20000) { // b =0
            pressure = Math.log10(22632.0) - (0.434294 * ACCELLER_ON_GRAVITY_ON_ECVATOR) * (geopotentialHigh - 11000) / (R * temperature);
        } else if (geopotentialHigh >= 20000 && geopotentialHigh < 32000) {
            pressure = Math.log10(5474.87) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((0.0010) * R) * Math.log10(temperature / 216.65);
        } else if (geopotentialHigh >= 32000 && geopotentialHigh < 47000) {
            pressure = Math.log10(868.014) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((0.0028) * R) * Math.log10(temperature / 228.65);
        } else if (geopotentialHigh >= 47000 && geopotentialHigh < 51000) { // b =0
            pressure = Math.log10(110.906) - (0.434294 * ACCELLER_ON_GRAVITY_ON_ECVATOR) * (geopotentialHigh - 47000) / (R * temperature);
        } else if (geopotentialHigh >= 51000 && geopotentialHigh < 71000) {
            pressure = Math.log10(66.9384) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((-0.0028) * R) * Math.log10(temperature / 270.65);
        } else if (geopotentialHigh >= 71000 && geopotentialHigh < 85000) {
            pressure = Math.log10(3.95639) - ACCELLER_ON_GRAVITY_ON_ECVATOR / ((-0.0020) * R) * Math.log10(temperature / 214.65);
        } else if (geopotentialHigh >= 85000 && geopotentialHigh < 94000) {
            pressure = Math.log10(8.86272) - (0.434294 * ACCELLER_ON_GRAVITY_ON_ECVATOR) * (geopotentialHigh - 85000) / (R * temperature);
        }
        return Math.pow(10, pressure);
    }

    public Double getDensity(Double high) {
        return getPressure(getGeopotencialHigh(high), getTemperature(getGeopotencialHigh(high))) /
                (R * getTemperature(getGeopotencialHigh(high)));
    }

    ////////
    public double dV_dt(double y, double Xa, double t, double tetta, double fetta_c) {
        return (getPushing(y) * Math.cos(alpha(tetta, fetta_c)) - Xa) / getCurrentWeight(t) - getAccelerationOfGravityOnHigh(y) * Math.sin(fetta_c);
    }

    public double dFettaC_dt(double y, double Ya, double t, double tetta, double fetta_c, double V) {
        return (getPushing(y) * Math.sin(alpha(tetta, fetta_c)) + Ya) / (getCurrentWeight(t) * V) - getAccelerationOfGravityOnHigh(y) * Math.cos(fetta_c) / V;
    }

    public double dX_dt(double V, double fetta_c) {
        return V * Math.cos(fetta_c);
    }

    public double dY_dt(double V, double fetta_c) {
        return V * Math.sin(fetta_c);
    }

    public double dWz_dt(double tetta, double fetta_c, double y, double V) {
        return Mz_a(tetta, fetta_c, y, V) * alpha(tetta, fetta_c) / Jz;
    }

    public double dTetta_dt(double Wz) {
        return Wz;
    }

    // градиент статического аэродинамического момента относительно связанной оси z ЛА
    public double Mz_a(double tetta, double fetta_c, double y, double V) {
        return -(approximateService.Cxa(getMach(y, V)) + interpolationService.Cya(getMach(y, V)) * alpha(tetta, fetta_c)) * Sm * getDensity(y) * Math.pow(V, 2) * Ld_Lc / 2;
    }

    public double Xa(double y, double V) {
        return approximateService.Cxa(getMach(y, V)) * Sm * getDensity(y) * Math.pow(V, 2) / 2;
    }

    public double Ya(double y, double V, double tetta, double fetta_c) {
        return interpolationService.Cya(getMach(y, V)) * alpha(tetta, fetta_c) * Sm * getDensity(y) * Math.pow(V, 2) / 2;
    }

    // угол альфа
    public double alpha(double tetta, double fetta_c) {
        return tetta - fetta_c;
    }

    // Текущий вес
    public double getCurrentWeight(double t) {
        return m0 - m_dot * t;
    }

    // Мах
    public double getMach(double y, double V) {
        return V / (20.0468 * Math.sqrt(getTemperature(getGeopotencialHigh(y))));
    }

    //Тяга
    public double getPushing(double y) {
        return 2100 * m_dot + Sa * (101325 - getPressure(getGeopotencialHigh(y), getTemperature(getGeopotencialHigh(y))));
    }
}
