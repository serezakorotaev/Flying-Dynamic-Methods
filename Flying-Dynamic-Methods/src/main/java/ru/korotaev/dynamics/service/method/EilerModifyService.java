package ru.korotaev.dynamics.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.korotaev.dynamics.model.Parameters;
import ru.korotaev.dynamics.service.ApproximateService;
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
public class EilerModifyService {

    private final DynamicService dynamicService;
    private final InterpolationService interpolationService;
    private final ConstructService constructService;

    public List<Parameters> eilerModifyMethod(double dt, boolean alphaIsZero) {
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
        double dV;
        double dFettaC;
        double dY;
        double dTetta;
        double dWz;

        while (t <= T_K - dt) {
            dV = dt * dynamicService.dV_dt(yOld, dynamicService.Xa(yOld, vOld), t, tettaOld, fettaCOld);
            dFettaC = dt * dynamicService.dFettaC_dt(yOld, dynamicService.Ya(yOld, vOld, tettaOld, fettaCOld), t, tettaOld, fettaCOld, vOld);
            dY = dt * dynamicService.dY_dt(vOld, fettaCOld);
            dTetta = dt * dynamicService.dTetta_dt(wzOld);
            dWz = dt * dynamicService.dWz_dt(tettaOld, fettaCOld, yOld, vOld);

            vNew = vOld + dt * dynamicService.dV_dt(yOld + dY / 2, dynamicService.Xa(yOld + dY / 2, vOld + dV / 2), t + dt, tettaOld + dTetta / 2, fettaCOld + dFettaC / 2);
            fettaCNew = fettaCOld + dt * dynamicService.dFettaC_dt(
                    yOld + dY / 2, dynamicService.Ya(yOld + dY / 2,
                            vOld + dV / 2, tettaOld + dTetta / 2, fettaCOld + dFettaC / 2), t + dt,
                    tettaOld + dTetta / 2, fettaCOld + dFettaC / 2, vOld + dV / 2);
            xNew = xOld + dt * dynamicService.dX_dt(vOld + dV/2, fettaCOld + dFettaC/2);
            yNew = yOld + dt * dynamicService.dY_dt(vOld + dV/2, fettaCOld + dFettaC/2);

            if (alphaIsZero) {
                tettaNew = fettaCNew;
                wzNew = dynamicService.dFettaC_dt(yNew, dynamicService.Ya(yNew, vNew, tettaNew, fettaCNew), t + dt, tettaNew, fettaCNew, vNew);
            } else {
                wzNew = wzOld + dt * dynamicService.dWz_dt(tettaOld + dTetta/2, fettaCOld + dFettaC/2, yOld + dY/2, vOld + dV/2);
                tettaNew = tettaOld + dt * dynamicService.dTetta_dt(wzOld + dWz/2);
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
