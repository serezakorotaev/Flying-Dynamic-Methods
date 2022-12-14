package ru.korotaev.dynamics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.korotaev.dynamics.model.Parameters;

@Service
@RequiredArgsConstructor
public class ConstructService {

    private final DynamicService dynamicService;
    private final InterpolationService interpolationService;
    private final ApproximateService approximateService;

    public Parameters constructParameters(double n, double t, double v, double y, double x, double tetta, double fettaC, double wz) {
        double mach = dynamicService.getMach(y, v);
        return new Parameters()
                .setN(n)
                .setT(t)
                .setCurrentWeight(dynamicService.getCurrentWeight(t))
                .setPush(dynamicService.getPushing(y))
                .setVelocity(v)
                .setMach(mach)
                .setCxa(approximateService.Cxa(mach))
                .setAlpha(dynamicService.alpha(tetta, fettaC))
                .setFettaC(fettaC)
                .setCya(interpolationService.Cya(mach))
                .setDVdt(dynamicService.dV_dt(y, dynamicService.Xa(y, v), t, tetta, fettaC))
                .setWz(wz)
                .setTetta(tetta)
                .setY(y)
                .setDYdt(dynamicService.dY_dt(v, fettaC))
                .setX(x)
                .setDXdt(dynamicService.dX_dt(v, fettaC));
    }
}
