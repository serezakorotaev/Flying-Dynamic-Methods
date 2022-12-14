package ru.korotaev.dynamics.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.korotaev.dynamics.model.Parameters;
import ru.korotaev.dynamics.service.ConstructService;
import ru.korotaev.dynamics.service.DynamicService;
import ru.korotaev.dynamics.service.InterpolationService;

import java.util.ArrayList;
import java.util.List;

import static ru.korotaev.dynamics.constant.Constant.T_1;
import static ru.korotaev.dynamics.constant.Constant.T_K;
import static ru.korotaev.dynamics.constant.Constant.V0;
import static ru.korotaev.dynamics.constant.Constant.fetta_C0;
import static ru.korotaev.dynamics.constant.Constant.t0;
import static ru.korotaev.dynamics.constant.Constant.tetta_0;
import static ru.korotaev.dynamics.constant.Constant.w_z0;
import static ru.korotaev.dynamics.constant.Constant.x0;
import static ru.korotaev.dynamics.constant.Constant.y0;

@Service
@RequiredArgsConstructor
public class KuttService {
    private final DynamicService dynamicService;
    private final InterpolationService interpolationService;
    private final ConstructService constructService;

    public List<Parameters> kuttMethod(double dt, boolean alphaIsZero) {
        if (alphaIsZero) System.out.println("запись при угле 0");
        else System.out.println("Изменияющийся угол");

        List<Parameters> parametersList = new ArrayList<>();
        double t = t0;
        double vOld = V0;
        double vNew;
        double fettaCOld = fetta_C0;
        double fettaCNew;
        double tettaOld = tetta_0;
        double tettaNew;
        double xOld = x0;
        double xNew;
        double yOld = y0;
        double yNew;
        double wzOld = w_z0;
        double wzNew;
        double n = 0;
        System.out.println("Вывод начальных данных, создание эксель");
        parametersList.add(constructService.constructParameters(n, t, vOld, yOld, xOld, tettaOld, fettaCOld, wzOld));
        //
        double kV1;
        double kV2;
        double kV3;
        double kV4;

        double kFettaC1;
        double kFettaC2;
        double kFettaC3;
        double kFettaC4;

        double kTetta1;
        double kTetta2;
        double kTetta3;
        double kTetta4;

        double kX1;
        double kX2;
        double kX3;
        double kX4;

        double kY1;
        double kY2;
        double kY3;
        double kY4;

        double kWz1;
        double kWz2;
        double kWz3;
        double kWz4;

        while (t <= T_K - dt) {
            kV1 = dt * dynamicService.dV_dt(yOld, dynamicService.Xa(yOld, vOld), t, tettaOld, fettaCOld);
            kFettaC1 = dt * dynamicService.dFettaC_dt(yOld, dynamicService.Ya(yOld, vOld, tettaOld, fettaCOld), t, tettaOld, fettaCOld, vOld);
            kTetta1 = dt * dynamicService.dTetta_dt(wzOld);
            kX1 = dt * dynamicService.dX_dt(vOld, fettaCOld);
            kY1 = dt * dynamicService.dY_dt(vOld, fettaCOld);
            kWz1 = dt * dynamicService.dWz_dt(tettaOld, fettaCOld, yOld, vOld);

            kV2 = dt * dynamicService.dV_dt(yOld + kY1 / 2, dynamicService.Xa(yOld + kY1 / 2, vOld + kV1 / 2), t + dt, tettaOld + kTetta1 / 2, fettaCOld + kFettaC1 / 2);
            kFettaC2 = dt * dynamicService.dFettaC_dt(yOld + kY1 / 2, dynamicService.Ya(yOld + kY1 / 2, vOld + kV1 / 2, tettaOld + kTetta1 / 2, fettaCOld + kFettaC1 / 2), t + dt, tettaOld + kTetta1 / 2, fettaCOld + kFettaC1 / 2, vOld + kV1 / 2);
            kTetta2 = dt * dynamicService.dTetta_dt(wzOld + kWz1 / 2);
            kX2 = dt * dynamicService.dX_dt(vOld + kV1 / 2, fettaCOld + kFettaC1 / 2);
            kY2 = dt * dynamicService.dY_dt(vOld + kV1 / 2, fettaCOld + kFettaC1 / 2);
            kWz2 = dt * dynamicService.dWz_dt(tettaOld + kTetta1 / 2, fettaCOld + kFettaC1 / 2, yOld + kY1 / 2, vOld + kV1 / 2);

            kV3 = dt * dynamicService.dV_dt(yOld + kY2 / 2, dynamicService.Xa(yOld + kY2 / 2, vOld + kV2 / 2), t + dt, tettaOld + kTetta2 / 2, fettaCOld + kFettaC2 / 2);
            kFettaC3 = dt * dynamicService.dFettaC_dt(yOld + kY2 / 2, dynamicService.Ya(yOld + kY2 / 2, vOld + kV2 / 2, tettaOld + kTetta2 / 2, fettaCOld + kFettaC2 / 2), t + dt, tettaOld + kTetta2 / 2, fettaCOld + kFettaC2 / 2, vOld + kV2 / 2);
            kTetta3 = dt * dynamicService.dTetta_dt(wzOld + kWz2 / 2);
            kX3 = dt * dynamicService.dX_dt(vOld + kV2 / 2, fettaCOld + kFettaC2 / 2);
            kY3 = dt * dynamicService.dY_dt(vOld + kV2 / 2, fettaCOld + kFettaC2 / 2);
            kWz3 = dt * dynamicService.dWz_dt(tettaOld + kTetta2 / 2, fettaCOld + kFettaC2 / 2, yOld + kY2 / 2, vOld + kV2 / 2);

            kV4 = dt * dynamicService.dV_dt(yOld + kY3 / 2, dynamicService.Xa(yOld + kY3 / 2, vOld + kV3 / 2), t + dt, tettaOld + kTetta3 / 2, fettaCOld + kFettaC3 / 2);
            kFettaC4 = dt * dynamicService.dFettaC_dt(yOld + kY3 / 2, dynamicService.Ya(yOld + kY3 / 2, vOld + kV3 / 2, tettaOld + kTetta3 / 2, fettaCOld + kFettaC3 / 2), t + dt, tettaOld + kTetta3 / 2, fettaCOld + kFettaC3 / 2, vOld + kV3 / 2);
            kTetta4 = dt * dynamicService.dTetta_dt(wzOld + kWz3 / 2);
            kX4 = dt * dynamicService.dX_dt(vOld + kV3 / 2, fettaCOld + kFettaC3 / 2);
            kY4 = dt * dynamicService.dY_dt(vOld + kV3 / 2, fettaCOld + kFettaC3 / 2);
            kWz4 = dt * dynamicService.dWz_dt(tettaOld + kTetta3 / 2, fettaCOld + kFettaC3 / 2, yOld + kY3 / 2, vOld + kV3 / 2);

            vNew = vOld + (kV1 + kV2 + kV3 + kV4) / 6;
            fettaCNew = fettaCOld + (kFettaC1 + kFettaC2 + kFettaC3 + kFettaC4) / 6;
            xNew = xOld + (kX1 + kX2 + kX3 + kX4) / 6;
            yNew = yOld + (kY1 + kY2 + kY3 + kY4) / 6;

            if (alphaIsZero) {
                tettaNew = fettaCNew;
                wzNew = dynamicService.dFettaC_dt(yNew, dynamicService.Ya(yNew, vNew, tettaNew, fettaCNew), t + dt, tettaNew, fettaCNew, vNew);
            } else {
                wzNew = wzOld + (kWz1 + kWz2 + kWz3 + kWz4) / 6;
                tettaNew = tettaOld + (kTetta1 + kTetta2 + kTetta3 + kTetta4) / 6;
            }

            //вырезать, сделать уникальный метод
            if (dt == 0.1 && (t + dt) > T_1 && t < T_K) {
                double[] t1 = {t, t + dt};
                double[] v = {vOld, vNew};
                double[] fetta_c = {fettaCOld, fettaCNew};
                double[] x = {xOld, xNew};
                double[] y = {yOld, yNew};
                double[] wz = {wzOld, wzNew};
                double[] tetta = {tettaOld, tettaNew};
                /// вывод переменных, посчитанных интерполяцией

                double v1 = interpolationService.lineInterpolation(T_1, t1, v);
                double fetta_c_1 = interpolationService.lineInterpolation(T_1, t1, fetta_c);
                double x1 = interpolationService.lineInterpolation(T_1, t1, x);
                double y1 = interpolationService.lineInterpolation(T_1, t1, y);
                double wz1 = interpolationService.lineInterpolation(T_1, t1, wz);
                double tetta1 = interpolationService.lineInterpolation(T_1, t1, tetta);

                n += 1;

                System.out.println("вывод в эксель параметров при 4.73");
                parametersList.add(constructService.constructParameters(n, T_1, v1, y1, x1, tetta1, fetta_c_1, wz1));
            }

            vOld = vNew;
            fettaCOld = fettaCNew;
            tettaOld = tettaNew;
            xOld = xNew;
            yOld = yNew;
            wzOld = wzNew;

            t += dt;

            n += 1;
            System.out.println("вывод параметров");
            parametersList.add(constructService.constructParameters(n, t, vNew, yNew, xNew, tettaNew, fettaCNew, wzNew));

        }

        return parametersList;
    }
}
